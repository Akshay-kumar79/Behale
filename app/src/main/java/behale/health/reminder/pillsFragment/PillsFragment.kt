package behale.health.reminder.pillsFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import behale.health.reminder.MainActivity
import behale.health.reminder.R
import behale.health.reminder.databinding.PillsFragmentBinding

import behale.health.reminder.pillsFragment.adapter.PillsReminderAdapter
import behale.health.reminder.pillsFragment.adapter.PillsReminderClickListener

class PillsFragment : Fragment() {

    private lateinit var viewModel: PillsViewModel
    private lateinit var binding: PillsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = PillsFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(findNavController().getViewModelStoreOwner(R.id.pills_nav_graph)).get(PillsViewModel::class.java)

        setUpStatusBarAndToolBar()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = PillsReminderAdapter(PillsReminderClickListener {
            viewModel.onEditPillReminderClicked(it)
            findNavController().navigate(PillsFragmentDirections.actionPillsFragmentToPillDetailFragment())
        })
        binding.recyclerView.adapter = adapter

        binding.addPillCardView.setOnClickListener {
            findNavController().navigate(PillsFragmentDirections.actionPillsFragmentToAddPillFragment())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}