package com.tpov.testphoto.api

import com.tpov.testphoto.API_BASE_URL
import com.tpov.testphoto.GET_POSTS_URL
import com.tpov.testphoto.models.PostsData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface MyApiService {

    @GET(GET_POSTS_URL)
    fun getPosts(@Query("page") page: Int): Call<PostsData>

    companion object {
        private lateinit var apiService: MyApiService

        private fun initializeRetrofit() {
            val trustManager = object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())

            val okHttpClient = OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .hostnameVerifier { _, _ -> true }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(MyApiService::class.java)
        }

        fun getApiService(): MyApiService {
            if (!::apiService.isInitialized) {
                initializeRetrofit()
            }
            return apiService
        }
    }
}
