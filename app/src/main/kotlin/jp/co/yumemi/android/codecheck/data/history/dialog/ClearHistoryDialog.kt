package jp.co.yumemi.android.codecheck.data.history.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.R

/**
 * 履歴を消去するかどうかの問い合わせを行うダイアログ
 */
@AndroidEntryPoint
class ClearHistoryDialog : DialogFragment() {
    // ダイアログとフラグメントの間のライフサイクルの差と、
    // 親画面に結果を教えるためのイベントの作成の手段を考慮するのが非常に面倒なため、
    // ビューモデル経由でこのダイアログに履歴の削除を行わせる。

    private val viewModel: ClearHistoryViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(R.string.clear_history_confirm)
            .setPositiveButton(R.string.button_delete, onClearNeeded)
            .setNegativeButton(R.string.button_cansel, emptyEventListener)

        return builder.create()
    }

    /**
     * 特に何もしないリスナ
     */
    private val emptyEventListener = DialogInterface.OnClickListener { _, _ ->
    }

    /**
     * 削除ボタンが押されたときのリスナ
     */
    private val onClearNeeded = DialogInterface.OnClickListener { _, _ ->
        viewModel.removeHistory()
    }
}
