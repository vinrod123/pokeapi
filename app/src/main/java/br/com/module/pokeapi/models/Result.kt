package br.com.module.pokeapi.models

import java.io.Serializable

data class Result(
    val name: String?,
    val url: String?
) : Serializable