package com.tpov.testphoto.second_task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tpov.testphoto.FORMAT_DATE
import com.tpov.testphoto.R
import com.tpov.testphoto.models.Post
import java.text.SimpleDateFormat
import java.util.*

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.profile_image)
        val userName: TextView = view.findViewById(R.id.user_name)
        val postTime: TextView = view.findViewById(R.id.post_time)
        val postDescription: TextView = view.findViewById(R.id.post_description)
        val postImage: ImageView = view.findViewById(R.id.post_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = posts[position]
        val outputFormat = SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH)

        try {
            val date = Date()
            val formattedDate = outputFormat.format(date)
            holder.postTime.text = formattedDate
        } catch (e: Exception) {
            e.printStackTrace()
            holder.postTime.text = ""
        }

        holder.userName.text = post.user_name
        holder.postDescription.text = post.message

        if (post.photo != null && post.photo.isNotEmpty()) {
            Picasso.get().load(post.photo).into(holder.postImage)
            holder.postImage.visibility = View.VISIBLE
        } else holder.postImage.visibility = View.GONE

        Picasso.get().load(post.user_pic).into(holder.profileImage)
        Picasso.get().load(post.photo).into(holder.postImage)
    }

    override fun getItemCount() = posts.size
}
