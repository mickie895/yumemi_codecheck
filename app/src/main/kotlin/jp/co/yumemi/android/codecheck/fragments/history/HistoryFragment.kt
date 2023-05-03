package jp.co.yumemi.android.codecheck.fragments.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.databinding.FragmentHistoryBinding
import jp.co.yumemi.android.codecheck.viewmodels.HistoryFragmentViewModel

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val viewModel: HistoryFragmentViewModel by viewModels()

    private var bindingSource: FragmentHistoryBinding? = null
    private val binding get() = bindingSource!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingSource = FragmentHistoryBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingSource = null
    }
}
