package motobeans.architecture.development.components

import android.app.Application
import com.optcrm.optreporting.AppModule
import com.finance.app.presenter.presenter.TempSyncPresenter
import com.finance.app.presenter.presenter.TestPresenter
import com.finance.app.TestActivity
import com.finance.app.presenter.presenter.LoginPresenter
import com.finance.app.view.activity.LoginActivity
import com.finance.app.view.adapters.Recycler.Adapter.TempRecyclerAdapter
import com.finance.app.view.adapters.Recycler.Holder.TempHolder
import com.finance.app.view.fragment.TestFragment
import com.finance.app.viewModel.TempViewModel
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
    fun inject(activity: LoginActivity)

    /**
     * Fragment
     */
    fun inject(fragment: TestFragment)

    /**
     * Presenters
     */
    fun inject(presenter: TestPresenter)
    fun inject(presenter: TempSyncPresenter)
    fun inject(loginPresenter: LoginPresenter)

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