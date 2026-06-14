package com.example.estagiotrackerapp.api

import retrofit2.Call
import com.example.estagiotrackerapp.models.Usuario
import com.example.estagiotrackerapp.models.Vaga
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("listar-vagas")
    fun buscarVagasDoUsuario(@Query("usuarioId") idUsuario: Long): Call<List<Vaga>>

    @DELETE("api/usuarios/deletar-usuario")
    fun deletarUsuario(@Query("id") id: Long): Call<Void>
}