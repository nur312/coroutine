package com.fintech.coroutine.entity

import com.fintech.coroutine.dto.UserDto
import org.springframework.data.annotation.Id

data class User(
    @Id
    var id: Long,
    val name: String,
    val username: String,
    val email: String,
    var quote: String?
) {
    constructor(name: String, username: String, email: String) : this(0, name, username, email, null)
}

fun User.toDto() = UserDto(id, name, username, email, quote)