package com.yenaly.duanzile.logic.network

import com.yenaly.duanzile.DUANZILE_BASE_URL
import com.yenaly.duanzile.PROJECT_TOKEN
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 16:20
 */
object DuanzileNetwork {

    val homeService = create<HomeService>(DUANZILE_BASE_URL)

    val slideVideoService = create<SlideVideoService>(DUANZILE_BASE_URL)

    private inline fun <reified T : DuanzileService> create(baseUrl: String): T {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient()).build()
            .create(T::class.java)
    }

    private fun okHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("project_token", PROJECT_TOKEN)
                    .build()
                return@addInterceptor chain.proceed(request)
            }
            .build()
}