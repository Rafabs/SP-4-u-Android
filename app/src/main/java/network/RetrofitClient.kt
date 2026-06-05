package com.rafabs.sp4u.network
import network.ApiService
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log

object RetrofitClient {
    private const val BASE_URL = "https://api.olhovivo.sptrans.com.br/v2.1/"
    var sessionCookie: String = "" // ← armazena o cookie após login


    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val cookieJar = object : CookieJar {
        private val cookieStore = mutableListOf<Cookie>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            Log.d("COOKIE_JAR", "saveFromResponse URL: $url")
            Log.d("COOKIE_JAR", "Cookies recebidos (${cookies.size}): $cookies")
            val names = cookies.map { it.name }
            cookieStore.removeAll { it.name in names }
            cookieStore.addAll(cookies)
            cookies.find { it.name == "apiCredentials" }?.let {
                sessionCookie = it.value
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            Log.d("COOKIE_JAR", "loadForRequest URL: $url | cookies: ${cookieStore.size}")
            return cookieStore
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (request.url.toString().contains("Posicao")) {
                val bodyString = response.peekBody(Long.MAX_VALUE).string()
                Log.d("RAW_BODY", bodyString)
            }
            response
        }
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
            if (sessionCookie.isNotEmpty()) {
                requestBuilder.addHeader("Cookie", "apiCredentials=$sessionCookie")
            }
            chain.proceed(requestBuilder.build())
        }
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}