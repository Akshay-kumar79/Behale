package com.example.behale.homeFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.behale.database.water.WaterHistoryDao
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val historyDatabase: WaterHistoryDao,
                           private val application: Application
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(historyDatabase,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}