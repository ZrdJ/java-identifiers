[![License](https://img.shields.io/github/license/mashape/apistatus.svg?maxAge=2592000)]()
[![](https://jitpack.io/v/ZrdJ/java-identifiers.svg)](https://jitpack.io/#ZrdJ/java-identifiers)
![GitHub Workflow Status (branch)](https://github.com/zrdj/java-identifiers/actions/workflows/maven.yml/badge.svg)

# java-identifiers

## Maven

Add the Jitpack repository to your build file

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Release artifact

```xml

<dependency>
    <groupId>com.github.zrdj</groupId>
    <artifactId>java-identifiers</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

### Identifiers and Codecs

Use the static methods of `Identifiers.*` to create your desired IDs of UUID's, Ulid's, Tsid's or Ksuid's and convert
them from or into a String using the methods of `Codecs.*` for choosing the desired codec.

```java
public static void main(String[] args) {
    final UUID id = Identifiers.UUIDv7();
    final Codec<UUID> base32 = Codecs.UUIDBase32();
    System.out.println("id as base32: " + base32.encode(id));
}
```