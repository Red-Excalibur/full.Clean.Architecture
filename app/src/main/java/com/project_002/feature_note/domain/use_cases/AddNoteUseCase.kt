package com.project_002.feature_note.domain.use_cases

import com.project_002.feature_note.domain.model.InvalidNoteException
import com.project_002.feature_note.domain.model.Note
import com.project_002.feature_note.domain.repository.NoteRepository

class AddNoteUseCase (
    private val reposiroty : NoteRepository
        ) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note : Note){
        //we validate first
        if(note.title.isBlank()){
            throw InvalidNoteException("The Title of The Note Can't Be Empty.")
        }
        if(note.content.isBlank()){
            throw InvalidNoteException("The Content of The Note Can't Be Empty.")
        }
        reposiroty.insertNote(note)  // here we are sure it's valid
    }
}