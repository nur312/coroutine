package com.fintech.coroutine.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.fintech.coroutine.dto.QuoteDto
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody


@Service
class RandomQuoteClient(
    @Value("\${url.randomquote}") private val getRandomQuoteUrl: String,
) {

    private val webClient: WebClient = WebClient.create()

    suspend fun getQuote(): String =
        webClient
            .get()
            .uri(getRandomQuoteUrl)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .awaitBody<QuoteDto>().quote
}
