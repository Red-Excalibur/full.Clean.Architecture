package com.project_002.feature_note.presentation.notes_screen.components_composables

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.project_002.feature_note.domain.model.Note
import com.project_002.feature_note.presentation.notes_screen.NotesEvent
import com.project_002.feature_note.presentation.notes_screen.NotesViewModel
import com.project_002.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    navController: NavController ,
    viewModel : NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    //for the snackbar
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          navController.navigate(Screen.AddEditNoteScreen.Route)
                          } ,
                backgroundColor = MaterialTheme.colors.primary ,
                
            ) {
                        //the content of that
                Icon(imageVector = Icons.Default.Add,
                    contentDescription ="add note" )
            }
        },
        scaffoldState = scaffoldState
    ) {
              Column(
                  modifier = Modifier
                      .fillMaxSize()
                      .padding(16.dp)

              ) {
                 Row(
                      modifier = Modifier
                          .fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceBetween ,
                     verticalAlignment = Alignment.CenterVertically
                 ) {

                      Text (
                          text =  "Your Note ",
                          style = MaterialTheme.typography.h4

                              )
                     IconButton(onClick = {
                         viewModel.onEvent(NotesEvent.ToggleOrderSection)
                     }) {
                         Icon(imageVector =Icons.Default.Sort,
                             contentDescription ="Sort" )
                     }
                 }
                  //its an animation we can add what content we want to it
                  AnimatedVisibility(
                      visible = state.isOrderSectionVisible,
                      enter = fadeIn() + slideInVertically (),
                      exit = fadeOut()+ slideOutVertically ()

                  ) {
                       OrderSection(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(vertical = 16.dp),
                           noteOrder = state.noteOrder,
                           onOrderChange = {
                               viewModel.onEvent(NotesEvent.Order(it))
                           }

                       )
                  }
                  Spacer(modifier = Modifier.height(16.dp))
                  LazyColumn(
                      modifier = Modifier
                          .fillMaxSize()
                  ){
                    items(state.notes){note->
                        NoteItem(
                            note = note,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                           //here we pass arguments of the item
                                           navController.navigate(Screen.AddEditNoteScreen.Route
                                           +"?noteId=${note.id}&noteColor=${note.color}")

                                },
                            onDeleteClick = {
                           viewModel.onEvent(NotesEvent.DeleteNote(note))
                                scope.launch {
                                 val result =   scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Note Deleted",
                                        actionLabel = "Undo"
                                    )
                                    if(result == SnackbarResult.ActionPerformed){
                                        viewModel.onEvent(NotesEvent.RestoreNote)
                                        
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                  }
              }
    }

}