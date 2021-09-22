package me.alexpetrakov.cyclone.locationsearch.domain

sealed class Fail(
    message: String,
    cause: Throwable?
) : RuntimeException(message, cause) {

    class NoConnection(cause: Throwable? = null) :
        Fail("Can't connect to the server", cause)
}
