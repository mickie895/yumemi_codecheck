/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.fragments.show

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.codecheck.databinding.FragmentRepositoryShowBinding

class RepositoryShowFragment : Fragment(R.layout.fragment_repository_show) {

    private val args: RepositoryShowFragmentArgs by navArgs()

    private var binding: FragmentRepositoryShowBinding? = null
    private val _binding get() = binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        binding = FragmentRepositoryShowBinding.bind(view)

        var item = args.repository

        _binding.ownerIconView.load(item.ownerIconUrl)
        _binding.nameView.text = item.name
        _binding.languageView.text = item.language
        _binding.starsView.text = "${item.stargazersCount} stars"
        _binding.watchersView.text = "${item.watchersCount} watchers"
        _binding.forksView.text = "${item.forksCount} forks"
        _binding.openIssuesView.text = "${item.openIssuesCount} open issues"
    }
}
