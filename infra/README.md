# GCP Infrastructure - Medical Triage Application

This directory contains Terraform configuration for deploying the Medical Triage application to Google Cloud Platform (GCP) using Cloud Run.

## Architecture

```
┌─────────────────┐
│  GitHub Actions │
│   (CI/CD)       │
└────────┬────────┘
         │
         ▼
┌─────────────────────┐
│ Artifact Registry   │
│ (Docker Images)     │
└────────┬────────────┘
         │
         ▼
┌──────────────────────────┐
│ Cloud Run Service        │
│ - Serverless Container   │
│ - Auto-scaling (0-10)    │
│ - 512MB RAM / 1 vCPU     │
└────────┬─────────────────┘
         │
         ▼
┌──────────────────────────┐
│ Secret Manager           │
│ - GEMINI_API_KEY         │
│ - TRIAGE_API_KEY         │
└──────────────────────────┘
```

## Prerequisites

1. **GCP Account** with billing enabled
2. **gcloud CLI** installed and configured
3. **Terraform** >= 1.5
4. **GCP Project** created

## Initial Setup

### 1. Install Tools

```powershell
# Install gcloud CLI (Windows)
# Download from: https://cloud.google.com/sdk/docs/install

# Verify installation
gcloud --version

# Install Terraform (using Chocolatey)
choco install terraform

# Or download from: https://www.terraform.io/downloads
```

### 2. Configure GCP Project

```powershell
# Create a new project (or use existing)
$PROJECT_ID = "medical-triage-prod"
gcloud projects create $PROJECT_ID --name="Medical Triage Production"

# Set as active project
gcloud config set project $PROJECT_ID

# Link billing account (replace BILLING_ACCOUNT_ID)
gcloud billing projects link $PROJECT_ID --billing-account=BILLING_ACCOUNT_ID

# Enable required APIs (Terraform will also do this, but speeds up first run)
gcloud services enable cloudresourcemanager.googleapis.com
gcloud services enable serviceusage.googleapis.com
```

### 3. Authenticate

```powershell
# Authenticate with user credentials
gcloud auth login

# Set application default credentials for Terraform
gcloud auth application-default login
```

### 4. Configure Terraform Variables

```powershell
cd infra

# Copy example variables file
cp terraform.tfvars.example terraform.tfvars

# Edit with your values
# Required:
#   - project_id: Your GCP project ID
#   - github_repo: Your GitHub repo (format: owner/repo)
```

Example `terraform.tfvars`:

```hcl
project_id  = "medical-triage-prod"
region      = "us-central1"
github_repo = "yourusername/tech-fase-4"
service_name = "medical-triage"
```

### 5. Initialize and Apply Terraform

```powershell
# Initialize Terraform (downloads providers)
terraform init

# Preview changes
terraform plan

# Apply infrastructure (creates all resources)
terraform apply
```

**Expected output:**
- Cloud Run service (not yet running - needs image)
- Artifact Registry repository
- Secret Manager secrets (empty - need values)
- Workload Identity Pool for GitHub Actions
- Service Accounts with proper IAM roles

### 6. Configure Secrets

After `terraform apply`, set secret values in GCP Console or via CLI:

```powershell
# Set Gemini API Key
$GEMINI_KEY = "your-actual-gemini-api-key"
echo $GEMINI_KEY | gcloud secrets versions add gemini-api-key --data-file=-

# Set Triage API Key (generate a strong random key)
$TRIAGE_KEY = -join ((65..90) + (97..122) + (48..57) | Get-Random -Count 32 | % {[char]$_})
echo $TRIAGE_KEY | gcloud secrets versions add triage-api-key --data-file=-

# Verify secrets exist
gcloud secrets list
```

### 7. Configure GitHub Secrets

Get Terraform outputs and add to GitHub:

```powershell
# Get outputs
terraform output

# Copy these values to GitHub:
# Repository Settings → Secrets and variables → Actions → New repository secret
```

**Required GitHub Secrets:**

| Secret Name | Value Source | Example |
|------------|--------------|---------|
| `GCP_PROJECT_ID` | Your GCP project ID | `medical-triage-prod` |
| `GCP_WORKLOAD_IDENTITY_PROVIDER` | `terraform output workload_identity_provider` | `projects/123.../locations/global/workloadIdentityPools/...` |
| `GCP_SERVICE_ACCOUNT` | `terraform output service_account_email` | `github-actions-sa@medical-triage-prod.iam.gserviceaccount.com` |

### 8. First Deployment

```powershell
# Push code to GitHub
git add .
git commit -m "Add GCP deployment configuration"
git push

# Trigger deployment manually
# Go to: GitHub → Actions → "Deploy to Cloud Run" → "Run workflow"
```

## Usage

### Deploy Application

**Manual trigger (configured):**
1. Go to GitHub repository
2. Click **Actions** tab
3. Select **Deploy to Cloud Run** workflow
4. Click **Run workflow** → **Run workflow**

The workflow will:
1. Run all tests (`./mvnw verify`)
2. Build Docker image
3. Push to Artifact Registry
4. Deploy to Cloud Run
5. Run health check

### View Deployed Service

```powershell
# Get service URL
terraform output service_url

# Or via gcloud
gcloud run services describe medical-triage --region=us-central1 --format="value(status.url)"

# Test health endpoint
curl (terraform output -raw service_url)/actuator/health
```

### View Logs

```powershell
# Real-time logs
gcloud run services logs tail medical-triage --region=us-central1

# Or in GCP Console:
# Cloud Run → medical-triage → Logs
```

### Update Infrastructure

When you need to change Cloud Run configuration (memory, scaling, etc.):

```powershell
# Edit variables.tf or terraform.tfvars
# Example: increase memory
# memory_limit = "1Gi"

# Apply changes
terraform plan
terraform apply
```

## Cost Estimation

**Cloud Run (pay-per-use):**
- **Free tier:** 2M requests/month, 360K vCPU-seconds, 180K GiB-seconds
- **After free tier:** ~$0.00002400/request + $0.00001800/vCPU-sec + $0.00000200/GiB-sec
- **Estimated for low traffic:** $0-5/month

**Artifact Registry:**
- **Storage:** $0.10/GB/month
- **Network egress:** Typically free within region
- **Estimated:** $0.50-2/month (depends on image count)

**Secret Manager:**
- **Active secrets:** $0.06/secret/month
- **Access operations:** $0.03/10K operations
- **Estimated:** $0.12-0.50/month

**Total estimated monthly cost:** $1-10/month for low traffic MVP

## Maintenance

### Update Terraform

```powershell
# Pull latest Terraform provider versions
terraform init -upgrade

# Review and apply
terraform plan
terraform apply
```

### Rotate Secrets

```powershell
# Generate new API key
$NEW_KEY = -join ((65..90) + (97..122) + (48..57) | Get-Random -Count 32 | % {[char]$_})

# Add new version
echo $NEW_KEY | gcloud secrets versions add triage-api-key --data-file=-

# Cloud Run will automatically use latest version on next deployment
```

### Monitor Costs

```powershell
# View billing
gcloud billing accounts list

# View project costs
gcloud billing projects describe $PROJECT_ID
```

## Troubleshooting

### Deployment fails with "permission denied"

**Solution:** Verify Workload Identity is configured correctly:
```powershell
# Check service account exists
gcloud iam service-accounts describe github-actions-sa@$PROJECT_ID.iam.gserviceaccount.com

# Verify IAM bindings
gcloud projects get-iam-policy $PROJECT_ID --flatten="bindings[].members" --filter="bindings.members:serviceAccount:github-actions-sa@*"
```

### Health check fails

**Solution:** Check application logs:
```powershell
gcloud run services logs read medical-triage --region=us-central1 --limit=50
```

### Secret not found

**Solution:** Verify secrets exist and have values:
```powershell
gcloud secrets list
gcloud secrets versions list gemini-api-key
gcloud secrets versions list triage-api-key
```

## Cleanup

To destroy all infrastructure:

```powershell
cd infra

# Preview what will be destroyed
terraform plan -destroy

# Destroy all resources
terraform destroy

# Confirm with: yes
```

**Warning:** This will delete:
- Cloud Run service
- Artifact Registry and all images
- Secret Manager secrets (recoverable for 30 days)
- Workload Identity configurations

## Files

| File | Purpose |
|------|---------|
| `main.tf` | Core infrastructure (Cloud Run, Artifact Registry, secrets) |
| `variables.tf` | Input variables and defaults |
| `outputs.tf` | Exported values (URLs, service accounts) |
| `workload-identity.tf` | GitHub Actions authentication via OIDC |
| `backend.tf` | (Optional) Remote state configuration |
| `terraform.tfvars` | Your actual values (do not commit!) |
| `terraform.tfvars.example` | Example configuration |

## Security Best Practices

✅ **Implemented:**
- Workload Identity Federation (no long-lived keys)
- Secret Manager for sensitive values
- Least privilege IAM roles
- Container image vulnerability scanning (enabled by default in Artifact Registry)

⚠️ **Recommended for production:**
- Enable Cloud Armor for DDoS protection
- Restrict Cloud Run ingress to Cloud Load Balancer
- Enable VPC Service Controls
- Implement Cloud CDN for static assets
- Add Cloud Monitoring alerts
- Enable audit logging

## References

- [Cloud Run Documentation](https://cloud.google.com/run/docs)
- [Terraform Google Provider](https://registry.terraform.io/providers/hashicorp/google/latest/docs)
- [Workload Identity Federation](https://cloud.google.com/iam/docs/workload-identity-federation)
- [GitHub Actions OIDC](https://docs.github.com/en/actions/deployment/security-hardening-your-deployments/configuring-openid-connect-in-google-cloud-platform)
