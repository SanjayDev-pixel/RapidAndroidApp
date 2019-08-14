package com.finance.app.others

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
}
