package com.project_002.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project_002.ui.theme.*

@Entity
data class Note(
    val title:String,
    val content:String,
    val timestamp:Long,
    val color:Int,
    @PrimaryKey val id:Int?=null
){
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

//just for validation stuff
class InvalidNoteException(message : String):Exception(message)
   //and we use it in the AddNote Use case
