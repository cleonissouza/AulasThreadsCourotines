package com.example.aulathreadscourotines.api

import com.example.aulathreadscourotines.model.Comentario
import com.example.aulathreadscourotines.model.Postagem
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostagemAPI {

    @GET("posts")
    suspend fun recuperarPostagens(): retrofit2.Response<List<Postagem>>

    @GET("posts/{id}")
    suspend fun recuperarPostagemUnica(
        @Path("id") id: Int
    ): retrofit2.Response<Postagem>

    @GET("posts/{id}/comments")
    suspend fun recuperarComentariosParaPostagem(
        @Path("id") id: Int
    ): retrofit2.Response<List<Comentario>>

    @GET("comments")
    suspend fun recuperarComentariosParaPostagemQurery(
        @Query("postId") id: Int
    ): retrofit2.Response<List<Comentario>>

    @POST("posts")
    suspend fun salvarPostagem(
        @Body postagem: Postagem
    ): retrofit2.Response<Postagem>
}