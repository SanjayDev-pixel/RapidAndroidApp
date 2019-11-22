package com.finance.app.presenter.connector

import motobeans.architecture.retrofit.response.Response

interface DistrictCityConnector {

    interface District : ReusableView {
        val stateId: String

        fun getDistrictSuccess(value: Response.ResponseDistrict)
        fun getDistrictFailure(msg: String)

    }

    interface City : ReusableView {
        val districtId: String

        fun getCitySuccess(value: Response.ResponseCity)
        fun getCityFailure(msg: String)

    }

    interface PresenterOpt : ReusableNetworkConnector
}