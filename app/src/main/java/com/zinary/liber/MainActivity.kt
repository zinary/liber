package com.zinary.liber

import android.os.Bundle
import android.view.WindowManager
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.zinary.liber.databinding.ActivityMainBinding
import com.zinary.liber.enums.MoviesType
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val popUpMenu = PopupMenu(this, null)
        popUpMenu.inflate(R.menu.bottom_nav_menu)
        binding.bottomBar.setupWithNavController(popUpMenu.menu, navController)

        // No Internet Dialog: Signal
        NoInternetDialogSignal.Builder(
            this,
            lifecycle
        ).apply {
            dialogProperties.apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {
                        // ...
                    }
                }

                cancelable = false // Optional
                noInternetConnectionTitle = "No Internet" // Optional
                noInternetConnectionMessage =
                    "Check your Internet connection and try again." // Optional
                showInternetOnButtons = true // Optional
                pleaseTurnOnText = "Please turn on" // Optional
                wifiOnButtonText = "Wifi" // Optional
                mobileDataOnButtonText = "Mobile data" // Optional

                onAirplaneModeTitle = "No Internet" // Optional
                onAirplaneModeMessage = "You have turned on the airplane mode." // Optional
                pleaseTurnOffText = "Please turn off" // Optional
                airplaneModeOffButtonText = "Airplane mode" // Optional
                showAirplaneModeOffButtons = true // Optional
            }
        }.build()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.apply {
            getMovies(MoviesType.POPULAR, mainViewModel.popularMovies)
            getMovies(MoviesType.UPCOMING, mainViewModel.upcomingMovies)
            getMovies(MoviesType.TOP_RATED, mainViewModel.topRatedMovies)
            getMovies(MoviesType.NOW_PLAYING, mainViewModel.nowPlayingMovies)
            getGenres()
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }
}