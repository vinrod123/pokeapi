package br.com.module.pokeapi.models

data class Pokemon(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<Result>
)