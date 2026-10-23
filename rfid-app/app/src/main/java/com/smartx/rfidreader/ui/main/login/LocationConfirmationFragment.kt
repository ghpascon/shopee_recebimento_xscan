package com.smartx.shopeerecebimento.ui.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartx.shopeerecebimento.R
import com.smartx.shopeerecebimento.databinding.FragmentLocationConfirmationBinding
import com.smartx.shopeerecebimento.ui.main.MainActivity
import com.smartx.shopeerecebimento.ui.main.MainViewModel
import kotlinx.coroutines.launch

class LocationConfirmationFragment : Fragment() {

    private var _binding: FragmentLocationConfirmationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonConfirmLocation.setOnClickListener {
            (requireActivity() as MainActivity).continueToLoginAfterLocationConfirmation()
        }
        binding.buttonRejectLocation.setOnClickListener {
            binding.confirmationContent.visibility = View.GONE
            binding.helpContent.visibility = View.VISIBLE
        }
        binding.buttonBackToConfirmation.setOnClickListener {
            binding.helpContent.visibility = View.GONE
            binding.confirmationContent.visibility = View.VISIBLE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val rawLocation = state.appSettings.selectedLocationName
                        .ifBlank { state.appSettings.selectedLocationId }
                        .trim()
                    val configuredLocation = rawLocation.substringBefore('/').trim()

                    binding.textReceivingLocation.text = when {
                        !state.isAppSettingsLoaded -> getString(R.string.location_confirmation_loading)
                        configuredLocation.isBlank() -> getString(R.string.location_not_configured)
                        else -> configuredLocation
                    }
                    binding.buttonConfirmLocation.isEnabled =
                        state.isAppSettingsLoaded && configuredLocation.isNotBlank()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
