package motobeans.architecture.util.delegates

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by munishkumarthakur on 29/12/17.
 */
class FragmentBindingProviderDelegate<out T : ViewDataBinding>(private val inflater: LayoutInflater,
    private val container: ViewGroup,
    @LayoutRes private val layoutRes: Int) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return binding ?: createBinding(thisRef).also { binding = it }
    }

    private fun createBinding(thisRef: Fragment): T {
        return DataBindingUtil.inflate(inflater, layoutRes, container, false)
    }
}