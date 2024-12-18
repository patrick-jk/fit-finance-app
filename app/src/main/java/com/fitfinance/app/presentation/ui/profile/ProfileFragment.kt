package com.fitfinance.app.presentation.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentProfileBinding
import com.fitfinance.app.domain.response.UserGetResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.presentation.ui.profile.changepassword.ChangePasswordFragment
import com.fitfinance.app.presentation.ui.profile.edituser.EditUserProfileFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.formatToCpf
import com.fitfinance.app.util.formatToCurrency
import com.fitfinance.app.util.formatToPhone
import com.fitfinance.app.util.getProgressDialog
import com.fitfinance.app.util.toLocalDateBrFormat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BottomSheetDialogFragment() {
    private lateinit var _binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private var progressDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = _binding.root

        requireActivity().supportFragmentManager.setFragmentResultListener("updateUserInfo", viewLifecycleOwner) { _, _ ->
            viewModel.getUserInfo(sharedPreferences.getString(getString(R.string.pref_user_token), "")!!)
        }

        viewModel.getUserInfo(sharedPreferences.getString(resources.getString(R.string.pref_user_token), "")!!)
        return root
    }

    private fun setupUi(info: UserGetResponse) {
        _binding.apply {
            tvUserName.text = info.name
            tvUserCpf.text = formatToCpf(info.cpf)
            tvUserEmail.text = info.email
            tvUserPhone.text = formatToPhone(info.phone)
            tvUserBirthdate.text = info.birthdate.toLocalDateBrFormat()
            tvUserIncome.text = formatToCurrency(info.income.toString())

            btnEditUser.setOnClickListener {
                EditUserProfileFragment.newInstance(info).show(parentFragmentManager, EditUserProfileFragment::class.java.simpleName)
            }
            btnEditPassword.setOnClickListener {
                ChangePasswordFragment.newInstance().show(parentFragmentManager, ChangePasswordFragment::class.java.simpleName)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.userGetResponse.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().getProgressDialog(resources.getString(R.string.txt_loading_home_data))
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    setupUi(it.info)
                }

                is State.Error -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setTitle(resources.getString(R.string.txt_error))
                        setMessage(it.error.message)
                        setPositiveButton(resources.getString(android.R.string.ok), null)
                    }.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding.root.removeAllViews()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}