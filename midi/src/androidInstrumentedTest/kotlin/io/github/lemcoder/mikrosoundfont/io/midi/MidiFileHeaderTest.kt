package io.github.lemcoder.mikrosoundfont.io.midi

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import io.github.lemcoder.mikrosoundfont.InvalidMidiDataException

@OptIn(ExperimentalStdlibApi::class)
class MidiFileHeaderTest {

    private val validBuffer = "4d546864000000060001000901e0".hexToByteArray()
    private val invalidLengthBuffer = "4e5468640000000600010009".hexToByteArray()
    private val invalidHeaderStringBuffer = "3d546864000000060001000901e0".hexToByteArray()
    private val invalidHeaderByteBuffer = "4d546864000000070001000901e0".hexToByteArray()
    private val invalidHeaderFormatBuffer = "4d546864000000060005000901e0".hexToByteArray()
    private val invalidTimingBuffer = "4d546864000000060001000980e0".hexToByteArray()
    private val invalidTrackCountBuffer = "4d546864000000060001ffff01e0".hexToByteArray()
    private val invalidDivisionBuffer = "4d5468640000000600010009ffff".hexToByteArray()

    @Test
    fun should_parse_midi_file_header() {
        val expectedHeader = MidiFile.Header(
            trackCount = 9,
            division = 480
        )

        val parser = MidiFileParser(validBuffer)
        val header = parser.parseHeader()

        assertEquals(expectedHeader, header)
    }

    @Test
    fun should_throw_invalid_length_exception() {
        try {
            MidiFileParser(invalidLengthBuffer).parseHeader()
        } catch (e: InvalidMidiDataException) {
            assertTrue(true)
            return
        }

        assertTrue(false)
    }

    @Test
    fun should_throw_invalid_mthd_exception() {
        try {
            MidiFileParser(invalidHeaderStringBuffer).parseHeader()
        } catch (e: InvalidMidiDataException) {
            assertTrue(true)
            return
        }

        assertTrue(false)
    }

    @Test
    fun should_throw_invalid_mthd_exception_2() {
        try {
            MidiFileParser(invalidHeaderByteBuffer).parseHeader()
        } catch (e: InvalidMidiDataException) {
            assertTrue(true)
            return
        }

        assertTrue(false)
    }

    @Test
    fun should_throw_invalid_mthd_exception_3() {
        try {
            MidiFileParser(invalidHeaderFormatBuffer).parseHeader()
        } catch (e: InvalidMidiDataException) {
            assertTrue(true)
            return
        }

        assertTrue(false)
    }

    @Test
    fun should_throw_unsupported_timing() {
        try {
            MidiFileParser(invalidTimingBuffer).parseHeader()
        } catch (e: InvalidMidiDataException) {
            assertTrue(true)
            return
        }

        assertTrue(false)
    }

    @Test
    fun should_throw_invalid_track_count() {
        try {
            MidiFileParser(invalidTrackCountBuffer).parseHeader()
        } catch (e: InvalidMidiDataException) {
            assertTrue(true)
            return
        }

        assertTrue(false)
    }

    @Test
    fun should_throw_invalid_division() {
        try {
            MidiFileParser(invalidDivisionBuffer).parseHeader()
        } catch (e: InvalidMidiDataException) {
            assertTrue(true)
            return
        }

        assertTrue(false)
    }
}