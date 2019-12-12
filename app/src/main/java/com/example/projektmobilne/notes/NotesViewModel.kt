package com.example.projektmobilne.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.projektmobilne.database.Note
import com.example.projektmobilne.database.NotesDatabaseDao
import com.example.projektmobilne.formatNotes
import kotlinx.coroutines.*

class NotesViewModel(
    val database: NotesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private var currentNote = MutableLiveData<Note?>()
    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val notes = database.getAllNotes()
    private val _navigateToEditor = MutableLiveData<Note>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent
    val navigateToEditor: LiveData<Note>
        get() = _navigateToEditor
    val notesString = Transformations.map(notes) { notes ->
        formatNotes(notes, application.resources)
    }
    val neededNotesButtonVisibility = Transformations.map(notes) {
        it?.isNotEmpty()
    }

    init {
        getNotes()
    }

    fun doneNavigating() {
        _navigateToEditor.value = null
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    fun onClear() {
        uiScope.launch {
            clear()
            currentNote.value = null
            _showSnackbarEvent.value = true
        }
    }

    fun createNewNote() {
        uiScope.launch {
            _navigateToEditor.value = Note(timeCreated = System.currentTimeMillis())
        }
    }

    fun editLastNote() {
        uiScope.launch {
            val note = currentNote.value ?: return@launch
            _navigateToEditor.value = note
        }
    }

    private fun getNotes() {
        uiScope.launch {
            currentNote.value = getNote()
        }
    }

    private suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            database.insert(note)
        }
    }

    private suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            database.update(note)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun getNote(): Note? {
        return withContext(Dispatchers.IO) {
            val lastNote = database.getLastNote()
            lastNote
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}