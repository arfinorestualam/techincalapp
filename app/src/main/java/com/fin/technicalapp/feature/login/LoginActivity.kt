package com.fin.technicalapp.feature.login

import android.content.Intent
import android.view.View
import com.fin.technicalapp.core.component.BaseActivity
import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.utils.extensions.collectFlow
import com.fin.technicalapp.core.utils.extensions.getLaunch
import com.fin.technicalapp.core.utils.extensions.showToast
import com.fin.technicalapp.databinding.ActivityLoginBinding
import com.fin.technicalapp.feature.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity: BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewmodel: LoginViewModel by viewModel()

    override fun setLayout(): View = binding.root

    override fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)

        with(binding) {
            btnLogin.setOnClickListener {
                viewmodel.loginRequest.username = etUsername.text.toString().trim()
                viewmodel.loginRequest.password = etPassword.text.toString().trim()

                if (viewmodel.loginRequest.username.isEmpty()) {
                    etUsername.error = "Please enter your email"
                    etUsername.requestFocus()
                    return@setOnClickListener
                }

                if (viewmodel.loginRequest.password.isEmpty()) {
                    etPassword.error = "Please enter your password"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }


                viewmodel.login()
            }
        }

        setFlowCollector()
    }

    private fun setFlowCollector() {
        collectFlow(viewmodel.login) { appResponse ->
            when(appResponse) {
                is AppResponse.Error -> {
                    loading(false)
                    showToast(appResponse.message)
                }

                is AppResponse.Loading -> {
                    loading(true)
                }

                is AppResponse.Success -> {
                    getLaunch {
                        loading(false)
                        if (viewmodel.saveUserData(appResponse.data.token)) {
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        } else {
                            showToast("There was an error, please try again")
                        }
                    }
                }

                else -> Unit
            }
        }
    }

    private fun loading(isShow: Boolean) {
        if (isShow) {
            binding.progressLoading.show()
            binding.ivLoading.visibility = View.VISIBLE
        } else {
            binding.progressLoading.hide()
            binding.ivLoading.visibility = View.GONE
        }
    }
}