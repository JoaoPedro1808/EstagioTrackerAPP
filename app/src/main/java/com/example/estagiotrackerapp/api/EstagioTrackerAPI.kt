package com.example.estagiotrackerapp.api

import retrofit2.Call
import com.example.estagiotrackerapp.models.Usuario
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface EstagioTrackerAPI {
    @GET("api/usuarios/listar-usuarios")
    fun listarTodos(): Call<List<Usuario>>

    @POST("api/usuarios/criar-usuario")
    @FormUrlEncoded
    fun cadastrarUsuario(
        @Field("nome") nome: String,
        @Field("email") email: String,
        @Field("senha") senha: String
    ): Call<Usuario>
}