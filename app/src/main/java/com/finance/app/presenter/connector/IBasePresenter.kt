package com.finance.app.presenter.connector

import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

interface IBasePresenter {
//    fun handleSuccess()
    fun callNetwork(type: ConstantsApi, postData:JvmType.Object)
}