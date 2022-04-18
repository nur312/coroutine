package com.fintech.coroutine.repo

import com.fintech.coroutine.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserCoroutineRepo : CoroutineCrudRepository<User, Long> {
}