package com.arunkumar.dailynews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.arunkumar.dailynews.model.Articles
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_news.view.*
import org.ocpsoft.prettytime.PrettyTime
import org.threeten.bp.ZoneOffset
import java.util.*

class ArticleAdapter(
        private val articleList: MutableList<Articles>
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    fun articleList(): List<Articles> = articleList

    fun articleList(articleList: List<Articles>): ArticleAdapter {
        this.articleList.clear()
        this.articleList.addAll(articleList)
        notifyDataSetChanged()
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_news, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int = articleList.size


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val animation: Animation = AnimationUtils.loadAnimation(holder.view.context, R.anim.abc_slide_in_bottom)
        holder.view.startAnimation(animation)

        val article = articleList[position]

        holder.view.tv_title.text = article.title
        holder.view.tv_description.text = article.description
        Picasso.get().load(article.urlToImage).placeholder(R.drawable.ic_place_holder).into(holder.view.iv_banner)

        val publishTime = article.publishedAt
        holder.view.tv_time.text = PrettyTime().format(Date(publishTime.toEpochSecond(ZoneOffset.UTC) * 1000))
    }

    class ArticleViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}