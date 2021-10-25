package me.alexpetrakov.cyclone.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import me.alexpetrakov.cyclone.common.presentation.extensions.requireAppComponent
import me.alexpetrakov.cyclone.databinding.FragmentSettingsBinding
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var router: Router

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        requireAppComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView(): Unit = with(binding) {
        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout, null)
        toolbar.setNavigationOnClickListener { router.exit() }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}