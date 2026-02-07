output "service_url" {
  description = "URL of the deployed Cloud Run service"
  value       = google_cloud_run_v2_service.medical_triage.uri
}

output "artifact_registry_url" {
  description = "Docker image repository URL"
  value       = "${var.region}-docker.pkg.dev/${var.project_id}/${google_artifact_registry_repository.medical.repository_id}"
}

output "workload_identity_provider" {
  description = "Workload Identity Provider for GitHub Actions (use in GCP_WORKLOAD_IDENTITY_PROVIDER secret)"
  value       = google_iam_workload_identity_pool_provider.github_actions.name
}

output "service_account_email" {
  description = "Service Account email for GitHub Actions (use in GCP_SERVICE_ACCOUNT secret)"
  value       = google_service_account.github_actions.email
}

output "gemini_secret_name" {
  description = "Name of the Gemini API Key secret in Secret Manager"
  value       = google_secret_manager_secret.gemini_api_key.secret_id
}

output "triage_secret_name" {
  description = "Name of the Triage API Key secret in Secret Manager"
  value       = google_secret_manager_secret.triage_api_key.secret_id
}

output "cloud_run_service_account" {
  description = "Service Account used by Cloud Run service"
  value       = google_service_account.cloud_run.email
}

output "project_id" {
  description = "GCP Project ID"
  value       = var.project_id
}

output "region" {
  description = "GCP Region"
  value       = var.region
}
