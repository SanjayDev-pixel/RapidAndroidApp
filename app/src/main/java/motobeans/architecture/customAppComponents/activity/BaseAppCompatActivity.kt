package motobeans.architecture.customAppComponents.activity

import android.app.ProgressDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.finance.app.R
import com.finance.app.databinding.ActivityBaseBinding
import com.finance.app.presenter.connector.ReusableView
import com.finance.app.view.activity.DashboardActivity
import com.finance.app.view.activity.LoginActivity
import com.finance.app.view.activity.ProfileActivity
import com.google.android.material.navigation.NavigationView
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.fragment.CommonDialogFragment
import motobeans.architecture.util.DialogFactory
import motobeans.architecture.util.exShowToast

/**
 * Created by munishkumarthakur on 04/11/17.
 */
abstract class BaseAppCompatActivity : BaseAppActivityImpl(), ReusableView {

  abstract fun init()
  private lateinit var toggle: ActionBarDrawerToggle
  private var isBackPressDialogToShow = false
  private var view: View? = null
  private lateinit var bindingParent: ActivityBaseBinding

  /**
   * View Binding setContentView(id)
   */

  companion object {
    internal var progressDialog: ProgressDialog? = null
  }

  private fun setContentBindingTemp() {
    bindingParent = DataBindingUtil.setContentView(getActivity(),
            R.layout.activity_base)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ArchitectureApp.instance.component.inject(this)
    setContentBindingTemp()
    hideToolbar()
    setToolbar()
    showToolbar()
    setUpHeaderView()
    initializeViewBindingTemp()
  }

  private fun initializeViewBindingTemp() {
    view = bindingParent.root
    initializeOtherViews()
    applyDefaultFont()
    init()
  }

  private fun initializeOtherViews() {
    toggle = ActionBarDrawerToggle(this, bindingParent.drawerLayout,
            bindingParent.appBarWithLayout.toolbarMain, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
    bindingParent.drawerLayout.addDrawerListener(toggle)
    setupDrawerContent(bindingParent.navView)
  }

  private fun setupDrawerContent(navView: NavigationView) {
    navView.setNavigationItemSelectedListener {
      selectDrawerItem(it)
      return@setNavigationItemSelectedListener true
    }
  }

  private fun selectDrawerItem(menuItem: MenuItem) {
    when (menuItem.itemId) {
      R.id.notification -> {
      }
      R.id.dashboard -> {
        DashboardActivity.start(this)
      }
      R.id.logout -> {
        LoginActivity.start(this)
      }
      R.id.assignedLeads -> {
      }
    }
    bindingParent.drawerLayout.closeDrawer(GravityCompat.START)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    toggle.syncState()
  }

  override fun onConfigurationChanged(newConfig: Configuration?) {
    super.onConfigurationChanged(newConfig)
    toggle.onConfigurationChanged(newConfig)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.notification -> {
        return true
      }
      R.id.dashboard -> {

        return true
      }

      R.id.logout -> {

        return true
      }
      R.id.assignedLeads -> {

        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun showToast(msg: String) {
    msg.exShowToast(getContext())
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

  private fun applyDefaultFont() {
    //ArchitectureApp.instance.settingFontToViewGroup(UBUNTU_REGULAR, view)
  }

  private fun setToolbar() {
    setSupportActionBar(bindingParent.appBarWithLayout.toolbarMain)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeButtonEnabled(true)
    bindingParent.appBarWithLayout.tvBackSecondary.setOnClickListener {
      onBackPressed()
    }
  }

  private fun setUpHeaderView() {
//    val headerLayout = bindingParent.navView.inflateHeaderView(R.layout.drawer_header)
//    val ivPhoto = headerLayout.ivProfile
//    ivPhoto.setImageResource(R.drawable.ic_bell)
//    val headerLayout = bindingParent.navView.getHeaderView(0)
//    headerLayout.setOnClickListener {
//      ProfileActivity.start(this)
//    }
  }

  private fun showToolbar() {
    supportActionBar?.show()
  }

  fun hideToolbar() {
    supportActionBar?.hide()
  }

  fun hideSecondaryToolbar() {
    bindingParent.appBarWithLayout.secondaryToolbar.visibility = View.GONE
  }

  fun getParentBinding(): ActivityBaseBinding {
    return bindingParent
  }
}