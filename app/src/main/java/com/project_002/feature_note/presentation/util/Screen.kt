package com.project_002.feature_note.presentation.util

sealed class Screen
    (
    val Route:String){
  object NoteScreen : Screen("Note_Screen")
  object AddEditNoteScreen : Screen("Add_Edit_Note_Screen")
}
