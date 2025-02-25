package org.fakechitor.cloudfilestorage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Path not exists")
class PathNotExistsException : RuntimeException {
    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)
}
