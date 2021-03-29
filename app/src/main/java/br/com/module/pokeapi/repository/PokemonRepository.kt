package br.com.module.pokeapi.repository

import br.com.module.pokeapi.api.RetrofitInstance


class PokemonRepository(){

    suspend fun getPokemons(limit: Int, offset: Int) =
        RetrofitInstance.api.getPokemons(limit, offset)

    suspend fun getPokemon(name: String) =
        RetrofitInstance.api.getPokemon(name)

}