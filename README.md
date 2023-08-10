<h1 align="center">TheMovieDB App</h1>
<p align="center">  
TheMovieDB App is an application written by considering MVVM architecture, Jetpack Components, Clean Architecture and SOLID principles. Offline cache, play video with ExoPlayer, infinite scroll, etc. are used in the app.
  </p>
</br>

<p align="center">
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/Android API-24%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://kotlinlang.org"><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-1.8.21-blue"/></a>
  <img alt="MVVM" src="https://img.shields.io/badge/MVVM-Architecture-purple"/>
</p>

## Screenshots
<table>
  <tr>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/e5c5abc0-1964-493a-a925-958be944d74c" width="100%"></td>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/a8747db9-cfa8-4282-99af-a12b4ea5f970" width="100%"></td>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/5a3f734f-a0f3-4e7f-b11e-b754e260c7ec" width="100%"></td>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/f3a455e0-e870-4a2d-b6df-78e819ec0ddf" width="100%"></td>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/b6b358f4-a0f4-43d3-ad4a-2959347b2e6c" width="100%"></td>
  </tr>  
  <tr>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/d87543b2-696c-4a4e-8e40-cf3eaaa9b682" width="100%"></td>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/9d006479-8811-47cf-92a6-db4bd660c71e" width="100%"></td>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/67573974-0a24-495e-8ad5-93947c6ddc10" width="100%"></td>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/379aef80-5181-4d16-8b51-e467d33d805b" width="100%"></td>
    <td><img src="https://github.com/Mehmetth/TheMovieDB/assets/18207490/ea510d55-ce5d-4a60-8def-72dc5dbc5782" width="100%"></td>
  </tr>  
</table>

https://github.com/Mehmetth/TheMovieDB/assets/18207490/6e0f5712-231e-4e94-98cd-be8b279a38b3

https://github.com/Mehmetth/TheMovieDB/assets/18207490/a70290d5-4609-41e6-8178-614aadab5a6c

## Tech stack & Open-source libraries
- 100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) and [Flow](https://developer.android.com/kotlin/flow)
   - An example of using [Build Variants](https://developer.android.com/build/build-variants), [Product Flavors](https://developer.android.com/reference/tools/gradle-api/7.4/com/android/build/api/dsl/ProductFlavor), [Kotlin DSL](https://developer.android.com/build/migrate-to-kotlin-dsl) are given.
- [MVVM Architecture](https://developer.android.com/jetpack/guide) - Modern, maintainable, and Google suggested app architecture
- [Navigation Component](https://developer.android.com/guide/navigation) - Single activity multiple fragments approach
- [Flow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - Notify domain layer data to views.
- [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - Dispose of observing data when lifecycle state changes.
- [View Binding](https://developer.android.com/topic/libraries/view-binding) - Allows you to more easily write code that interacts with views
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - UI related data holder, lifecycle aware.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency injection library for Android
- [Retrofit](https://square.github.io/retrofit/) - It is used for http requests in the app.
- [Room DB](https://developer.android.com/training/data-storage/room) - The Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite
- [ExoPlayer](https://exoplayer.dev/) - It is used to play the videos contained in the app.
- [Coil](https://coil-kt.github.io/coil/) - It is used to upload images contained in the app.
- [Chucker](https://github.com/ChuckerTeam/chucker) - It is used to follow the requests made in the app in detail.

### Layers :bookmark_tabs:
- **Domain** - Would execute business logic which is independent of any layer and is just a pure kotlin/java package with no android specific dependency.
- **Data** - Would dispense the required data for the application to the domain layer by implementing interface exposed by the domain.
- **Presentation** - Would include both domain and data layer and is android specific which executes the UI logic.

<!-- GETTING STARTED -->
## Getting Started

### Installation

1. Get a free API
2. Enter your API in `build.gradle`
   ```js
   buildConfigField(
                "String",
                "API_TOKEN",
                "You have to enter the token you got from TheMovieDB"
            )
   ```
