package com.tpov.testphoto.second_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpov.testphoto.R
import com.tpov.testphoto.models.Post

class SecondTaskFragment : Fragment() {

    private var posts = mutableListOf<Post>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var viewModel: PostViewModel
    private lateinit var pbLoadData: ProgressBar

    private var currentPage = 1
    private val visibleThreshold = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second_task, container, false)

        recyclerView = view.findViewById(R.id.rv_post)
        pbLoadData = view.findViewById(R.id.pb_load_data)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        postsAdapter = PostsAdapter(posts)
        recyclerView.adapter = postsAdapter

        viewModel = ViewModelProvider(this)[PostViewModel::class.java]
        viewModel.loadData(currentPage)

        initObserve()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (viewModel.isLoading.value == false
                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    currentPage++
                    viewModel.loadData(currentPage)
                }
            }
        })
    }

    private fun initObserve() {
        viewModel.postsLiveData.observe(viewLifecycleOwner) {  newPosts ->
            if (newPosts.isNotEmpty()) {
                val currentSize = postsAdapter.itemCount
                posts.addAll(newPosts)
                postsAdapter.notifyItemRangeInserted(currentSize, newPosts.size)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) pbLoadData.visibility = View.VISIBLE
            else pbLoadData.visibility = View.GONE
        }
    }
}