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
import jp.co.yumemi.android.codecheck.data.history.dialog.ClearHistoryDialog
import jp.co.yumemi.android.codecheck.databinding.FragmentHistoryBinding
import jp.co.yumemi.android.codecheck.viewmodels.HistoryFragmentViewModel

/**
 * 検索履歴の表示用フラグメント
 */
@AndroidEntryPoint
class HistoryFragment : HistoryAdapter.OnHistoryClickedListener, Fragment(R.layout.fragment_history) {
    // おおよそテンプレート化されている実装

    private val viewModel: HistoryFragmentViewModel by viewModels()

    private var bindingSource: FragmentHistoryBinding? = null
    private val binding get() = bindingSource!!

    // 検索履歴表示用のアダプター
    private lateinit var adapter: HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingSource = FragmentHistoryBinding.bind(view)

        viewModel.historyList.observe(viewLifecycleOwner, historyListObserver)
        viewModel.searching.observe(viewLifecycleOwner, searchingStateObserver)

        adapter = HistoryAdapter(this)
        binding.historyList.layoutManager = LinearLayoutManager(requireContext())
        binding.historyList.adapter = adapter

        binding.clearHistoryButton.setOnClickListener(clearHistoryClickListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingSource = null
    }

    // ここからメインの実装

    /**
     * 検索結果取得時処理
     */
    private val historyListObserver = Observer<List<SearchHistory>> {
        adapter.submitList(it)
    }

    /**
     * 通信状況の切り替えに追従するためのオブサーバ
     */
    private val searchingStateObserver = Observer<Boolean> {
        binding.historyList.isEnabled = !it
    }

    /**
     * 検索履歴を選択したときの処理
     */
    override fun onHistoryClicked(query: String) {
        if (viewModel.searching.value == false) {
            viewModel.searchFromHistory(query)
            // 選択したら前画面（検索画面）に戻す
            findNavController().popBackStack()
        }
    }

    /**
     * 履歴クリアボタンを押したときの処理
     */
    private val clearHistoryClickListener = View.OnClickListener {
        // ライフサイクルを考慮すると無駄に複雑になるため、
        // フラグメント視点ではダイアログは作って表示させればOKな作りにしておく
        val clearHistoryDialog = ClearHistoryDialog()
        clearHistoryDialog.show(parentFragmentManager, null)
    }
}
