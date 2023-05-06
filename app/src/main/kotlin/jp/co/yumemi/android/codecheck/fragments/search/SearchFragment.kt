package jp.co.yumemi.android.codecheck.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.data.search.RepositoryProperty
import jp.co.yumemi.android.codecheck.data.search.SearchApiResponse
import jp.co.yumemi.android.codecheck.databinding.FragmentSearchBinding
import jp.co.yumemi.android.codecheck.viewmodels.SearchFragmentViewModel
import jp.co.yumemi.android.codecheck.viewmodels.SearchResultItem

/**
 * 検索画面
 */
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), SearchResultAdapter.OnItemClickListener {
    // おおよそテンプレート化されている実装

    private val viewModel: SearchFragmentViewModel by viewModels()

    private var bindingSource: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = bindingSource!!
    private lateinit var adapter: SearchResultAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingSource = FragmentSearchBinding.bind(view)

        // 値変化時の見た目の追従
        viewModel.searchedRepositoryList.observe(viewLifecycleOwner, searchResultObserver)
        viewModel.lastError.observe(viewLifecycleOwner, searchErrorObserver)
        viewModel.searching.observe(viewLifecycleOwner, searchProgressObserver)

        // recyclerviewの準備
        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)

        adapter = SearchResultAdapter(this)

        binding.recyclerView.layoutManager = layoutManager
        // 区切り線を設ける
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.adapter = adapter

        binding.searchInputText.setOnEditorActionListener(searchTextInput)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingSource = null
    }

    // 以下より詳細な実装

    /**
     * 検索欄で動きがあったときの対応
     */
    private val searchTextInput =
        TextView.OnEditorActionListener { searchView: TextView, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val text = searchView.text.toString()
                viewModel.search(text)
            }
            false
        }

    /**
     * 検索結果の更新を受け取ったときの処理
     */
    private val searchResultObserver: Observer<List<SearchResultItem>> = Observer {
        adapter.submitList(it)
    }

    /**
     * 検索が何らかの原因によって失敗したときの処理
     */
    private val searchErrorObserver: Observer<SearchApiResponse.Error?> = Observer {
        // nullになったことを受け取った場合は何もしない
        val error = when (it) {
            null -> return@Observer
            else -> it
        }

        val toastText = when (error) {
            is SearchApiResponse.Error.ByNetwork -> R.string.error_by_network
            is SearchApiResponse.Error.ByQuery -> R.string.error_by_query
            is SearchApiResponse.Error.ByUnknownSource -> R.string.error_by_unknwoun_reason
        }

        Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
        Log.i("Repository search error", "Error \"${error.causedBy}\" happened ")
        viewModel.errorMessageRecieved()
    }

    /**
     * 検索中かどうかを視覚的にわかりやすくするための処理
     */
    private val searchProgressObserver: Observer<Boolean> = Observer {
        binding.searchProgress.visibility = when (it) {
            true -> View.VISIBLE
            false -> View.INVISIBLE
        }
    }

    /**
     * 検索結果表示用の項目を選択したときの挙動
     */
    override fun itemClick(item: SearchResultItem) {
        when (item) {
            SearchResultItem.EmptyItem -> {} // ※何らかの拍子に選択できてしまったときの事故防止
            is SearchResultItem.Repository -> gotoRepositoryFragment(item.repository)
            SearchResultItem.SearchNextItem -> viewModel.nextPage()
        }
    }

    /**
     * 対象のリポジトリへ遷移する
     */
    private fun gotoRepositoryFragment(item: RepositoryProperty) {
        val action = SearchFragmentDirections.actionSearchFragmentToRepositoryShowFragment(item)
        findNavController().navigate(action)
    }
}
