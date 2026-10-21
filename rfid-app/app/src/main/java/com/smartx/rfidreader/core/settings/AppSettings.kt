package com.smartx.shopeerecebimento.core.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.smartx.shopeerecebimento.core.reader.ReaderConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class AppSettings(
    val lastReaderId: String = AppSettingsRepository.DEFAULT_READER_ID,
    val buzzerEnabled: Boolean = true,
    val rssiFilter: Int = -120,
    val epcLen: Int? = null,
    val epcLengths: List<Int>? = null,
    val prefixes: List<String> = emptyList(),
    val xtrackUrl: String = AppSettingsRepository.DEFAULT_XTRACK_URL,
    val selectedDeviceUid: String = "",
    val selectedDeviceId: String = "",
    val selectedDeviceName: String = "",
    val selectedLocationId: String = "",
    val selectedLocationName: String = "",
    val webhookUrl: String = "",
    val webhookIntervalSeconds: Int = 30,
    /** Último endereço MAC BLE selecionado (global) */
    val lastBleAddress: String = "",
    /** Potência TX do leitor persistida entre reinícios */
    val readerTxPower: Int = ReaderConfig().txPower,
    /** Session do leitor persistida entre reinícios */
    val readerSession: Int = ReaderConfig().session
)

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class AppSettingsRepository(context: Context) {

    private val store = context.applicationContext.dataStore

    companion object {
        const val DEFAULT_READER_ID = "XR2"
        const val DEFAULT_XTRACK_URL = "http://34.67.217.146:6101/req"
        private val KEY_READER_ID = stringPreferencesKey("last_reader_id")
        private val KEY_BUZZER = booleanPreferencesKey("buzzer_enabled")
        private val KEY_RSSI = intPreferencesKey("rssi_filter")
        private val KEY_EPC_LEN = intPreferencesKey("epc_len")
        private val KEY_EPC_LENGTHS = stringPreferencesKey("epc_lengths")
        private val KEY_PREFIXES = stringPreferencesKey("prefixes")
        private val KEY_LAST_BLE = stringPreferencesKey("last_ble_address")
        private val KEY_XTRACK_URL = stringPreferencesKey("xtrack_url")
        private val KEY_DEVICE_UID = stringPreferencesKey("selected_device_uid")
        private val KEY_DEVICE_ID = stringPreferencesKey("selected_device_id")
        private val KEY_DEVICE_NAME = stringPreferencesKey("selected_device_name")
        private val KEY_LOCATION_ID = stringPreferencesKey("selected_location_id")
        private val KEY_LOCATION_NAME = stringPreferencesKey("selected_location_name")
        private val KEY_WEBHOOK = stringPreferencesKey("webhook_url")
        private val KEY_WEBHOOK_INTERVAL = intPreferencesKey("webhook_interval_seconds")
        private val KEY_READER_TX_POWER = intPreferencesKey("reader_tx_power")
        private val KEY_READER_SESSION = intPreferencesKey("reader_session")
    }

    val flow: Flow<AppSettings> = store.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            val prefixStr = prefs[KEY_PREFIXES] ?: ""
            val epcLengthsStr = prefs[KEY_EPC_LENGTHS]
            AppSettings(
                lastReaderId = prefs[KEY_READER_ID]?.takeIf { it.isNotBlank() } ?: DEFAULT_READER_ID,
                buzzerEnabled = prefs[KEY_BUZZER] ?: true,
                rssiFilter = prefs[KEY_RSSI] ?: -120,
                epcLen = prefs[KEY_EPC_LEN]?.takeIf { it > 0 },
                epcLengths = epcLengthsStr?.split("|")
                    ?.mapNotNull { it.toIntOrNull()?.takeIf { length -> length > 0 } }
                    ?: if (epcLengthsStr != null) emptyList() else null,
                prefixes = if (prefixStr.isBlank()) emptyList()
                           else prefixStr.split("|").filter { it.isNotBlank() },
                xtrackUrl = prefs[KEY_XTRACK_URL]?.takeIf { it.isNotBlank() } ?: DEFAULT_XTRACK_URL,
                selectedDeviceUid = prefs[KEY_DEVICE_UID] ?: "",
                selectedDeviceId = prefs[KEY_DEVICE_ID] ?: "",
                selectedDeviceName = prefs[KEY_DEVICE_NAME] ?: "",
                selectedLocationId = prefs[KEY_LOCATION_ID] ?: "",
                selectedLocationName = prefs[KEY_LOCATION_NAME] ?: "",
                webhookUrl = prefs[KEY_WEBHOOK] ?: "",
                webhookIntervalSeconds = prefs[KEY_WEBHOOK_INTERVAL] ?: 30,
                lastBleAddress = prefs[KEY_LAST_BLE] ?: "",
                readerTxPower = prefs[KEY_READER_TX_POWER] ?: ReaderConfig().txPower,
                readerSession = (prefs[KEY_READER_SESSION] ?: ReaderConfig().session).coerceIn(0, 3)
            )
        }

    suspend fun save(settings: AppSettings) {
        store.edit { prefs ->
            prefs[KEY_READER_ID] = settings.lastReaderId
            prefs[KEY_BUZZER] = settings.buzzerEnabled
            prefs[KEY_RSSI] = settings.rssiFilter
            if (settings.epcLen != null && settings.epcLen > 0) {
                prefs[KEY_EPC_LEN] = settings.epcLen
            } else {
                prefs.remove(KEY_EPC_LEN)
            }
            settings.epcLengths?.let { lengths ->
                prefs[KEY_EPC_LENGTHS] = lengths.filter { it > 0 }.distinct().joinToString("|")
            } ?: prefs.remove(KEY_EPC_LENGTHS)
            prefs[KEY_PREFIXES] = settings.prefixes.joinToString("|")
            prefs[KEY_XTRACK_URL] = settings.xtrackUrl
            prefs[KEY_DEVICE_UID] = settings.selectedDeviceUid
            prefs[KEY_DEVICE_ID] = settings.selectedDeviceId
            prefs[KEY_DEVICE_NAME] = settings.selectedDeviceName
            prefs[KEY_LOCATION_ID] = settings.selectedLocationId
            prefs[KEY_LOCATION_NAME] = settings.selectedLocationName
            prefs[KEY_WEBHOOK] = settings.webhookUrl
            prefs[KEY_WEBHOOK_INTERVAL] = settings.webhookIntervalSeconds
            prefs[KEY_LAST_BLE] = settings.lastBleAddress
            prefs[KEY_READER_TX_POWER] = settings.readerTxPower
            prefs[KEY_READER_SESSION] = settings.readerSession.coerceIn(0, 3)
        }
    }
}
