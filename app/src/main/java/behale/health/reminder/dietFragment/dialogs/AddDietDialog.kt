package behale.health.reminder.dietFragment.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import behale.health.reminder.R
import behale.health.reminder.database.diet.DietReminderModel
import behale.health.reminder.databinding.DialogAddDietBinding
import behale.health.reminder.dietFragment.DietViewModel
import java.util.*

class AddDietDialog : DialogFragment() {

    private val viewModel : DietViewModel by navGraphViewModels(R.id.diet_nav_graph)
    private lateinit var binding: DialogAddDietBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DialogAddDietBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.timePicker.setIs24HourView(true)

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.okButton.setOnClickListener {

            val dietName = binding.dietEditText.text.toString()
            val calorieText = binding.calorieEditText.text.toString()

            if(dietName.isEmpty() || dietName == ""){
                Toast.makeText(context, "Enter Diet Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(calorieText.isEmpty() || calorieText == ""){
                Toast.makeText(context, "Enter calorie count", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calorie = calorieText.toInt()

            val time = Calendar.getInstance()
            time.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
            time.set(Calendar.MINUTE, binding.timePicker.minute)
            time.set(Calendar.SECOND, 0)
            time.set(Calendar.MILLISECOND, 0)

            val dietReminderModel = DietReminderModel(
                time = time,
                dietName = dietName,
                calorie = calorie,
                isReminderOn = binding.checkBox.isChecked
            )
            viewModel.onAddDietDialogOkButtonClicked(dietReminderModel)
            dismiss()
        }

        return binding.root
    }

}