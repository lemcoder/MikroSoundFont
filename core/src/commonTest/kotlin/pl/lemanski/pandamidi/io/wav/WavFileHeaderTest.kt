package pl.lemanski.pandamidi.io.wav

import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test

class WavFileHeaderTest {
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun should_write_correct_bytes_for_empty_file_header() {
        val wavFileHex = WavFileHeader.write(44100u, 0u, 2u).toByteArray().toHexString().uppercase()
        val expectedHex = "524946462400000057415645666d7420100000000300020044ac000020620500080020006461746100000000".uppercase()
        assertEquals("", expectedHex, wavFileHex)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun should_write_correct_bytes_for_non_empty_file_header() {
        val wavFileHex = WavFileHeader.write(44100u, 1024u, 2u).toByteArray().toHexString().uppercase()
        val expectedHex = "524946462420000057415645666D7420100000000300020044AC000020620500080020006461746100200000".uppercase()
        assertEquals("", expectedHex, wavFileHex)
    }
}