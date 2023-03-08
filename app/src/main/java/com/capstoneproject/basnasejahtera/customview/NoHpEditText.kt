package com.capstoneproject.basnasejahtera.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.capstoneproject.basnasejahtera.R

class NoHpEditText : AppCompatEditText {
    private var noHpLength = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, after: Int, count: Int) {
                noHpLength = start + count
                if (noHpLength < 11) {
                    error = context.getString(R.string.noHp_validation)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //do nothing
            }
        })
    }
}