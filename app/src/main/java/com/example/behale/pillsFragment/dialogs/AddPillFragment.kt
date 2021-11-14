package com.example.behale.pillsFragment.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.behale.R
import com.example.behale.databinding.FragmentAddPillBinding
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.behale.MainActivity
import com.example.behale.database.pills.PillsReminderModel
import com.example.behale.pillsFragment.PillsViewModel
import com.example.behale.pillsFragment.adapter.AddPillTimeAdapter
import com.example.behale.utils.ConstantUtils
import com.google.android.material.chip.Chip
import java.util.*
import kotlin.collections.ArrayList


class AddPillFragment : Fragment() {

    private lateinit var binding: FragmentAddPillBinding
    private val viewModel: PillsViewModel by navGraphViewModels(R.id.pills_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPillBinding.inflate(inflater, container, false)
        setUpStatusBarAndToolBar()

        val items = arrayOf("EveryDay", "Alternate Days", "Periodic")
        val spinnerAdapter = ArrayAdapter(requireActivity(), R.layout.spiner_item, items)
        binding.spinner
        binding.spinner.adapter = spinnerAdapter

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val timeAdapter = AddPillTimeAdapter()
        binding.timeRecyclerView.adapter = timeAdapter

        binding.timePicker.setIs24HourView(true)


        binding.addPillCardView.setOnClickListener {
            val timePicker = TimePickerDialog(context, viewModel.timePickerDialogListener, 0, 0, true)
            timePicker.show()
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.clearAddPillScreenCache()
                if (isEnabled) {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }

        })

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
        binding.toolBar.setNavigationIcon(R.drawable.ic_baseline_close_24)

        (activity as MainActivity).setSupportActionBar(binding.toolBar)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val doneMenuItem = menu.add("Done")
        doneMenuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_24)
        doneMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
        }
        if (item.title == "Done") {

            val pillName = binding.pillEditText.text.toString()
            var period = 1
            var times: List<Calendar> = ArrayList()

            if (pillName.isEmpty() || pillName == "") {
                Toast.makeText(context, "Enter Pill Name", Toast.LENGTH_SHORT).show()
                return true
            }

            if (binding.spinner.selectedItem == ConstantUtils.ADD_PILL_TYPE_EVERYDAY) {
                if (viewModel.listOfTimeForAddPill.value!!.isEmpty()) {
                    Toast.makeText(context, "Set Time", Toast.LENGTH_SHORT).show()
                    return true
                }
                period = 1
                val currentTimes: ArrayList<Calendar> = ArrayList()
                for (c in viewModel.listOfTimeForAddPill.value!!){
                    currentTimes.add(c)
                }
                times = currentTimes

            }
            else if(binding.spinner.selectedItem == ConstantUtils.ADD_PILL_TYPE_ALTERNATIVE_DAY){
                period = 2
                val time = Calendar.getInstance()
                time.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                time.set(Calendar.MINUTE, binding.timePicker.minute)
                time.set(Calendar.SECOND, 0)
                time.set(Calendar.MILLISECOND, 0)
                times = listOf(time)

            }
            else if (binding.spinner.selectedItem == ConstantUtils.ADD_PILL_TYPE_PERIODIC) {
                if (binding.periodEditText.text.isEmpty() || binding.periodEditText.text.toString() == "") {
                    Toast.makeText(context, "Enter Period", Toast.LENGTH_SHORT).show()
                    return true
                }
                period = binding.periodEditText.text.toString().toInt()
                val time = Calendar.getInstance()
                time.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                time.set(Calendar.MINUTE, binding.timePicker.minute)
                time.set(Calendar.SECOND, 0)
                time.set(Calendar.MILLISECOND, 0)
                times = listOf(time)
            }
            // Alright now add pill

            // get startDate
            var selectedChipText = ""
            binding.chipGroup.children.toList().filter { (it as Chip).isChecked }.forEach{
                selectedChipText = (it as Chip).text.toString()
            }
            val startDate = Calendar.getInstance()
            startDate.add(Calendar.DAY_OF_YEAR, viewModel.sevenDayListForChip.value!!.indexOf(selectedChipText))

            val pillReminder = PillsReminderModel(
                pillName = pillName,
                type = binding.spinner.selectedItem.toString(),
                startFrom = startDate,
                period = period,
                time = times,
            )

            viewModel.onAddPillTickButtonClick(pillReminder)
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}