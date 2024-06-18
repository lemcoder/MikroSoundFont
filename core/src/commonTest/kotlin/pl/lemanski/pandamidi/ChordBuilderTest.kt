package pl.lemanski.pandamidi

import org.junit.Test
import kotlin.test.assertContentEquals

class ChordBuilderTest {
    @Test
    fun shouldCreateCorrectNotesForMinorTriad() {
        val expectedCMinorTriadNotes = listOf(Note(24), Note(27), Note(31)) // C E G
        val cMinorTriad = ChordBuilder(Note(24)).minorTriad()

        assertContentEquals(cMinorTriad, expectedCMinorTriadNotes)
    }

    @Test
    fun shouldCreateCorrectNotesForMajorTriad() {
        val expectedAMajorTriadNotes = listOf(Note(33), Note(37), Note(40)) // A C# E
        val aMajorTriad = ChordBuilder(Note(33)).majorTriad()

        assertContentEquals(aMajorTriad, expectedAMajorTriadNotes)
    }

    @Test
    fun shouldCreateCorrectNotesForDiminishedTriad() {
        val expectedDDiminishedTriadNotes = listOf(Note(26), Note(29), Note(32)) // D F G#
        val dMinorTriad = ChordBuilder(Note(26)).diminishedTriad()

        assertContentEquals(dMinorTriad, expectedDDiminishedTriadNotes)
    }

    @Test
    fun shouldCreateCorrectNotesForAugmentedTriad() {
        val expectedAAugmentedTriadNotes = listOf(Note(33), Note(37), Note(41)) // A C# F
        val aAugmentedTriad = ChordBuilder(Note(33)).augmentedTriad()

        assertContentEquals(aAugmentedTriad, expectedAAugmentedTriadNotes)
    }
}