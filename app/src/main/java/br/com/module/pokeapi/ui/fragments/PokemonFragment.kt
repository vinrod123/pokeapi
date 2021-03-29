package br.com.module.pokeapi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.module.pokeapi.R
import br.com.module.pokeapi.adapters.PokemonAdapter
import br.com.module.pokeapi.ui.PokemonActivity
import br.com.module.pokeapi.ui.PokemonViewModel
import br.com.module.pokeapi.utils.Constants.Companion.QUERY_PAGE_SIZE
import br.com.module.pokeapi.utils.Resource
import kotlinx.android.synthetic.main.fragment_pokemon.*

class PokemonFragment : Fragment(R.layout.fragment_pokemon) {

    lateinit var pokemonViewModel: PokemonViewModel

    lateinit var pokemonAdapter: PokemonAdapter

    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokemonViewModel = (activity as PokemonActivity).pokemonViewModel

        setupRecyclerView()

        pokemonAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("result", it)
            }

            findNavController().navigate(R.id.action_pokemonFragments_to_detailFragments,
                bundle
            )
        }

        pokemonViewModel.pokemons.observe(viewLifecycleOwner, Observer {
                response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { pokemonResponse ->
                        pokemonAdapter.differ.submitList(pokemonResponse.results.toList())
                        val totalPages = pokemonResponse.count / QUERY_PAGE_SIZE + 2
                        isLastPage = pokemonViewModel.pokemonPage == totalPages
                        if(isLastPage)
                            rvPokemon.setPadding(0,0,0,0)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {message ->
                        Log.e(TAG,"An error occured: $message")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        } )
    }

    private fun setupRecyclerView(){
        pokemonAdapter = PokemonAdapter()
        rvPokemon.apply {
            adapter = pokemonAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@PokemonFragment.scrollLister)
        }
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollLister = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstItemVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPaging = !isLoading && !isLastPage
            val isAtLastItem = firstItemVisiblePosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstItemVisiblePosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE;
            val shouldPaginate = isNotLoadingAndNotLastPaging && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                pokemonViewModel.getPokemons()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }
}