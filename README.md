# phive-rules-shared

<!-- ph-badge-start -->
[![Sonatype Central](https://maven-badges.sml.io/sonatype-central/com.helger.phive.rules/phive-rules-shared/badge.svg)](https://maven-badges.sml.io/sonatype-central/com.helger.phive.rules/phive-rules-shared/)
[![javadoc](https://javadoc.io/badge2/com.helger.phive.rules/phive-rules-shared/javadoc.svg)](https://javadoc.io/doc/com.helger.phive.rules/phive-rules-shared)

> If this project saved you some time or made your day a little easier, a star would mean a lot — it helps others find it too.
<!-- ph-badge-end -->

The shared base library for [PHIVE](https://github.com/phax/phive) (Philip Helger Integrative Validation Engine) validation rule modules - pronounced `[ˈfaɪv]`.

This project holds the **shared registration SPI and helper classes** used across the whole phive-rules ecosystem:

* `IValidationRulesRegistrarSPI` - the SPI every rule module implements to register its validation execution sets. All implementations are discovered from the classpath via the JDK `ServiceLoader` mechanism.
* `ValidationRulesRegistrar` - the discovery/registration engine that invokes every discovered SPI implementation, resolving cross-module prerequisite ordering automatically (deferring and retrying an implementation until its prerequisites are present).
* `DVRHelper` - creates `DVRCoordinate` instances with version parsing.
* `PhiveRulesInitializationException` - thrown when a prerequisite VES is missing.
* `PhiveRulesTestHelper` - test utilities (`isContentCorrect`) used by every rule module's tests.

The dependencies are deliberately minimal: `slf4j-api` + `phive-xml` (which transitively brings in ph-commons, ph-diver and ph-schematron). This library has **no** dependency on any document-binding library.

This project is part of my Peppol solution stack. See https://github.com/phax/peppol for other components and libraries in that area.

The Java code in this project is licensed under the Apache 2 license.

# Maven usage

Add the following to your `pom.xml` to use this artifact, replacing `x.y.z` with the latest version:

```xml
<dependency>
  <groupId>com.helger.phive.rules</groupId>
  <artifactId>phive-rules-shared</artifactId>
  <version>x.y.z</version>
</dependency>
```

# News and noteworthy

v1.0.0 - 2026-03-08
* Initial release
* Holds the shared registration SPI (`IValidationRulesRegistrarSPI`), the `ValidationRulesRegistrar` and the core helper classes (`DVRHelper`, `PhiveRulesInitializationException`, `PhiveRulesTestHelper`) in package `com.helger.phive.rules.shared`

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodingStyleguide.md) |
It is appreciated if you star the GitHub project if you like it.
