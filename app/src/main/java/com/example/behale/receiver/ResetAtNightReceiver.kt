package com.example.behale.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.behale.utils.ConstantUtils

class ResetAtNightReceiver: BroadcastReceiver() {

    private lateinit var sp: SharedPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        sp = context!!.getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        sp.edit().putInt(ConstantUtils.WATER_PROGRESS_PREFERENCE, 0).apply()
        sp.edit().putBoolean(ConstantUtils.FIRST_STEP_VALUE_STORED_PREFERENCE, false).apply()


        val previousTotalStep = sp.getFloat(ConstantUtils.FIRST_STEP_VALUE_PREFERENCE, 0f)
        val currentStep = sp.getInt(ConstantUtils.CURRENT_STEPS_PREFERENCE, 0)

        sp.edit().putInt(ConstantUtils.YESTERDAY_STEPS, currentStep).apply()

        val newFirstStep = previousTotalStep + currentStep.toFloat()

        sp.edit().putInt(ConstantUtils.CURRENT_STEPS_PREFERENCE, 0).apply()
        sp.edit().putFloat(ConstantUtils.FIRST_STEP_VALUE_PREFERENCE, newFirstStep).apply()
    }

}