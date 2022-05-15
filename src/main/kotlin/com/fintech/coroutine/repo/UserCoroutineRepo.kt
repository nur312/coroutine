package com.fintech.coroutine.repo

import com.fintech.coroutine.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component

@Component
interface UserCoroutineRepo : CoroutineCrudRepository<User, Long> {

    // Ставил delay в сохранении пользователя в 10 секунд, все хорошо работает,
    // первые 10 секунд 400 возвращает, а потом все норм.
    suspend fun findByUsername(username: String): User?
}