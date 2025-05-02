package com.example.evav3.ui.memories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.evav3.databinding.FragmentMemoriesBinding // Import ViewBinding class
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // For Hilt dependency injection
class MemoriesFragment : Fragment() {

    // Declare ViewBinding variable
    private var _binding: FragmentMemoriesBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using ViewBinding
        _binding = FragmentMemoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // You can access views using binding here, e.g.:
        // binding.textMemories.text = "Updated Text"

        return root
    }

    // Clean up the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}