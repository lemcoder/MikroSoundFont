package pl.lemanski.pandamidi.sequencer

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import org.tsf.tsf_get_presetcount
import org.tsf.tsf_load_filename
import platform.posix.printf

@OptIn(ExperimentalForeignApi::class)
actual fun loadSoundFont(path: String) {
    memScoped {
        val soundFont = tsf_load_filename("C:\\Users\\lenovo\\Downloads\\florestan-subset.sf2");
        val presetCount = tsf_get_presetcount(soundFont)
        printf("Presets: $presetCount")
    }
}