import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.io.Buffer
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import pl.lemanski.pandamidi.core.Note
import pl.lemanski.pandamidi.io.getMidiProcessor
import pl.lemanski.pandamidi.io.getMidiWavConverter
import pl.lemanski.pandamidi.sequencer.MidiEvent
import pl.lemanski.pandamidi.sequencer.MidiSequence
import platform.posix.sleep

@OptIn(ExperimentalForeignApi::class)
fun main() {
    val c = MidiEvent(MidiEvent.Type.NOTE_ON, Note(60), 0, 0)
    val e = MidiEvent(MidiEvent.Type.NOTE_ON, Note(64), 0, 0)

    val sequence = MidiSequence()
    sequence.addEvent(c)
    sequence.addEvent(e)

    val soundFontPath = Path("D:\\src\\MidiWavConverter\\Example\\florestan-subset.sf2")
    val midiPath = Path("D:\\src\\MidiWavConverter\\Example\\venture.mid")

    SystemFileSystem.metadataOrNull(midiPath)?.let {
        println("size of file is: ${it.size}")
    }

    val sink = Buffer()

    SystemFileSystem.source(midiPath).let {
        var bytesRead = it.readAtMostTo(sink, 100)
        while (bytesRead > 0) {
            bytesRead = it.readAtMostTo(sink, 100)
        }
    }

    val midiToWavConverter = getMidiWavConverter()
    val path = midiToWavConverter.generate(soundFontPath.toString(), midiPath.toString())
    println(path)


    val midiProcessor = getMidiProcessor()
    midiProcessor.setSoundFontFromPath(soundFontPath.toString())
    midiProcessor.setAudioCallback { audioBytes ->
        println("Audio data received: ${audioBytes.size} bytes")

    }

    memScoped {
        CoroutineScope(Job()).launch {
            midiProcessor.processMidiBytes(sink.readByteArray())
        }
    }

    sleep(5u)
}