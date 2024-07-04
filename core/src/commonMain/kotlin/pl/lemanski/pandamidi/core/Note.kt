package pl.lemanski.pandamidi.core

import java.lang.IllegalStateException
import kotlin.ranges.contains

//+------+----+----+----+----+----+----+----+----+----+----+----+
//| Note | -1 |  0 |  1 |  2 |  3 |  4 |  5 |  6 |  7 |  8 |  9 |
//+------+----+----+----+----+----+----+----+----+----+----+----+
//|  C   |  0 | 12 | 24 | 36 | 48 | 60 | 72 | 84 | 96 |108 |120 |
//|  C#  |  1 | 13 | 25 | 37 | 49 | 61 | 73 | 85 | 97 |109 |121 |
//|  D   |  2 | 14 | 26 | 38 | 50 | 62 | 74 | 86 | 98 |110 |122 |
//|  D#  |  3 | 15 | 27 | 39 | 51 | 63 | 75 | 87 | 99 |111 |123 |
//|  E   |  4 | 16 | 28 | 40 | 52 | 64 | 76 | 88 |100 |112 |124 |
//|  F   |  5 | 17 | 29 | 41 | 53 | 65 | 77 | 89 |101 |113 |125 |
//|  F#  |  6 | 18 | 30 | 42 | 54 | 66 | 78 | 90 |102 |114 |126 |
//|  G   |  7 | 19 | 31 | 43 | 55 | 67 | 79 | 91 |103 |115 |127 |
//|  G#  |  8 | 20 | 32 | 44 | 56 | 68 | 80 | 92 |104 |116 |    |
//|  A   |  9 | 21 | 33 | 45 | 57 | 69 | 81 | 93 |105 |117 |    |
//|  A#  | 10 | 22 | 34 | 46 | 58 | 70 | 82 | 94 |106 |118 |    |
//|  B   | 11 | 23 | 35 | 47 | 59 | 71 | 83 | 95 |107 |119 |    |
//+------+----+----+----+----+----+----+----+----+----+----+----+

/**
 * This class represents a MIDI note.
 */

@JvmInline
value class Note(val value: Int) : Comparable<Note> {
    init {
        require(value in 0..127) {
            "Note is 7 bits"
        }
    }

    /**
     * Returns the octave of the note. (MIDI standard - 12 for C0).
     */
    fun octave(): Int {
        return (value / 12) - 1
    }

    /**
     * Returns the step of the note in the octave beginning from C.
     * @return value in between 0 and 11
     */
    fun note(): Int {
        return value % 12
    }

    fun addInterval(interval: Interval): Note {
        return interval.fromNote(this)
    }

    fun intervalTo(other: Note): Interval {
        return Interval.between(this, other)
    }

    override fun compareTo(other: Note): Int {
        return this.value - other.value
    }

    override fun toString(): String = when (this.note()) {
        0 -> "C"
        1 -> "C#/Db"
        2 -> "D"
        3 -> "D#/Eb"
        4 -> "E"
        5 -> "F"
        6 -> "F#/Gb"
        7 -> "G"
        8 -> "G#/Ab"
        9 -> "A"
        10 -> "A#/Bb"
        11 -> "B"
        else -> throw IllegalStateException()
    } + octave()
}