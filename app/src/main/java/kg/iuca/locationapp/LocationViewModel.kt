package kg.iuca.locationapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel

// Данные о местоположении, включая широту и долготу
data class LocationData(val latitude: Double, val longitude: Double)

// ViewModel для управления состоянием местоположения
class LocationViewModel : ViewModel() {
    // Приватное состояние для хранения местоположения (инициализировано значением null)
    private val _location = mutableStateOf<LocationData?>(null)

    // Публичное состояние для доступа к текущему местоположению
    val location: State<LocationData?> = _location

    // Функция для обновления местоположения в ViewModel
    fun updateLocation(newLocation: LocationData) {
        // Обновляем значение местоположения
        _location.value = newLocation
    }
}
