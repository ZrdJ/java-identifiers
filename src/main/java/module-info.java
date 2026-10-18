// Named explicitly because the name derived from the jar file ("java.identifiers") would land in
// the java.* namespace the JDK reserves for platform modules.
module com.github.zrdj.java.identifiers {
    requires transitive com.github.f4b6a3.uuid;
    requires transitive com.github.f4b6a3.ulid;
    requires transitive com.github.f4b6a3.tsid;
    requires transitive com.github.f4b6a3.ksuid;

    exports com.github.zrdj.java.identifiers;
    exports com.github.zrdj.java.identifiers.codecs;
}
