package behale.health.reminder.waterFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import behale.health.reminder.database.water.WaterHistoryDao
import behale.health.reminder.database.water.WaterReminderDao
import java.lang.IllegalArgumentException

class WaterViewModelFactory(private val reminderDatabase: WaterReminderDao,
                            private val historyDatabase: WaterHistoryDao,
                            private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WaterViewModel::class.java)){
            return WaterViewModel(reminderDatabase,historyDatabase,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}