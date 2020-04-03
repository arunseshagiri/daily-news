package com.arunkumar.dailynews

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.arunkumar.dailynews.utils.PREFERENCE_COUNTRY

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey)

        val preference = findPreference<DropDownPreference>(PREFERENCE_COUNTRY)

        Preference.OnPreferenceChangeListener { pref, newValue ->
            pref.summary = newValue.toString()
            true
        }
    }


}