---
type: research
title: README vs. code drift found while writing the Ist-Spec
updated: 2026-09-01
---

Found while deriving `docs/specs/` from the code (see
`docs/specs/identifier-generation/spec.md`,
`docs/specs/identifier-text-codec/spec.md`,
`docs/specs/distribution/spec.md`). None of this is written into the spec
itself — the spec describes the code, not this mismatch.

## Version in README does not match version in pom.xml

`README.md`'s Maven snippet pins `<version>0.2.1</version>`; `pom.xml`
itself declares `<version>0.2.0</version>` for the same artifact.

Cause: `.github/workflows/release.yml` rewrites only `README.md`'s
`<version>` elements on a published GitHub release — it never touches
`pom.xml`. So README always reflects the most recently released tag, while
`pom.xml`'s own `<version>` is bumped separately (or not at all) and can
fall behind. This is likely harmless in practice — JitPack resolves an
artifact by git tag, not by the value inside `pom.xml` — but it means
`pom.xml`'s `<version>` is not the authoritative version number, and a
developer reading only `pom.xml` (not the README, not the release history)
would see a stale value.

## Everything else checked out

- The README's "Usage" code block is character-for-character the same
  example as `src/main/java/.../main.java` — no drift there.
- The README does not claim anything about `isValid`, multiple UUID text
  formats beyond the one shown, or the JDK 11/17/21 build matrix — it is
  silent on these rather than wrong about them.
