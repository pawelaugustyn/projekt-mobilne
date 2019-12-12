package com.example.projektmobilne.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projektmobilne.database.Note
import com.example.projektmobilne.database.NotesDatabaseDao
import kotlinx.coroutines.*

class EditorViewModel(
    private val noteKey: Long = 0L,
    val database: NotesDatabaseDao) : ViewModel() {
    private val _navigateToNotes = MutableLiveData<Boolean?>()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val navigateToNotes: LiveData<Boolean?>
        get() = _navigateToNotes

    fun doneNavigating() {
        _navigateToNotes.value = null
    }

    fun loadText(): String {
        val note = database.get(noteKey) ?: Note()
        return note.noteText
    }

    fun onSave(text: String) {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val note = database.get(noteKey) ?: Note()
                note.noteText = text
                if (note.noteId != 0L) {
                    database.update(note)
                } else {
                    database.insert(note)
                }
            }
            // Setting this state variable to true will alert the observer and trigger navigation.
            _navigateToNotes.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}