package com.smartx.shopeerecebimento.ui.main.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.smartx.shopeerecebimento.R
import com.smartx.shopeerecebimento.core.settings.AppSettings
import com.smartx.shopeerecebimento.core.xtrack.XTrackDevice
import com.smartx.shopeerecebimento.core.xtrack.XTrackLocation
import com.smartx.shopeerecebimento.core.xtrack.xtrack_manager
import com.smartx.shopeerecebimento.databinding.FragmentXtrackConfigBinding
import com.smartx.shopeerecebimento.ui.main.MainViewModel
import kotlinx.coroutines.launch

class XTrackConfigFragment : Fragment() {

    private var _binding: FragmentXtrackConfigBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private var allLocationOptions: List<LocationOption> = emptyList()
    private var lastLoadedLocationsUrl: String = ""
    private var lastTechnicalDetails: String = ""
    private var deviceOptions: List<XTrackDevice> = emptyList()
    private var activeLocationPicker: SearchPicker? = null

    private data class PickerOption(
        val label: String,
        val onSelected: () -> Unit
    )

    private data class SearchPicker(
        val dialog: AlertDialog,
        val search: com.google.android.material.textfield.TextInputEditText,
        val optionsContainer: LinearLayout,
        val emptyText: TextView,
        val emptyMessage: String,
        var options: List<PickerOption> = emptyList()
    )

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
        binding.btnSelectLocation.setOnClickListener {
            showLocationPicker()
        }

        binding.btnSelectDevice.setOnClickListener {
            showDevicePicker()
        }

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
            if (allLocationOptions.isEmpty()) {
                loadAvailableLocations(url)
            }
            showSnackbar(getString(R.string.app_settings_saved))
        }

    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateFromSettings(state.appSettings)
                    state.xtrackDeviceConfigWarning?.let { warning ->
                        binding.textDeviceStatus.visibility = View.VISIBLE
                        binding.textDeviceStatus.text = getString(
                            R.string.xtrack_device_config_warning,
                            warning
                        )
                    }
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
        val selectedUid = settings.selectedDeviceUid.trim()
        val deviceLabel = selectedUid.ifBlank { getString(R.string.hint_xtrack_device) }
        if (binding.btnSelectDevice.text?.toString() != deviceLabel) {
            binding.btnSelectDevice.text = deviceLabel
        }
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
            allLocationOptions = emptyList()
            activeLocationPicker?.dialog?.dismiss()
            binding.btnSelectLocation.text = getString(R.string.hint_location)
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
        binding.btnSelectLocation.isEnabled = false
        binding.btnReloadLocations.isEnabled = false
        binding.progressLocations.visibility = View.VISIBLE
        setTechnicalDetails(getString(R.string.xtrack_details_loading, normalized))

        viewLifecycleOwner.lifecycleScope.launch {
            val result = xtrack_manager.fetchLocations(normalized)
            if (_binding == null) return@launch

            binding.btnSelectLocation.isEnabled = true
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

    private fun showLocationPicker() {
        val picker = createSearchPicker(
            title = getString(R.string.label_location),
            emptyMessage = getString(R.string.location_options_empty)
        )
        activeLocationPicker = picker
        picker.dialog.setOnDismissListener {
            if (activeLocationPicker === picker) activeLocationPicker = null
        }
        renderPickerOptions(picker, allLocationOptions.map { option ->
            PickerOption(option.toString()) { selectLocation(option, picker) }
        })

        if (allLocationOptions.isEmpty()) {
            val url = binding.editXtrackUrl.text?.toString()?.trim().orEmpty()
            if (url.isNotBlank()) loadAvailableLocations(url)
        }
    }

    private fun showDevicePicker() {
        val picker = createSearchPicker(
            title = getString(R.string.label_xtrack_device),
            emptyMessage = getString(R.string.xtrack_devices_empty)
        )
        renderPickerOptions(picker, deviceOptions.map { device ->
            PickerOption(device.uid) { selectDevice(device, picker) }
        })
        loadAvailableDevices(picker)
    }

    private fun createSearchPicker(title: String, emptyMessage: String): SearchPicker {
        val content = layoutInflater.inflate(R.layout.dialog_xtrack_search_picker, null, false)
        val search = content.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.pickerSearchInput)
        val container = content.findViewById<LinearLayout>(R.id.pickerOptionsContainer)
        val emptyText = content.findViewById<TextView>(R.id.pickerEmptyText)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(content)
            .setNegativeButton(android.R.string.cancel, null)
            .create()
        val picker = SearchPicker(dialog, search, container, emptyText, emptyMessage)
        search.doAfterTextChanged { renderPickerOptions(picker) }
        dialog.show()
        return picker
    }

    private fun renderPickerOptions(picker: SearchPicker, options: List<PickerOption> = picker.options) {
        picker.options = options
        val query = picker.search.text?.toString()?.trim().orEmpty()
        val filtered = options.filter { it.label.contains(query, ignoreCase = true) }
        picker.optionsContainer.removeAllViews()
        picker.emptyText.text = if (query.isBlank()) picker.emptyMessage else getString(R.string.location_options_empty)
        picker.emptyText.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE

        filtered.forEach { option ->
            val button = MaterialButton(requireContext()).apply {
                text = option.label
                isAllCaps = false
                gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                setOnClickListener { option.onSelected() }
            }
            val margin = (4 * resources.displayMetrics.density).toInt()
            picker.optionsContainer.addView(
                button,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = margin }
            )
        }
    }

    private fun loadAvailableDevices(picker: SearchPicker) {
        binding.progressDevices.visibility = View.VISIBLE
        binding.btnSelectDevice.isEnabled = false
        binding.textDeviceStatus.visibility = View.VISIBLE
        binding.textDeviceStatus.text = getString(R.string.xtrack_devices_loading)

        viewLifecycleOwner.lifecycleScope.launch {
            val result = viewModel.fetchXtrackDevices()
            if (_binding == null) return@launch

            binding.progressDevices.visibility = View.GONE
            binding.btnSelectDevice.isEnabled = true
            result.onSuccess { devices ->
                deviceOptions = devices.filter {
                    !it.active.trim().equals("0", ignoreCase = true) && it.uid.isNotBlank()
                }
                renderPickerOptions(picker, deviceOptions.map { device ->
                    PickerOption(device.uid) { selectDevice(device, picker) }
                })
                binding.textDeviceStatus.text = getString(
                    R.string.xtrack_devices_loaded,
                    deviceOptions.size
                )
            }.onFailure { error ->
                deviceOptions = emptyList()
                renderPickerOptions(picker, emptyList())
                binding.textDeviceStatus.text = getString(
                    R.string.xtrack_devices_error,
                    error.message ?: error::class.java.simpleName
                )
            }
        }
    }

    private fun selectDevice(device: XTrackDevice, picker: SearchPicker) {
        binding.progressDevices.visibility = View.VISIBLE
        binding.btnSelectDevice.isEnabled = false
        binding.textDeviceStatus.text = getString(R.string.xtrack_device_applying, device.uid)

        viewLifecycleOwner.lifecycleScope.launch {
            val result = viewModel.selectXtrackDevice(device.uid)
            if (_binding == null) return@launch

            binding.progressDevices.visibility = View.GONE
            binding.btnSelectDevice.isEnabled = true
            result.onSuccess { selected ->
                binding.btnSelectDevice.text = selected.uid
                picker.dialog.dismiss()
                binding.textDeviceStatus.text = getString(R.string.xtrack_device_applied, selected.uid)
            }.onFailure { error ->
                binding.textDeviceStatus.text = getString(
                    R.string.xtrack_devices_error,
                    error.message ?: error::class.java.simpleName
                )
            }
        }
    }

    private fun applyLocationOptions(locations: List<XTrackLocation>) {
        allLocationOptions = locations.map { LocationOption(id = it.id, name = it.name) }
        activeLocationPicker?.let { picker ->
            renderPickerOptions(picker, allLocationOptions.map { option ->
                PickerOption(option.toString()) { selectLocation(option, picker) }
            })
        }
    }

    private fun selectLocation(option: LocationOption, picker: SearchPicker) {
        val current = viewModel.uiState.value.appSettings
        val normalizedUrl = binding.editXtrackUrl.text?.toString()?.trim().orEmpty()

        viewModel.saveAppSettings(
            current.copy(
                xtrackUrl = normalizedUrl,
                selectedLocationId = option.id,
                selectedLocationName = option.name
            )
        )
        binding.btnSelectLocation.text = option.toString()
        picker.dialog.dismiss()
        showSnackbar(getString(R.string.app_settings_saved))
    }

    private fun syncSelectedLocationText(settings: AppSettings) {
        val selected = allLocationOptions.firstOrNull { it.id == settings.selectedLocationId }
        val displayValue = when {
            selected != null -> selected.toString()
            settings.selectedLocationName.isNotBlank() && settings.selectedLocationId.isNotBlank() ->
                "${settings.selectedLocationName} (${settings.selectedLocationId})"
            settings.selectedLocationName.isNotBlank() -> settings.selectedLocationName
            settings.selectedLocationId.isNotBlank() -> settings.selectedLocationId
            else -> ""
        }

        val currentText = binding.btnSelectLocation.text?.toString().orEmpty()
        if (currentText != displayValue) {
            binding.btnSelectLocation.text = displayValue.ifBlank { getString(R.string.hint_location) }
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
