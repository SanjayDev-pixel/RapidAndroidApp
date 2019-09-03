package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityAddNewTaskBinding
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class AddNewTaskActivity : BaseAppCompatActivity() {

    // used to bind element of layout to activity
    private val binding: ActivityAddNewTaskBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_add_new_task)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddNewTaskActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideToolbar()
        hideSecondaryToolbar()
//        Call login api on login button
        binding.btnAddTask.setOnClickListener {
        }
    }
}
