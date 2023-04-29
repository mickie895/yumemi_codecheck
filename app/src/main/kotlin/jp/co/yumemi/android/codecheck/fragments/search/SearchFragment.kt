package jp.co.yumemi.android.codecheck.fragments.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.data.SearchFragmentViewModel
import jp.co.yumemi.android.codecheck.data.RepositoryProperty
import jp.co.yumemi.android.codecheck.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _binding = FragmentSearchBinding.bind(view)

        val _viewModel = SearchFragmentViewModel(context!!)

        val _layoutManager = LinearLayoutManager(context!!)
        val _dividerItemDecoration =
            DividerItemDecoration(context!!, _layoutManager.orientation)
        val _adapter = SearchResultAdapter(object : SearchResultAdapter.OnItemClickListener {
            override fun itemClick(item: RepositoryProperty) {
                gotoRepositoryFragment(item)
            }
        })

        _binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    editText.text.toString().let {
                        _viewModel.searchResults(it).apply {
                            _adapter.submitList(this)
                        }
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        _binding.recyclerView.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = _adapter
        }
    }

    fun gotoRepositoryFragment(item: RepositoryProperty) {
        val _action = SearchFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(item)
        findNavController().navigate(_action)
    }
}
