package jp.co.yumemi.android.codecheck.fragments.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.data.history.SearchHistory
import jp.co.yumemi.android.codecheck.databinding.FragmentHistoryBinding
import jp.co.yumemi.android.codecheck.viewmodels.HistoryFragmentViewModel

/**
 * 検索履歴の表示用フラグメント
 */
@AndroidEntryPoint
class HistoryFragment : HistoryAdapter.OnHistoryClickedListener, Fragment(R.layout.fragment_history) {
    private val viewModel: HistoryFragmentViewModel by viewModels()

    private var bindingSource: FragmentHistoryBinding? = null
    private val binding get() = bindingSource!!

    private lateinit var adapter: HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingSource = FragmentHistoryBinding.bind(view)
        viewModel.historyList.observe(viewLifecycleOwner, historyListObserver)

        adapter = HistoryAdapter(this)
        binding.historyList.layoutManager = LinearLayoutManager(requireContext())
        binding.historyList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingSource = null
    }

    /**
     * 検索結果取得時処理
     */
    private val historyListObserver = Observer<List<SearchHistory>> {
        adapter.submitList(it)
    }

    /**
     * 検索履歴を選択したときの処理
     */
    override fun onHistoryClicked(query: String) {
        viewModel.searchFromHistory(query)
        // 選択したら前画面に戻す
        findNavController().navigate(R.id.action_historyFragment_to_searchFragment)
    }
}
