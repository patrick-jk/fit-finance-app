package com.fitfinance.app.presentation.ui.profile.changepassword

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.fitfinance.app.R
import com.fitfinance.app.databinding.FragmentChangePasswordBinding
import com.fitfinance.app.domain.request.ChangePasswordRequest
import com.fitfinance.app.presentation.statepattern.State
import com.fitfinance.app.util.ClearErrorTextWatcher
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.fitfinance.app.util.ValidateInput
import com.fitfinance.app.util.createDialog
import com.fitfinance.app.util.text
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ChangePasswordViewModel>()

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
    private var progressDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val root = binding.root

        setupUi()
        return root
    }

    private fun setupUi() {
        binding.apply {
            tilCurrentPassword.editText?.addTextChangedListener(ClearErrorTextWatcher(tilCurrentPassword))
            tilNewPassword.editText?.addTextChangedListener(ClearErrorTextWatcher(tilNewPassword))
            tilNewPasswordConfirmation.editText?.addTextChangedListener(ClearErrorTextWatcher(tilNewPasswordConfirmation))

            btnChangePassword.setOnClickListener {
                if (!validateFields()) {
                    return@setOnClickListener
                }

                requireContext().createDialog {
                    setTitle(resources.getString(R.string.txt_confirm_changes))
                    setMessage(resources.getString(R.string.txt_confirm_change_password))
                    setPositiveButton(resources.getString(android.R.string.ok)) { _, _ ->
                        viewModel.updatePassword(
                            sharedPreferences.getString(resources.getString(R.string.pref_user_token), "")!!,
                            ChangePasswordRequest(
                                binding.tilCurrentPassword.text,
                                binding.tilNewPassword.text,
                                binding.tilNewPasswordConfirmation.text
                            )
                        )
                    }
                    setNegativeButton(resources.getString(android.R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                        dismiss()
                    }
                }.show()
            }
        }
    }

    private fun validateFields(): Boolean {
        val inputValidator = ValidateInput(requireContext())
        val isCurrentPasswordValid = inputValidator.validateInputText(binding.tilCurrentPassword) && inputValidator.validatePassword(binding.tilCurrentPassword)
        val isNewPasswordValid = inputValidator.validateInputText(binding.tilNewPassword) && inputValidator.validatePassword(binding.tilNewPassword)
        val isNewPasswordConfirmationValid =
            inputValidator.validateInputText(binding.tilNewPasswordConfirmation) && inputValidator.validatePassword(binding.tilNewPasswordConfirmation)

        return isCurrentPasswordValid && isNewPasswordValid && isNewPasswordConfirmationValid && (binding.tilNewPassword.text == binding.tilNewPasswordConfirmation.text)
    }

    override fun onStart() {
        super.onStart()
        viewModel.changePasswordState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    progressDialog = requireContext().createDialog {
                        setMessage(resources.getString(R.string.txt_changing_password))
                        setCancelable(false)
                    }
                    progressDialog?.show()
                }

                is State.Success -> {
                    progressDialog?.dismiss()
                    requireContext().createDialog {
                        setTitle(resources.getString(R.string.txt_success))
                        setMessage(resources.getString(R.string.txt_password_updated))
                        setPositiveButton(resources.getString(android.R.string.ok)) { _, _ ->
                            requireActivity().supportFragmentManager.setFragmentResult(REQUEST_FINISH_SESSION, Bundle())
                            dismiss()
                        }
                    }.show()
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

    companion object {
        const val REQUEST_FINISH_SESSION = "finishUserSession"

        fun newInstance() = ChangePasswordFragment()
    }
}