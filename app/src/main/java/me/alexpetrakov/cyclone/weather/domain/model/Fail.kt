package me.alexpetrakov.cyclone.weather.domain.model

sealed class Fail(
    message: String,
    cause: Throwable?
) : RuntimeException(message, cause) {

    class LocationAccessDenied(cause: Throwable? = null) :
        Fail("Location permission is missing", cause)

    class LocationIsDisabled(cause: Throwable?) :
        Fail("Can't obtain device location. User action is required", cause)

    class LocationIsNotAvailable(cause: Throwable? = null) :
        Fail("Can't obtain device location", cause)

    class NoConnection(cause: Throwable? = null) :
        Fail("Can't connect to the server", cause)
}