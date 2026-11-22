package cl.duoc.agricol.remote

import cl.duoc.agricol.model.PublicacionRemota
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PublicacionesApi {
    // Trae todas las publicaciones
    @GET("publicacion")
    suspend fun obtenerPublicaciones(): List<PublicacionRemota>

    // Guarda una publicación nueva y devuelve la que quedó registrada
    @POST("publicacion")
    suspend fun crearPublicacion(@Body publicacion: PublicacionRemota): PublicacionRemota

    // Borra la publicación con ese id (Xano responde sin contenido útil)
    @DELETE("publicacion/{id}")
    suspend fun eliminarPublicacion(@Path("id") id: Int): Response<Unit>
}

object RetrofitClient {
    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:GOcHn4ic/"

    private fun retrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun crearApi(baseUrl: String = BASE_URL): PublicacionesApi =
        retrofit(baseUrl).create(PublicacionesApi::class.java)

    fun crearAuthApi(baseUrl: String = BASE_URL): AuthApi =
        retrofit(baseUrl).create(AuthApi::class.java)

    fun crearChatApi(baseUrl: String = BASE_URL): ChatApi =
        retrofit(baseUrl).create(ChatApi::class.java)
}
