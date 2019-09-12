package com.finance.app.model

class Modals {
    data class NavItems(val images: Int, val title: String)
    data class Spinner(val value: SpinnerValue)
    data class SpinnerValue(val name: String, val id: Int)
}
