package com.example.evav3.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.evav3.databinding.FragmentSettingsBinding // Import ViewBinding class
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // For Hilt dependency injection
class SettingsFragment : Fragment() {

    // Declare ViewBinding variable
    private var _binding: FragmentSettingsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using ViewBinding
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // You can access views using binding here, e.g.:
        // binding.textSettings.text = "Updated Text"

        return root
    }

    // Clean up the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}