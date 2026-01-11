applyTo: '**'
[Clean Code Rules]

All generated code must strictly follow these clean code principles:

## Method Size
- Methods should be concise and focused. Limit method length to a maximum of 30 lines (excluding comments and blank lines).
- If a method exceeds this size, refactor into smaller, well-named helper methods.

## Number of Parameters
- Limit method parameters to a maximum of 4. If more are needed, use a parameter object or DTO.
- Avoid boolean parameters; prefer enums or descriptive objects.

## Try-Catch Structure
- Always catch the most specific exception possible.
- Never leave catch blocks empty. Always log the error or handle it appropriately.
- Avoid using generic `Exception` unless absolutely necessary.
- Place only the code that may throw inside the try block; keep try blocks minimal.

## Naming Conventions
- Method names must be verbs and describe the action performed (e.g., `processAudio`, `analyzeReport`).
- Class names must be nouns and use PascalCase (e.g., `AudioProcessorStrategy`).
- Variable names must be descriptive and use camelCase.
- Avoid abbreviations unless they are universally understood.

## General Best Practices
- Each class should have a single responsibility.
- Avoid magic numbers and strings; use constants or enums.
- Write self-documenting code; minimize comments by making code clear.
- Prefer immutability and final fields where possible.
- Use dependency injection for service and repository classes.
- Avoid deep nesting; use early returns to simplify logic.
- Always validate method arguments and fail fast.

## Example

```java
public class AudioProcessorStrategy {
	private final AudioService audioService;

	public AudioProcessorStrategy(AudioService audioService) {
		this.audioService = audioService;
	}

	public AnalysisResult processAudio(AudioUploadRequest request) {
		validateRequest(request);
		try {
			return audioService.analyze(request.getAudioFile());
		} catch (AudioProcessingException ex) {
			// Log and handle specific exception
			log.error("Audio processing failed", ex);
			throw new AnalysisException("Failed to process audio", ex);
		}
	}

	private void validateRequest(AudioUploadRequest request) {
		if (request == null || request.getAudioFile() == null) {
			throw new IllegalArgumentException("Audio file is required");
		}
	}
}
```

---
All code must comply with these rules. Any generated code that does not meet these standards should be refactored before submission.
