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

        companion object {
            fun getData(screenName: String?): ScreenLoanInfo {
                return when (screenName) {
                    LOAN_INFORMATION.screenName -> LOAN_INFORMATION
                    PERSONAL.screenName -> PERSONAL
                    EMPLOYMENT.screenName -> EMPLOYMENT
                    BANK_DETAIL.screenName -> BANK_DETAIL
                    LIABILITY_AND_ASSET.screenName -> LIABILITY_AND_ASSET
                    REFERENCE.screenName -> REFERENCE
                    PROPERTY.screenName -> PROPERTY
                    DOCUMENT_CHECKLIST.screenName -> DOCUMENT_CHECKLIST
                    else -> DEFAULT
                }
            }
        }
    }

    enum class ADDRESS_TYPE (val addressType: String) {
        PERMANENT ("PERMANENT"), CURRENT ("CURRENT")
    }

    enum class EMPLOYMENT_TYPE (val employmentType: String) {
        SENP ("SENP"), SALARY ("SALARY")
    }
}
