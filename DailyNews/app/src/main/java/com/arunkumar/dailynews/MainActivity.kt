package com.arunkumar.dailynews

import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arunkumar.dailynews.api.ArticlesApiService
import com.arunkumar.dailynews.model.Articles
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var articlesApiService: ArticlesApiService

    @Inject
    lateinit var viewModel: ArticleViewModel

    @Inject
    lateinit var disposables: CompositeDisposable

    @Inject
    lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        (applicationContext as MainApp).component.inject(this)

        initializeRecyclerView()

        disposables.add(listenToArticlesList())
        disposables.add(listenToShowProgress())
        disposables.add(listenToHideProgress())
        disposables.add(listenToShowError())
        disposables.add(listenForRecentArticleList())
        disposables.add(listenForPopularArticleList())

        when {
            !(savedInstanceState?.containsKey("article_list") ?: false) -> viewModel.onCreate()
            else -> hideProgressUI()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        when {
            articleAdapter.articleList().isNotEmpty() -> outState.apply {
                putParcelableArrayList("article_list", ArrayList<Parcelable>(articleAdapter.articleList()))
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        when {
            savedInstanceState.containsKey("article_list") -> savedInstanceState.apply {
                articleAdapter.articleList(getParcelableArrayList<Articles>("article_list") as MutableList<Articles>)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.recent -> viewModel.sortBasedOnRecentArticle(articleAdapter.articleList())
            R.id.popular -> viewModel.sortBasedOnPopularArticle(articleAdapter.articleList())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listenForRecentArticleList() =
        viewModel
            .articleListSortedForRecent()
            .subscribe(
                {
                    articleAdapter.articleList(it)
                },
                {
                    showErrorUI()
                }
            )

    private fun listenForPopularArticleList() =
        viewModel
            .articleListSortedForPopular()
            .subscribe(
                {
                    articleAdapter.articleList(it)
                },
                {
                    showErrorUI()
                }
            )

    private fun initializeRecyclerView() {
        rv_news.setHasFixedSize(true)
        rv_news.layoutManager = LinearLayoutManager(this)
        rv_news.adapter = articleAdapter
    }

    private fun listenToArticlesList(): Disposable =
        viewModel
            .updateArticleList()
            .subscribe(
                {
                    articleAdapter.articleList(it)
                },
                {
                    showErrorUI()
                }
            )

    private fun listenToShowProgress(): Disposable =
        viewModel
            .showProgress()
            .subscribe(
                {
                    showProgressUI()
                },
                {
                    showErrorUI()
                }
            )

    private fun listenToHideProgress(): Disposable =
        viewModel
            .hideProgress()
            .subscribe(
                {
                    hideProgressUI()
                },
                {
                    showErrorUI()
                }
            )

    private fun listenToShowError(): Disposable =
        viewModel
            .showError()
            .subscribe(
                {
                    showErrorUI()
                },
                {
                    showErrorUI()
                }
            )

    private fun showProgressUI() {
        val animationSet = AnimationSet(false)
        val animRotate: Animation = AnimationUtils.loadAnimation(this,
            R.anim.rotate
        )
        val animSlideIn: Animation = AnimationUtils.loadAnimation(this,
            R.anim.abc_slide_in_bottom
        )
        animationSet.addAnimation(animRotate)
        animationSet.addAnimation(animSlideIn)
        iv_progress.startAnimation(animationSet)
        iv_progress.visibility = View.VISIBLE
    }

    private fun hideProgressUI() {
        val animSlideOut: Animation = AnimationUtils.loadAnimation(this,
            R.anim.abc_slide_out_bottom
        )
        iv_progress.startAnimation(animSlideOut)
        iv_progress.visibility = View.GONE
    }

    private fun showErrorUI() {
        hideProgressUI()
        val errorMsg = Snackbar.make(
            main_layout,
            getString(R.string.error_message),
            Snackbar.LENGTH_INDEFINITE
        )
        errorMsg.setAction(getString(R.string.dismiss)) { errorMsg.dismiss() }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
