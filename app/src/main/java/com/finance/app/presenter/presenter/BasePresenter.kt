package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.IBasePresenter
import motobeans.architecture.constants.ConstantsApi
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class BasePresenter : IBasePresenter {
    override fun callNetwork(type: ConstantsApi, postData: JvmType.Object) {
    }
}