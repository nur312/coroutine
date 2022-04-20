package com.fintech.coroutine.repo

import com.fintech.coroutine.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component

@Component
interface UserCoroutineRepo : CoroutineCrudRepository<User, Long>