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
import behale.health.reminder.databinding.DialogEditWaterReminderBinding

import behale.health.reminder.waterFragment.WaterViewModel
import java.util.*

class EditWaterReminderDialog : DialogFragment() {

    private val viewModel: WaterViewModel by navGraphViewModels(R.id.water_nav_graph)
    private lateinit var binding : DialogEditWaterReminderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DialogEditWaterReminderBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.timePicker.setIs24HourView(true)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.deleteButton.setOnClickListener {
            viewModel.onEditReminderDeleteButtonClicked()
            dismiss()
        }

        binding.okButton.setOnClickListener {

            val time = Calendar.getInstance()
            time.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
            time.set(Calendar.MINUTE, binding.timePicker.minute)
            time.set(Calendar.SECOND, 0)
            time.set(Calendar.MILLISECOND, 0)

            val waterReminder = WaterReminderModel(time = time)
            viewModel.onEditReminderDialogOkButtonClicked(waterReminder)
            dismiss()
        }

        return binding.root
    }

}