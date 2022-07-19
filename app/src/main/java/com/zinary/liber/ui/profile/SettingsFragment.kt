package com.zinary.liber.ui.profile

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.zinary.liber.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}