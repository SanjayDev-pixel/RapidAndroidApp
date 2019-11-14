package com.finance.app.others

import com.finance.app.R

/**
 * Created by munishkumarthakur on 02/01/18.
 */
class AppEnums {
    enum class Temp(val id: Int, val dataName: String) {
        Val1(1001, "Value1001");

        companion object {
            fun getDataType(id: Int?): Temp? {
                var dataType: Temp? = null
                when (id) {
                    Val1.id -> dataType = Val1
                }
                return dataType
            }
        }
    }

    enum class ScreenLoanInfo(val screenName: String, val icon: Int) {
        LOAN_INFORMATION("Loan Information", R.drawable.loan_info_white),
        PERSONAL("Personal", R.drawable.personal_info_white),
        EMPLOYMENT("Employment", R.drawable.employment_icon_white),
        BANK_DETAIL("Bank Details", R.drawable.bank_icon_white),
        LIABILITY_AND_ASSET("Liability & Asset", R.drawable.assest_details_white),
        REFERENCE("Reference", R.drawable.reffrence_white),
        PROPERTY("Property", R.drawable.property_icon_white),
        DOCUMENT_CHECKLIST("Document Checklist", R.drawable.checklist),
        DEFAULT("Default", R.drawable.app_logo);

        fun isMatch(screenItem: ScreenLoanInfo): Boolean {
            try {
                return ((this.screenName.trim().equals(screenItem.screenName.trim(), ignoreCase = true)) && (this.icon == screenItem.icon))
            } catch (e: Exception) {}

            return false
        }

        companion object {
            fun getData(screenName: String?): ScreenLoanInfo {
                var dataType: ScreenLoanInfo = DEFAULT
                when (screenName) {
                    LOAN_INFORMATION.screenName -> dataType = LOAN_INFORMATION
                    PERSONAL.screenName -> dataType = PERSONAL
                    EMPLOYMENT.screenName -> dataType = EMPLOYMENT
                    BANK_DETAIL.screenName -> dataType = BANK_DETAIL
                    LIABILITY_AND_ASSET.screenName -> dataType = LIABILITY_AND_ASSET
                    REFERENCE.screenName -> dataType = REFERENCE
                    PROPERTY.screenName -> dataType = PROPERTY
                    DOCUMENT_CHECKLIST.screenName -> dataType = DOCUMENT_CHECKLIST
                    else -> dataType = DEFAULT
                }
                return dataType
            }
        }
    }
}
