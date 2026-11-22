package cl.duoc.agricol.remote

import cl.duoc.agricol.model.PublicacionRemota
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PublicacionesApi {
    // Trae todas las publicaciones
    @GET("publicacion")
    suspend fun obtenerPublicaciones(): List<PublicacionRemota>

    // Guarda una publicación nueva y devuelve la que quedó registrada
    @POST("publicacion")
    suspend fun crearPublicacion(@Body publicacion: PublicacionRemota): PublicacionRemota
}

object RetrofitClient {
    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:GOcHn4ic/"

    fun crearApi(baseUrl: String = BASE_URL): PublicacionesApi =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PublicacionesApi::class.java)
}