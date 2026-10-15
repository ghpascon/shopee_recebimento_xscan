package com.smartx.rfidreader.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.RfidTag
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.core.webhook.WebhookService
import com.smartx.rfidreader.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var registeredAdapter: HomeEpcAdapter
    private lateinit var unregisteredAdapter: HomeUnregisteredAdapter
    private var currentTagCount: Int = 0
    private var isCurrentlyInventorying: Boolean = false
    private var allTagsWithDescription: Boolean = false
    private var hasRegisteredTags: Boolean = false

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
        unregisteredAdapter = HomeUnregisteredAdapter()

        binding.recyclerEpcs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEpcs.adapter = registeredAdapter
        binding.recyclerEpcs.isNestedScrollingEnabled = false

        binding.recyclerUnregisteredTags.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerUnregisteredTags.adapter = unregisteredAdapter
        binding.recyclerUnregisteredTags.isNestedScrollingEnabled = false
    }

    private fun setupActions() {
        binding.btnConfirm.isEnabled = false
        binding.btnConfirm.setOnClickListener {
            viewModel.confirmMoveLocation()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.tags.collect { tags ->
                        currentTagCount = tags.size
                        allTagsWithDescription = tags.isNotEmpty() && tags.all { it.description.isNotBlank() }
                        binding.textTotalLido.text = currentTagCount.toString()

                        val unregisteredTags = tags.filter { it.descriptionLookupError }
                        val tagsForMainList = tags.filterNot { it.descriptionLookupError }
                        hasRegisteredTags = tagsForMainList.any { it.objectIdCode.isNotBlank() }

                        updateUnregisteredTagsSection(unregisteredTags)
                        registeredAdapter.submitList(tagsForMainList.toList())
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

    private fun updateUnregisteredTagsSection(tags: List<RfidTag>) {
        if (tags.isEmpty()) {
            binding.cardUnregisteredTags.visibility = View.GONE
            unregisteredAdapter.submitList(emptyList())
            return
        }

        binding.cardUnregisteredTags.visibility = View.VISIBLE
        binding.textUnregisteredTitle.text =
            getString(R.string.home_unregistered_title_with_count, tags.size)
        unregisteredAdapter.submitList(tags.map { it.epc })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
