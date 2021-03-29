package br.com.module.pokeapi.api

import br.com.module.pokeapi.models.DetailResult
import br.com.module.pokeapi.models.Pokemon
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit")
        limit: Int = 10,
        @Query("offset")
        offset: Int = 10
    ): Response<Pokemon>

    @GET("pokemon-form/{name}/")
    suspend fun getPokemon(
        @Path("name")
        path: String
    ): Response<DetailResult>

}