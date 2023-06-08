
plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("groovy")
    alias(libs.plugins.micronaut.application)
}

version = "0.1"
group = "micronaut.publisher.error"

repositories {
    mavenCentral()
}

dependencies {
    if (libs.versions.micronaut.get().startsWith("3")) {
        annotationProcessor("io.micronaut:micronaut-http-validation")
    } else {
        annotationProcessor("io.micronaut.validation:micronaut-validation")
    }

    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("jakarta.annotation:jakarta.annotation-api")
    if (libs.versions.micronaut.get().startsWith("3")) {
        implementation("io.micronaut:micronaut-validation")
    } else {
        implementation("io.micronaut.validation:micronaut-validation")
    }

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
}

application {
    mainClass.set("micronaut.publisher.error.Application")
}

java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}

micronaut {
    version.set(libs.versions.micronaut.get())
    runtime("netty")
    testRuntime("spock")
    processing {
        incremental(true)
        annotations("micronaut.publisher.error.*")
    }
}
