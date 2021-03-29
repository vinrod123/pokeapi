package br.com.module.pokeapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import br.com.module.pokeapi.R
import br.com.module.pokeapi.repository.PokemonRepository
import br.com.module.pokeapi.utils.ViewModelFactory

class PokemonActivity : AppCompatActivity() {

    lateinit var pokemonViewModel: PokemonViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)

       val repository = PokemonRepository()
       val viewModelProviderFactory = ViewModelFactory(application, repository)
       pokemonViewModel = ViewModelProvider(this,viewModelProviderFactory).get(PokemonViewModel::class.java)

    }
}
