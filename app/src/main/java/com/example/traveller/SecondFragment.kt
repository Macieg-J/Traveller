package com.example.traveller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.example.traveller.databinding.FragmentSecondBinding
import com.example.traveller.settings.SettingsFragment
import java.util.function.Consumer

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {


    private lateinit var binding: FragmentSecondBinding
    lateinit var navigable: Navigable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSecondBinding.inflate(
            inflater,
            container,
            false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as Navigable).let {
            it.navigateTo(SettingsFragment())
        }
    }

    override fun onPause() {
        super.onPause()
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val allSettings = preferences.all
        val fontSize = allSettings["font_size"]
        val fontColor = allSettings["font_colour"]

        (requireActivity() as? MainActivity)!!.callback.accept(
            listOf(
                fontSize.toString(),
                fontColor.toString()
            )
        )
    }
}