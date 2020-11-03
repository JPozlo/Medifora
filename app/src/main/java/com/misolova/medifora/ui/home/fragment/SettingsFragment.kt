package com.misolova.medifora.ui.home.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.checkbox.MaterialCheckBox
import com.misolova.medifora.R
import com.misolova.medifora.util.Constants.DARK_STATUS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                SettingsFragment()
            }

        private const val TAG = "SETTINGS FRAGMENT"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val darkModeSwitch = view.findViewById(R.id.checkboxDarkMode) as MaterialCheckBox?

        darkModeSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putInt(DARK_STATUS, 1).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putInt(DARK_STATUS, 0).apply()
            }
        }

        tvFeedback?.setOnClickListener {
            findNavController().navigate(R.id.feedbackFragment)
        }

    }


}