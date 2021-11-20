package behale.health.reminder.pillsFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import behale.health.reminder.database.pills.PillsReminderModel
import behale.health.reminder.databinding.ListItemForPillsReminderBinding

class PillsReminderAdapter(private val pillsReminderClickListener: PillsReminderClickListener) : RecyclerView.Adapter<PillsReminderAdapter.PillsReminderViewHolder>() {

    private var pillsReminders : List<PillsReminderModel> = ArrayList()

    fun setData(pillsReminders: List<PillsReminderModel>){
        this.pillsReminders = pillsReminders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillsReminderViewHolder {
        return PillsReminderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PillsReminderViewHolder, position: Int) {
        holder.bind(pillsReminders[position], pillsReminderClickListener)
    }

    override fun getItemCount(): Int {
        return pillsReminders.size
    }

    class PillsReminderViewHolder(private val binding: ListItemForPillsReminderBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): PillsReminderViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemForPillsReminderBinding.inflate(inflater, parent, false)
                return PillsReminderViewHolder(binding)
            }
        }

        fun bind(pillsReminderModel: PillsReminderModel, pillsReminderClickListener: PillsReminderClickListener) {

            binding.pillsReminder = pillsReminderModel
            binding.pillsReminderClickListener = pillsReminderClickListener
            binding.executePendingBindings()

        }

    }
}

class PillsReminderClickListener(val clickListener: (pillReminderId: Int) -> Unit){
    fun onClick(pillReminder: PillsReminderModel) = clickListener(pillReminder.id)
}