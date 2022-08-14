package com.project_002.feature_note.presentation.notes_screen

import com.project_002.feature_note.domain.model.Note
import com.project_002.feature_note.domain.util.NoteOrder
import com.project_002.feature_note.domain.util.OrderType

// a class that have the different states for our screen..could be a sealed class i guess too
data class NotesState(
    val notes : List<Note> = emptyList(),
    val noteOrder : NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible : Boolean = false
)

//so this is kind of the state of our screen ...with initial values

//now we need a sealed class to collect the event from the Ui ..from the user...his clicks and stuff
//onEvent