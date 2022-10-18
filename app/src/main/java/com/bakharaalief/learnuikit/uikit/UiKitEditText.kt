package com.bakharaalief.learnuikit.uikit

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setPadding
import com.bakharaalief.learnuikit.R
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class UiKitEditText(context: Context, attrs: AttributeSet?) : TextInputLayout(context, attrs) {

    companion object {
        private const val FILLED = 1
        private const val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"
    }

    private var shapeModel: ShapeAppearanceModel = ShapeAppearanceModel()
    private var boxBackground: MaterialShapeDrawable = MaterialShapeDrawable(shapeModel)

    val editText = TextInputEditText(context)
        .apply {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }

    private var outlineColor =
        ContextCompat.getColorStateList(context, R.color.text_input_box_stroke)

    /* Type */
    private var inputType = 0
    private var fillColor = 0
    private var outlineWidth = 1f.px
    private var boxType = 0
    private var isTextAreaMode = false

    init {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.UiKitEditText
        )

        try {
            inputType = typedArray.getInt(R.styleable.UiKitEditText_input_type, 0)
            fillColor = typedArray.getInt(R.styleable.UiKitEditText_fill_color, 0)
            boxType = typedArray.getInt(R.styleable.UiKitEditText_box_type, 0)
            isTextAreaMode = typedArray.getBoolean(R.styleable.UiKitEditText_textAreaMode, false)
            attrs?.apply {
                editText.gravity = getAttributeIntValue(ANDROID_NAMESPACE, "gravity", Gravity.START)
                editText.setLines(getAttributeIntValue(ANDROID_NAMESPACE, "lines", 1))
                editText.maxLines = getAttributeIntValue(ANDROID_NAMESPACE, "maxLines", 1)
            }
            setInputType()
        } finally {
            typedArray.recycle()
        }

        setup()
    }

    private fun setInputType() {
        when (inputType) {
            0 -> {
                editText.inputType = InputType.TYPE_CLASS_TEXT
            }
            1 -> {
                endIconMode = END_ICON_PASSWORD_TOGGLE
                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            2 -> {
                endIconMode = END_ICON_CLEAR_TEXT
            }
            3 -> {
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }
            4 -> {
                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            }
        }
    }

    private fun setup() {
        addView(editText)

        isHintEnabled = false
        isHintAnimationEnabled = false
        boxBackgroundMode = BOX_BACKGROUND_NONE
        shapeModel = if (isTextAreaMode) {
            editText.setLines(4)
            editText.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            editText.isSingleLine = false
            shapeModel.toBuilder()
                .setAllCornerSizes(RelativeCornerSize(0.1f))
                .build()
        } else {
            shapeModel
                .toBuilder()
                .setAllCornerSizes(RelativeCornerSize(0.1f))
                .build()
        }

        setBoxType()
        updateLayout()

        ViewCompat.setBackground(editText, boxBackground)
        editText.setPadding(editText.paddingBottom)
    }

    private fun setBoxType() {
        when (boxType) {
            FILLED -> {
                outlineWidth = 0f
            }
        }
    }

    private fun updateLayout() {
        val colorStateListValue = when (fillColor) {
            1 -> getColorCompat(context, R.color.uikit_white)
            else -> getColorCompat(context, R.color.uikit_transparent)
        }
        boxBackground.apply {
            shapeAppearanceModel = shapeModel
            fillColor = ColorStateList.valueOf(
                if (isEnabled) colorStateListValue
                else getColorCompat(context, R.color.uikit_color_line)
            )
            setStroke(outlineWidth, outlineColor)
        }

        invalidate()
    }

    override fun setError(errorText: CharSequence?) {
        super.setError(errorText)
        outlineColor =
            if (errorText != null) ColorStateList.valueOf(errorCurrentTextColors)
            else ContextCompat.getColorStateList(context, R.color.uikit_gray)
        updateLayout()
    }

    fun getString(): String {
        return this.editText.text.toString()
    }

    fun getStringOrNull(): String? {
        return if (editText.text.isNullOrEmpty()) null else editText.text.toString()
    }

    fun getStringTrim(): String = this.getString().trim()

    fun clearText() {
        editText.setText("")
    }
}