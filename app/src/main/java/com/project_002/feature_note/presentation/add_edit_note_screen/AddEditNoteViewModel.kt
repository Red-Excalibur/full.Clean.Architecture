package com.project_002.feature_note.presentation.add_edit_note_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project_002.feature_note.domain.model.InvalidNoteException
import com.project_002.feature_note.domain.model.Note
import com.project_002.feature_note.domain.use_cases.NoteUseCases
import com.project_002.feature_note.presentation.notes_screen.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor (
    private val noteUseCases : NoteUseCases,
    savedStateHandle: SavedStateHandle
//from hilt it contains the navigation arguments
        ):ViewModel() {

    private val _noteTitle = mutableStateOf<NoteTextFieldState>(NoteTextFieldState(
        hint = "Entre Title ... "
    ))
    val noteTitle : State<NoteTextFieldState> =_noteTitle

    private val _noteContent = mutableStateOf<NoteTextFieldState>(NoteTextFieldState(
        hint = "Entre Some Content ... "

    ))
    val noteContent : State<NoteTextFieldState> =_noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor : State<Int> =_noteColor

    //one time event(for toasts and snackbars
    private val _eventFlow = MutableSharedFlow<UiEvents>()
     val eventFlow = _eventFlow.asSharedFlow()

    //
    private var currentNoteId :Int? = null

    // getting the navigation arguments
    init {
        savedStateHandle.get<Int>("noteId")?.let{noteId ->
            if(noteId != -1 ){
                viewModelScope.launch {
                    noteUseCases.getNoteUseCase(noteId)?.also { note->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title ,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content ,
                            isHintVisible = false
                        )
                        _noteColor.value=note.color


                    }
                }
            }

        }
    }
    fun onEvent(event:AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused
                            && noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused
                            && _noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                         noteUseCases.addNoteUseCase(
                             Note(
                                 title = noteTitle.value.text ,
                                 content = noteContent.value.text ,
                                 timestamp = System.currentTimeMillis(),
                                 color = noteColor.value,
                                 //here we update or create

                             )
                         )
                        _eventFlow.emit(UiEvents.SaveNote)
                    }catch (e :InvalidNoteException ){
                        _eventFlow.emit(UiEvents.ShowSnackBar(
                            message = e.message ?: "Couldn't save note"
                        ))

                    }
                }
            }
        }
    }

    //u better create this in a seperate file
    sealed class UiEvents {
        data class ShowSnackBar(val message:String ) :UiEvents()
        object SaveNote : UiEvents()

    }


}