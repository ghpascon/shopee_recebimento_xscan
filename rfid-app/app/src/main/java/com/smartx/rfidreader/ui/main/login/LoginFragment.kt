package com.smartx.rfidreader.ui.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.FragmentLoginBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private var lastCheckedXtrackUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        observeState()
    }

    private fun setupActions() {
        binding.btnLogin.setOnClickListener {
            submitLogin()
        }

        binding.textXtrackWarning.setOnClickListener {
            viewModel.checkXtrackCommunication(force = true)
        }

        binding.editPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitLogin()
                true
            } else {
                false
            }
        }
    }

    private fun submitLogin() {
        val loginBlockReason = viewModel.getLoginBlockReason()
        if (loginBlockReason != null) {
            Snackbar.make(binding.root, loginBlockReason, Snackbar.LENGTH_LONG).show()
            return
        }

        val username = binding.editUsername.text?.toString()?.trim().orEmpty()
        val password = binding.editPassword.text?.toString().orEmpty()

        if (username.isBlank() || password.isBlank()) {
            Snackbar.make(binding.root, getString(R.string.error_login_empty_fields), Snackbar.LENGTH_SHORT).show()
            return
        }

        viewModel.loginXTrack(username, password)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val currentUrl = state.appSettings.xtrackUrl.trim()
                    if (currentUrl != lastCheckedXtrackUrl) {
                        lastCheckedXtrackUrl = currentUrl
                        viewModel.checkXtrackCommunication(force = true)
                    }

                    val loginBlockReason = viewModel.getLoginBlockReason()
                    binding.progressLogin.visibility = if (state.isLoggingIn) View.VISIBLE else View.GONE
                    binding.btnLogin.isEnabled = !state.isLoggingIn && loginBlockReason == null
                    binding.textLoginBlockReason.visibility = if (loginBlockReason == null) View.GONE else View.VISIBLE
                    binding.textLoginBlockReason.text = loginBlockReason.orEmpty()

                    when {
                        state.isCheckingXtrackCommunication -> {
                            binding.textXtrackWarning.visibility = View.VISIBLE
                            binding.textXtrackWarning.text = getString(R.string.xtrack_warning_checking)
                        }
                        !state.xtrackCommunicationWarning.isNullOrBlank() -> {
                            binding.textXtrackWarning.visibility = View.VISIBLE
                            binding.textXtrackWarning.text =
                                state.xtrackCommunicationWarning + "\n" + getString(R.string.xtrack_warning_retry_hint)
                        }
                        else -> {
                            binding.textXtrackWarning.visibility = View.GONE
                            binding.textXtrackWarning.text = ""
                        }
                    }

                    state.loginErrorMessage?.let { error ->
                        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                        viewModel.consumeLoginError()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
