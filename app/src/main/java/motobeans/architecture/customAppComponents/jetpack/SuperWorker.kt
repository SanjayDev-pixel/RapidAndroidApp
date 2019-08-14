package motobeans.architecture.customAppComponents.jetpack

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject

abstract class SuperWorker(context : Context, params : WorkerParameters) : Worker(context, params) {
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var api: ApiProject

    init {
        ArchitectureApp.instance.component.inject(this)
    }
}