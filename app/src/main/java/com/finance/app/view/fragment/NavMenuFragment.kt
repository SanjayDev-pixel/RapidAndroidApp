package com.finance.app.view.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.databinding.FragmentNavMenuBinding
import com.finance.app.others.AppEnums
import com.finance.app.view.adapters.recycler.adapter.NavMenuAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class NavMenuFragment : Fragment() {
    private lateinit var binding: FragmentNavMenuBinding
    private lateinit var navMenuAdapter: NavMenuAdapter
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private var menuList: List<AppEnums.ScreenLoanInfo>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNavMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        ArchitectureApp.instance.component.inject(this)
        menuList = sharedPreferences.getNavMenuItem()
        binding.rcNavMenu.layoutManager = LinearLayoutManager(requireContext())
        navMenuAdapter = NavMenuAdapter(requireContext(), menuList!!)
        binding.rcNavMenu.adapter = navMenuAdapter
    }

    fun toggleMenu() {
        navMenuAdapter.setMenuExpanded()
    }
}