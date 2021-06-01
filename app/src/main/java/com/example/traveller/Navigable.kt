package com.example.traveller

import androidx.fragment.app.Fragment

interface Navigable {
    fun navigateTo(fragment: Fragment)
}