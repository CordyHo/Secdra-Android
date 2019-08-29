package com.cordy.secdra.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.cordy.secdra.SecdraApplication

object InputMethodUtils {
    fun showInputMethod(editText: EditText) {
        val inputManager = SecdraApplication.application?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
    }

    fun closeInputMethod(editText: EditText) {
        val imm = SecdraApplication.application?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
        editText.clearFocus()
    }
}