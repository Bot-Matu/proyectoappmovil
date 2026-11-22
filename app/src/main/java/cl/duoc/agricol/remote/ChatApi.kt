package cl.duoc.agricol.remote

import cl.duoc.agricol.model.ConversacionRemota
import cl.duoc.agricol.model.MensajeRemoto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApi {
    @GET("conversacion")
    suspend fun obtenerConversaciones(): List<ConversacionRemota>

    @POST("conversacion")
    suspend fun crearConversacion(@Body conversacion: ConversacionRemota): ConversacionRemota

    @GET("mensaje")
    suspend fun obtenerMensajes(): List<MensajeRemoto>

    @POST("mensaje")
    suspend fun enviarMensaje(@Body mensaje: MensajeRemoto): MensajeRemoto
}
