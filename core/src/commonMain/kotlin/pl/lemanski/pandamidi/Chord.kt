package pl.lemanski.pandamidi

sealed interface Chord {
    fun notes(): List<Note>
    fun chordType(): Type

    enum class Type {
        MAJOR,
        MINOR
    }
}