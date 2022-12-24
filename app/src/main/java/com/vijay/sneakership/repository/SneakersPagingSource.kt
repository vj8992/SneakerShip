package com.vijay.sneakership.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vijay.sneakership.model.SneakersModel
import java.io.IOException

import kotlin.math.max

private const val DEFAULT_PAGE_INDEX = 1


class SneakersPagingSource(private val sneakersDatabase: SneakersDatabase,
private val filter: String):
  PagingSource<Int, SneakersModel>() {

  override fun getRefreshKey(state: PagingState<Int, SneakersModel>): Int? {
    val anchorPosition = state.anchorPosition ?: return null
    val sneaker = state.closestItemToPosition(anchorPosition) ?: return null
    return ensureValidKey(key = sneaker.id - (state.config.pageSize / 2))
  }

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SneakersModel> {
    val page = params.key ?: DEFAULT_PAGE_INDEX
    return try {
      val list = if (filter.isEmpty()){
        sneakersDatabase.getSneakersModelDao().getAllSneakers(page, params.loadSize)
      }else{
        val query = "%${filter}%"
        sneakersDatabase.getSneakersModelDao().getSearchSneakers(query)
      }
      LoadResult.Page(
        list, prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
        nextKey = if (list.isEmpty()) null else page + 1
      )
    } catch (exception: IOException) {
      return LoadResult.Error(exception)
    }
  }

  /**
   * Makes sure the paging key is never less than [DEFAULT_PAGE_INDEX]
   */
  private fun ensureValidKey(key: Int) = max(DEFAULT_PAGE_INDEX, key)
}