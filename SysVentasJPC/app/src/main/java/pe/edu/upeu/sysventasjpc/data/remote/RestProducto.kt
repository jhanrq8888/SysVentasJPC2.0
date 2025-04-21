package pe.edu.upeu.sysventasjpc.data.remote

import retrofit2.Response
import pe.edu.upeu.sysventasjpc.modelo.ProductoDto
import pe.edu.upeu.sysventasjpc.modelo.ProductoRespon
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RestProducto {
    @GET("/productos")
    suspend fun reportarProducto(
        @Header("Authorization") token: String
    ): Response<List<ProductoRespon>>

    @GET("/productos/{id}")
    suspend fun getProductoId(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<ProductoRespon>

    @DELETE("/productos/{id}")
    suspend fun deleteProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Void>

    @PUT("/productos/{id}")
    suspend fun actualizarProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body producto: ProductoDto
    ): Response<ProductoRespon>

    @POST("/productos")
    suspend fun insertarProducto(
        @Header("Authorization") token: String,
        @Body producto: ProductoDto
    ): Response<Void>

    companion object {
        const val BASE_PROD = "/productos"
    }
}
