plugins {
    java
    application
}

group = "org.onedata"
version = "1.0.0"

repositories {
    mavenCentral()
}

val vertxVersion = "5.0.5"

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    implementation("io.vertx:vertx-core:5.0.5")
    implementation("io.vertx:vertx-web:5.0.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("org.onedata.TravSignOnService.Main")
}

// create FAT jar to include vertx dependencies within the jar file
tasks.jar {
    archiveFileName.set("${project.name}-fat.jar")

    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })

    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    exclude("META-INF/*.MF")
}
tasks.test {
    useJUnitPlatform()
}
