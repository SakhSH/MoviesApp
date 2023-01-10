package com.example.moviesapp.presentation.critics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.moviesapp.databinding.FragmentCriticsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CriticsFragment : Fragment() {

    private val viewModel by viewModels<CriticsViewModel>()

    private var _binding: FragmentCriticsBinding? = null
    private val binding: FragmentCriticsBinding
        get() = _binding ?: throw RuntimeException("FragmentCriticsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCriticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CriticsFragment {
            return CriticsFragment()
        }
    }

}