package com.digitaltasbeeh.etasbih.dhikrcounter.utils

import android.content.Context
import com.digitaltasbeeh.etasbih.dhikrcounter.MainActivity

object SPRepository {

    private const val TASBIH_SOUND = "TASBIH_SOUND"
    private const val TASBIH_COUNTER = "TASBIH_COUNTER"
    private const val TASBIH_LAYOUT = "TASBIH_LAYOUT"
    private const val LOCK = "LOCK"
    private const val VIBRATE = "VIBRATE"

    fun setTasbihSound(context: MainActivity, value: Boolean) {
        val sharedPreference =  context.getSharedPreferences(TASBIH_SOUND,Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(TASBIH_SOUND, value)
        editor.apply()
    }

    fun getTasbihSound(context: Context): Boolean {
            val sharedPreference =  context.getSharedPreferences(TASBIH_SOUND,Context.MODE_PRIVATE)
            return sharedPreference.getBoolean(TASBIH_SOUND, true)
    }

    fun setTasbihLayout(context: Context, value: Boolean) {
        val sharedPreference =  context.getSharedPreferences(TASBIH_LAYOUT,Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(TASBIH_LAYOUT, value)
        editor.apply()
    }

    fun getTasbihLayout(context: Context): Boolean {
        val sharedPreference =  context.getSharedPreferences(TASBIH_LAYOUT,Context.MODE_PRIVATE)
        return sharedPreference.getBoolean(TASBIH_LAYOUT, false)
    }
    fun setTasbihCounter(context: Context, value : Int) {
        val sharedPreference =  context.getSharedPreferences(TASBIH_COUNTER,Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putInt(TASBIH_COUNTER, value)
        editor.apply()
    }

    fun getTasbihCounter(context: Context): Int {
        val sharedPreference =  context.getSharedPreferences(TASBIH_COUNTER,Context.MODE_PRIVATE)
        return sharedPreference.getInt(TASBIH_COUNTER, 0)
    }

    fun setLock(context: Context, value: Boolean) {
        val sharedPreference =  context.getSharedPreferences(LOCK,Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(LOCK, value)
        editor.apply()
    }

    fun getLock(context: Context): Boolean {
        val sharedPreference =  context.getSharedPreferences(LOCK,Context.MODE_PRIVATE)
        return sharedPreference.getBoolean(LOCK, false)
    }

    fun setVibrate(context: Context, value: Boolean) {
        val sharedPreference =  context.getSharedPreferences(VIBRATE,Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(VIBRATE, value)
        editor.apply()
    }

    fun getVibrate(context: Context): Boolean {
        val sharedPreference =  context.getSharedPreferences(VIBRATE,Context.MODE_PRIVATE)
        return sharedPreference.getBoolean(VIBRATE, true)
    }

}