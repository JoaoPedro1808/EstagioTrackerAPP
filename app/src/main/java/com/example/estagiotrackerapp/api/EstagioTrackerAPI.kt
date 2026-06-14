package com.example.estagiotrackerapp.api

import retrofit2.Call
import com.example.estagiotrackerapp.models.Usuario
import com.example.estagiotrackerapp.models.Vaga
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.math.BigDecimal

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

    @POST("criar-vaga")
    @FormUrlEncoded
    fun cadastrarVaga(
        @Field("nomeEmpresa") nomeEmpresa: String,
        @Field("nomeVaga") nomeVaga: String,
        @Field("salario") salario: BigDecimal,
        @Field("statusKanban") statusKanban: String,
        @Field("modelo") modelo: String,
        @Field("descricao") descricao: String,
        @Field("usuarioId") usuarioId: Long
    ) : Call<Vaga>

    @DELETE("deletar-vaga")
    fun deletarVaga(@Query("id") id: Long): Call<Void>

    @PUT("atualizar-vaga")
    fun atualizarVaga(
        @Query("id") id: Long,
        @Query("nomeEmpresa") nomeEmpresa: String,
        @Query("nomeVaga") nomeVaga: String,
        @Query("salario") salario: String,
        @Query("statusKanban") statusKanban: String,
        @Query("modelo") modelo: String,
        @Query("descricao") descricao: String
    ) : Call<Vaga>

    @GET("buscar-por-nome")
    fun buscarVagaPorNome(@Query("nomeVaga") nomeVaga: String): Call<List<Vaga>>

    @GET("buscar-por-id")
    fun buscarVagaPorId(@Query("id") id: Long): Call<Vaga>
}