package pl.lemanski.mikroSoundFont.io.midi

import kotlinx.io.Buffer
import pl.lemanski.mikroSoundFont.InvalidMidiDataException
import pl.lemanski.mikroSoundFont.io.midi.message.readVarLen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MidiMessageParserTest {

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testSingleByteValues() {
        // Single-byte values (no continuation bits)
        val buffer1 = buildBuffer(0x7Fu)
        assertEquals(0x7F, buffer1.readVarLen())

        val buffer2 = buildBuffer(0x00u)
        assertEquals(0x00, buffer2.readVarLen())

        val buffer3 = buildBuffer(0x40u)
        assertEquals(0x40, buffer3.readVarLen())
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testMultiByteValues() {
        // Multi-byte values (with continuation bits)
        val buffer1 = buildBuffer(0x81u, 0x00u) // 128
        assertEquals(0x80, buffer1.readVarLen())

        val buffer2 = buildBuffer(0xC0u, 0x00u) // 8192
        assertEquals(0x2000, buffer2.readVarLen())

        val buffer3 = buildBuffer(0xFFu, 0x7Fu) // 16,383 (maximum 2-byte value)
        assertEquals(0x3FFF, buffer3.readVarLen())

        val buffer4 = buildBuffer(0x81u, 0x80u, 0x00u) // 16,384
        assertEquals(0x4000, buffer4.readVarLen())
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testMaximumValue() {
        // Test the maximum value that can be represented in a 4-byte MIDI variable-length
        val buffer = buildBuffer(0xFFu, 0xFFu, 0xFFu, 0x7Fu)
        assertEquals(0x0FFFFFFF, buffer.readVarLen())
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testInvalidInput() {
        val buffer = buildBuffer(0xFFu, 0xFFu, 0xFFu, 0xFFu, 0x7Fu)
        assertFailsWith<InvalidMidiDataException> {
            buffer.readVarLen()
        }
    }

    //---

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun buildBuffer(vararg bytes: UByte): Buffer {
        return Buffer().apply {
            write(bytes.toByteArray())
        }
    }
}