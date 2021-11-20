package behale.health.reminder.waterFragment

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import behale.health.reminder.MainActivity

import behale.health.reminder.database.ReminderDataBase

import behale.health.reminder.waterFragment.adapters.WaterReminderAdapter
import behale.health.reminder.waterFragment.adapters.WaterReminderClickListener
import behale.health.reminder.waterFragment.adapters.WaterReminderSwitchClickListener
import android.text.InputType

import android.widget.EditText
import behale.health.reminder.utils.ConstantUtils

import android.view.ViewGroup
import android.widget.Toast
import behale.health.reminder.R
import behale.health.reminder.databinding.WaterFragmentBinding


class WaterFragment : Fragment() {

    private lateinit var viewModel: WaterViewModel
    private lateinit var binding: WaterFragmentBinding
    private lateinit var sp: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = WaterFragmentBinding.inflate(inflater, container, false)
        setUpStatusBarAndToolBar()
        sp = requireActivity().getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        val application: Application = requireNotNull(this.activity).application
        val waterReminderDatasource = ReminderDataBase.getInstance(application).waterReminderDao
        val waterHistoryDatasource = ReminderDataBase.getInstance(application).waterHistoryDao
        val viewModelFactory = WaterViewModelFactory(waterReminderDatasource, waterHistoryDatasource, application)
        viewModel = ViewModelProvider(findNavController().getViewModelStoreOwner(R.id.water_nav_graph), viewModelFactory).get(WaterViewModel::class.java)

        binding.waterViewModel = viewModel
        binding.lifecycleOwner = this


        val adapter = WaterReminderAdapter(
            WaterReminderClickListener {
            viewModel.onEditWaterReminderClick(it)
            findNavController().navigate(WaterFragmentDirections.actionWaterFragmentToEditWaterReminderDialog())
        },
            WaterReminderSwitchClickListener {
                viewModel.onWaterReminderSwitchClick(it)
            }
        )
        binding.waterReminderRecyclerView.adapter = adapter

        binding.changeContainerButton.setOnClickListener {
            findNavController().navigate(WaterFragmentDirections.actionWaterFragmentToChangeContainerDialog())
        }

        binding.addWaterReminderCardView.setOnClickListener {
            findNavController().navigate(WaterFragmentDirections.actionWaterFragmentToAddWaterReminderDialog())
        }

        binding.waterGoal.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Enter daily water goal")

            val input = EditText(requireContext())

            input.filters = arrayOf(InputFilter.LengthFilter(5))
            input.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, which ->
                if (input.text.isEmpty() || input.text == null) {
                    Toast.makeText(context, "Enter some value", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                sp.edit().putInt(ConstantUtils.WATER_GOAL_PREFERENCE, Integer.parseInt(input.text.toString())).apply()
            }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

            builder.show()
        }

        return binding.root
    }

    private fun setUpStatusBarAndToolBar() {
        val window: Window = requireActivity().window
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

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
        } else if (item.title == "Reset") {
            sp.edit().putInt(ConstantUtils.WATER_PROGRESS_PREFERENCE, 0).apply()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}