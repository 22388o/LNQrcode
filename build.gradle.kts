plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.71"

    application
}
val mainClass = "io.jlightning.qr.cli.AppKt"

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.material-ui-swing:DarkStackOverflowTheme:0.0.1-rc2")
    implementation("io.github.vincenzopalazzo:material-ui-swing:1.1.1")
    implementation("io.github.material-ui-swing:LinkLabelUI:0.0.1-rc1")

    api(fileTree("${project.projectDir}/devlib") { include("JQRInterface-0.0.1-rc1-all.jar") })
    api(fileTree("${project.projectDir}/devlib") { include("jrpclightning-0.1.6-SNAPSHOT.jar") })


    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClassName = "io.jlightning.qr.cli.AppKt"
}


tasks {
    register("fatJar", Jar::class.java) {
        archiveClassifier.set("all")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes("Main-Class" to mainClass)
        }
        from(configurations.runtimeClasspath.get()
                .onEach { println("add from dependencies: ${it.name}") }
                .map { if (it.isDirectory) it else zipTree(it) })
        val sourcesMain = sourceSets.main.get()
        sourcesMain.allSource.forEach { println("add from sources: ${it.name}") }
        from(sourcesMain.output)
    }
}
