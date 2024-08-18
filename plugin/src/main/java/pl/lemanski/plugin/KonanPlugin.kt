package pl.lemanski.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.konan.target.KonanTarget
import java.io.File

abstract class KonanPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Define the properties that can be configured by the user
        val extension = project.extensions.create("konanConfig", KonanPluginExtension::class.java)

        // Register the konanClangCompile task
        val konanClangCompile: TaskProvider<RunKonanClangTask> = project.tasks.register("konanc", RunKonanClangTask::class.java) {
            group = project.name

            kotlinTarget.set(extension.kotlinTarget)

            sourceFiles.from(
                project.layout.projectDirectory
                    .dir(extension.sourceDir.get())
                    .asFileTree
            )

             includeDirs.from(project.layout.projectDirectory.dir(extension.headerDir))

            arguments.addAll(
                "-std=c99",
                "-fno-sanitize=undefined",
                "-D" + "JPH_CROSS_PLATFORM_DETERMINISTIC",
                "-D" + "JPH_ENABLE_ASSERTS",
            )

            runKonan.set(File(extension.konanPath.get()).resolve("bin/run_konan.bat"))
        }

        // Register the konanLink task
        project.tasks.register("konanLink", RunKonanLinkTask::class.java) {
            group = project.name

            libName.set(extension.libName)
            objectFiles.from(konanClangCompile)
            // check if we are on windows
            val isWindows = System.getProperty("os.name").lowercase().contains("windows")
            val scriptPath = if (isWindows) "bin/run_konan.bat" else "bin/run_konan"
            runKonan.set(File(extension.konanPath.get()).resolve(scriptPath))
        }
    }
}

interface KonanPluginExtension {
    val kotlinTarget: Property<KonanTarget>
    val sourceDir: Property<String>
    val headerDir: Property<String>
    val libName: Property<String>
    val konanPath: Property<String>
}