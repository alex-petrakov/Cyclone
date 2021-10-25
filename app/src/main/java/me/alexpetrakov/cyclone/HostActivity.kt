package me.alexpetrakov.cyclone

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.material.transition.MaterialSharedAxis
import me.alexpetrakov.cyclone.common.presentation.extensions.requireAppComponent
import me.alexpetrakov.cyclone.databinding.ActivityHostBinding
import javax.inject.Inject

class HostActivity : AppCompatActivity() {

    private val navigator = object : AppNavigator(this, R.id.fragment_container) {
        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment
        ) {
            if (currentFragment == null) {
                return
            }
            currentFragment.apply {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            }
            nextFragment.apply {
                enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            }
        }
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        requireAppComponent().inject(this)
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        val binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
                val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = systemBars.top
                    leftMargin = systemBars.left
                    rightMargin = systemBars.right
                }
                windowInsets
            }
        }

        if (savedInstanceState == null) {
            navigator.applyCommands(arrayOf(Replace(AppScreens.weather())))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}