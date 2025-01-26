package com.kodeleku.pmdm_u4_firebase

import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.kodeleku.pmdm_u4_firebase.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var realTimeDBManager: RealTimeDatabaseManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        realTimeDBManager=RealTimeDatabaseManager()
    }

    private fun addFavourite(favourite: FavouriteAdvertisement){
        lifecycleScope.launch (Dispatchers.IO) {
            val newFavourite: FavouriteAdvertisement? = realTimeDBManager.addFavourite(favourite!!)
            withContext(Dispatchers.Main){
                setFavouriteIcon(newFavourite !=null)
            }
        }
    }

    private fun setFavouriteIcon(b: Boolean) {

    }

    // En esta función no necesitamos manejar corrutinas debido a que en nuestro manager no se establece como suspend
    private fun updateFavourite(newTitle: String, favourite: FavouriteAdvertisement){
        realTimeDBManager.updateFavourite(favourite.copy(title = newTitle))
    }



}