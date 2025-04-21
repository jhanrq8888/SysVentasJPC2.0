package pe.edu.upeu.sysventasjpc.repository

import pe.edu.upeu.sysventasjpc.data.remote.RestMarca
import pe.edu.upeu.sysventasjpc.modelo.Marca
import pe.edu.upeu.sysventasjpc.utils.TokenUtils
import javax.inject.Inject

interface MarcaRepository {
    suspend fun findAll(): List<Marca>
}
class MarcaRepositoryImp @Inject constructor(
    private val rest: RestMarca,
): MarcaRepository{
    override suspend fun findAll(): List<Marca> {
        val response =
            rest.reportarMarcas(TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() ?:
        emptyList()
        else emptyList()
    }
}
