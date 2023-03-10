# Music Player app

Music streaming app
With this app  you are able to see popular artist, detail screen of the artist along with the tracks. 
App is written fully with Jetpack compose.
UI Layer is exposed with StateFlow and StateIn.
Data Layer contains Repositories and Domain layer contains UseCase for usiness logic.

<img width="600" alt="Screenshot 2023-02-15 at 20 43 42" src="https://user-images.githubusercontent.com/5040186/219110607-7ad8c58e-39b8-44f9-804d-94c59df98a27.png">

* UI
    * [Compose](https://developer.android.com/jetpack/compose)
    * [Material design](https://material.io/design)
    * [Dark/Lite theme](https://material.io/design/color/dark-theme.html)

* Tech stuff
    * [Kotlin](https://kotlinlang.org/)
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and [Flow](https://developer.android.com/kotlin/flow) for async operations
    * [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection
    * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) for navigation between composables
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that stores, exposes and manages UI state
    * [Retrofit](https://square.github.io/retrofit/) for networking
    * [Coil](https://github.com/coil-kt/coil) for image loading
    * [Mockito](https://site.mockito.org/) for unit test
    
* Modern Architecture
    * Single activity architecture
    * MVVM for presentation layer
    * Modularized architecture
    * Domain layer for business logic
    * UI Layer is exposed with [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture)
    * Unit tests
