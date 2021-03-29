package br.com.module.pokeapi.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.module.pokeapi.repository.PokemonRepository
import br.com.module.pokeapi.ui.PokemonViewModel

class ViewModelFactory(    val app: Application,
                           val pokemonRepository: PokemonRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PokemonViewModel(app, pokemonRepository) as T
    }
}