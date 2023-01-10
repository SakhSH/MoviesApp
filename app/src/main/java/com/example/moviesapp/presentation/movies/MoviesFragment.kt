package com.example.moviesapp.presentation.movies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.Util.hideKeyboard
import com.example.moviesapp.databinding.FragmentMoviesBinding
import com.example.moviesapp.presentation.models.ScreenState
import com.example.moviesapp.presentation.movies.recycler.MoviesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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

        lifecycleScope.launchWhenStarted {
            viewModel.screenState.collect { screenState ->
                when (screenState) {
                    ScreenState.Content -> showLoading(false)
                    is ScreenState.Error -> showError(screenState.text, screenState.exception)
                    ScreenState.Loading -> showLoading(true)
                }
            }
        }

        setupRecyclerView()
        lifecycleScope.launchWhenStarted {
            viewModel.movies.collect { movies ->
                moviesAdapter.submitList(movies)
            }
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

        (binding.toolbar.menu.findItem(R.id.search).actionView as SearchView)
            .apply {
                setOnQueryTextListener(
                    object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            Toast.makeText(requireContext(), "submit", Toast.LENGTH_SHORT).show()
//                            requireActivity().hideKeyboard()
                            moviesAdapter.submitList(listOf())
                            if (query.isEmpty()) viewModel.loadAllMovies()

                            viewModel.searchMovies(query)
                            clearFocus()
                            return true
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            Toast.makeText(requireContext(), "text change", Toast.LENGTH_SHORT).show()
                            /*eventSender?.sendEvent(
                                PatientListEvent.SearchQueryChanged(
                                    searchQuery = newText
                                )
                            )*/
                            return true
                        }

                    }
                )
                setOnCloseListener {
                    Toast.makeText(requireContext(), "close", Toast.LENGTH_SHORT).show()
                    requireActivity().hideKeyboard()
                    clearFocus()
                    viewModel.loadAllMovies()
                    true
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val HTTP_429_TOO_MANY_REQUESTS = "429"

        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

}