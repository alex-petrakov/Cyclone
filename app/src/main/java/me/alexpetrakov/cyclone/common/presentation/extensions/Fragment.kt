package me.alexpetrakov.cyclone.common.presentation.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}

inline fun <reified T : ViewModel> Fragment.parentViewModel(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> {
    return lazy(mode) {
        requireParentFragment().getViewModel(qualifier, parameters)
    }
}