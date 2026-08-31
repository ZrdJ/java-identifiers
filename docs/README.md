---
type: index
title: java-identifiers — Knowledge Layer
lang: en
updated: 2026-08-31
---

# java-identifiers — Knowledge Layer

Thin wrapper around the f4b6a3 ID libraries — generates UUIDv7, Ulid, Tsid and Ksuid
identifiers via `Identifiers.*` and encodes/decodes them to `String` via matching
`Codec` implementations from `Codecs.*`.

Repo-specific knowledge. What concerns more than this repo lives in the knowledge layer
of the WS root (`~/workspaces/personal/docs/`).

## Folders

- `project/decisions/` — why things are the way they are (ADRs)
- `project/worklog/` — work logs, one file per day
- `project/research/` — self-collected material
- `project/sources/` — material delivered by others
- `wayfinding/` — undertakings whose path is not yet settled
- `changes/` — ongoing undertakings whose path is settled
- `archive/` — completed changes
- `specs/` — current state per capability

## Entry points

- `pom.xml` — coordinates (`com.github.zrdj:java-identifiers`), Java 11, f4b6a3 dependency
- `src/main/java/com/github/zrdj/java/identifiers/Identifiers.java` — static factory methods per identifier type
- `src/main/java/com/github/zrdj/java/identifiers/Codecs.java` — static factory methods per codec
- `src/main/java/com/github/zrdj/java/identifiers/main.java` — runnable usage example
- `README.md` (repo root) — Maven coordinates and usage example

This repo does not (yet) have its own `CLAUDE.md` — working rules apply from
`zrdj/CLAUDE.md` and the provider levels above it.
