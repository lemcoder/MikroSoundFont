import pl.lemanski.pandamidi.core.Note
import pl.lemanski.pandamidi.io.getMidiWavConverter
import pl.lemanski.pandamidi.sequencer.MidiEvent
import pl.lemanski.pandamidi.sequencer.MidiSequence

fun main() {
    val c = MidiEvent(MidiEvent.Type.NOTE_ON, Note(60), 0, 0)
    val e = MidiEvent(MidiEvent.Type.NOTE_ON, Note(64), 0, 0)

    val sequence = MidiSequence()
    sequence.addEvent(c)
    sequence.addEvent(e)

    val soundFontPath = "C:\\Users\\lenovo\\Desktop\\midiWavConverter\\florestan-subset.sf2"
    val midiPath = "C:\\Users\\lenovo\\Desktop\\midiWavConverter\\venture.mid"

    val midiToWavConverter = getMidiWavConverter()
    val path = midiToWavConverter.generate(soundFontPath, midiPath)
    println(path)
}