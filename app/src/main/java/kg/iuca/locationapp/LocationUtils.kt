package kg.iuca.locationapp

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.location.Geocoder
import android.os.Looper
import com.google.android.gms.location.*
import java.util.Locale

// Утилитный класс для работы с местоположением
class LocationUtils(val context: Context) {

    // Создание клиента для получения местоположения с использованием FusedLocationProvider
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Проверка, есть ли у приложения разрешение на доступ к местоположению
    fun hasLocationPermission(): Boolean {
        // Проверка разрешений на доступ к точному и приблизительному местоположению
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Запрос обновлений местоположения с высокой точностью
    @Suppress("MissingPermission") // Отключение предупреждения о возможном отсутствии разрешения
    fun requestLocationUpdates(viewModel: LocationViewModel) {
        // Создание запроса для получения местоположения с высокой точностью
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, // Высокая точность
            1000 // Интервал обновлений в миллисекундах (1 секунда)
        ).build()

        // Callback для получения местоположения
        val locationCallback = object : LocationCallback() {
            // Когда приходит новое местоположение, обновляем ViewModel
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    // Обновляем местоположение в ViewModel
                    val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                    viewModel.updateLocation(location)
                }
            }
        }

        // Запрашиваем обновления местоположения, передаем callback и указываем главный поток для обновлений
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    // Функция для преобразования координат в строковое описание адреса
    fun reverseGeocodeLocation(location: LocationData): String {
        // Используем Geocoder для получения адреса по координатам
        val geocoder = Geocoder(context, Locale.getDefault())
        // Получаем список адресов для указанных координат
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        // Если адрес найден, возвращаем первую строку адреса, иначе возвращаем "Address not found"
        return if (addresses?.isNotEmpty() == true) {
            addresses[0].getAddressLine(0) ?: "Address not found"
        } else {
            "Address not found"
        }
    }
}
