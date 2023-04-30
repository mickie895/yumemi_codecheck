package jp.co.yumemi.android.codecheck.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.data.RepositoryProperty
import jp.co.yumemi.android.codecheck.data.SearchApiResult
import jp.co.yumemi.android.codecheck.databinding.FragmentSearchBinding
import jp.co.yumemi.android.codecheck.viewmodels.SearchFragmentViewModel

/**
 * 検索画面
 */
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchFragmentViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchResultAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)

        viewModel.searchedRepositoryList.observe(viewLifecycleOwner, searchResultObserver)
        viewModel.lastError.observe(viewLifecycleOwner, searchErrorObserver)

        // recyclerviewの準備
        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)

        adapter = SearchResultAdapter(object : SearchResultAdapter.OnItemClickListener {
            override fun itemClick(item: RepositoryProperty) {
                gotoRepositoryFragment(item)
            }
        })

        binding.recyclerView.apply {
            this.layoutManager = layoutManager
            this.addItemDecoration(dividerItemDecoration)
            this.adapter = adapter
        }

        // 検索欄の準備
        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    editText.text.toString().let {
                        viewModel.searchRepository(it)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
    }

    /**
     * 対象のリポジトリへ遷移する
     */
    fun gotoRepositoryFragment(item: RepositoryProperty) {
        val action = SearchFragmentDirections.actionSearchFragmentToRepositoryShowFragment(item)
        findNavController().navigate(action)
    }

    /**
     * 検索結果の更新を受け取ったときの処理
     */
    private val searchResultObserver: Observer<List<RepositoryProperty>> = Observer {
        adapter.submitList(it)
    }

    /**
     * 検索画面何らかの原因によって失敗したときの処理
     */
    private val searchErrorObserver: Observer<SearchApiResult.Error?> = Observer {
        // nullになったことを受け取った場合は何もしない
        val error = when (it) {
            null -> return@Observer
            else -> it
        }

        val toastText = when (error) {
            is SearchApiResult.Error.ByNetwork -> R.string.error_by_network
            is SearchApiResult.Error.ByQuery -> R.string.error_by_query
            is SearchApiResult.Error.ByUnknownSource -> R.string.error_by_unknwoun_reason
        }

        Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
        Log.i("SearchFragment", "Error \"${error.causedBy}\" happened ")
        viewModel.errorMessageRecieved()
    }
}
