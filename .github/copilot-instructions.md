# Copilot Instructions for tech-fase-4

## Project Overview
This is a multimodal medical patient screening system that classifies patient urgency using video, audio, and medical reports. It is inspired by the Manchester Triage System and uses color-coded urgency levels (Red, Yellow, Green).

## Architectural Evolution
The architecture will evolve to a simple layer architecture, streamlining responsibilities and improving maintainability. Future updates may consolidate or refactor components to fit this layered approach.

## Architecture & Data Flow
- **Modalities**: Audio, Video, and Medical Report are processed independently in real time.
- **Aggregation**: Results from each modality are combined to classify patient condition and trigger emergency notifications if needed.
- **Key Components**:
  - `src/main/java/com/tech_challenge/medical/api/`: REST controllers for audio, video, and text uploads.
  - `src/main/java/com/tech_challenge/medical/application/processor/`: Processing strategies for each modality.
  - `src/main/java/com/tech_challenge/medical/domain/`: Core domain models (e.g., `AnalysisCase`, `AudioUpload`, `VideoUpload`).
  - `src/main/java/com/tech_challenge/medical/infraestructure/`: Integrations (e.g., storage, YOLOv8 client, repositories).

## Developer Workflows
- **Build**: `./mvnw clean install`
- **Run**: `./mvnw spring-boot:run`
- **Start Dependencies**: `docker-compose up -d` (PostgreSQL, Azure Blob Storage emulator, YOLOv8)
- **Config**: Edit `src/main/resources/application.yaml` for environment variables and endpoints.

## Patterns & Conventions
- **Strategy Pattern**: Used for modality processing (`AudioProcessorStrategy`, `VideoProcessorStrategy`, etc.).
- **DTOs**: All API requests use DTOs in `api/dto/`.
- **Validation**: Custom file type validation in `api/validation/`.
- **Repository Pattern**: Data access via repositories in `infraestructure/`.
- **External Services**: YOLOv8 for video analysis, Azure Speech for audio, LLMs for report processing.

## Integration Points
- **Azure Blob Storage**: For file persistence.
- **PostgreSQL**: For metadata and results.
- **YOLOv8**: Video analysis via external service (`YoloV8Client`).
- **Azure Speech Service**: Speech-to-text (see README for setup).

## Testing & Debugging
- Tests are in `src/test/java/com/tech_challenge/medical/`.
- Use Maven for running tests: `./mvnw test`
- Debug using standard Spring Boot and IDE tools.

## Examples
- To add a new modality, implement a new `ProcessorStrategy` and register it in `ProcessorService`.
- To extend API, add a new controller in `api/` and corresponding DTOs/validators.

## Quick Start
1. `docker-compose up -d`
2. `./mvnw clean install`
3. `./mvnw spring-boot:run`

---
For more details, see the [README.md](../../README.md) and key source directories above.
