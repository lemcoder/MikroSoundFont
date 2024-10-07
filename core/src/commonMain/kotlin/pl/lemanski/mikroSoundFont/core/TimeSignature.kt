package pl.lemanski.mikroSoundFont.core

data class TimeSignature(
    val numerator: Int,
    val denominator: Int
) {
    init {
        require(numerator in 1..32)
        require(denominator in 1..32)
    }
}