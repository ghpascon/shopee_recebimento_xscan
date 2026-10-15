package com.smartx.rfidreader.ui.main.access

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.databinding.FragmentConfigAccessBinding
import com.smartx.rfidreader.ui.main.MainActivity
import com.smartx.rfidreader.ui.main.MainViewModel
import com.smartx.rfidreader.ui.main.config.ConfigFragment
import com.smartx.rfidreader.ui.main.config.XTrackConfigFragment
import com.smartx.rfidreader.ui.main.login.LoginFragment
import com.smartx.rfidreader.ui.main.reader.ReaderSelectionFragment

class ConfigAccessFragment : Fragment() {

    private var _binding: FragmentConfigAccessBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigAccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChooseReader.setOnClickListener {
            (requireActivity() as MainActivity).navigateTo(ReaderSelectionFragment())
        }

        binding.btnOpenConfig.setOnClickListener {
            (requireActivity() as MainActivity).navigateTo(ConfigFragment())
        }

        binding.btnOpenXtrackConfig.setOnClickListener {
            (requireActivity() as MainActivity).navigateTo(XTrackConfigFragment())
        }

        binding.btnGoLogin.setOnClickListener {
            val reason = viewModel.getLoginBlockReason()
            if (reason != null) {
                Snackbar.make(binding.root, reason, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            (requireActivity() as MainActivity).navigateTo(LoginFragment(), MainActivity.TAG_LOGIN)
        }

        binding.btnBackHome.setOnClickListener {
            (requireActivity() as MainActivity).goHome()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
