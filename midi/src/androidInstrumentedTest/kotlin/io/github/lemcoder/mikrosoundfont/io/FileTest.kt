package io.github.lemcoder.mikrosoundfont.io

import org.junit.Assert.assertTrue
import org.junit.Test

class FileTest {
    private val dir = "C:\\Users\\Mikolaj\\Desktop\\midi"

    @Test
    fun should_load_file_as_byte_array() {
        val byteArray = loadFile("$dir\\venture.mid")
        assertTrue("File should not be empty", byteArray.isNotEmpty())
    }

    @Test
    fun should_save_file() {
        val byteArray = loadFile("$dir\\venture.mid")
        saveFile(byteArray, "$dir\\venture_copy.mid")
        val byteArrayCopy = loadFile("$dir\\venture_copy.mid")

        assertTrue("Content should be equal", byteArrayCopy.contentEquals(byteArray))
    }
}