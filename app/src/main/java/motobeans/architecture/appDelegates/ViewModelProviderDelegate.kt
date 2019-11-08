package motobeans.architecture.appDelegates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import motobeans.architecture.appDelegates.ViewModelType.WITHOUT_FACTORY
import motobeans.architecture.appDelegates.ViewModelType.WITH_DAO
import com.finance.app.others.Injection

/**
 * Created by munishkumarthakur on 29/12/17.
 */

enum class ViewModelType {
    WITHOUT_FACTORY, WITH_DAO
}

inline fun <reified VM : ViewModel> androidx.fragment.app.Fragment.viewModelProvider(activity: androidx.fragment.app.FragmentActivity,
                                                                                     viewModelType: ViewModelType = WITHOUT_FACTORY) = lazy {
    var viewModelFactory: ViewModelProvider.Factory? = null
    when (viewModelType) {
        WITH_DAO -> viewModelFactory = Injection.provideViewModelFactory(activity)
    }

    // Initialize Product View Model
    if (viewModelFactory != null) {
        ViewModelProviders.of(activity, viewModelFactory).get(VM::class.java)
    } else {
        ViewModelProviders.of(activity).get(VM::class.java)
    }
}

inline fun <reified VM : ViewModel> androidx.fragment.app.FragmentActivity.viewModelProvider(activity: androidx.fragment.app.FragmentActivity,
                                                                                             viewModelType: ViewModelType = WITHOUT_FACTORY) = lazy {
    var viewModelFactory: ViewModelProvider.Factory? = null
    when (viewModelType) {
        WITH_DAO -> viewModelFactory = Injection.provideViewModelFactory(activity)
    }

    // Initialize Product View Model
    if (viewModelFactory != null) {
        ViewModelProviders.of(activity, viewModelFactory).get(VM::class.java)
    } else {
        ViewModelProviders.of(activity).get(VM::class.java)
    }
}