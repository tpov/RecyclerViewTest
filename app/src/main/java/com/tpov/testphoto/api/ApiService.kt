package com.tpov.testphoto.api

import com.tpov.testphoto.GET_POSTS_URL
import com.tpov.testphoto.models.PostsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApiService {

    @GET(GET_POSTS_URL)
    fun getPosts(@Query("page") page: Int): Call<PostsData>

}
