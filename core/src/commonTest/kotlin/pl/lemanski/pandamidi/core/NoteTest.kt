package pl.lemanski.pandamidi.core

import kotlin.test.Test
import kotlin.test.assertEquals

class stepInCTest {

    @Test
    fun shouldReturnValidStepInC() {
        val expectedNote = "C#/Db0"
        val note = Note(13)

        assertEquals(expectedNote, note.toString())
    }

    @Test
    fun shouldReturnValidNoteAfterAddingInterval() {
        val expectedNote = "E1"
        val note = Note(24) // C1
        assertEquals(expectedNote, note.addInterval(Interval.MajorThird).toString())
    }

    @Test
    fun shouldGetValidIntervalBetweenNotes() {
        val expectedInterval = Interval.MinorThird

        val note1 = Note(24)
        val note2 = Note(27)
        assertEquals(expectedInterval, note1.intervalTo(note2))
    }
}