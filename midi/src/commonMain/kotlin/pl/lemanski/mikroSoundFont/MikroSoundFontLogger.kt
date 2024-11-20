package pl.lemanski.mikroSoundFont

interface MikroSoundFontLogger {
    fun log(message: String)
}

expect fun getLogger(): MikroSoundFontLogger