Two screens

Short description:
This is small application with tasks to do in near future. It's possible to add title, description and some url image to it, remove items and edit them.
Every data is stored and updated in real time. Thanks to that doesnt matter on which phone you saved your tasks it will always be up to date.


Guidelines:
- The minimum requirement for the app will be Android 5.x
- The application must be wrote in Kotlin
- The application must be use Coroutines for requests
- The application must use dependency injection
- The application must be covered by unit tests
- The application must support phone in portrait and landscape mode

Technologies used:
- fire store
- coroutines
- navigation component
- koin
- MVVM pattern with view states


Coding Standard & Development Process:
All data related actions should be encapsulated in the use case class. Such class should have a single invoke method.
In case of network calls, interface should be placed in the domain module, while implementation in the network module.
