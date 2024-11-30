package io.github.lemcoder.mikrosoundfont.io.midi

import io.github.lemcoder.mikrosoundfont.io.loadFile
import kotlin.test.Test


class MidiFileParserTest {
    private val dir = "C:\\Users\\Mikolaj\\Desktop\\midi"

    @Test
    fun should_parse_midi_file() {
        val byteArray = loadFile("$dir\\venture.mid")
    }
}