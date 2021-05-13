package com.example.traveller

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

//        val preference = preferenceManager.onPreferenceTreeClickListener.onPreferenceTreeClick(this.)
    }

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