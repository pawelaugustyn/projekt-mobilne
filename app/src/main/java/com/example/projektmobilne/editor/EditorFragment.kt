package com.example.projektmobilne.editor


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
import com.example.projektmobilne.databinding.FragmentEditorBinding

/**
 * A simple [Fragment] subclass.
 */
class EditorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentEditorBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_editor, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = EditorFragmentArgs.fromBundle(arguments!!)
        val dataSource = NotesDatabase.getInstance(application).notesDatabaseDao
        val viewModelFactory = EditorViewModelFactory(arguments.noteId, dataSource)
        val editorViewModel = ViewModelProviders.of(
            this, viewModelFactory)
            .get(EditorViewModel::class.java)
        binding.editorViewModel = editorViewModel

        editorViewModel.navigateToNotes.observe(this, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                    EditorFragmentDirections.actionEditorFragmentToNotesFragment())
                editorViewModel.doneNavigating()
            }
        })

        return binding.root
    }


}
