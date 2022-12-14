package com.project_002.feature_note.domain.use_cases

data class NoteUseCases (
    val getNotesUseCase : GetNotesUseCase,
    val deleteNoteUseCase : DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val getNoteUseCase : GetNoteUseCase
        )