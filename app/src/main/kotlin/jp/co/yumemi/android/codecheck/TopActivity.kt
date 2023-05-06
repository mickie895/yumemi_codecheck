/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.databinding.ActivityTopBinding

/**
 * 画面表示の外枠相当（メニューの処理がメイン）
 */
@AndroidEntryPoint
class TopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 他Fragmentと同様にViewBindingを利用する
        binding = ActivityTopBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // FragmentContainerViewにNavHostFragmentを格納しているため、下記の書き方でないとNavControllerが取れないらしい
        // 参考: https://issuetracker.google.com/issues/142847973?pli=1
        val navFragment = supportFragmentManager.findFragmentById(R.id.navigationHostFragment)
        val navController = navFragment?.findNavController()
            ?: throw NullPointerException("レイアウトの変更にコードが追従できていません。")

        // 画面遷移用のメニューの作成
        NavigationUI.setupWithNavController(binding.menuNavigation, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.activityTopLayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        // ライフサイクルの都合上、こちらでは問題なく取得できるらしい。
        val navController = binding.navigationHostFragment.findNavController()
        return NavigationUI.navigateUp(navController, binding.activityTopLayout)
    }
}
