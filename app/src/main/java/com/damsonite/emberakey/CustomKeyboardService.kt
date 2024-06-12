package com.damsonite.emberakey

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
import androidx.core.text.isDigitsOnly

class CustomKeyboardService : InputMethodService() {

    private lateinit var keyboardView: View
    private var isShiftActive = false

    private val keyboardLetters = mapOf(
        R.id.key_q to "q",
        R.id.key_w to "w",
        R.id.key_e to "e",
        R.id.key_r to "r",
        R.id.key_t to "t",
        R.id.key_y to "y",
        R.id.key_u to "u",
        R.id.key_i to "i",
        R.id.key_o to "o",
        R.id.key_p to "p",

        R.id.key_a to "a",
        R.id.key_s to "s",
        R.id.key_d to "d",
        R.id.key_f to "f",
        R.id.key_g to "g",
        R.id.key_h to "h",
        R.id.key_j to "j",
        R.id.key_k to "k",
        R.id.key_l to "l",
        R.id.key_nn to "ñ",

        R.id.key_z to "z",
        R.id.key_x to "x",
        R.id.key_c to "c",
        R.id.key_v to "v",
        R.id.key_b to "b",
        R.id.key_n to "n",
        R.id.key_m to "m",

        R.id.key_an to "ã",
        R.id.key_en to "ẽ",
        R.id.key_in to "ĩ",
        R.id.key_on to "õ",
        R.id.key_un to "ũ",
        R.id.key_uln to "ʉ̃",
        R.id.key_ul to "ʉ",
    )

    private val keyboardPunctuation = mapOf(
        R.id.key_comma to ",",
        R.id.key_tilde to "'",
        R.id.key_period to ".",
    )

    @SuppressLint("InflateParams")
    override fun onCreateInputView(): View {
        keyboardView = LayoutInflater.from(this).inflate(R.layout.keyboard_view, null)

        keyboardLetters.keys.forEach { keyId ->
            val button = keyboardView.findViewById<Button>(keyId)
            button.setOnClickListener { onKeyPress(keyboardLetters[keyId].toString()) }
        }

        keyboardPunctuation.keys.forEach { keyId ->
            val button = keyboardView.findViewById<Button>(keyId)
            button.setOnClickListener { onKeyPress(keyboardPunctuation[keyId].toString()) }
        }

        val keyShift = keyboardView.findViewById<Button>(R.id.key_shift)
        keyShift.setOnClickListener {
            isShiftActive = !isShiftActive
            updateKeyLabels()
        }

        val keySpace = keyboardView.findViewById<Button>(R.id.key_space)
        keySpace.setOnClickListener { onKeyPress(" ") }

        val keyDel = keyboardView.findViewById<Button>(R.id.key_del)
        keyDel.setOnClickListener {
            currentInputConnection?.deleteSurroundingText(1, 0)
        }

        val keyEnter = keyboardView.findViewById<Button>(R.id.key_enter)
        keyEnter.setOnClickListener { onKeyPress("\n") }

        return keyboardView
    }

    private fun onKeyPress(key: String) {
        val inputConnection: InputConnection = currentInputConnection ?: return
        val textToCommit = if (isShiftActive && !key.isDigitsOnly()) {
            key.uppercase()
        } else {
            key
        }

        inputConnection.commitText(textToCommit, 1)
    }

    private fun updateKeyLabels() {
        keyboardLetters.keys.forEach { keyId ->
            val button = keyboardView.findViewById<Button>(keyId)
            val currentText = button.text.toString()
            button.text = if (isShiftActive) {
                currentText.uppercase()
            } else {
                currentText.lowercase()
            }
        }
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
    }
}
