package pl.lemanski.mikroSoundFont.io.midi

import pl.lemanski.mikroSoundFont.InvalidMidiDataException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
        assertFailsWith<InvalidMidiDataException> {
            MidiFileParser(invalidLengthBuffer).parseHeader()
        }
    }

    @Test
    fun should_throw_invalid_mthd_exception() {
        assertFailsWith<InvalidMidiDataException> {
            MidiFileParser(invalidHeaderStringBuffer).parseHeader()
        }
    }

    @Test
    fun should_throw_invalid_mthd_exception_2() {
        assertFailsWith<InvalidMidiDataException> {
            MidiFileParser(invalidHeaderByteBuffer).parseHeader()
        }
    }

    @Test
    fun should_throw_invalid_mthd_exception_3() {
        assertFailsWith<InvalidMidiDataException> {
            MidiFileParser(invalidHeaderFormatBuffer).parseHeader()
        }
    }

    @Test
    fun should_throw_unsupported_timing() {
        assertFailsWith<InvalidMidiDataException> {
            MidiFileParser(invalidTimingBuffer).parseHeader()
        }
    }

    @Test
    fun should_throw_invalid_track_count() {
        assertFailsWith<InvalidMidiDataException>("Invalid track values: -1") {
            MidiFileParser(invalidTrackCountBuffer).parseHeader()
        }
    }

    @Test
    fun should_throw_invalid_division() {
        assertFailsWith<InvalidMidiDataException>("Invalid division values: -1") {
            MidiFileParser(invalidDivisionBuffer).parseHeader()
        }
    }
}