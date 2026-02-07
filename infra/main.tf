terraform {
  required_version = ">= 1.5"

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 6.0"
    }
    google-beta = {
      source  = "hashicorp/google-beta"
      version = "~> 6.0"
    }
  }
}

provider "google" {
  project = var.project_id
  region  = var.region
}

provider "google-beta" {
  project = var.project_id
  region  = var.region
}

locals {
  api_services = [
    "run.googleapis.com",
    "artifactregistry.googleapis.com",
    "secretmanager.googleapis.com",
    "iam.googleapis.com",
    "iamcredentials.googleapis.com",
    "cloudresourcemanager.googleapis.com"
  ]
  
  image_name = "${var.region}-docker.pkg.dev/${var.project_id}/${google_artifact_registry_repository.medical.repository_id}/${var.service_name}"
}

resource "google_project_service" "required_apis" {
  for_each = toset(local.api_services)
  
  project = var.project_id
  service = each.value

  disable_on_destroy = false
}

resource "google_artifact_registry_repository" "medical" {
  location      = var.region
  repository_id = "${var.service_name}-images"
  description   = "Docker repository for ${var.service_name}"
  format        = "DOCKER"

  depends_on = [google_project_service.required_apis]
}

resource "google_secret_manager_secret" "gemini_api_key" {
  secret_id = "gemini-api-key"

  replication {
    auto {}
  }

  depends_on = [google_project_service.required_apis]
}

resource "google_secret_manager_secret" "triage_api_key" {
  secret_id = "triage-api-key"

  replication {
    auto {}
  }

  depends_on = [google_project_service.required_apis]
}

resource "google_cloud_run_v2_service" "medical_triage" {
  name     = var.service_name
  location = var.region
  ingress  = "INGRESS_TRAFFIC_ALL"

  template {
    scaling {
      min_instance_count = var.min_instances
      max_instance_count = var.max_instances
    }

    service_account = google_service_account.cloud_run.email

    containers {
      image = "${local.image_name}:latest"

      resources {
        limits = {
          cpu    = var.cpu_limit
          memory = var.memory_limit
        }
        cpu_idle = true
        startup_cpu_boost = true
      }

      env {
        name = "GEMINI_API_KEY"
        value_source {
          secret_key_ref {
            secret  = google_secret_manager_secret.gemini_api_key.secret_id
            version = "latest"
          }
        }
      }

      env {
        name = "TRIAGE_API_KEY"
        value_source {
          secret_key_ref {
            secret  = google_secret_manager_secret.triage_api_key.secret_id
            version = "latest"
          }
        }
      }

      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = "prod"
      }

      startup_probe {
        http_get {
          path = "/actuator/health"
        }
        initial_delay_seconds = 5
        timeout_seconds       = 3
        period_seconds        = 10
        failure_threshold     = 3
      }

      liveness_probe {
        http_get {
          path = "/actuator/health"
        }
        initial_delay_seconds = 30
        timeout_seconds       = 3
        period_seconds        = 30
        failure_threshold     = 3
      }
    }

    timeout = "${var.timeout_seconds}s"
    max_instance_request_concurrency = var.container_concurrency
  }

  traffic {
    type    = "TRAFFIC_TARGET_ALLOCATION_TYPE_LATEST"
    percent = 100
  }

  depends_on = [
    google_project_service.required_apis,
    google_secret_manager_secret_iam_member.cloud_run_gemini_secret_accessor,
    google_secret_manager_secret_iam_member.cloud_run_triage_secret_accessor
  ]
}

resource "google_cloud_run_service_iam_member" "public_access" {
  location = google_cloud_run_v2_service.medical_triage.location
  service  = google_cloud_run_v2_service.medical_triage.name
  role     = "roles/run.invoker"
  member   = "allUsers"
}

resource "google_service_account" "cloud_run" {
  account_id   = "${var.service_name}-sa"
  display_name = "Service Account for ${var.service_name} Cloud Run"
}

resource "google_secret_manager_secret_iam_member" "cloud_run_gemini_secret_accessor" {
  secret_id = google_secret_manager_secret.gemini_api_key.secret_id
  role      = "roles/secretmanager.secretAccessor"
  member    = "serviceAccount:${google_service_account.cloud_run.email}"
}

resource "google_secret_manager_secret_iam_member" "cloud_run_triage_secret_accessor" {
  secret_id = google_secret_manager_secret.triage_api_key.secret_id
  role      = "roles/secretmanager.secretAccessor"
  member    = "serviceAccount:${google_service_account.cloud_run.email}"
}
