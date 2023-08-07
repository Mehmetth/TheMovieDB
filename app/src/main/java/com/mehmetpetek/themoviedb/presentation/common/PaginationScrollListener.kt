package com.mehmetpetek.themoviedb.presentation.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Timer
import kotlin.concurrent.schedule


abstract class PaginationScrollListener(layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private val layoutManager: LinearLayoutManager

    private var sendLoadMoreItems = true

    init {
        this.layoutManager = layoutManager
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        if (!isLastPage() && visibleItemCount + firstVisibleItemPosition >= totalItemCount
            && firstVisibleItemPosition >= 0 && dx > 0
        ) {
            if (sendLoadMoreItems) {
                sendLoadMoreItems = false
                loadMoreItems()
                Timer().schedule(1000) {
                    sendLoadMoreItems = true
                }
            }
        }

        val pastVisibleItems: Int = layoutManager.findFirstVisibleItemPosition()
        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
            isLastedPage()
        } else {
            isNotLastedPage()
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    protected abstract fun isLastedPage()
    protected abstract fun isNotLastedPage()

}