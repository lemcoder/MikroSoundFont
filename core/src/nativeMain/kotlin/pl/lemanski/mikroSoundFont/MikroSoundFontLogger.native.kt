package pl.lemanski.mikroSoundFont

actual fun getLogger(): MikroSoundFontLogger = object : MikroSoundFontLogger {
    override fun log(message: String) {
        println(message)
    }
}