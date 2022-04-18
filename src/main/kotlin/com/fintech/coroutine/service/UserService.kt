package com.fintech.coroutine.service

import org.springframework.stereotype.Service
import com.fintech.coroutine.client.RandomQuoteClient
import com.fintech.coroutine.entity.User
import com.fintech.coroutine.repo.UserCoroutineRepo

@Service
class UserService(
    private val quoteClient: RandomQuoteClient,
    private val repo: UserCoroutineRepo
) {

    suspend fun addUser(user: User): User {

        user.quote = quoteClient.getQuote()
        return repo.save(user)
    }

    suspend fun getUser(id: Long): User {

        return repo.findById(id) ?: throw  IllegalArgumentException("There is not user id = $id")
    }
}