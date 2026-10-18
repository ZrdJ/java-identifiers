---
type: decision
title: A real module descriptor, not an automatic module name
updated: 2026-10-18
status: accepted
origin: []
---

## Context

`tibbots/bots` is cut into named JPMS modules and wants to use this library from one of them. A
named module can only `requires` another named module. An automatic module (one derived from a
plain jar without a descriptor) works there too, but its name is derived from the jar filename, not
chosen by the library.

Checked directly against the published artifact
(`https://jitpack.io/com/github/zrdj/java-identifiers/0.2.1/java-identifiers-0.2.1.jar`): no
`module-info.class` and no `Automatic-Module-Name` manifest entry. Absent both, the JPMS filename
algorithm turns `java-identifiers-0.2.1.jar` into the automatic module name `java.identifiers` — a
name in the `java.*` namespace the JDK reserves for platform modules.

The same situation was resolved in `zrdj/java-properties` (pull request #18) for the same consumer.
There it was measured that the reserved name compiles and runs today, but only because the consumer
packages into a single jar and starts from the classpath, so the runtime module system never
resolves the name.

## Decision

**Ship a real `module-info.java`, module name `com.github.zrdj.java.identifiers`, matching the root
package.** No `Automatic-Module-Name` fallback: that manifest entry would need the same
non-`java.*` value anyway, and a real descriptor additionally states the module's dependencies and
its public surface explicitly.

Exported packages are both that exist:

- `com.github.zrdj.java.identifiers` — `Identifiers`, `Codecs`, `Codec`
- `com.github.zrdj.java.identifiers.codecs` — the `UUIDCodec`, `UlidCodec`, `TsidCodec` and
  `KsuidCodec` enums

Required modules, all four as `requires transitive`:

- `com.github.f4b6a3.uuid` (uuid-creator 6.1.1)
- `com.github.f4b6a3.ulid` (ulid-creator 5.2.4)
- `com.github.f4b6a3.tsid` (tsid-creator 5.2.6)
- `com.github.f4b6a3.ksuid` (ksuid-creator 4.1.1)

## Rationale

- **The trigger is a latent failure, not a current one.** A consumer can use this library from a
  named module today; what it cannot do is choose the name it uses. The derived name lands in the
  namespace the JDK reserves for itself, and the first module-path start, `jlink` image, or JDK
  that tightens the check turns a green build red. No test in either repo would catch that
  beforehand, because the descriptor is the only place the name appears.
- **A descriptor is also documentation.** `requires java.identifiers;` in a consumer reads as a
  platform module. `requires com.github.zrdj.java.identifiers;` reads as what it is.
- **Both packages are exported.** The `codecs` package holds the implementations behind `Codecs.*`,
  and the code reaches them only through that facade, but its enums are `public` and reachable on
  the classpath today. Hiding the package would break any consumer that uses `UUIDCodec.Base32`
  directly, and this change is meant to remove a latent problem, not to narrow the API.
- **`requires transitive` for all four f4b6a3 modules.** Each of them appears in this library's
  public signatures: `Identifiers.Ulid()`, `Identifiers.Tsid()` and `Identifiers.Ksuid()` return
  `Ulid`, `Tsid` and `Ksuid`; `Codecs.UlidBase32()`, `Codecs.TsidBase32()` and
  `Codecs.KsuidBase62()` return `Codec<Ulid>`, `Codec<Tsid>` and `Codec<Ksuid>`; and
  `Identifiers.UUIDv2(...)`, `UUIDv3(...)` and `UUIDv5(...)` take `UuidLocalDomain` and
  `UuidNamespace` from uuid-creator. A consumer calling any of these must read the module that
  declares the type, so the dependency is implied rather than left to every consumer to repeat.
- **The f4b6a3 module names are stable.** None of the four jars ships a `module-info.class`, but
  each declares an `Automatic-Module-Name` in its manifest (`com.github.f4b6a3.uuid`,
  `com.github.f4b6a3.ulid`, `com.github.f4b6a3.tsid`, `com.github.f4b6a3.ksuid`), checked in the
  jars of the versions pinned in `pom.xml`. That is a name chosen by the author, not derived from
  the filename.

## Consequences

- `pom.xml`'s `<version>` moves from `0.2.0` to `0.3.0`, catching up with the already-tagged
  `0.2.1` and going one further because this changes what JPMS consumers write, not a patch. The
  README's dependency snippet moves to `0.3.0` as well.
- `maven.compiler.source`/`target` stay at `11`, and no other build configuration changes:
  `maven-compiler-plugin` 3.16.0 detects `module-info.java` on its own, and the JUnit 4 tests keep
  running on the classpath under the default surefire.
- javac warns `requires transitive directive for an automatic module` for each of the four
  `requires transitive` lines. The warning states a fact about the f4b6a3 jars, not a defect here;
  it goes away on its own if they ever ship real descriptors under the same names.
- Classpath consumers (an unnamed module, or a plain `<dependency>` without a `module-info.java` of
  its own) are unaffected: a modular jar keeps working unchanged when read from the classpath.
- The f4b6a3 libraries must keep publishing the same module names for this descriptor to resolve.
  A dependency upgrade that changes or drops one of them breaks module-path consumers and has to be
  checked against the jar manifest before it is merged.
- Consumers already on `0.2.1` keep working. This release removes a latent problem; nothing is
  urgent about adopting it.
