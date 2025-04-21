package pe.edu.upeu.sysventasjpc.repository

import pe.edu.upeu.sysventasjpc.data.remote.RestUnidadMedida
import pe.edu.upeu.sysventasjpc.modelo.UnidadMedida
import pe.edu.upeu.sysventasjpc.utils.TokenUtils
import javax.inject.Inject

interface UnidadMedidaRepository {
    suspend fun findAll(): List<UnidadMedida>
}
class UnidadMedidaRepositoryImp @Inject constructor(
    private val rest: RestUnidadMedida,
): UnidadMedidaRepository{
    override suspend fun findAll(): List<UnidadMedida> {
        val response =
            rest.reportarUnidadMedida(TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() ?:
        emptyList()
        else emptyList()
    }
}
