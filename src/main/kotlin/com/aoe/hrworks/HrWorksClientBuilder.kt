package com.aoe.hrworks

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField
import java.util.concurrent.TimeUnit

object HrWorksClientBuilder {

    private const val HEADER_CONTENT_TYPE = "Content-Type"
    private const val HEADER_DATE = "Date"
    private const val HEADER_HOST = "Host"

    private const val CLOSING_STRING = "hrworks_api_request"

    const val HEADER_HR_WORKS_TARGET = "x-hrworks-target"
    const val ENCODING = "UTF-8"

    private val DATE_FORMAT = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral('T')
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
        .appendLiteral('Z').toFormatter()

    private val USE_IN_SIGNATURE =
        listOf(HEADER_CONTENT_TYPE, HEADER_DATE, HEADER_HOST, HEADER_HR_WORKS_TARGET).sortedBy { it }

    @JvmOverloads
    fun buildClient(
        apiEndpoint: String = "api.hrworks.de",
        protocol: String = "https",
        realm: Realm = Realm.PRODUCTION,
        apiKey: String,
        apiSecret: String
    ): HrWorksClient =

        Retrofit.Builder()
            .client(buildHttpClient(apiEndpoint, realm.apiValue, apiKey, apiSecret))
            .baseUrl("$protocol://$apiEndpoint")
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(gson()))
            .build().create(HrWorksClient::class.java)

    private fun gson(): Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .registerTypeAdapter(IntervalType::class.java, IntervalTypeAdapter())
        .create()

    private fun buildHttpClient(apiEndpoint: String, realm: String, apiKey: String, apiSecret: String) =
        OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor { chain ->
                val initialRequest = chain.request()
                val builder = initialRequest.newBuilder()

                val date = DATE_FORMAT.format(LocalDateTime.now())

                builder.addHeader(HEADER_DATE, date)
                builder.addHeader(HEADER_HOST, apiEndpoint)
                builder.addHeader(HEADER_CONTENT_TYPE, initialRequest.body()?.contentType().toString())

                val headerValues = initialRequest.headers()
                    .toMultimap().apply {
                        put(HEADER_DATE, listOf(date))
                        put(HEADER_HOST, listOf(apiEndpoint))
                        put(HEADER_CONTENT_TYPE, listOf(initialRequest.body()?.contentType().toString()))
                    }
                    .asSequence()
                    .filter { it.key in USE_IN_SIGNATURE }
                    .sortedBy { it.key }
                    .fold("") { output, entry ->
                        entry.value.fold(output) { acc, currentValue ->
                            acc + ("${entry.key.toLowerCase()}:$currentValue".trim() + "\n")
                        }
                    }

                val requestBody = initialRequest.body()
                val hash = when (requestBody) {
                    is RequestBody -> {
                        val buffer = Buffer()
                        requestBody.writeTo(buffer)
                        buffer.sha256().hex()
                    }
                    else -> ""
                }

                val canonicalRequest =
                    "${initialRequest.method()}\n/\n\n$headerValues\n$hash"

                val stringToSign =
                    "HRWORKS-HMAC-SHA256\n$date\n${canonicalRequest.sha256hex()}"

                val signature = sequenceOf(date.substringBefore("T"),
                    realm,
                    CLOSING_STRING,
                    stringToSign).fold("HRWORKS$apiSecret".toByteArray()) { acc, input ->
                    input.hmacSHA256hex(acc)
                }.hexString()

                builder.addHeader("Authorization", "HRWORKS-HMAC-SHA256 Credential=${URLEncoder.encode(apiKey, ENCODING)}/$realm, " +
                    "SignedHeaders=${USE_IN_SIGNATURE.map { it.toLowerCase().trim() }.joinToString(";")}, " +
                    "Signature=$signature")

                chain.proceed(builder.build())
            }.build()
}