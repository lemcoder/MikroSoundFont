package io.github.lemcoder.mikrosoundfont.io.midi.message

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import org.junit.Test
import io.github.lemcoder.mikrosoundfont.InvalidMidiDataException

class BufferExtTest {

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testSingleByteValues() {
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
        val buffer = buildBuffer(0xFFu, 0xFFu, 0xFFu, 0x7Fu)
        assertEquals(0x0FFFFFFF, buffer.readVarLen())
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testInvalidInput() {
        val buffer = buildBuffer(0xFFu, 0xFFu, 0xFFu, 0xFFu, 0x7Fu)
        try {
            buffer.readVarLen()
        } catch (ex: InvalidMidiDataException) {
            assertTrue(true)
            return
        }

        assertTrue(false)
    }

    //---

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun buildBuffer(vararg bytes: UByte): Buffer {
        return Buffer().apply {
            write(bytes.toByteArray())
        }
    }

    //---

    @Test
    fun testWriteSingleByteValues() {
        val buffer1 = Buffer()
        buffer1.writeVarLen(0x7F)
        assertTrue(byteArrayOf(0x7F.toByte()).contentEquals(buffer1.readByteArray()))

        val buffer2 = Buffer()
        buffer2.writeVarLen(0x00)
        assertTrue(byteArrayOf(0x00.toByte()).contentEquals(buffer2.readByteArray()))

        val buffer3 = Buffer()
        buffer3.writeVarLen(0x40)
        assertTrue(byteArrayOf(0x40.toByte()).contentEquals(buffer3.readByteArray()))
    }

    @Test
    fun testWriteMultiByteValues() {
        val buffer1 = Buffer()
        buffer1.writeVarLen(0x80) // 128
        assertTrue(byteArrayOf(0x81.toByte(), 0x00.toByte()).contentEquals(buffer1.readByteArray()))

        val buffer2 = Buffer()
        buffer2.writeVarLen(0x2000) // 8192
        assertTrue(byteArrayOf(0xC0.toByte(), 0x00.toByte()).contentEquals(buffer2.readByteArray()))

        val buffer3 = Buffer()
        buffer3.writeVarLen(0x3FFF) // 16,383
        assertTrue(byteArrayOf(0xFF.toByte(), 0x7F.toByte()).contentEquals(buffer3.readByteArray()))

        val buffer4 = Buffer()
        buffer4.writeVarLen(0x4000) // 16,384
        assertTrue(byteArrayOf(0x81.toByte(), 0x80.toByte(), 0x00.toByte()).contentEquals(buffer4.readByteArray()))
    }

    @Test
    fun testWriteMaximumValue() {
        val buffer = Buffer()
        buffer.writeVarLen(0x0FFFFFFF)
        assertTrue(byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte()).contentEquals(buffer.readByteArray()))
    }

    @Test
    fun testWriteAndReadVarLen() {
        val originalValues = listOf(0x00, 0x40, 0x7F, 0x80, 0x2000, 0x3FFF, 0x4000, 0x0FFFFFFF)

        for (value in originalValues) {
            val buffer = Buffer()
            buffer.writeVarLen(value)
            val readValue = buffer.readVarLen()

            assertEquals("Failed for value: $value", value, readValue)
        }
    }
}