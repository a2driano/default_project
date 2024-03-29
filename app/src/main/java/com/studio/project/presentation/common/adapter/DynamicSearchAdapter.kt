package com.studio.project.presentation.common.adapter

import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrew on 25.04.2020
 */
abstract class DynamicSearchAdapter<T : DynamicSearchAdapter.Searchable>(private val searchableList: MutableList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    // Single not-to-be-modified copy of original data in the list.
    private val originalList = ArrayList(searchableList)
    // a method-body to invoke when search returns nothing. It can be null.
    private var onNothingFound: (() -> Unit)? = null

    protected fun setSearchData(searchableList: MutableList<T>) {
        originalList.clear()
        originalList.addAll(ArrayList(searchableList))
    }

    /**
     * Searches a specific item in the list and updates adapter.
     * if the search returns empty then onNothingFound callback is invoked if provided which can be used to update UI
     * @param s the search query or text. It can be null.
     * @param onNothingFound a method-body to invoke when search returns nothing. It can be null.
     */
    fun search(s: String?, onNothingFound: (() -> Unit)?) {
        this.onNothingFound = onNothingFound
        filter.filter(s)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableList.clear()
                if (constraint.isNullOrBlank()) {
                    searchableList.addAll(originalList)
                } else {
                    val search = constraint.toString()
                    val searchResults =
//                        originalList.filter { it.getSearchCriteria().contains(constraint) }
                        originalList.filter {
                            it.getSearchCriteria().toLowerCase().contains(search.toLowerCase())
                        }
                    searchableList.addAll(searchResults)
                }
                return filterResults.also {
                    it.values = searchableList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // no need to use "results" filtered list provided by this method.
                if (searchableList.isNullOrEmpty())
                    onNothingFound?.invoke()
                notifyDataSetChanged()
            }
        }
    }

    interface Searchable {
        /** This method will allow to specify a search string to compare against
        your search this can be anything depending on your use case.
         */
        fun getSearchCriteria(): String
    }
}