package com.arunkumar.dailynews

import com.arunkumar.dailynews.api.ArticlesApiService
import com.arunkumar.dailynews.model.Articles
import io.mockk.*
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class ArticleViewModelTest {
    private lateinit var viewModel: ArticleViewModel
    private val articleService = mockk<ArticlesApiService>()

    private lateinit var articleListSingle: Single<List<Articles>>
    private lateinit var articleListSingleError: Single<List<Articles>>
    private lateinit var articleList: List<Articles>


    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        viewModel = spyk(ArticleViewModel(articleService))

        val article1 = Articles(
            "Carousell is launching its own digital wallet to improve payments for its users",
            "Due to launch next month in Singapore, CarouPay will allow buyers and sellers to complete transactions without leaving the Carousell app, rather than having to rely on third-party platforms or doing meet-ups to hand over cash. CarouPay will be a digital wallet within the Carousell app. \\\"More than half of our sellers will end up buying items as well, so maybe it makes sense to have that money in the wallet for purchases\\\" - Quek tells Tech in Asia.",
            "https://storage.googleapis.com/carousell-interview-assets/android/images/carousell-siu-rui-ceo-tia-sg-2018.jpg",
            1532853058,
            2
        )

        val article2 = Articles(
            "Carousell is launching its own digital wallet to improve payments for its users",
            "Due to launch next month in Singapore, CarouPay will allow buyers and sellers to complete transactions without leaving the Carousell app, rather than having to rely on third-party platforms or doing meet-ups to hand over cash. CarouPay will be a digital wallet within the Carousell app. \\\"More than half of our sellers will end up buying items as well, so maybe it makes sense to have that money in the wallet for purchases\\\" - Quek tells Tech in Asia.",
            "https://storage.googleapis.com/carousell-interview-assets/android/images/carousell-siu-rui-ceo-tia-sg-2018.jpg",
            1532939458,
            5
        )

        articleList = mutableListOf(article1, article2)

        articleListSingle = Single.just(articleList)
        articleListSingleError = Single.error(Throwable("error message"))
    }

    @Test
    fun testFetchArticleFromServerSuccess() {
        every { articleService.articles() } returns articleListSingle
        viewModel.onCreate()

        verify(exactly = 1) {
            viewModel.onCreate()
            viewModel.showProgress()
            viewModel.hideProgress()
            viewModel.updateArticleList()
        }

        verify(exactly = 0) {
            viewModel.showError()
        }

        confirmVerified(viewModel)
    }

    @Test
    fun testFetchArticleFromServerFailure() {
        every { articleService.articles() } returns articleListSingleError

        viewModel.onCreate()

        verify(exactly = 0) {
            viewModel.updateArticleList()
        }

        verify(exactly = 1) {
            viewModel.onCreate()
            viewModel.showError()
            viewModel.showProgress()
            viewModel.hideProgress()
        }

        confirmVerified(viewModel)
    }

    @Test
    fun testSortBasedOnRecentArticleTest() {
        viewModel.sortBasedOnRecentArticle(articleList)

        verify(exactly = 1) {
            viewModel.sortBasedOnRecentArticle(articleList)
            viewModel.articleListSortedForRecent()
        }
    }

    @Test
    fun testSortBasedOnPopularArticle() {
        viewModel.sortBasedOnPopularArticle(articleList)

        verify(exactly = 1) {
            viewModel.sortBasedOnPopularArticle(articleList)
            viewModel.articleListSortedForPopular()
        }
    }
}