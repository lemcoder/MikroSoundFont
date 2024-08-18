package pl.lemanski.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.*
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs
import javax.inject.Inject

/**
 * Link compiled C/C++ source files using the
 * [`run_konan`](https://github.com/JetBrains/kotlin/blob/v1.9.0/kotlin-native/HACKING.md#running-clang-the-same-way-kotlinnative-compiler-does)
 * utility.
 */
abstract class RunKonanLinkTask @Inject constructor(
    private val exec: ExecOperations,
    private val fs: FileSystemOperations,
    objects: ObjectFactory,
) : DefaultTask() {

    /** The linked file */
    @get:OutputFile
    val compiledLib: Provider<RegularFile>
        get() = workingDir.file(libFileName)

    /** All `.o` object files that will be linked */
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    abstract val objectFiles: ConfigurableFileCollection

    /** Path to the (platform specific) `run_konan` utility */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    abstract val runKonan: RegularFileProperty

    @get:Input
    abstract val libName: Property<String>

    private val libFileName: Provider<String>
        get() = libName.map { "lib${it}.a" }

    @get:Internal
    val workingDir: DirectoryProperty =
        objects.directoryProperty().convention(
            // workaround for https://github.com/gradle/gradle/issues/23708
            objects.directoryProperty().fileValue(temporaryDir)
        )

    @TaskAction
    fun compile() {
        val workingDir = workingDir.asFile.get()
        val libFileName = libFileName.get()

        // prepare output dir
        fs.delete { delete(workingDir) }
        workingDir.mkdirs()

        // prepare args file
        val sourceFilePaths = objectFiles
            .asFileTree
            .matching { include("**/*.o") }
            .joinToString("\n") { it.invariantSeparatorsPath }

        workingDir.resolve("args").writeText(/*language=text*/ """
          |-rv
          |$libFileName
          |$sourceFilePaths
        """.trimMargin()
        )

        // compile files
        val linkResult = exec.execCapture {
            executable(runKonan.asFile.get())
            args(parseSpaceSeparatedArgs(
                "llvm llvm-ar @args"
            ))
            workingDir(workingDir)
        }

        logger.lifecycle(linkResult.output)

        linkResult.assertNormalExitValue()
    }
}