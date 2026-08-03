# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**phive-rules-shared** is a single-artifact Java library providing the *shared base* used by all validation rule modules of [PHIVE](https://github.com/phax/phive) (Philip Helger Integrative Validation Engine): the registration SPI, the discovery/registration engine, and the small set of helper classes that every rule module (in `phive-rules-foundations` **and** in `phive-rules`) depends on.

It was extracted in 2026 from `phive-rules-foundations` (where it lived as the `phive-rules-foundation-api` module in package `com.helger.phive.rules.foundation`) into this standalone repository, and in the process the package was renamed to **`com.helger.phive.rules.shared`**. It is versioned independently, **starting at `1.0.0`**.

Part of the Peppol solution stack: https://github.com/phax/peppol

## Build Commands

```bash
mvn clean install        # Build, test and install to the local repo
mvn test                 # Run the tests only
mvn test -Dtest=SPITest  # Run a single test class
```

CI targets Java 17 (built/tested on 17, 21, 25), matching the rest of the phive-rules ecosystem.

### Relationship to the rest of the ecosystem

- The dependency direction is strictly `phive-rules` → `phive-rules-foundations` → **`phive-rules-shared`**. This library sits at the bottom and must **not** depend on any of them.
- Consumers depend on this artifact as `com.helger.phive.rules:phive-rules-shared`, so **this project must be installed/released before `phive-rules-foundations` (and therefore `phive-rules`) builds**. Locally: run `mvn install` here first.
- Because it is consumed as a `SNAPSHOT` until released, sibling projects that reference `1.0.0-SNAPSHOT` need it present in the local `~/.m2` (or deployed) before they compile.

## Architecture

Single Maven module, single Java package `com.helger.phive.rules.shared`, plain JAR packaging.

```
phive-rules-shared/
├── src/main/java/com/helger/phive/rules/shared/
│   ├── IValidationRulesRegistrarSPI.java     # The SPI every rule module implements
│   ├── ValidationRulesRegistrar.java         # ServiceLoader-based discovery/registration engine
│   ├── DVRHelper.java                        # DVRCoordinate creation with version parsing
│   ├── PhiveRulesInitializationException.java # Thrown when a prerequisite VES is missing
│   └── PhiveRulesTestHelper.java             # Test utility (isContentCorrect) used by rule modules
├── src/test/java/com/helger/phive/rules/shared/
│   └── SPITest.java                          # Validates the META-INF/services declarations
├── src/main/resources/{LICENSE,NOTICE}
└── src/etc/{javadoc.css,license-template.txt}
```

### Key types

- `IValidationRulesRegistrarSPI` — the SPI every rule module implements (annotated `@IsSPIInterface`). Each rule module ships a `{Format}ValidationSPI` implementation, listed in its own `src/main/resources/META-INF/services/com.helger.phive.rules.shared.IValidationRulesRegistrarSPI` file, and delegates `registerValidationRules` to its `init…` method(s). Implementations with cross-module dependencies declare them via `getAllPrerequisites()`; the default implementation returns an empty list.
- `ValidationRulesRegistrar` — `registerAllValidationRules (registry)` discovers all SPI implementations on the classpath via the JDK `ServiceLoader`, then registers them in rounds: an implementation is only invoked once every coordinate from its `getAllPrerequisites()` is present in the registry, otherwise it is deferred and retried in a later round. If a full round passes with no progress, the remaining prerequisites are considered unresolvable and an `IllegalStateException` is thrown.
- `DVRHelper` — `createCoordinate (…)` wraps `DVRCoordinate.create`, converting a checked `DVRVersionException` into an `IllegalArgumentException`.
- `PhiveRulesInitializationException` — runtime exception thrown when a prerequisite VES coordinate is missing.
- `PhiveRulesTestHelper` — `isContentCorrect (executor)` verifies that the rule resource behind a validation executor is valid (XSD is skipped; each Schematron flavour is parsed via the matching `ph-schematron` resource type).

### Dependencies

Deliberately minimal — this library has **no** dependency on any document-binding library:

| Dependency | Scope | Purpose |
|---|---|---|
| `slf4j-api` | compile | Logging facade |
| `phive-xml` | compile | PHIVE XML validation API (transitively brings ph-commons, ph-diver and ph-schematron) |
| `junit` | test | Test framework (JUnit 4) |
| `slf4j-simple` | test | Test logging |
| `ph-unittest-support-ext` | test | `SPITestHelper` and other test utilities |

The POM is **self-contained**: it inherits directly from `com.helger:parent-pom:3.1.0` and imports the BOMs (`ph-commons-parent-pom` 12.3.3, `ph-schematron-parent-pom` 10.0.0, `ph-diver-parent-pom` 4.2.1, `phive-parent-pom` 12.1.0) itself, rather than inheriting a project-specific parent.

## Imports & Annotations

This codebase uses **ph-commons 12.x** (restructured packages) and **JSpecify** nullness annotations. When writing or editing Java:
- Nullness: `org.jspecify.annotations.{NonNull,Nullable}` — never `javax.annotation.*` or `jakarta.annotation.*`.
- Core utilities live under `com.helger.base.*`, `com.helger.annotation.*` and `com.helger.collection.commons.*` (e.g. `com.helger.annotation.Nonempty`, `com.helger.annotation.concurrent.Immutable`, `com.helger.base.enforce.ValueEnforcer`, `com.helger.collection.commons.ICommonsList`) — not the old monolithic `com.helger.commons.*`.
- SPI markers come from `com.helger.annotation.style` (`@IsSPIInterface`, `@IsSPIImplementation`, `@ReturnsMutableCopy`).

## Testing

- **Framework:** JUnit 4
- **Test logging:** SLF4J Simple
- `SPITest` calls `SPITestHelper.testIfAllSPIImplementationsAreValid ()` to validate the `META-INF/services` registration. This library ships the SPI *interface* only (no implementation of its own), so the test guards the declaration mechanics.

## Packaging

Plain JAR (`<packaging>jar</packaging>`).
