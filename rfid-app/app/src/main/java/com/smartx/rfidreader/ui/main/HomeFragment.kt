package com.smartx.rfidreader.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartx.rfidreader.core.webhook.WebhookService
import com.smartx.rfidreader.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onResume() {
        super.onResume()
        stopWebhookSendingOnHome()
    }

    private fun stopWebhookSendingOnHome() {
        if (!WebhookService.isRunning) return
        val stopIntent = Intent(requireContext(), WebhookService::class.java).apply {
            action = WebhookService.ACTION_STOP
        }
        requireContext().startService(stopIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
