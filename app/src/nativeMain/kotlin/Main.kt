import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.sizeOf
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.io.Buffer
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import pl.lemanski.pandamidi.generator.MidiMessageNoteOff
import pl.lemanski.pandamidi.generator.MidiMessageNoteOn
import pl.lemanski.pandamidi.generator.getGenerator
import pl.lemanski.pandamidi.io.audio.playFromBuffer
import pl.lemanski.pandamidi.io.toByteArrayLittleEndian
import pl.lemanski.pandamidi.io.wav.WavFileHeader
import platform.posix.sleep

@OptIn(ExperimentalForeignApi::class)
fun main(args: Array<String>) {
    val gOff = MidiMessageNoteOff(
        time = 10000,
        channel = 6,
        key = 32,
        next = null
    )

    val g = MidiMessageNoteOn(
        time = 2500,
        channel = 6,
        key = 43,
        velocity = 80,
        next = gOff
    )

    val eOff = MidiMessageNoteOff(
        time = 2000,
        channel = 3,
        key = 40,
        next = g
    )

    val e = MidiMessageNoteOn(
        time = 1_000,
        channel = 3,
        key = 40,
        velocity = 80,
        next = eOff
    )

    val cOff = MidiMessageNoteOff(
        time = 100,
        channel = 0,
        key = 36,
        next = e
    )

    val c = MidiMessageNoteOn(
        time = 0,
        channel = 0,
        key = 36,
        velocity = 80,
        next = cOff
    )

    var bytes = ByteArray(0)
    val generator = getGenerator()

//    val soundFontPath = Path(args[0])
    val soundFontPath = Path("C:\\Users\\Mikolaj\\Desktop\\test\\florestan-subset.sf2")

    generator.setSoundFont(soundFontPath.toString())
    val midiBytes = generator.generate(c)
    val numSamples = midiBytes.size.toUInt() / sizeOf<FloatVar>().toUInt()
    val wavFileHeader = WavFileHeader.write(44100u, numSamples, 2u)

    // bytes += wavFileHeader.toByteArray()
    bytes += midiBytes.toByteArrayLittleEndian()


    println(bytes.size)

    runBlocking {
        coroutineScope {
            launch {
                println("Start play")
                playFromBuffer(bytes)
                sleep(10u)
                println("End play")
            }
        }
    }
    val file = Buffer()

    file.write(bytes, 0, bytes.size)

    SystemFileSystem.sink(Path("./output.wav")).write(file, file.size)

    sleep(5u)
}