package com.smartx.rfidreader.ui.main.access

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartx.rfidreader.databinding.FragmentConfigAccessBinding
import com.smartx.rfidreader.ui.main.MainActivity
import com.smartx.rfidreader.ui.main.config.ConfigFragment
import com.smartx.rfidreader.ui.main.reader.ReaderSelectionFragment

class ConfigAccessFragment : Fragment() {

    private var _binding: FragmentConfigAccessBinding? = null
    private val binding get() = _binding!!

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

        binding.btnBackHome.setOnClickListener {
            (requireActivity() as MainActivity).goHome()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
