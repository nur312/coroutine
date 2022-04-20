package com.fintech.coroutine.service

import org.springframework.stereotype.Service
import com.fintech.coroutine.client.RandomQuoteClient
import com.fintech.coroutine.entity.User
import com.fintech.coroutine.repo.UserCoroutineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Service
class UserService(
    private val quoteClient: RandomQuoteClient,
    private val repo: UserCoroutineRepo
) {

    suspend fun addUser(user: User): User {

        user.quote = withContext(Dispatchers.Default) {

            return@withContext quoteClient.getQuote()
        }

        return withContext(Dispatchers.IO) {
            return@withContext repo.save(user)
        }
    }

    suspend fun getUser(id: Long): User = withContext(Dispatchers.IO) {

        return@withContext repo.findById(id)
    }
        ?: throw  IllegalArgumentException("There is not user id = $id")
}