# Location App

This Android app retrieves and displays your current location, including latitude, longitude, and the corresponding address. It uses the `FusedLocationProviderClient` for location tracking and `Geocoder` to convert coordinates into an address.

## Features

- Requests permissions for accessing fine and coarse location.
- Displays current location in real-time.
- Converts coordinates into a human-readable address using Geocoder.
- Simple and intuitive UI.

## Tech Stack

- **Kotlin**
- **Jetpack Compose** (UI)
- **Google Play Services** (FusedLocationProvider for location)
- **Geocoder** (for address conversion)

## Demo (gif)
<img src="/app/src/main/res/demo-location-app.gif" alt="Example GIF" width="200" />

## Setup

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/gubskaia/location-app.git
2. Open the project in Android Studio.
3. Build the project.
4. Run the app on an emulator or a physical device.
