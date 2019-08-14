package motobeans.architecture.customAppComponents.activity

import android.app.ProgressDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
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
abstract class BaseAppCompatActivity : BaseAppActivityImpl(), ReusableView {

  abstract fun init()

  private var isBackPressDialogToShow = false
  private var view: View? = null
  private lateinit var bindingParent: CustomActionbaractivityWithBackBinding

  val fragmentActivity: FragmentActivity = this
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

    setContentBindingTemp()
    initializeViewBindingTemp()

  }

  private fun initializeViewBindingTemp() {
    view = bindingParent?.root

    initializeOtherViews()

    setToolbar()
    showToolbar()
    showBack()
    applyDefaultFont()

    init()
  }

  private fun initializeOtherViews() {
    bindingParent.includeToolbar!!.llToolbarBack.setOnClickListener { _ -> onBackPressed() }
  }

  fun getParentBinding(): CustomActionbaractivityWithBackBinding {
    return bindingParent
  }

  fun getView(): View? {
    return view
  }

  fun applyDefaultFont() {
    //ArchitectureApp.instance.settingFontToViewGroup(UBUNTU_REGULAR, view)
  }

  fun setToolbar() {
    setSupportActionBar(bindingParent.toolbarMain)
  }

  fun showBack() {
    showToolbar()
    bindingParent.includeToolbar!!.ivToolbarBack.visibility = View.VISIBLE
  }

  fun hideBack() {
    bindingParent.includeToolbar!!.ivToolbarBack.visibility = View.GONE
  }

  fun showToolbar() {
    supportActionBar?.show()
  }

  fun hideToolbar() {
    supportActionBar?.hide()
  }

  fun setTitleCustom(title: String?) {
    bindingParent.includeToolbar?.tvToolbarTitle?.text = title ?: ""
  }

  fun setIsBackPressDialogToShow(isBackPressDialogToShow: Boolean){
    this.isBackPressDialogToShow = isBackPressDialogToShow
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
        var msg = "You really want to exit?"
        var fragment = CommonDialogFragment.newInstance(msg)
        fragment.show(supportFragmentManager, "")
        fragment.setListener({ fragment.dismiss() }, { super.onBackPressed() })
      }
      false -> {
        super.onBackPressed()
      }
    }

  }
}