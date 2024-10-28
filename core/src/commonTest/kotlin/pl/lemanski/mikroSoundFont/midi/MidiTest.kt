package pl.lemanski.mikroSoundFont.midi

import pl.lemanski.mikroSoundFont.io.loadFile
import pl.lemanski.mikroSoundFont.io.midi.MidiFileParser
import kotlin.test.Test

class MidiTest {
    private val dir = "C:\\Users\\Mikolaj\\Desktop\\midi"

    @Test
    fun testMidi() {
        val midiFileBuffer = loadFile("$dir\\venture.mid")
        val midiFile = MidiFileParser(midiFileBuffer).parse()
        midiFile.tracks.forEach {
            it.messages.forEach {
                println(it.type.name)
            }
        }
    }
}