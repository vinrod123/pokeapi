package br.com.module.pokeapi.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.module.pokeapi.models.DetailResult
import br.com.module.pokeapi.models.Pokemon
import br.com.module.pokeapi.repository.PokemonRepository
import br.com.module.pokeapi.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class PokemonViewModel(val app: Application, val pokemonRepository: PokemonRepository) : ViewModel() {

    val pokemons: MutableLiveData<Resource<Pokemon>> = MutableLiveData()
    var mResultResponse: Pokemon? = null
    var pokemonPage = 10

    val pokemon: MutableLiveData<Resource<DetailResult>> = MutableLiveData()

    init {
        getPokemons()
    }

    fun getPokemons() = viewModelScope.launch {
        pokemons.postValue(Resource.Loading())
        val response = pokemonRepository.getPokemons(pokemonPage, pokemonPage)
        pokemons.postValue(handleResponse(response))
    }

    fun getPokemon(name: String) = viewModelScope.launch {
        pokemon.postValue(Resource.Loading())
        val response = pokemonRepository.getPokemon(name)
        pokemon.postValue(handleResponsePokemon(response))
    }

    private fun handleResponsePokemon(response: Response<DetailResult>): Resource<DetailResult>? {
        if(response.isSuccessful){
            response.body()?.let {
                detailResult ->
                return Resource.Success(detailResult)
            }
        }
        return Resource.Error(response.message())
    }

    fun handleResponse(response: Response<Pokemon>) : Resource<Pokemon>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                pokemonPage = pokemonPage + 10
                if(resultResponse == null)
                    mResultResponse = resultResponse
                else{
                    val oldResult = mResultResponse?.results
                    val newResult = resultResponse?.results
                    oldResult?.addAll(newResult)
                }
                return Resource.Success(mResultResponse ?: resultResponse )
            }
        }
        return Resource.Error(response.message())
    }
}