package pe.edu.upeu.sysventasjpc.ui.presentation.screens.producto

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.sysventasjpc.modelo.Categoria
import pe.edu.upeu.sysventasjpc.modelo.Marca
import pe.edu.upeu.sysventasjpc.modelo.ProductoDto
import pe.edu.upeu.sysventasjpc.modelo.ProductoRespon
import pe.edu.upeu.sysventasjpc.modelo.UnidadMedida
import pe.edu.upeu.sysventasjpc.repository.CategoriaRepository
import pe.edu.upeu.sysventasjpc.repository.MarcaRepository
import pe.edu.upeu.sysventasjpc.repository.ProductoRepository
import pe.edu.upeu.sysventasjpc.repository.UnidadMedidaRepository
import javax.inject.Inject

@HiltViewModel
class ProductoFormViewModel @Inject constructor(
    private val prodRepo: ProductoRepository,
    private val marcRepo: MarcaRepository,
    private val cateRepo: CategoriaRepository,
    private val umRepo: UnidadMedidaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _producto = MutableStateFlow<ProductoRespon?>(null)
    val producto: StateFlow<ProductoRespon?> = _producto

    private val _marcs = MutableStateFlow<List<Marca>>(emptyList())
    val marcs: StateFlow<List<Marca>> = _marcs

    private val _categors = MutableStateFlow<List<Categoria>>(emptyList())
    val categors: StateFlow<List<Categoria>> = _categors

    private val _unidMeds = MutableStateFlow<List<UnidadMedida>>(emptyList())
    val unidMeds: StateFlow<List<UnidadMedida>> = _unidMeds

    fun getProducto(idX: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = prodRepo.buscarProductoId(idX)
                if (result.isSuccess) {
                    _producto.value = result.getOrNull()
                } else {
                    _producto.value = null
                    Log.e("ProductoFormVM", "Error al obtener producto: ${result.exceptionOrNull()}")
                }
            } catch (e: Exception) {
                _producto.value = null
                Log.e("ProductoFormVM", "Excepción: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getDatosPrevios() {
        viewModelScope.launch {
            _marcs.value = marcRepo.findAll()
            _categors.value = cateRepo.findAll()
            _unidMeds.value = umRepo.findAll()
        }
    }

    fun addProducto(producto: ProductoDto) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            Log.i("REAL", producto.toString())
            prodRepo.insertarProducto(producto)
            _isLoading.value = false
        }
    }

    fun editProducto(producto: ProductoDto) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            prodRepo.modificarProducto(producto)
            _isLoading.value = false
        }
    }
}
