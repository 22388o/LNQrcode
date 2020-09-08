import org.jetbrains.kotlin.codegen.optimization.common.ControlFlowGraph.Companion.build

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.material-ui-swing:DarkStackOverflowTheme:0.0.1-rc3")
    implementation("io.github.vincenzopalazzo:material-ui-swing:1.1.1")
    implementation("io.github.material-ui-swing:LinkLabelUI:0.0.1-rc1")
    implementation("io.github.vincenzopalazzo:JQRInterface:0.0.1-rc1")
    implementation("io.github.material-ui-swing:SwingSnackBar:0.0.1-rc2")
    implementation("com.github.jiconfont:jiconfont:1.0.0") //TODO this is because meterial-ui-swing has a bug

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
        dependsOn("build")
        archiveClassifier.set("all")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes("Main-Class" to application.mainClassName)
        }
        from(configurations.runtimeClasspath.get()
                .onEach { println("add from dependencies: ${it.name}") }
                .map { if (it.isDirectory) it else zipTree(it) })
        val sourcesMain = sourceSets.main.get()
        sourcesMain.allSource.forEach { println("add from sources: ${it.name}") }
        from(sourcesMain.output)
    }

    register("createRunnableScript") {
        dependsOn("fatJar")
        file("${projectDir}/${project.name}-gen.sh").createNewFile()
        file("${projectDir}/${project.name}-gen.sh").writeText(
                """
                # Script generated from gradle! By clightning4j
                #!/bin/bash
                ${System.getProperties().getProperty("java.home")}/bin/java -jar ${project.buildDir.absolutePath}/libs/${project.name}-all.jar
                """.trimIndent())
    }
}
