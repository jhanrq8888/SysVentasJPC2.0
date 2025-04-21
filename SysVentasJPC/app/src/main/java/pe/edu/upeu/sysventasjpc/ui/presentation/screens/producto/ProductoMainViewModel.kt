package pe.edu.upeu.sysventasjpc.ui.presentation.screens.producto

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pe.edu.upeu.sysventasjpc.modelo.ProductoDto
import pe.edu.upeu.sysventasjpc.modelo.ProductoRespon
import pe.edu.upeu.sysventasjpc.repository.ProductoRepository
import javax.inject.Inject

@HiltViewModel
class ProductoMainViewModel @Inject constructor(
    private val prodRepo: ProductoRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _deleteSuccess = MutableStateFlow<Boolean?>(null)
    val deleteSuccess: StateFlow<Boolean?> get() = _deleteSuccess

    private val _prods = MutableStateFlow<List<ProductoRespon>>(emptyList())
    val prods: StateFlow<List<ProductoRespon>> = _prods

    fun cargarProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = prodRepo.reportarProductos()
            _prods.value = result.getOrElse {
                Log.e("ProductoVM", "Error cargando productos", it)
                emptyList()
            }
            _isLoading.value = false
        }
    }

    fun buscarPorId(id: Long): Flow<ProductoRespon?> = flow {
        val result = prodRepo.buscarProductoId(id)
        emit(result.getOrNull())
    }

    fun eliminar(producto: ProductoDto) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = prodRepo.deleteProducto(producto)
            if (result.getOrDefault(false)) {
                cargarProductos()
                _deleteSuccess.value = true
            } else {
                _deleteSuccess.value = false
            }
        } catch (e: Exception) {
            Log.e("ProductoVM", "Error al eliminar producto", e)
            _deleteSuccess.value = false
        } finally {
            _isLoading.value = false
        }
    }

    fun clearDeleteResult() {
        _deleteSuccess.value = null
    }
}
