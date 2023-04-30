/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.fragments.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.data.RepositoryProperty
import jp.co.yumemi.android.codecheck.databinding.FragmentRepositoryShowBinding

/**
 * リポジトリの表示画面
 */
class RepositoryShowFragment : Fragment(R.layout.fragment_repository_show) {

    // 他画面から提供される、リポジトリ表示用の引数
    private val args: RepositoryShowFragmentArgs by navArgs()

    private var bindingSource: FragmentRepositoryShowBinding? = null
    private val binding get() = bindingSource!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingSource = FragmentRepositoryShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRepositoryStatus(args.repository)
    }

    private fun setRepositoryStatus(property: RepositoryProperty) {
        binding.ownerIcon.load(property.owner.avatarIconUrl)
        binding.repositoryName.text = property.name

        binding.languageDescription.text =
            when (property.language) {
                null -> getString(R.string.no_language_detected)
                else -> getString(R.string.written_language, property.language)
            }

        binding.stars.text = resources.getQuantityText(R.plurals.stars, property.stargazersCount)
        binding.watchers.text = resources.getQuantityText(R.plurals.watchers, property.watchersCount)
        binding.forks.text = resources.getQuantityText(R.plurals.forks, property.forksCount)
        binding.issues.text = resources.getQuantityText(R.plurals.issues, property.openIssuesCount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingSource = null
    }
}
