package com.project_002.feature_note.domain.use_cases

import com.project_002.feature_note.domain.model.Note
import com.project_002.feature_note.domain.repository.NoteRepository

class GetNoteUseCase (
    private val repository: NoteRepository
        ) {
    suspend operator fun invoke (id:Int) : Note?  {
         return repository.getNoteById(id)
    }
}