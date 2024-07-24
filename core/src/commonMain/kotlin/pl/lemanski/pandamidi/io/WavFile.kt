package pl.lemanski.pandamidi.io

data class WavFile(
    val header: WavFileHeader,
    val data: ByteArray
)

expect fun WavFile.save(name: String)