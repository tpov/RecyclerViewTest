package com.tpov.testphoto.second_task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpov.testphoto.API_BASE_URL
import com.tpov.testphoto.R
import com.tpov.testphoto.api.MyApiService
import com.tpov.testphoto.models.Post
import com.tpov.testphoto.models.PostsData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SecondTaskFragment : Fragment() {

    private var posts = mutableListOf<Post>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter

    private var currentPage = 1
    private var isLoading = false
    private val visibleThreshold = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second_task, container, false)

        recyclerView = view.findViewById(R.id.rv_post)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        postsAdapter = PostsAdapter(posts)
        recyclerView.adapter = postsAdapter
        loadData(currentPage)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    currentPage++
                    loadData(currentPage)
                    isLoading = true
                }
            }
        })
    }

    private fun loadData(page: Int) {
        if (isLoading) return
        isLoading = true

        //Not safe
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

        val apiService = retrofit.create(MyApiService::class.java)
        apiService.getPosts(page).enqueue(object : Callback<PostsData> {
            override fun onResponse(call: Call<PostsData>, response: Response<PostsData>) {
                if (response.isSuccessful) {
                    val postsData = response.body()
                    postsData?.let {
                        onDataLoaded(it.posts)
                    }
                } else isLoading = false
            }

            override fun onFailure(call: Call<PostsData>, t: Throwable) {
                t.printStackTrace()
                isLoading = false
            }
        })
    }

    fun onDataLoaded(newPosts: List<Post>) {
        Log.d("dawdaw", "list: $newPosts")
        isLoading = false
        if (newPosts.isNotEmpty()) {
            val currentSize = postsAdapter.itemCount
            posts.addAll(newPosts)
            postsAdapter.notifyItemRangeInserted(currentSize, newPosts.size)
        }
    }
}