package com.fintech.coroutine.controller

import org.springframework.web.bind.annotation.*
import com.fintech.coroutine.dto.UserDto
import com.fintech.coroutine.dto.toEntity
import com.fintech.coroutine.entity.toDto
import com.fintech.coroutine.service.UserService

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {


    @PostMapping
    suspend fun addUser(@RequestBody userDto: UserDto): UserDto {//TODO поправить возвращаемый тип на Dto

        return userService.addUser(userDto.toEntity()).toDto()
    }

    @GetMapping("/{userId}")
    suspend fun getUser(@PathVariable userId: Long): UserDto {//TODO поправить возвращаемый тип на Dto

        return userService.getUser(userId).toDto()
    }
}

