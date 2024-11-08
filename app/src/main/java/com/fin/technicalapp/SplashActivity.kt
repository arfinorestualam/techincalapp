package com.fin.technicalapp

import android.content.Intent
import android.view.View
import com.fin.technicalapp.core.component.BaseActivity
import com.fin.technicalapp.core.utils.extensions.getLaunch
import com.fin.technicalapp.databinding.ActivitySplashBinding
import com.fin.technicalapp.feature.home.HomeActivity
import com.fin.technicalapp.feature.login.LoginActivity
import com.fin.technicalapp.feature.login.LoginViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity: BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val viewmodel: LoginViewModel by viewModel()

    override fun setLayout(): View = binding.root

    override fun initView() {
        binding = ActivitySplashBinding.inflate(layoutInflater)

        getLaunch {
            delay(2000)
            viewmodel.readUserData().let {
                if (it.username.isNotEmpty()) {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }

                finish()
            }
        }

    }
}