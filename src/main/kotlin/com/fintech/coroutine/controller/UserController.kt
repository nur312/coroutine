package com.fintech.coroutine.controller

import org.springframework.web.bind.annotation.*
import com.fintech.coroutine.dto.UserDto
import com.fintech.coroutine.dto.toEntity
import com.fintech.coroutine.entity.toDto
import com.fintech.coroutine.service.UserService
import kotlinx.coroutines.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun addUserAsync(@RequestBody userDto: UserDto): String {

        CoroutineScope(Dispatchers.Default).launch { userService.addUser(userDto.toEntity()) }

        return "In Process"
    }

    @GetMapping("/{username}")
    suspend fun getUser(@PathVariable username: String): UserDto {

        val user = CoroutineScope(Dispatchers.Default).async {
            userService.getUserByUsername(username).toDto()
        }

        return user.await()
    }
}

