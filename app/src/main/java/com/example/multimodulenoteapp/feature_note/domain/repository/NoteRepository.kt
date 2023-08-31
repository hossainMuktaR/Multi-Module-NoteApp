package com.example.multimodulenoteapp.feature_note.domain.repository

import com.example.multimodulenoteapp.feature_note.domain.model.Note

interface NoteRepository {

    suspend fun getAllNotes(): List<Note>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}