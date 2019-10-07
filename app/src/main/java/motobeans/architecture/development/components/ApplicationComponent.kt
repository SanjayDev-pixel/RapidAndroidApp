package motobeans.architecture.development.components

import android.app.Application
import com.finance.app.TestActivity
import com.finance.app.presenter.presenter.*
import com.finance.app.view.activity.AddLeadActivity
import com.finance.app.view.activity.DashboardActivity
import com.finance.app.view.activity.LoginActivity
import com.finance.app.view.activity.SplashScreen
import com.finance.app.view.adapters.Recycler.Adapter.TempRecyclerAdapter
import com.finance.app.view.adapters.Recycler.Holder.TempHolder
import com.finance.app.view.fragment.*
import com.finance.app.viewModel.TempViewModel
import com.optcrm.optreporting.AppModule
import com.optcrm.optreporting.app.workers.UtilWorkersTask
import dagger.Component
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.customAppComponents.jetpack.SuperWorker
import motobeans.architecture.development.modules.NetworkModule
import motobeans.architecture.development.modules.PrimitivesModule
import motobeans.architecture.development.modules.UtilityModule
import javax.inject.Singleton

/**
 * Created by munishkumarthakur on 04/11/17.
 */
@Singleton
@Component(modules = arrayOf(
    AppModule::class, NetworkModule::class, UtilityModule::class, PrimitivesModule::class
))
interface ApplicationComponent {

    fun inject(app: Application)

    /**
     * Activities
     */
    fun inject(activity: TestActivity)
    fun inject(activity: BaseAppCompatActivity)
    fun inject(activity: DashboardActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: SplashScreen)
    fun inject(activity: AddLeadActivity)

    /**
     * Fragment
     */
    fun inject(fragment: TestFragment)
    fun inject(fragment: PersonalInfoFragment)
    fun inject(fragment: NavMenuFragment)
    fun inject(fragment: LoanInformationFragment)
    fun inject(fragment: EmploymentFragment)
    fun inject(fragment: BankDetailFragment)
    fun inject(fragment: AssetLiabilityFragment)

    /**
     * Presenters
     */
    fun inject(presenter: TestPresenter)
    fun inject(presenter: TempSyncPresenter)
    fun inject(loginPresenter: LoginPresenter)
    fun inject(addLeadPresenter: AddLeadPresenter)
    fun inject(allSpinnerValuePresenter: AllSpinnerValuePresenter)
    fun inject(loanInfoPresenter: LoanInfoPresenter)
    fun inject(sourceChannelPartnerNamePresenter: SourceChannelPartnerNamePresenter)
    fun inject(loanProductPresenter: LoanProductPresenter)

    /**
     * View Model
     */
    fun inject(viewModel: TempViewModel)

    /**
     * Adapters
     */
    fun inject(adapter: TempRecyclerAdapter)

    /**
     * Holders
     */
    fun inject(other: TempHolder)

    /**
     * Others
     */
    fun inject(other: SuperWorker)
    fun inject(other: UtilWorkersTask)
}