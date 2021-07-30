package org.xapps.apps.foodiex.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.foodiex.databinding.ActivityMainBinding
import javax.inject.Inject


@AndroidEntryPoint
class FoodiexActivity @Inject constructor(): AppCompatActivity() {

    private lateinit var bindings: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindings.root)
    }

}