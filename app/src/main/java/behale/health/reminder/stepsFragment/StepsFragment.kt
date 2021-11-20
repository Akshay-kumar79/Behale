package behale.health.reminder.stepsFragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import behale.health.reminder.MainActivity
import behale.health.reminder.R
import behale.health.reminder.databinding.StepsFragmentBinding

import behale.health.reminder.utils.ConstantUtils

class StepsFragment : Fragment() {

    private lateinit var viewModel: StepsViewModel
    private lateinit var binding: StepsFragmentBinding
    private lateinit var sp: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = StepsFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(StepsViewModel::class.java)
        sp = requireActivity().getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setUpStatusBarAndToolBar()

        binding.stepGoal.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Enter daily steps goal")

            val input = EditText(requireContext())

            input.filters = arrayOf(InputFilter.LengthFilter(5))
            input.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, which ->
                if(input.text.isEmpty() || input.text == null) {
                    Toast.makeText(context, "Enter some value", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                sp.edit().putInt(ConstantUtils.STEPS_GOAL_PREFERENCE, Integer.parseInt(input.text.toString())).apply()
            }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

            builder.show()
        }

        return binding.root
    }

    private fun setUpStatusBarAndToolBar() {
        val window: Window = requireActivity().window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        )

        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.categoryStatusBarColor)

        binding.toolBar.setupWithNavController(
            findNavController(),
            AppBarConfiguration(findNavController().graph)
        )
        (activity as MainActivity).setSupportActionBar(binding.toolBar)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val doneMenuItem = menu.add("Reset")
        doneMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
            return true
        } else if (item.title == "Reset"){
            resetSteps()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resetSteps() {
        val previousTotalStep = sp.getFloat(ConstantUtils.FIRST_STEP_VALUE_PREFERENCE, 0f)
        val currentStep = sp.getInt(ConstantUtils.CURRENT_STEPS_PREFERENCE, 0)

        val newPreviousTotalStep = previousTotalStep + currentStep.toFloat()

        sp.edit().putInt(ConstantUtils.CURRENT_STEPS_PREFERENCE, 0).apply()
        sp.edit().putFloat(ConstantUtils.FIRST_STEP_VALUE_PREFERENCE, newPreviousTotalStep).apply()
    }

}