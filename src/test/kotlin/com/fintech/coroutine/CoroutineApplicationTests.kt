package com.fintech.coroutine

import com.fintech.coroutine.dto.UserDto
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CoroutineApplicationTests(@Autowired val webTestClient: WebTestClient) {

    @Test
    @Order(1)
    fun addUserTest() {
        val messi = UserDto("Messi", "mes", "mes@mes.sp")

        val responseBody: UserDto = webTestClient
            .post()
            .uri("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(messi))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(UserDto::class.java)
            .returnResult()
            .responseBody!!

        assertAll(
            { assertEquals(messi.name, responseBody.name) },
            { assertEquals(messi.username, responseBody.username) },
            { assertEquals(messi.email, responseBody.email) },
            { assertNotEquals(0, responseBody.id) },
            { assertFalse(responseBody.quote.isNullOrEmpty()) }
        )
    }

    @Test
    @Order(2)
    fun getUser() {
        val responseBody: UserDto = webTestClient
            .get()
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody(UserDto::class.java)
            .returnResult()
            .responseBody!!

        val messi = UserDto("Messi", "mes", "mes@mes.sp")
        assertAll(
            { assertEquals(messi.name, responseBody.name) },
            { assertEquals(messi.username, responseBody.username) },
            { assertEquals(messi.email, responseBody.email) },
            { assertNotEquals(0, responseBody.id) },
            { assertFalse(responseBody.quote.isNullOrEmpty()) }
        )
    }

}
