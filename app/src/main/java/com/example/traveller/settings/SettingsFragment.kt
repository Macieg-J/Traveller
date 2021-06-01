package com.example.traveller.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.traveller.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
//        val preference = preferenceManager.onPreferenceTreeClickListener.onPreferenceTreeClick(this.)
    }


//    onBackPressed

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val preferenceList = preferenceManager.findPreference<Preference>("preference_key")
//        preferenceList.setOnPreferenceChangeListener(Preference.OnPreferenceChangeListener{
//            onPreferenceTreeClick()
//        })
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }
}