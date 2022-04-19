package com.fintech.coroutine.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.fintech.coroutine.dto.QuoteDto
import io.netty.resolver.DefaultAddressResolverGroup
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import reactor.netty.http.client.HttpClient


@Service
class RandomQuoteClient(
    @Value("\${url.randomquote}") private val getRandomQuoteUrl: String,
) {

    private val webClient: WebClient = WebClient.builder()
        .clientConnector(
            ReactorClientHttpConnector(
                HttpClient.create()
                    .resolver(DefaultAddressResolverGroup.INSTANCE)
            )
        ).build()

    suspend fun getQuote(): String =
        webClient
            .get()
            .uri(getRandomQuoteUrl)
            .accept(MediaType.APPLICATION_JSON)
            .awaitExchange { response ->
                if (response.statusCode().is2xxSuccessful) {
                    return@awaitExchange response.awaitBody<QuoteDto>().quote
                } else {
                    throw Exception("Cannot get client")
                }
            }
}
