package br.com.module.pokeapi.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import br.com.module.pokeapi.R
import br.com.module.pokeapi.ui.PokemonActivity
import br.com.module.pokeapi.ui.PokemonViewModel
import br.com.module.pokeapi.utils.Resource
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_details.*

class DetailFragment : Fragment(R.layout.fragment_details) {

    lateinit var pokemonViewModel: PokemonViewModel

    val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = args.result

        pokemonViewModel = (activity as PokemonActivity).pokemonViewModel

        var name = result.name

        pokemonViewModel.getPokemon(name!!)

        pokemonViewModel.pokemon.observe(viewLifecycleOwner, Observer {
                response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { pokemonResponse ->
                        Glide.with(this).load(pokemonResponse.sprites.back_default).into(idImg)
                        id01.text = "Location area: "+ pokemonResponse.location_area_encounters
                        id02.text = "Name: "+ pokemonResponse.name
                        id03.text = "Height: "+ pokemonResponse.height
                        id04.text = "Weight: "+ pokemonResponse.weight
                        id05.text = "Order: "+ pokemonResponse.order
                        id06.text = "Base experience:"+ pokemonResponse.base_experience
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

    private fun hideProgressBar(){
        paginationProgressBarDetails.visibility = View.INVISIBLE

    }

    private fun showProgressBar(){
        paginationProgressBarDetails.visibility = View.VISIBLE
    }
}