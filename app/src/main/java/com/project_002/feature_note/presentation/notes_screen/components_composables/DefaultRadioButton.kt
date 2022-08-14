package com.project_002.feature_note.presentation.notes_screen.components_composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DefaultRadioButton(
    text:String,
    selected:Boolean,
    onSelect:()->Unit,
    modifier: Modifier =Modifier
){
     Row(
       modifier=modifier,
       verticalAlignment = Alignment.CenterVertically
     ){
         RadioButton(selected = selected,
                     onClick = onSelect,
                     colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary   ,
                         unselectedColor =MaterialTheme.colors.background  ,
                     )
             )
         Spacer(modifier = Modifier.width(8.dp))
         Text(text =text ,
           style = MaterialTheme.typography.body1
             )
     }
}
