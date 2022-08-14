package com.project_002.feature_note.presentation.add_edit_note_screen.components_composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import java.time.format.TextStyle

@Composable
fun TransparentHintTextField(
    text:String,
    hint:String,
    modifier:Modifier =Modifier,
    isHintVisible :Boolean =true,
    onValueChange: (String)-> Unit,
    textStyle : TextStyle ? = null  ,
    singleLine:Boolean = false,
    onFocusChange:(FocusState)-> Unit
    ){
      Box(
          modifier = modifier
      ) {
          BasicTextField (
              value = text,
              onValueChange = onValueChange,
              singleLine = singleLine,
//              textStyle = textStyle ,
              modifier = Modifier
                  .fillMaxWidth()
                  .onFocusChanged {
                      onFocusChange(it)
                  }
          )
       if(isHintVisible){
           Text(text = hint, color = Color.DarkGray)

       }
      }
}