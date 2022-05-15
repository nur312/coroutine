package com.fintech.coroutine

import com.fintech.coroutine.client.RandomQuoteClient
import com.fintech.coroutine.dto.UserDto
import com.fintech.coroutine.entity.User
import com.fintech.coroutine.entity.toDto
import com.fintech.coroutine.repo.UserCoroutineRepo
import com.fintech.coroutine.service.UserService
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
class CoroutineApplicationTests(
    @Autowired val webTestClient: WebTestClient,
    @Autowired val userCoroutineRepo: UserCoroutineRepo
) {
    private var userService: UserService? = null
    private var userDto: UserDto? = null
    private val randomQuoteClient: RandomQuoteClient = mockk()

    @BeforeEach
    fun beforeEach() {
        coEvery { randomQuoteClient.getQuote() } returns "I'm the best player"
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }

    @Test
    @Order(1)
    fun addUserRepo() = runBlocking {

        val zidane = User("Zidane", "ziz", "ziz@ziz.fr")

        val savedEntity = userCoroutineRepo.save(zidane)

        assertNotEquals(0, savedEntity.id)
        userDto = savedEntity.toDto()
    }

    @Test
    @Order(2)
    fun getUserRepo() = runBlocking {

        val savedEntity = userCoroutineRepo.findById(userDto!!.id)

        val expected = User("Zidane", "ziz", "ziz@ziz.fr")

        requireNotNull(savedEntity)
        assertAll(
            { assertEquals(expected.name, savedEntity.name) },
            { assertEquals(expected.username, savedEntity.username) },
            { assertEquals(expected.email, savedEntity.email) },
            { assertNotEquals(0, savedEntity.id) },
            { assertNull(savedEntity.quote) }
        )
    }

    @Test
    @Order(3)
    fun addUserService() = runBlocking {

        userService = UserService(randomQuoteClient, userCoroutineRepo)
        val zidane = User("Ronaldo", "cr7", "cr7@cr7.sp")

        val savedEntity = userService!!.addUser(zidane)

        assertAll(
            { assertNotEquals(0, savedEntity.id) },
            { assertFalse(savedEntity.quote.isNullOrEmpty()) }
        )
        userDto = savedEntity.toDto()
    }

    @Test
    @Order(4)
    fun getUserService() = runBlocking {

        val expected = User(userDto!!.id, "Ronaldo", "cr7", "cr7@cr7.sp", randomQuoteClient.getQuote())

        val savedEntity = userService!!.getUserByUsername(userDto!!.username)!!
        assertAll(
            { assertEquals(expected.name, savedEntity.name) },
            { assertEquals(expected.username, savedEntity.username) },
            { assertEquals(expected.email, savedEntity.email) },
            { assertEquals(expected.id, savedEntity.id) },
            { assertEquals(expected.quote, savedEntity.quote) }
        )
    }

    @Test
    @Order(5)
    fun addUserController() {

        val messi = UserDto("Messi", "mes", "mes@mes.sp")

        val responseBody: String = webTestClient
            .post()
            .uri("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(messi))
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody!!

//        assertAll(
//            { assertEquals(messi.name, responseBody.name) },
//            { assertEquals(messi.username, responseBody.username) },
//            { assertEquals(messi.email, responseBody.email) },
//            { assertNotEquals(0, responseBody.id) },
//            { assertFalse(responseBody.quote.isNullOrEmpty()) }
//        )

        assertEquals("In Process", responseBody)

        userDto = messi
    }

    @Test
    @Order(6)
    fun getUserController() {

        Thread.sleep(1000)

        val responseBody: UserDto = webTestClient
            .get()
            .uri("/users/${userDto!!.username}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody(UserDto::class.java)
            .returnResult()
            .responseBody!!

        assertAll(
            { assertEquals(userDto!!.name, responseBody.name) },
            { assertEquals(userDto!!.username, responseBody.username) },
            { assertEquals(userDto!!.email, responseBody.email) },
            { assertNotEquals(0, responseBody.id) },
            { assertFalse(responseBody.quote.isNullOrEmpty()) }
        )
    }
}
