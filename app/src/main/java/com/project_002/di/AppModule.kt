package com.project_002.di

import android.app.Application
import androidx.room.Room
import com.project_002.feature_note.data.data_source.NoteDataBase
import com.project_002.feature_note.data.repository.NoteRepositoryImpl
import com.project_002.feature_note.domain.repository.NoteRepository
import com.project_002.feature_note.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDataBase(app :Application) :NoteDataBase {
        return Room.databaseBuilder(
            app,
            NoteDataBase::class.java,
            NoteDataBase.DATABASE_NAME
        ).build()

    }

    @Provides
    @Singleton
    fun providesNoteRepository(db :NoteDataBase) : NoteRepository{
       return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository : NoteRepository) : NoteUseCases{
        return NoteUseCases (
            getNotesUseCase = GetNotesUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository)
                )
    }
}