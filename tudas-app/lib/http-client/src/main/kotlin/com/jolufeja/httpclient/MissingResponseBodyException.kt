package com.jolufeja.httpclient

class MissingResponseBodyException(
    message: String = "Response body expected, but response did not have a body"
) : Exception(message)