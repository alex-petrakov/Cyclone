package me.alexpetrakov.cyclone

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import me.alexpetrakov.cyclone.databinding.ActivityHostBinding
import javax.inject.Inject

@AndroidEntryPoint
class HostActivity : AppCompatActivity() {

    private val navigator = AppNavigator(this, R.id.fragment_container)

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
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