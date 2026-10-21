package com.smartx.shopeerecebimento.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.smartx.shopeerecebimento.R
import com.smartx.shopeerecebimento.core.reader.RfidTag
import com.smartx.shopeerecebimento.core.reader.ReaderConnectionState
import com.smartx.shopeerecebimento.core.webhook.WebhookService
import com.smartx.shopeerecebimento.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var registeredAdapter: HomeEpcAdapter
    private var currentTagCount: Int = 0
    private var isCurrentlyInventorying: Boolean = false
    private var allTagsWithDescription: Boolean = false
    private var hasRegisteredTags: Boolean = false
    private var lastUnregisteredEpcs: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupActions()
        observeState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.clearTags()
        stopWebhookSendingOnHome()
    }

    private fun stopWebhookSendingOnHome() {
        if (!WebhookService.isRunning) return
        val stopIntent = Intent(requireContext(), WebhookService::class.java).apply {
            action = WebhookService.ACTION_STOP
        }
        requireContext().startService(stopIntent)
    }

    private fun setupRecyclerView() {
        registeredAdapter = HomeEpcAdapter()

        binding.recyclerEpcs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEpcs.adapter = registeredAdapter
        binding.recyclerEpcs.isNestedScrollingEnabled = false
    }

    private fun setupActions() {
        binding.btnConfirm.isEnabled = false
        binding.btnConfirm.setOnClickListener {
            viewModel.confirmMoveLocation()
        }
        binding.btnAddManualEpc.setOnClickListener {
            showManualEpcDialog()
        }

        binding.textUnregisteredInfo.setOnClickListener {
            showUnregisteredTagsDialog()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.tags.collect { tags ->
                        currentTagCount = tags.size
                        allTagsWithDescription = tags.isNotEmpty() && tags.all { it.description.isNotBlank() }

                        val unregisteredTags = tags.filter { it.descriptionLookupError }
                        val registeredTags = tags.filterNot { it.descriptionLookupError }
                        hasRegisteredTags = registeredTags.any { it.objectIdCode.isNotBlank() }

                        updateCounters(registeredTags.size, unregisteredTags.size)
                        lastUnregisteredEpcs = unregisteredTags.map { it.epc }
                        registeredAdapter.submitList(groupTagsByDescription(registeredTags))
                        updateConfirmButtonState()
                    }
                }

                launch {
                    viewModel.uiState.collect { state ->
                        isCurrentlyInventorying = state.isInventorying
                        updateHomeStatus(state)
                        updateConfirmButtonState()
                    }
                }

                launch {
                    viewModel.moveLocationResult.collect { result ->
                        val activity = requireActivity() as MainActivity
                        activity.navigateTo(ConfirmResultFragment.newInstance(result))
                    }
                }
            }
        }
    }

    private fun updateConfirmButtonState() {
        binding.btnConfirm.isEnabled =
            currentTagCount > 0 &&
                !isCurrentlyInventorying &&
                allTagsWithDescription &&
                hasRegisteredTags
    }

    private fun updateCounters(registeredCount: Int, unregisteredCount: Int) {
        binding.textRegisteredCount.text = registeredCount.toString()
        binding.textUnregisteredCount.text = unregisteredCount.toString()
        binding.textUnregisteredInfo.visibility = if (unregisteredCount > 0) View.VISIBLE else View.INVISIBLE
    }

    private fun groupTagsByDescription(tags: List<RfidTag>): List<HomeDescriptionGroup> {
        return tags
            .groupBy { tag ->
                tag.description.trim().ifBlank {
                    getString(R.string.tag_description_loading)
                }
            }
            .map { (description, groupedTags) ->
                HomeDescriptionGroup(
                    description = description,
                    totalTags = groupedTags.size
                )
            }
            .sortedWith(
                compareByDescending<HomeDescriptionGroup> { it.totalTags }
                    .thenBy { it.description.lowercase(Locale.ROOT) }
            )
    }

    private fun showUnregisteredTagsDialog() {
        if (lastUnregisteredEpcs.isEmpty()) {
            showSnackbar(getString(R.string.home_unregistered_empty))
            return
        }

        val listText = lastUnregisteredEpcs
            .sorted()
            .joinToString(separator = "\n")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.home_unregistered_title_with_count, lastUnregisteredEpcs.size))
            .setMessage(listText)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun showManualEpcDialog() {
        val content = layoutInflater.inflate(R.layout.dialog_manual_epc_capture, null, false)
        val captureText = content.findViewById<android.widget.TextView>(R.id.textManualEpcCapture)
        val wedgeReceiver = content.findViewById<EditText>(R.id.editManualEpcWedgeReceiver)
        val statusText = content.findViewById<android.widget.TextView>(R.id.textManualEpcStatus)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.home_manual_epc_title)
            .setView(content)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.home_manual_epc_add, null)
            .create()

        fun refreshCapture() {
            captureText.text = wedgeReceiver.text?.toString()?.trim()?.ifBlank {
                getString(R.string.home_manual_epc_waiting)
            } ?: getString(R.string.home_manual_epc_waiting)
            statusText.visibility = View.GONE
        }

        fun commitCapturedEpc(): Boolean {
            when (viewModel.addManualEpc(wedgeReceiver.text?.toString().orEmpty())) {
                ManualEpcResult.ADDED -> {
                    dialog.dismiss()
                    showSnackbar(getString(R.string.home_manual_epc_added))
                }
                ManualEpcResult.EMPTY -> {
                    statusText.text = getString(R.string.home_manual_epc_required)
                    statusText.visibility = View.VISIBLE
                    wedgeReceiver.requestFocus()
                }
                ManualEpcResult.INVALID_LENGTH -> {
                    statusText.text = getString(R.string.home_manual_epc_invalid_length)
                    statusText.visibility = View.VISIBLE
                    wedgeReceiver.requestFocus()
                }
                ManualEpcResult.PREFIX_MISMATCH -> {
                    statusText.text = getString(R.string.home_manual_epc_prefix_mismatch)
                    statusText.visibility = View.VISIBLE
                    wedgeReceiver.requestFocus()
                }
            }
            return true
        }

        wedgeReceiver.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = refreshCapture()
            override fun afterTextChanged(s: android.text.Editable?) = Unit
        })
        wedgeReceiver.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                commitCapturedEpc()
            } else {
                false
            }
        }

        dialog.setOnShowListener {
            dialog.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            wedgeReceiver.showSoftInputOnFocus = false
            wedgeReceiver.requestFocus()

            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener { commitCapturedEpc() }
        }
        dialog.show()
    }

    private fun updateHomeStatus(state: MainUiState) {
        val statusText = when {
            state.isInventorying -> "Lendo"
            state.connectionState == ReaderConnectionState.CONNECTING -> "Conectando..."
            state.connectionState == ReaderConnectionState.CONNECTED -> "Conectado"
            state.connectionState == ReaderConnectionState.ERROR -> "Erro"
            else -> "Desconectado"
        }

        val statusDrawable = when {
            state.isInventorying -> R.drawable.ic_status_active
            state.connectionState == ReaderConnectionState.CONNECTED -> R.drawable.ic_status_connected
            state.connectionState == ReaderConnectionState.CONNECTING -> R.drawable.ic_status_connected
            else -> R.drawable.ic_status_disconnected
        }

        binding.textStatusHome.text = statusText
        binding.statusDot.setBackgroundResource(statusDrawable)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
