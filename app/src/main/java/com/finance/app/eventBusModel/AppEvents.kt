package com.finance.app.eventBusModel

import com.finance.app.others.AppEnums
import org.greenrobot.eventbus.EventBus

class AppEvents {
    companion object {
        fun fireEventLoanAppChangeNavFragmentNext() {
            val enumChangeLoanAppNavFragment = AppEnums.EnumEventChangeLoanApplicationFragmentNavigation.NEXT
            val event = AppEventsClasses.EnumChangeLoanAppNavFragment(enumChangeLoanAppNavFragment = enumChangeLoanAppNavFragment)
            EventBus.getDefault().post(event)
        }

        fun fireEventLoanAppChangeNavFragmentPrevious() {
            val enumChangeLoanAppNavFragment = AppEnums.EnumEventChangeLoanApplicationFragmentNavigation.PREVIOUS
            val event = AppEventsClasses.EnumChangeLoanAppNavFragment(enumChangeLoanAppNavFragment = enumChangeLoanAppNavFragment)
            EventBus.getDefault().post(event)
        }
    }
}