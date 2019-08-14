package motobeans.architecture.development.interfaces

import com.finance.app.databinding.TempActivityBinding

/**
 * Created by munishkumarthakur on 04/11/17.
 */

interface FormValidation {
    fun validateTemp(binding: TempActivityBinding): Boolean
}