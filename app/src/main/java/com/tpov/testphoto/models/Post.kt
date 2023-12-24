package com.tpov.testphoto.models

data class Post(
    val id: Long,
    val user_name: String,
    val user_id: String,
    val user_pic: String,
    val message: String,
    val photo: String,
    val date: String
)

data class PostsData(
    val total_pages: Int,
    val page: String,
    val posts: List<Post>
)

