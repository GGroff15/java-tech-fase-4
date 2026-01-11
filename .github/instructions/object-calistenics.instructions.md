applyTo: '**'
[Object Calisthenics Rules]

All generated code must strictly follow the Object Calisthenics principles. Each rule is described below:

## 1. Only One Level of Indentation per Method
- Methods should not have nested blocks beyond one level. Refactor deep nesting into separate methods.

## 2. Don’t Use the ELSE Keyword
- Avoid `else` statements. Prefer early returns or guard clauses to simplify logic.

## 3. Wrap All Primitives and Strings
- Do not use raw primitives or strings as fields, parameters, or return types. Always wrap them in domain-specific classes.

## 4. First Class Collections
- Collections should be encapsulated in their own classes. Never expose raw collections; provide behavior through methods.

## 5. No Getters/Setters/Properties
- Avoid exposing internal state via getters/setters. Prefer behavior methods that express intent.

## 6. Only One Dot per Line
- Limit method calls to a single dot per line (e.g., `user.getName()` is allowed, but `user.getAddress().getStreet()` is not). Use intermediate variables or methods to break chains.

## 7. Don’t Abbreviate
- Use full, descriptive names for classes, methods, and variables. Avoid abbreviations unless universally understood.

## 8. Keep Classes Small
- Classes should have a single responsibility and be as small as possible. If a class grows too large, split it into smaller, focused classes.

## 9. No Classes with More Than Two Instance Variables
- Limit classes to a maximum of two instance variables. If more are needed, refactor into smaller classes or use composition.

## 10. No Static Methods
- Avoid static methods. Prefer instance methods to encourage object-oriented design and testability.

---
All generated code must comply with these Object Calisthenics rules. Any code that does not meet these standards should be refactored before submission.
