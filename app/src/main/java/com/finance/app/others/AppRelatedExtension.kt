package com.finance.app.others

import motobeans.architecture.retrofit.response.Response.ResponseSample
import motobeans.architecture.util.exToDouble


fun List<ResponseSample>.totalAmountOfProducts(): Double {
    return getTotalAmount(this)
}

fun getTotalAmount(list: List<ResponseSample>): Double {
    var totalAmount = 0.0
    list.forEach { product -> totalAmount += product.message.exToDouble() }
    return totalAmount
}
