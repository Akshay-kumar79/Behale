package behale.health.reminder.waterFragment.dialogs

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
import behale.health.reminder.databinding.DialogChangeContainerBinding
import behale.health.reminder.waterFragment.WaterViewModel

class ChangeContainerDialog : DialogFragment() {

    private val viewModel: WaterViewModel by navGraphViewModels(R.id.water_nav_graph)
    private lateinit var binding : DialogChangeContainerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DialogChangeContainerBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.changeButton.setOnClickListener {
            val newContainerSizeString = binding.editText.text.toString()
            if (newContainerSizeString.isEmpty() || newContainerSizeString == "") {
                Toast.makeText(context, "Enter valid size", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.updateContainerSize(newContainerSizeString.toInt())
            dismiss()
        }

        return binding.root
    }

}