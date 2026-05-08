package com.rafabs.sp4u

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rafabs.sp4u.databinding.ActivityMainBinding
import com.rafabs.sp4u.BuildConfig

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura a Toolbar
        setSupportActionBar(binding.toolbar)

        // Configura a Navegação
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.newsFragment,
                R.id.MapredeFragment,
                R.id.sptransFragment,
                R.id.aqiFragment,
                R.id.exploreFragment,
                R.id.turismoFragment
            ),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        // Atualiza a versão no Header de forma automática
        try {
            val headerView = binding.navView.getHeaderView(0)
            val tvVersion = headerView.findViewById<TextView>(R.id.tv_app_version)

            // Agora o BuildConfig refere-se ao seu app, não ao osmdroid
            val versaoAtual = "v${BuildConfig.VERSION_NAME}"
            tvVersion.text = versaoAtual
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Verifica se os dados GTFS já foram importados
        val prefs = getSharedPreferences("sp4u_prefs", MODE_PRIVATE)
        val imported = prefs.getBoolean("gtfs_imported", false)
        if (!imported) {
            navController.navigate(R.id.importFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}