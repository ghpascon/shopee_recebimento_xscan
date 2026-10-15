package com.smartx.rfidreader.ui.main.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.settings.AppSettings
import com.smartx.rfidreader.core.xtrack.XTrackLocation
import com.smartx.rfidreader.core.xtrack.xtrack_manager
import com.smartx.rfidreader.databinding.FragmentXtrackConfigBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.launch

class XTrackConfigFragment : Fragment() {

    private var _binding: FragmentXtrackConfigBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private var locationOptions: List<LocationOption> = emptyList()
    private var lastLoadedLocationsUrl: String = ""
    private var lastTechnicalDetails: String = ""

    private data class LocationOption(
        val id: String,
        val name: String
    ) {
        override fun toString(): String {
            return if (name.isBlank()) id else "$name ($id)"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentXtrackConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textXtrackDetails.text = getString(R.string.xtrack_details_placeholder)
        setupActions()
        observeState()
    }

    private fun setupActions() {
        binding.btnReloadLocations.setOnClickListener {
            val url = saveUrlAndReturn(clearLocationIfUrlChanged = true)
            if (url.isBlank()) {
                showSnackbar(getString(R.string.error_xtrack_url_required))
                return@setOnClickListener
            }
            loadAvailableLocations(url)
        }

        binding.btnSaveXtrack.setOnClickListener {
            val url = saveUrlAndReturn(clearLocationIfUrlChanged = true)
            if (url.isBlank()) {
                showSnackbar(getString(R.string.error_xtrack_url_required))
                return@setOnClickListener
            }
            if (locationOptions.isEmpty()) {
                loadAvailableLocations(url)
            }
            showSnackbar(getString(R.string.app_settings_saved))
        }

        binding.autoLocation.setOnItemClickListener { _, _, position, _ ->
            val option = locationOptions.getOrNull(position) ?: return@setOnItemClickListener
            val current = viewModel.uiState.value.appSettings
            val normalizedUrl = binding.editXtrackUrl.text?.toString()?.trim().orEmpty()

            viewModel.saveAppSettings(
                current.copy(
                    xtrackUrl = normalizedUrl,
                    selectedLocationId = option.id,
                    selectedLocationName = option.name
                )
            )
            showSnackbar(getString(R.string.app_settings_saved))
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateFromSettings(state.appSettings)
                }
            }
        }
    }

    private fun updateFromSettings(settings: AppSettings) {
        val currentXtrack = binding.editXtrackUrl.text?.toString().orEmpty()
        if (currentXtrack != settings.xtrackUrl) {
            binding.editXtrackUrl.setText(settings.xtrackUrl)
        }

        maybeReloadLocations(settings.xtrackUrl)
        syncSelectedLocationText(settings)
    }

    private fun saveUrlAndReturn(clearLocationIfUrlChanged: Boolean): String {
        val normalizedUrl = binding.editXtrackUrl.text?.toString()?.trim().orEmpty()
        val current = viewModel.uiState.value.appSettings
        val urlChanged = current.xtrackUrl.trim() != normalizedUrl

        val updated = if (clearLocationIfUrlChanged && urlChanged) {
            current.copy(
                xtrackUrl = normalizedUrl,
                selectedLocationId = "",
                selectedLocationName = ""
            )
        } else {
            current.copy(xtrackUrl = normalizedUrl)
        }

        viewModel.saveAppSettings(updated)

        if (clearLocationIfUrlChanged && urlChanged) {
            locationOptions = emptyList()
            binding.autoLocation.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, locationOptions)
            )
            binding.autoLocation.setText("", false)
            lastLoadedLocationsUrl = ""
        }

        return normalizedUrl
    }

    private fun maybeReloadLocations(xtrackUrl: String) {
        val normalized = xtrackUrl.trim()
        if (normalized.isBlank()) return
        if (normalized == lastLoadedLocationsUrl) return
        loadAvailableLocations(normalized)
    }

    private fun loadAvailableLocations(xtrackUrl: String) {
        val normalized = xtrackUrl.trim()
        if (normalized.isBlank()) return

        lastLoadedLocationsUrl = normalized
        binding.layoutLocation.isEnabled = false
        binding.btnReloadLocations.isEnabled = false
        binding.progressLocations.visibility = View.VISIBLE
        setTechnicalDetails(getString(R.string.xtrack_details_loading, normalized))

        viewLifecycleOwner.lifecycleScope.launch {
            val result = xtrack_manager.fetchLocations(normalized)
            if (_binding == null) return@launch

            binding.layoutLocation.isEnabled = true
            binding.btnReloadLocations.isEnabled = true
            binding.progressLocations.visibility = View.GONE

            result.onSuccess { locations ->
                applyLocationOptions(locations)
                syncSelectedLocationText(viewModel.uiState.value.appSettings)

                val sample = locations.firstOrNull()?.let {
                    if (it.name.isBlank()) it.id else "${it.name} (${it.id})"
                } ?: "-"
                setTechnicalDetails(
                    getString(R.string.xtrack_details_success, locations.size, sample)
                )
            }.onFailure { err ->
                applyLocationOptions(emptyList())
                setTechnicalDetails(
                    getString(
                        R.string.xtrack_details_error,
                        err.message ?: err::class.java.simpleName
                    )
                )
                showSnackbar(getString(R.string.error_load_locations, err.message ?: "erro"))
            }
        }
    }

    private fun setTechnicalDetails(message: String) {
        lastTechnicalDetails = message
        if (_binding != null) {
            binding.textXtrackDetails.text = lastTechnicalDetails
        }
    }

    private fun applyLocationOptions(locations: List<XTrackLocation>) {
        locationOptions = locations.map { LocationOption(id = it.id, name = it.name) }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, locationOptions)
        binding.autoLocation.setAdapter(adapter)
    }

    private fun syncSelectedLocationText(settings: AppSettings) {
        val selected = locationOptions.firstOrNull { it.id == settings.selectedLocationId }
        val displayValue = when {
            selected != null -> selected.toString()
            settings.selectedLocationName.isNotBlank() && settings.selectedLocationId.isNotBlank() ->
                "${settings.selectedLocationName} (${settings.selectedLocationId})"
            settings.selectedLocationName.isNotBlank() -> settings.selectedLocationName
            settings.selectedLocationId.isNotBlank() -> settings.selectedLocationId
            else -> ""
        }

        val currentText = binding.autoLocation.text?.toString().orEmpty()
        if (currentText != displayValue) {
            binding.autoLocation.setText(displayValue, false)
        }
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(requireActivity().window.decorView, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
