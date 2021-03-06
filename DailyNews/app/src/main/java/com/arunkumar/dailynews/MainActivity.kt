package com.arunkumar.dailynews

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.arunkumar.dailynews.api.ArticlesApiService
import com.arunkumar.dailynews.utils.ARTICLE_URL
import com.arunkumar.dailynews.utils.PREFERENCE_COUNTRY
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
        disposables.add(listenToNoNewsAvailable())

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

    private fun listenToNoNewsAvailable(): Disposable =
        viewModel
            .noNewsAvailable()
            .subscribe {
                Toast.makeText(this, "No news available for this country", LENGTH_LONG).show()
            }


    override fun onStart() {
        super.onStart()
        viewModel.onStart(
            PreferenceManager.getDefaultSharedPreferences(this).getString(PREFERENCE_COUNTRY, "")
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
