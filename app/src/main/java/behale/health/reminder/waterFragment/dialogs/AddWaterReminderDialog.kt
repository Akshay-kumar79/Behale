package behale.health.reminder.waterFragment.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import behale.health.reminder.R

import behale.health.reminder.database.water.WaterReminderModel
import behale.health.reminder.databinding.DialogAddWaterReminderBinding

import behale.health.reminder.waterFragment.WaterViewModel
import java.util.*

class AddWaterReminderDialog : DialogFragment() {

    private val viewModel: WaterViewModel by navGraphViewModels(R.id.water_nav_graph)
    private lateinit var binding: DialogAddWaterReminderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DialogAddWaterReminderBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.timePicker.setIs24HourView(true)

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.okButton.setOnClickListener {

            val time = Calendar.getInstance()
            time.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
            time.set(Calendar.MINUTE, binding.timePicker.minute)
            time.set(Calendar.SECOND, 0)
            time.set(Calendar.MILLISECOND, 0)

            val waterReminder = WaterReminderModel(time = time)
            viewModel.onAddReminderDialogOkButtonClicked(waterReminder)
            dismiss()
        }

        return binding.root
    }

}