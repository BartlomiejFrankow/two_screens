package com.example.twoscreens.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.twoscreens.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setGraph()
        get<SnackBarError>().registerActivity(this)
    }

    private fun setGraph() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationHost) as NavHostFragment
        navHostFragment.navController.setGraph(R.navigation.main_navigation)
        setupActionBarWithNavController(navHostFragment.navController, AppBarConfiguration(navHostFragment.navController.graph))
    }

    override fun onSupportNavigateUp() = navigationHost.findNavController().navigateUp()

}
