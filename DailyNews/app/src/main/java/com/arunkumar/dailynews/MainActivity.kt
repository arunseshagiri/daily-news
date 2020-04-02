package com.arunkumar.dailynews

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arunkumar.dailynews.api.ArticlesApiService
import com.arunkumar.dailynews.model.Articles
import com.arunkumar.dailynews.utils.ARTICLE_URL
import com.arunkumar.dailynews.utils.hideProgressUI
import com.arunkumar.dailynews.utils.showProgressUI
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
            else -> hideProgressUI(iv_progress)
        }
        articleAdapter
            .launchWebView()
            .subscribe(
                {
                    val intent = Intent(this, ArticleWebViewActivity::class.java)
                    intent.putExtra(ARTICLE_URL, it)
                    startActivity(intent)
                },
                {
                    it.printStackTrace()
                }
            )
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
            R.id.settings -> {
                //TODO: Open settings preference
            }
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
                    showProgressUI(iv_progress)
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
                    hideProgressUI(iv_progress)
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

    private fun showErrorUI() {
        hideProgressUI(iv_progress)
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
