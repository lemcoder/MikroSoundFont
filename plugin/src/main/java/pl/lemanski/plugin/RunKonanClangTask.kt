package pl.lemanski.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.NAME_ONLY
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.konan.target.KonanTarget
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs
import javax.inject.Inject

/**
 * Compile C/C++ source files using the
 * [`run_konan`](https://github.com/JetBrains/kotlin/blob/v1.9.0/kotlin-native/HACKING.md#running-clang-the-same-way-kotlinnative-compiler-does)
 * utility.
 */
abstract class RunKonanClangTask @Inject constructor(
    private val exec: ExecOperations,
    private val fs: FileSystemOperations,
    private val objects: ObjectFactory,
) : DefaultTask() {

    /** Destination of compiled `.o` object files */
    @get:OutputDirectory
    val outputDir: Provider<Directory>
        get() = objects.directoryProperty().fileValue(temporaryDir.resolve("output"))

    /** C and C++ source files to compile to object files */
    @get:InputFiles
    @get:PathSensitive(RELATIVE)
    abstract val sourceFiles: ConfigurableFileCollection

    /** Directories that include `.h` header files */
    @get:InputFiles
    @get:PathSensitive(RELATIVE)
    abstract val includeDirs: ConfigurableFileCollection

    /** Path to the (platform specific) `run_konan` utility */
    @get:InputFile
    @get:PathSensitive(NAME_ONLY)
    abstract val runKonan: RegularFileProperty

    /** Kotlin target platform, e.g. `mingw_x64` */
    @get:Input
    abstract val kotlinTarget: Property<KonanTarget>

    @get:Input
    @get:Optional
    abstract val arguments: ListProperty<String>

    @get:Internal
    abstract val workingDir: DirectoryProperty

    @TaskAction
    fun compile() {
        val workingDir = workingDir.asFile.getOrElse(temporaryDir)
        val kotlinTarget = kotlinTarget.get()

        // prepare output dirs
        val sourcesDir = workingDir.resolve("sources")
        val headersDir = workingDir.resolve("headers")
        val compileDir = workingDir.resolve("compile")

        fs.sync {
            from(sourceFiles)
            into(sourcesDir)
        }
        fs.sync {
            from(includeDirs)
            into(headersDir)
        }
        fs.delete { delete(compileDir) }
        compileDir.mkdirs()

        // prepare args file
        val sourceFilePaths = sourcesDir.walk()
            .filter { it.extension in listOf("cpp", "c") }
            .joinToString("\n") { it.invariantSeparatorsPath }

        compileDir.resolve("args").writeText(/*language=text*/ """
          |--include-directory ${headersDir.invariantSeparatorsPath}
          |${arguments.getOrElse(emptyList()).joinToString("\n")}
          |-c $sourceFilePaths
        """.trimMargin()
        )

        // compile files
        val compileResult = exec.execCapture {
            executable(runKonan.asFile.get())
            args(
                parseSpaceSeparatedArgs(
                    "clang clang $kotlinTarget @args"
                )
            )
            workingDir(compileDir)
        }

        // verify output
        logger.lifecycle(compileResult.output)
        compileResult.assertNormalExitValue()

        // move compiled files to output directory
        fs.sync {
            from(compileDir) {
                include("**/*.o")
            }
            into(outputDir)
        }
    }
}