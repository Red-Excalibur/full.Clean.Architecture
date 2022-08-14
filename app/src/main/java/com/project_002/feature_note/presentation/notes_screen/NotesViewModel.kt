package com.project_002.feature_note.presentation.notes_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project_002.feature_note.domain.model.Note
import com.project_002.feature_note.domain.use_cases.NoteUseCases
import com.project_002.feature_note.domain.util.NoteOrder
import com.project_002.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
  private val noteUseCases : NoteUseCases
        ):ViewModel() {
    private val _state = mutableStateOf<NotesState>(NotesState())
    val state : State<NotesState> =_state

    //
    private var recentlyDeletedNote :Note?=null
    private var getNotesJob: Job?=null
    //this is the function we will trigger from our UI
    //now the job of the viewModel is either to call the useCases from that or Update the state(make the isLoading
    // true for example }
    //or do both ...
    init {
        //we wanna call it also here with some default order
        getNotes(NoteOrder.Date(OrderType.Descending))
    }
   fun onEvent(event : NotesEvent)  {
       when(event){
           is NotesEvent.Order -> {
               //we check if the order changed
               if (state.value.noteOrder::class == event.noteOrder::class
                   //we used ::class cuz they don't overRide the equals methode so
                   // they will compare references @
                   // ...data class here are just an object  so
                   //we can compare directly
                   && state.value.noteOrder.orderType
               == event.noteOrder.orderType) {
                    return
               }
               getNotes(event.noteOrder)  //we are calling it every time
           }
           is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNoteUseCase(event.note)
                    recentlyDeletedNote = event.note // we save it in that object
                }
           }
           is NotesEvent.RestoreNote -> {
               viewModelScope.launch {
                   noteUseCases.addNoteUseCase(recentlyDeletedNote ?: return@launch )
                   recentlyDeletedNote = null //reset it
               }

           }
           is NotesEvent.ToggleOrderSection -> {
               //for example here we just updated a state value
               _state.value = state.value.copy(
                   isOrderSectionVisible = !state.value.isOrderSectionVisible
               )
           }
       }
   }

    private fun getNotes(noteOrder: NoteOrder) {

        // we cancel the old one
        getNotesJob?.cancel()

            getNotesJob=  noteUseCases.getNotesUseCase(noteOrder).onEach { notes ->
                //on each emition we mean..on each new data sent from this producer
                _state.value=state.value.copy(
                    notes = notes,
                    noteOrder =noteOrder
                )

            }.launchIn(viewModelScope)

        //that return a flow that come from our data base
    }
}