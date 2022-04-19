package com.fintech.coroutine

import com.fintech.coroutine.dto.UserDto
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoroutineApplicationTests(@Autowired val webTestClient: WebTestClient) {

    private var controllerAddedUserDto: UserDto? = null

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

        controllerAddedUserDto = responseBody
    }

    @Test
    @Order(2)
    fun getUser() {

        val responseBody: UserDto = webTestClient
            .get()
            .uri("/users/${controllerAddedUserDto!!.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody(UserDto::class.java)
            .returnResult()
            .responseBody!!

        assertAll(
            { assertEquals(controllerAddedUserDto!!.name, responseBody.name) },
            { assertEquals(controllerAddedUserDto!!.username, responseBody.username) },
            { assertEquals(controllerAddedUserDto!!.email, responseBody.email) },
            { assertNotEquals(0, responseBody.id) },
            { assertFalse(responseBody.quote.isNullOrEmpty()) }
        )
    }

}
