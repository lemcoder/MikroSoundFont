package pl.lemanski.pandamidi

class ChordBuilder(private val root: Note) {
    private val notes = mutableListOf<Note>()

    init {
        notes.add(root)
    }

    fun addIntervalToRoot(interval: Interval): ChordBuilder {
        notes.add(root.addInterval(interval))
        return this
    }

    fun build(): List<Note> {
        if (notes.size < 3 || notes.size > 7) {
            throw IllegalArgumentException("Chord must have at least 3 notes and no more than 7")
        }

        if (notes.distinct().size != notes.size) {
            throw IllegalArgumentException("Chord must have unique notes")
        }

        return notes.sorted()
    }
}