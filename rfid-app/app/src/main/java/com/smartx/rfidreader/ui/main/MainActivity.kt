package com.smartx.rfidreader.ui.main

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.databinding.ActivityMainBinding
import com.smartx.rfidreader.ui.base.BaseActivity
import com.smartx.rfidreader.ui.main.access.ConfigAccessFragment
import com.smartx.rfidreader.ui.main.login.LoginFragment
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        private val TRIGGER_KEYCODES = intArrayOf(
            KeyEvent.KEYCODE_F1,
            KeyEvent.KEYCODE_FOCUS,
            KeyEvent.KEYCODE_CAMERA,
            293,
            KeyEvent.KEYCODE_HEADSETHOOK,
            KeyEvent.KEYCODE_BUTTON_R1,
            523   // XR2 handle trigger
        )

        private const val HEADER_HOLD_MS = 5000L
        const val TAG_HOME = "home"
        const val TAG_LOGIN = "login"
        const val TAG_CONFIG_ACCESS = "config_access"
    }

    val viewModel: MainViewModel by viewModels()

    private var backPressedTime = 0L
    private var headerDownAt = 0L
    private var headerDownX = 0f
    private var headerDownY = 0f
    private var headerGestureCanceled = false

    override fun inflateBinding(inflater: LayoutInflater) = ActivityMainBinding.inflate(inflater)

    override fun onActivityReady(savedInstanceState: Bundle?) {
        // Observa e atualiza o header global (leitor + status de conexão)
        observeHeader()

        // Sempre tenta reconectar no último leitor salvo (padrão XR2 no primeiro uso).
        viewModel.autoConnectLastReader()

        if (savedInstanceState == null) {
            loadLogin()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            enforceAuthOnHome()
        }

        setupBackHandler()
        setupHeaderAccessGesture()
        observeNavigation()
    }

    private fun loadHomeOrLogin() {
        if (viewModel.uiState.value.isAuthenticated) loadHome() else loadLogin()
    }

    private fun observeHeader() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val name = viewModel.reader?.displayName ?: getString(R.string.nav_reader)
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
                    updateHeader(name, statusText, statusDrawable)
                }
            }
        }
    }

    private fun loadHome() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment(), TAG_HOME)
            .commit()
    }

    private fun loadLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LoginFragment(), TAG_LOGIN)
            .commit()
    }

    private fun loadConfigAccess() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ConfigAccessFragment(), TAG_CONFIG_ACCESS)
            .commit()
    }

    fun goHome() {
        supportFragmentManager.popBackStackImmediate(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        loadHomeOrLogin()
    }

    private fun enforceAuthOnHome() {
        val current = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (current is HomeFragment && !viewModel.uiState.value.isAuthenticated) {
            loadHomeOrLogin()
        }
    }

    /** Navega para um fragmento, adicionando ao back stack */
    fun navigateTo(fragment: Fragment, tag: String? = null) {
        // Evita empilhar o mesmo fragmento
        val existing = supportFragmentManager.findFragmentByTag(tag ?: fragment::class.java.simpleName)
        if (existing != null && existing.isVisible) return

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.fragmentContainer, fragment, tag ?: fragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }

    private fun setupBackHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    binding.root.post { enforceAuthOnHome() }
                } else {
                    val now = SystemClock.elapsedRealtime()
                    if (now - backPressedTime < 2000L) {
                        finish()
                    } else {
                        backPressedTime = now
                        Toast.makeText(
                            this@MainActivity,
                            "Pressione voltar novamente para sair",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    private fun observeNavigation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToReading.collect {
                    // Volta para a tela anterior após conectar
                    supportFragmentManager.popBackStack()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToHome.collect {
                    goHome()
                }
            }
        }
    }

    private fun setupHeaderAccessGesture() {
        val touchSlop = ViewConfiguration.get(this).scaledTouchSlop
        headerBinding.root.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    headerDownAt = SystemClock.elapsedRealtime()
                    headerDownX = event.rawX
                    headerDownY = event.rawY
                    headerGestureCanceled = false
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val movedX = kotlin.math.abs(event.rawX - headerDownX)
                    val movedY = kotlin.math.abs(event.rawY - headerDownY)
                    if (movedX > touchSlop || movedY > touchSlop) {
                        headerGestureCanceled = true
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    val heldMs = SystemClock.elapsedRealtime() - headerDownAt
                    if (!headerGestureCanceled && heldMs >= HEADER_HOLD_MS) {
                        navigateTo(ConfigAccessFragment(), TAG_CONFIG_ACCESS)
                    }
                    headerDownAt = 0L
                    headerGestureCanceled = false
                    true
                }

                MotionEvent.ACTION_CANCEL -> {
                    headerDownAt = 0L
                    headerGestureCanceled = true
                    true
                }

                else -> false
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode in TRIGGER_KEYCODES) {
            return when (event.action) {
                KeyEvent.ACTION_DOWN -> { viewModel.onTriggerPressed(); true }
                KeyEvent.ACTION_UP -> { viewModel.onTriggerReleased(); true }
                else -> super.dispatchKeyEvent(event)
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onLogoutPressed() {
        supportFragmentManager.popBackStackImmediate(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        loadLogin()
    }

}
