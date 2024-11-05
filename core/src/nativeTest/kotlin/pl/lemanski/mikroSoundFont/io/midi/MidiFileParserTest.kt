package pl.lemanski.mikroSoundFont.io.midi

import pl.lemanski.mikroSoundFont.io.loadFile
import kotlin.test.Test


class MidiFileParserTest {
    private val dir = "C:\\Users\\Mikolaj\\Desktop\\midi"

    @Test
    fun should_parse_midi_file() {
        val byteArray = loadFile("$dir\\venture.mid")
    }
}