package motobeans.architecture.customAppComponents.activity

import android.app.ProgressDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentActivity
import android.view.MenuItem
import android.view.View
import com.finance.app.R
import com.finance.app.presenter.connector.ReusableView
import com.finance.app.databinding.CustomActionbaractivityWithBackBinding
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.fragment.CommonDialogFragment
import motobeans.architecture.util.DialogFactory
import motobeans.architecture.util.exShowToast

/**
 * Created by munishkumarthakur on 04/11/17.
 */
abstract class BaseAppCompatActivity : BaseAppActivityImpl(), ReusableView, NavigationView.OnNavigationItemSelectedListener {

  abstract fun init()

  private var isBackPressDialogToShow = false
  private var view: View? = null
  private lateinit var bindingParent: CustomActionbaractivityWithBackBinding

  /**
   * View Binding setContentView(id)
   */
  fun setContentBindingTemp() {
    bindingParent = DataBindingUtil.setContentView(getActivity(),
        R.layout.custom_actionbaractivity_with_back)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ArchitectureApp.instance.component.inject(this)

    hideToolbar()
    setContentBindingTemp()
    initializeViewBindingTemp()

  }

  private fun initializeViewBindingTemp() {
    view = bindingParent.root

    initializeOtherViews()

    setToolbar()
    showToolbar()
    applyDefaultFont()

    init()
  }

  private fun initializeOtherViews() {
//    bindingParent.includeToolbar.llToolbarBack.setOnClickListener { onBackPressed() }
  }

  fun getParentBinding(): CustomActionbaractivityWithBackBinding {
    return bindingParent
  }

  fun applyDefaultFont() {
    //ArchitectureApp.instance.settingFontToViewGroup(UBUNTU_REGULAR, view)
  }

  fun setToolbar() {
    setSupportActionBar(bindingParent.toolbarMain)
  }

  fun showToolbar() {
    supportActionBar?.show()
  }

  fun hideToolbar() {
    supportActionBar?.hide()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.getItemId()) {
      android.R.id.home -> {
        finish()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  override fun showToast(msg: String) {
    msg.exShowToast(getContext())
  }

  companion object {
    internal var progressDialog: ProgressDialog? = null
  }

  override fun showProgressDialog() {
    when (progressDialog == null) {
      true -> progressDialog = DialogFactory.getInstance(context = getContext())
    }
    progressDialog?.show()
  }

  override fun hideProgressDialog() {
    progressDialog?.hide()
  }

  override fun onBackPressed() {
    when(isBackPressDialogToShow){
      true -> {
        val msg = "You really want to exit?"
        val fragment = CommonDialogFragment.newInstance(msg)
        fragment.show(supportFragmentManager, "")
        fragment.setListener({ fragment.dismiss() }, { super.onBackPressed() })
      }
      false -> {
        super.onBackPressed()
      }
    }
  }

  override fun onNavigationItemSelected(p0: MenuItem): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}