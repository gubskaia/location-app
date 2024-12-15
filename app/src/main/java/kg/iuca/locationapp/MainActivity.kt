package kg.iuca.locationapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kg.iuca.locationapp.ui.theme.LocationAppTheme
import android.Manifest
import android.widget.Toast

// Основная активность приложения
class MainActivity : ComponentActivity() {
    // ViewModel для работы с местоположением
    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocationAppTheme {
                MyApp(locationViewModel = locationViewModel)
            }
        }
    }
}

// Компонент MyApp, который является оберткой для всех UI элементов
@Composable
fun MyApp(locationViewModel: LocationViewModel) {
    // Получаем доступ к контексту приложения
    val context = LocalContext.current
    // Создаем объект LocationUtils для работы с местоположением
    val locationUtils = LocationUtils(context)

    // Отображаем UI для отображения местоположения
    LocationDisplay(locationUtils = locationUtils, context = context, viewModel = locationViewModel)
}

// Компонент для отображения местоположения и запроса разрешений
@Composable
fun LocationDisplay(locationUtils: LocationUtils, context: Context, viewModel: LocationViewModel) {
    // Создаем launcher для запроса нескольких разрешений
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            // Если разрешения получены, начинаем запрос местоположения
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                locationUtils.requestLocationUpdates(viewModel)
            } else {
                // Если разрешения отклонены, показываем сообщение об ошибке
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Получаем текущее местоположение из ViewModel
    val location = viewModel.location.value

    // Используем Column для вертикального расположения элементов
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        // Если местоположение получено, отображаем координаты
        if (location != null) {
            Text("Location: ${location.latitude}, ${location.longitude}")

            // Получаем и отображаем адрес по координатам
            val address = locationUtils.reverseGeocodeLocation(location)
            Text("Address: $address")
        }

        // Кнопка для получения местоположения
        Button(onClick = {
            // Если разрешения на доступ к местоположению есть, начинаем запрос местоположения
            if (locationUtils.hasLocationPermission()) {
                locationUtils.requestLocationUpdates(viewModel)
            } else {
                // Если разрешений нет, запрашиваем их
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        }) {
            Text(text = "Get Location")
        }
    }
}
