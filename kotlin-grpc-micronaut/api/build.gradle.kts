import com.google.protobuf.gradle.*
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    id("org.jetbrains.kotlin.kapt") version "1.4.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.10"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("io.micronaut.application") version "1.2.0"
    id("com.google.protobuf") version "0.8.13"
}

version = "0.1"
group = "com.joaoigm"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
    jcenter()
}

micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.joaoigm.kotlin.grpc.*")
    }
}

dependencies {

    // GRPC dependencies
    implementation("io.micronaut.grpc:micronaut-grpc-runtime")
    implementation("io.grpc:grpc-kotlin-stub:0.1.1")

    // Micronaut dependencies
    kapt("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-tracing")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    testImplementation("io.micronaut:micronaut-http-client")

    // JETBRAINS
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")

    // Postgres, Jaeger e etc
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.testcontainers:postgresql")
    runtimeOnly("io.jaegertracing:jaeger-thrift")
    implementation("javax.annotation:javax.annotation-api")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
}


application {
    mainClass.set("com.joaoigm.kotlin.grpc.taskManager.api" +
            ".ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/grpckt")
            srcDirs("build/generated/source/proto/main/java")
        }

        proto {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/grpckt")
            srcDirs("build/generated/source/proto/main/java")
            exclude("src/main/java")
            exclude("src/main/proto")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.33.1"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:0.1.1"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
                id("grpckt")
            }
        }
    }
}
