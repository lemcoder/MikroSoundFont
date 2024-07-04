package pl.lemanski.pandamidi.core

data class Interval(private val semitones: Int) {

    fun fromNote(note: Note): Note {
        val newValue = note.value + semitones
        require(newValue in 0..127) {
            "Resulting note is out of range"
        }
        return Note(newValue)
    }

    companion object {
        val Unison = Interval(0)
        val MinorSecond = Interval(1)
        val MajorSecond = Interval(2)
        val MinorThird = Interval(3)
        val MajorThird = Interval(4)
        val PerfectFourth = Interval(5)
        val Tritone = Interval(6)
        val PerfectFifth = Interval(7)
        val MinorSixth = Interval(8)
        val MajorSixth = Interval(9)
        val MinorSeventh = Interval(10)
        val MajorSeventh = Interval(11)
        val Octave = Interval(12)

        fun between(first: Note, second: Note): Interval {
            return Interval(second.value - first.value)
        }
    }
}
