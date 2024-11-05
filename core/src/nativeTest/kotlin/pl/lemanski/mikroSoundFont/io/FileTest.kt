package pl.lemanski.mikroSoundFont.io

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertContentEquals

class FileTest {
    private val dir = "C:\\Users\\Mikolaj\\Desktop\\midi"

    @Test
    fun should_load_file_as_byte_array() {
        val byteArray = loadFile("$dir\\venture.mid")
        assertTrue { byteArray.isNotEmpty() }
    }

    @Test
    fun should_save_file() {
        val byteArray = loadFile("$dir\\venture.mid")
        saveFile(byteArray, "$dir\\venture_copy.mid")
        val byteArrayCopy = loadFile("$dir\\venture_copy.mid")

        assertContentEquals(byteArrayCopy, byteArray)
    }
}