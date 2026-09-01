---
type: research
title: UlidCodec's narrower catch clause is not observable for the pinned ulid-creator version
updated: 2026-09-01
---

## The finding under investigation

`UUIDCodec.isValid`, `TsidCodec.isValid` and `KsuidCodec.isValid` catch
`Exception` around their `decode()` call; `UlidCodec.isValid`
(`src/main/java/com/github/zrdj/java/identifiers/codecs/UlidCodec.java:42`)
catches only `IllegalArgumentException`. The Ist-Spec
(`docs/specs/identifier-text-codec/spec.md`, requirement
`req~identifier-text-codec.text-validation~1`) requires the same behaviour
— no exception escapes `isValid`, `false` instead — for all four codecs.
The question was whether an input string exists for which `Ulid.from`
(pinned `com.github.f4b6a3:ulid-creator:5.2.4`, see `pom.xml`) throws
something other than `IllegalArgumentException`, which would make
`UlidCodec.isValid` throw instead of returning `false`.

## Method

Read `Ulid.java` from the `ulid-creator-5.2.4-sources.jar` (pulled into the
devcontainer's Maven cache via `mvn dependency:sources -Dclassifier=sources`
— it is not vendored in this repo). Then ran a throwaway JUnit test
(`ScratchUlidProbe`, written under `src/test/java/`, run with
`mvn test -Dtest=ScratchUlidProbe -Dsurefire.useFile=false`, deleted
afterwards — never committed) that calls `Ulid.from(String)` directly on a
battery of candidate inputs and prints the concrete exception class for
each.

## Why the library forecloses the difference

`Ulid.from(String)` (`Ulid.java:328`) does not decode length-by-length —
it first calls `toCharArray(string)` (`Ulid.java:775`), which runs
`isValidCharArray(chars)` (`Ulid.java:788`) before any bit-shifting
happens:

1. `chars == null || chars.length != ULID_CHARS` (26) -> `false`. Covers
   `null`, empty, too short, too long.
2. Per character: `ALPHABET_VALUES[chars[i]]` is looked up in a
   `static final byte[256]` table (`Ulid.java:98`); the lookup itself is
   wrapped in `try { ... } catch (ArrayIndexOutOfBoundsException e) {
   return false; }` (`Ulid.java:801-806`) with the comment `// Multibyte
   character!`. Any `char` code unit >= 256 — every non-Latin-1 character,
   including UTF-16 surrogate halves of a supplementary-plane character
   such as an emoji — trips this and is turned into `false` *inside the
   library*, never reaching the caller as an exception. A value found in
   the table but marked unused (`-1`, e.g. `U`, punctuation) also yields
   `false` via the explicit alphabet check.
3. `(ALPHABET_VALUES[chars[0]] & 0b11000) != 0` catches ULID timestamp
   overflow (first char must encode a value 0-7) -> `false`.

Only after all three checks pass does `from(String)` index into `chars`
with fixed offsets `0x00..0x19` and shift values from the (now guaranteed
valid) `ALPHABET_VALUES` table — no further exception is reachable at that
point, because length and alphabet membership are already established. The
one exception type this method can throw for any `String` input —
including `null` — is `IllegalArgumentException` (`Ulid.java:782`,
`"Invalid ULID: \"%s\""`).

## Inputs tried and what each one threw

All of the following were run against `Ulid.from(String)` directly (not
through `UlidCodec`, to see the library's raw behaviour):

| Input | Exception thrown |
|---|---|
| `""` (empty) | `IllegalArgumentException` |
| `"0"` (1 char) | `IllegalArgumentException` |
| `"0123456789ABCDEFGHJKMNPQ"` (25 chars, too short) | `IllegalArgumentException` |
| `"01ARZ3NDEKTSV4RRFFQ69G5FAVX"` (27 chars, too long) | `IllegalArgumentException` |
| `"01ARZ3NDEKTSV4RRFFQ69G5FAU"` (contains excluded letter `U`) | `IllegalArgumentException` |
| `"01arz3ndektsv4rrffq69g5fau"` (lowercase, still contains `u`) | `IllegalArgumentException` |
| `"01ARZ3NDEKTSV4RRFFQ69G5FA "` (trailing space) | `IllegalArgumentException` |
| `"01ARZ3NDEKTSV4RRFFQ69G5FA-"` (trailing dash) | `IllegalArgumentException` |
| `...5Fü` (U+00FC, inside the `byte[256]` table but unmapped) | `IllegalArgumentException` |
| `...5F€` (U+20AC, euro sign, code unit > 255) | `IllegalArgumentException` |
| `...5` + `😀` (U+1F600 emoji, supplementary plane, two surrogate code units) | `IllegalArgumentException` |
| `...5FД` (U+0414, Cyrillic De, code unit > 255) | `IllegalArgumentException` |
| `...5FĀ` (U+0100 = 256, first code unit value outside `byte[256]`) | `IllegalArgumentException` |
| `...5Fÿ` (U+00FF = 255, last code unit value still inside `byte[256]`) | `IllegalArgumentException` |
| First char `8` (value 8, top two bits of the 5-bit group set -> timestamp overflow per the ULID spec) | `IllegalArgumentException` |
| First char `Z` (value 31, maximum overflow) | `IllegalArgumentException` |
| First char `7` (value 7, maximum *non*-overflowing value — control: succeeds) | none — decodes |
| `null` | `IllegalArgumentException` |

As a cross-check, the same battery was run against `Tsid.from` and
`Ksuid.from` (the two other single-format codecs, both catching `Exception`
in their `isValid`): every rejected input threw `IllegalArgumentException`
there too — the same exception type across all three libraries for every
input tried, which is itself worth recording.

## Conclusion

No input was found, in the pinned `ulid-creator:5.2.4`, for which
`Ulid.from(String)` throws anything other than `IllegalArgumentException`.
The library's own `isValidCharArray` absorbs `ArrayIndexOutOfBoundsException`
(the one exception type its own indexing could raise, for out-of-range
`char` values) before it can escape, and every other malformed-input path
(wrong length, non-alphabet character, timestamp overflow, `null`) is an
explicit `false` return converted to the same `IllegalArgumentException`
by `toCharArray`. `UlidCodec.isValid`'s narrower `catch
(IllegalArgumentException)` (`UlidCodec.java:42`) is therefore behaviourally
identical to `catch (Exception)` for every input reachable through
`isValid` today — the difference described in the finding is real as a
maintenance hazard (a future `ulid-creator` version, or a change to this
method, could introduce a differently-typed exception the narrower catch
would miss) but is not observable with a test today. No code was changed
on this basis: a `catch` clause widened without a red test demonstrating
the wider clause is needed would be an unbacked change, not a repair.

## What would make the difference observable

Any of these would reopen the question and are worth re-running this probe
for:

- A `ulid-creator` upgrade past 5.2.4 that changes `isValidCharArray` or
  removes the internal `ArrayIndexOutOfBoundsException` guard.
- A change to `UlidCodec.decode` that does more than delegate straight to
  `Ulid.from(String)` (e.g. pre-processing the input before decoding).
- Calling `isValid` with something other than a `String` — not possible
  today since `isValid(String)` is the only signature.
