package com.ekezet.othello.core.data.serialize.throwable

class InvalidTokenException(override val message: String?) : IllegalArgumentException(message)
