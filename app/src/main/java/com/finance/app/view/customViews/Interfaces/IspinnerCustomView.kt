package com.finance.app.view.customViews.Interfaces

import android.view.View

/**
 * Created by Vishal Rathi on 10/1/2019.
 */
interface IspinnerCustomView<Type> {
    fun initializeViews(rootView: View)
    fun setSelection(iSpinnerValue: String?)
    fun getSelectedValue(): Type?
}