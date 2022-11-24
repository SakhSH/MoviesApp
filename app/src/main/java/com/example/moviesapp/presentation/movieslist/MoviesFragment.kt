package com.example.moviesapp.presentation.movieslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.presentation.models.ScreenState
import com.example.moviesapp.presentation.movieslist.recycler.MoviesListAdapter
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel by viewModels<MoviesViewModel>()

    @Inject
    lateinit var moviesAdapter: MoviesListAdapter

    private var isLoadingItem = true
    private var isListLoading = false
    private var isCreateLoader = true
    private var pressedTime: Long = 0

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = _binding ?: throw RuntimeException("FragmentMoviesBinding == null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (pressedTime + 2000 > System.currentTimeMillis()) {
                        requireActivity().finish()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.on_back_pressed_text),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    pressedTime = System.currentTimeMillis()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isListLoading = true

        viewModel.screenState.observe(viewLifecycleOwner) {
            when (it) {
                ScreenState.Content -> showLoading(false)
                is ScreenState.Error -> showError(it.text, it.exception)
                ScreenState.Loading -> showLoading(true)
            }
        }
        setupRecyclerView()
        viewModel.movies.observe(viewLifecycleOwner) {
            moviesAdapter.submitList(it)
        }

        binding.rvMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPos == totalItemCount - 1) {
                    if (!isListLoading || !isCreateLoader) {
                        return
                    }
                    loadingNextPage()
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoadingItem) {
            binding.rvMoviesList.isVisible = !isLoading
            binding.loader.isVisible = isLoading
        } else {
            isCreateLoader = true
            binding.rvMoviesList.isVisible = true
            binding.loader.isVisible = false
        }
    }

    private fun showError(message: String, exception: String) {
        if (exception.contains(HTTP_429_TOO_MANY_REQUESTS)) {
            isCreateLoader = false
            moviesAdapter.createTooManyRequest()
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            binding.rvMoviesList.isVisible = false
            binding.loader.isVisible = false
        }
    }

    private fun setupRecyclerView() {
        binding.rvMoviesList.adapter = moviesAdapter
        moviesAdapter.tryAgainLoadAllMovies = {
            loadingNextPage()
        }
    }

    private fun loadingNextPage() {
        isLoadingItem = false
        moviesAdapter.createLoader()
        viewModel.loadingNextPage()
    }

    companion object {
        private const val HTTP_429_TOO_MANY_REQUESTS = "429"
    }

}