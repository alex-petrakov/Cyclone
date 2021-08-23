package me.alexpetrakov.cyclone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.alexpetrakov.cyclone.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}