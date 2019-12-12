package com.example.projektmobilne.notes


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.projektmobilne.R
import com.example.projektmobilne.database.NotesDatabase
import com.example.projektmobilne.databinding.FragmentNotesBinding

class NotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNotesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notes, container,
            false)
        val application = requireNotNull(this.activity).application

        val dataSource =
            NotesDatabase.getInstance(application).notesDatabaseDao
        val viewModelFactory = NotesViewModelFactory(dataSource,
            application)
        val notesViewModel =
            ViewModelProviders.of(
                this,
                viewModelFactory).get(NotesViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.notesViewModel = notesViewModel

        notesViewModel.navigateToEditor.observe(this, Observer {
                note ->
            note?.let {
                this.findNavController().navigate(
                    NotesFragmentDirections
                        .actionNotesFragmentToEditorFragment(note.noteId))
                notesViewModel.doneNavigating()
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }


}
