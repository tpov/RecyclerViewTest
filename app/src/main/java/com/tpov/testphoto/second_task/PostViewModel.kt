package com.tpov.testphoto.second_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tpov.testphoto.api.MyApiService.Companion.getApiService
import com.tpov.testphoto.models.Post
import com.tpov.testphoto.models.PostsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private val _postsLiveData = MutableLiveData<List<Post>>()
    val postsLiveData: LiveData<List<Post>> = _postsLiveData

    init {
        _isLoading.value = false
    }

    fun loadData(page: Int) {
        if (_isLoading.value == true) return
        _isLoading.value = true

        getApiService().getPosts(page).enqueue(object : Callback<PostsData> {
            override fun onResponse(call: Call<PostsData>, response: Response<PostsData>) {
                if (response.isSuccessful)
                    response.body()?.let {
                        _postsLiveData.value = it.posts
                        _isLoading.value = false
                    }
                else _isLoading.value = false
            }

            override fun onFailure(call: Call<PostsData>, t: Throwable) {
                t.printStackTrace()
                _isLoading.value = false
            }
        })
    }
}