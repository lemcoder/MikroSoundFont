package pl.lemanski.mikroSoundFont

import android.util.Log

actual fun getLogger(): MikroSoundFontLogger = object : MikroSoundFontLogger {
    override fun log(message: String) {
        Log.d("MikroSoundFont", message)
    }
}