package com.smartx.shopeerecebimento

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.smartx.shopeerecebimento.core.db.AppDatabase
import com.smartx.shopeerecebimento.core.events.EventRepository
import com.smartx.shopeerecebimento.core.settings.AppSettingsRepository
import com.smartx.shopeerecebimento.readers.xr2.XR2Reader

class RfidApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val eventRepository: EventRepository by lazy { EventRepository(database.eventDao()) }
    val settingsRepository: AppSettingsRepository by lazy { AppSettingsRepository(this) }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        XR2Reader.configureExclusiveRfidTrigger()
    }
}
