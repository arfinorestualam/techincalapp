package com.fin.technicalapp.feature.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.fin.technicalapp.R
import com.fin.technicalapp.core.component.BaseBottomSheetFragment
import com.fin.technicalapp.core.data.authentication.api.model.Login
import com.fin.technicalapp.databinding.BottomSheetProfileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileBottomSheet: BaseBottomSheetFragment() {

    private lateinit var binding: BottomSheetProfileBinding
    companion object {
        const val ARG_USERNAME  ="arg_username"
        const val TAG = "bottomsheet_profile"
        @JvmStatic
        fun newInstance(user: Login) = ProfileBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_USERNAME, user.username)
            }
        }
    }

    var onLogoutClick: () -> Unit = {}

    override fun setLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        with(binding) {
            btnLogout.setOnClickListener {
                onLogoutClick.invoke()
            }

            tvUsername.text = getString(R.string.profile_name, arguments?.getString(ARG_USERNAME))
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.apply {
                add(this@ProfileBottomSheet, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            Log.e(tag, e.message.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
    }
}