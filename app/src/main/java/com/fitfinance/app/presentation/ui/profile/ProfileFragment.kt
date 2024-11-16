package com.fitfinance.app.presentation.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentProfileBinding
import com.fitfinance.app.domain.response.UserGetResponse
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.formatToCpf
import com.fitfinance.app.util.formatToCurrency
import com.fitfinance.app.util.formatToPhone
import com.fitfinance.app.util.getProgressDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ProfileViewModel>()

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private var progressDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        viewModel.getUserInfo(sharedPreferences.getString(resources.getString(R.string.pref_user_token), "")!!)
        return root
    }

    private fun setupUi(info: UserGetResponse) {
        binding.apply {
            tvUserName.text = info.name
            tvUserCpf.text = formatToCpf(info.cpf)
            tvUserEmail.text = info.email
            tvUserPhone.text = formatToPhone(info.phone)
            tvUserBirthdate.text = info.birthdate
            tvUserIncome.text = formatToCurrency(info.income.toString())
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
        _binding = null
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}