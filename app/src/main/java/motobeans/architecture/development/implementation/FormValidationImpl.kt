package motobeans.architecture.development.implementation

import android.content.Context
import com.google.android.material.textfield.TextInputLayout
import android.widget.EditText
import com.finance.app.databinding.TempActivityBinding
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

/**
 * Created by munishkumarthakur on 04/11/17.
 */

class FormValidationImpl(private val context: Context) : FormValidation {

    override fun validateTemp(binding: TempActivityBinding): Boolean {
        var errorCount = 0

        return isValidForm(errorCount)
    }

    private fun checkAndSetError(etToCheck: EditText, tilToSet: TextInputLayout,
                                 errorText: String): Int {
        var errorCount = 0
        val valToCheck = etToCheck.text.toString().trim { it <= ' ' }
        if (valToCheck.exIsNotEmptyOrNullOrBlank()) {
            tilToSet.isErrorEnabled = false
        } else {
            errorCount++
            tilToSet.isErrorEnabled = true
            tilToSet.error = errorText
        }

        return errorCount
    }


    private fun isValidForm(errorCount: Int): Boolean {
        return errorCount <= 0
    }

    private fun isPinValid(pin: String): Boolean {
        if (pin.length == 4 && pin.exIsNotEmptyOrNullOrBlank())
            return true
        return false
    }
}