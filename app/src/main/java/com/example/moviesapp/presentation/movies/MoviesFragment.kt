package com.example.moviesapp.presentation.movies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.api.CodeResponse
import com.example.moviesapp.databinding.FragmentMoviesBinding
import com.example.moviesapp.presentation.models.ScreenState
import com.example.moviesapp.presentation.movies.recycler.MoviesListAdapter
import com.example.moviesapp.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel by viewModels<MoviesViewModel>()

    private val onScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()

                if ((lastVisibleItemPos == totalItemCount - 1)
                    && (isCreateLoader) && totalItemCount > 0
                ) {
                    loadingNextPage()
                }
            }
        }
    }

    private var isCreateLoader = true
    private var pressedTime: Long = 0

    @Inject
    lateinit var moviesAdapter: MoviesListAdapter

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
        setupRecyclerView()
        setOnClickSearchAndClearButton()
        setTextWatcherForEditText()
        initObserve()
    }

    private fun isOnScrollListenerRV(isActive: Boolean) {
        with(binding.rvMoviesList) {
            if (isActive) addOnScrollListener(onScrollListener)
            else removeOnScrollListener(onScrollListener)
        }
    }

    private fun setOnClickSearchAndClearButton() {
        with(binding) {
            materialCardViewSearch.setOnClickListener { doOnSearch() }
            materialCardViewCancel.setOnClickListener { doOnCleanSearch() }
        }
    }


    private fun doOnSearch() {
        with(binding) {
            moviesAdapter.submitList(emptyList())
            requireActivity().hideKeyboard()
            editText.clearFocus()
            val query = editText.text.trim().toString()
            viewModel.searchMovies(query)
        }
    }


    private fun doOnCleanSearch() {
        binding.editText.text.clear()
    }

    private fun setTextWatcherForEditText() {
        with(binding) {
            editText.doAfterTextChanged {
                if (it.toString().isEmpty()) {
                    moviesAdapter.submitList(emptyList())
                    rvMoviesList.smoothScrollToPosition(0)
                    materialCardViewCancel.isVisible = false
                    requireActivity().hideKeyboard()
                    editText.clearFocus()
                    viewModel.loadAllMovies()
                } else {
                    if (it.toString().isNotEmpty()) {
                        materialCardViewCancel.isVisible = true
                    }
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            rvMoviesList.isVisible = !isLoading
            loader.isVisible = isLoading
        }
    }

    private fun showLoadingNextPage() {
        with(binding) {
            isCreateLoader = true
            rvMoviesList.isVisible = true
            loader.isVisible = false
        }
    }

    private fun showError(message: String, exception: String) {
        if (exception.contains(CodeResponse.TOO_MANY_REQUESTS.code)) {
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

    private fun initObserve() {
        lifecycleScope.launchWhenStarted {
            viewModel.screenState.collect { screenState ->
                when (screenState) {
                    ScreenState.Loading -> showLoading(true)
                    ScreenState.LoadingNextPage -> showLoadingNextPage()
                    is ScreenState.Content -> {
                        moviesAdapter.submitList(screenState.content)
                        showLoading(false)
                    }
                    is ScreenState.Error -> showError(screenState.text, screenState.exception)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isOnScrollListenerRecyclerView.collect {
                isOnScrollListenerRV(it)
            }
        }
    }

    private fun loadingNextPage() {
        moviesAdapter.createLoader()
        viewModel.loadingNextPage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }
}