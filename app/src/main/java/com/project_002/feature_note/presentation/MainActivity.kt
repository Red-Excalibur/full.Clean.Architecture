package com.project_002.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project_002.feature_note.presentation.add_edit_note_screen.components_composables.AddEditNoteScreen
import com.project_002.feature_note.presentation.notes_screen.components_composables.NotesScreen
import com.project_002.feature_note.presentation.util.Screen
import com.project_002.ui.theme.CleanArchitectureNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanArchitectureNoteAppTheme {

                  Surface(
                      color = MaterialTheme.colors.background
                  ) {
                      val navController = rememberNavController()
                      NavHost(
                          navController = navController, startDestination =
                          Screen.NoteScreen.Route
                      ) {
                          composable(route = Screen.NoteScreen.Route) {
                              NotesScreen(navController = navController)

                          }

                          composable(
                              route = Screen.AddEditNoteScreen.Route +
                                      "?noteId={noteId}&noteColor={noteColor}",
                              arguments = listOf(
                                  navArgument(
                                      name = "noteId"
                                  ) {
                                      //builder
                                      type = NavType.IntType
                                      defaultValue = -1

                                  },
                                  navArgument(
                                      name = "noteColor"
                                  ) {
                                      //builder
                                      type = NavType.IntType
                                      defaultValue = -1

                                  }
                              )

                          ) {
                              val color = it.arguments?.getInt("noteColor") ?: -1
                              AddEditNoteScreen(
                                  navController = navController,
                                  noteColor = color
                              )
                          }

                      }
                  }}
        }
    }
}

