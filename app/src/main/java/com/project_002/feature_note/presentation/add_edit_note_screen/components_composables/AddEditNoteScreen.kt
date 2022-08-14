package com.project_002.feature_note.presentation.add_edit_note_screen.components_composables

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.project_002.feature_note.domain.model.Note
import com.project_002.feature_note.presentation.add_edit_note_screen.AddEditNoteEvent
import com.project_002.feature_note.presentation.add_edit_note_screen.AddEditNoteViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//we pass the color also with the id
@Composable
fun AddEditNoteScreen(
    navController: NavController ,
    noteColor :Int ,
    viewmodel: AddEditNoteViewModel = hiltViewModel()
){
   //our states from the viewModel
    val titleState = viewmodel.noteTitle.value
    val contentState = viewmodel.noteContent.value

    val scaffoldState = rememberScaffoldState()

    val noteBackGroundAnimatable = remember {
        Animatable(
            Color(if (noteColor!= -1 ) noteColor else viewmodel.noteColor.value )
        )
    }

    val scope = rememberCoroutineScope()

    //one single time events
    LaunchedEffect(key1 = true ){
        viewmodel.eventFlow.collectLatest {event ->
            when(event){
                is AddEditNoteViewModel.UiEvents.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvents.SaveNote -> {
                    navController.navigateUp()
                }
            }

        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewmodel.onEvent(AddEditNoteEvent.SaveNote)
            }, backgroundColor = MaterialTheme.colors.primary) {
                //content of the floating button
                Icon(imageVector =Icons.Default.Save,
                    contentDescription = "Save Note")

            }
        },
        scaffoldState = scaffoldState
    ) {
        //the Colors icons will be just boxes but round
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackGroundAnimatable.value)
                .padding(16.dp)

        ) {
             Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(8.dp) ,
                 horizontalArrangement = Arrangement.SpaceBetween
             ) {
                Note.noteColors.forEach{color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(50.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewmodel.noteColor.value == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackGroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewmodel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )

                }
             }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                                viewmodel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                textStyle = null ,
                onFocusChange = {
                    viewmodel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible ,
                singleLine = true ,

            )

            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewmodel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                textStyle = null ,
                onFocusChange = {
                    viewmodel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible ,
                modifier = Modifier.fillMaxHeight()

                )
        }
    }
}