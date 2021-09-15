package me.alexpetrakov.cyclone.weather.domain

sealed class Fail(
    message: String,
    cause: Throwable?
) : RuntimeException(message, cause) {

    class NoLocationAccess(cause: Throwable? = null) :
        Fail("Location permission is missing", cause)

    class NoAvailableLocation(cause: Throwable? = null) :
        Fail("Can't obtain device location", cause)

    class NoConnection(cause: Throwable? = null) :
        Fail("Can't connect to the server", cause)
}