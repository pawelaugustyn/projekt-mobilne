package com.example.projektmobilne.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projektmobilne.database.NotesDatabaseDao

class EditorViewModelFactory(
    private val noteKey: Long,
    private val dataSource: NotesDatabaseDao) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditorViewModel::class.java))
        {
            return EditorViewModel(noteKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}