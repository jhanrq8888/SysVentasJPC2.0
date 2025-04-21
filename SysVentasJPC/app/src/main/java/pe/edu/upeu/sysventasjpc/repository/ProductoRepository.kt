package pe.edu.upeu.sysventasjpc.repository

import pe.edu.upeu.sysventasjpc.data.remote.RestProducto
import pe.edu.upeu.sysventasjpc.modelo.ProductoDto
import pe.edu.upeu.sysventasjpc.modelo.ProductoRespon
import pe.edu.upeu.sysventasjpc.utils.TokenUtils
import javax.inject.Inject

// Interfaz que define las operaciones disponibles en el repositorio
interface ProductoRepository {
    suspend fun deleteProducto(producto: ProductoDto): Result<Boolean>
    suspend fun reportarProductos(): Result<List<ProductoRespon>>
    suspend fun buscarProductoId(id: Long): Result<ProductoRespon>
    suspend fun insertarProducto(producto: ProductoDto): Result<Boolean>
    suspend fun modificarProducto(producto: ProductoDto): Result<Boolean>
}

// Implementación del repositorio que maneja las operaciones de producto
class ProductoRepositoryImp @Inject constructor(
    private val restProducto: RestProducto
) : ProductoRepository {

    // Elimina un producto
    override suspend fun deleteProducto(producto: ProductoDto): Result<Boolean> {
        return try {
            val response = restProducto.deleteProducto(
                TokenUtils.TOKEN_CONTENT,
                producto.idProducto
            )
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error al eliminar el producto: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Reporta todos los productos
    override suspend fun reportarProductos(): Result<List<ProductoRespon>> {
        return try {
            val response = restProducto.reportarProducto(TokenUtils.TOKEN_CONTENT)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener los productos: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Busca un producto por ID
    override suspend fun buscarProductoId(id: Long): Result<ProductoRespon> {
        return try {
            val response = restProducto.getProductoId(TokenUtils.TOKEN_CONTENT, id)
            if (response.isSuccessful) {
                val producto = response.body()
                if (producto != null) {
                    Result.success(producto)
                } else {
                    Result.failure(Exception("Producto no encontrado"))
                }
            } else {
                Result.failure(Exception("Error al obtener el producto: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Inserta un producto
    override suspend fun insertarProducto(producto: ProductoDto): Result<Boolean> {
        return try {
            val response = restProducto.insertarProducto(TokenUtils.TOKEN_CONTENT, producto)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error al insertar el producto: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Modifica un producto
    override suspend fun modificarProducto(producto: ProductoDto): Result<Boolean> {
        return try {
            val response = restProducto.actualizarProducto(
                TokenUtils.TOKEN_CONTENT,
                producto.idProducto,
                producto
            )
            if (response.isSuccessful && response.body()?.idProducto != null) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error al modificar el producto: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
