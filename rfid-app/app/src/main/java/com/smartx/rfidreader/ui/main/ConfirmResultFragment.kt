package com.smartx.rfidreader.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.FragmentConfirmResultBinding
import kotlinx.coroutines.launch

class ConfirmResultFragment : Fragment() {

    private var _binding: FragmentConfirmResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialResult = readInitialResult()
        renderResult(initialResult)

        binding.btnRetrySend.setOnClickListener {
            binding.btnRetrySend.isEnabled = false
            binding.btnNewReading.isEnabled = false
            binding.textResultMessage.text = getString(R.string.move_result_sending)
            viewModel.retryLastMoveLocation()
        }

        binding.btnNewReading.setOnClickListener {
            viewModel.stopInventoryAndClear()
            (requireActivity() as MainActivity).goHome()
        }

        observeMoveResultUpdates()
    }

    private fun observeMoveResultUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moveLocationResult.collect { result ->
                    renderResult(result)
                }
            }
        }
    }

    private fun renderResult(result: MoveLocationUiResult) {
        when (result) {
            is MoveLocationUiResult.Success -> {
                binding.textResultSymbol.text = "✅"
                binding.textResultTitle.text = getString(R.string.move_result_success_title)
                binding.textResultMessage.text =
                    getString(
                        R.string.move_result_success_message,
                        result.sentCount,
                        result.locationName
                    )
                binding.btnRetrySend.visibility = View.GONE
                binding.btnRetrySend.isEnabled = false
                binding.btnNewReading.isEnabled = true
                binding.btnNewReading.text = getString(R.string.move_result_new_reading)
            }

            is MoveLocationUiResult.Error -> {
                binding.textResultSymbol.text = "❌"
                binding.textResultTitle.text = getString(R.string.move_result_error_title)
                binding.textResultMessage.text =
                    getString(R.string.move_result_error_message, result.message)
                binding.btnRetrySend.visibility = if (result.canRetry) View.VISIBLE else View.GONE
                binding.btnRetrySend.isEnabled = result.canRetry
                binding.btnNewReading.isEnabled = true
                binding.btnNewReading.text = getString(R.string.move_result_new_reading)
            }
        }
    }

    private fun readInitialResult(): MoveLocationUiResult {
        val success = requireArguments().getBoolean(ARG_SUCCESS)
        return if (success) {
            MoveLocationUiResult.Success(
                sentCount = requireArguments().getInt(ARG_SENT_COUNT),
                locationName = requireArguments().getString(ARG_LOCATION_NAME).orEmpty()
            )
        } else {
            MoveLocationUiResult.Error(
                message = requireArguments().getString(ARG_MESSAGE).orEmpty(),
                canRetry = requireArguments().getBoolean(ARG_CAN_RETRY, true)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_SUCCESS = "success"
        private const val ARG_SENT_COUNT = "sent_count"
        private const val ARG_LOCATION_NAME = "location_name"
        private const val ARG_MESSAGE = "message"
        private const val ARG_CAN_RETRY = "can_retry"

        fun newInstance(result: MoveLocationUiResult): ConfirmResultFragment {
            val args = Bundle().apply {
                when (result) {
                    is MoveLocationUiResult.Success -> {
                        putBoolean(ARG_SUCCESS, true)
                        putInt(ARG_SENT_COUNT, result.sentCount)
                        putString(ARG_LOCATION_NAME, result.locationName)
                    }

                    is MoveLocationUiResult.Error -> {
                        putBoolean(ARG_SUCCESS, false)
                        putString(ARG_MESSAGE, result.message)
                        putBoolean(ARG_CAN_RETRY, result.canRetry)
                    }
                }
            }
            return ConfirmResultFragment().apply { arguments = args }
        }
    }
}
