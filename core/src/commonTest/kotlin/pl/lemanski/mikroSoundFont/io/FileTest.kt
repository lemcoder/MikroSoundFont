package pl.lemanski.mikroSoundFont.io

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertContentEquals

class FileTest {
    private val dir = "C:\\Users\\Mikolaj\\Desktop\\midi"

    @Test
    fun should_load_file_as_byte_array() {
        val byteArray = loadFile("$dir\\under.mid")
        assertTrue { byteArray.isNotEmpty() }
    }

    @Test
    fun should_save_file() {
        val byteArray = loadFile("$dir\\under.mid")
        saveFile(byteArray, "$dir\\under_copy.mid")
        val byteArrayCopy = loadFile("$dir\\under_copy.mid")

        assertContentEquals(byteArrayCopy, byteArray)
    }
}