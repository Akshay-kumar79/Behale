package behale.health.reminder.dietFragment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import behale.health.reminder.database.diet.DietReminderModel
import behale.health.reminder.databinding.ListItemForDietReminderBinding

class DietReminderAdapter(private val dietReminderClickListener: DietReminderClickListener) : RecyclerView.Adapter<DietReminderAdapter.DietReminderViewHolder>() {

    private var dietReminderList: List<DietReminderModel> = ArrayList()

    fun setData(dietReminders: List<DietReminderModel>){
        dietReminderList = dietReminders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietReminderViewHolder {
        return DietReminderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DietReminderViewHolder, position: Int) {
        holder.bind(dietReminderList[position], dietReminderClickListener)
    }

    override fun getItemCount(): Int {
        return dietReminderList.size
    }

    class DietReminderViewHolder private constructor(private val binding: ListItemForDietReminderBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun from(parent: ViewGroup): DietReminderViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemForDietReminderBinding.inflate(inflater, parent, false)
                return DietReminderViewHolder(binding)
            }
        }

        fun bind(dietReminder: DietReminderModel, dietReminderClickListener: DietReminderClickListener){
            binding.dietReminder = dietReminder
            binding.dietReminderClickListener = dietReminderClickListener
            binding.executePendingBindings()
        }
    }

}

class DietReminderClickListener(val clickListener: (dietReminderId : Int) -> Unit){
    fun onClick(dietReminder: DietReminderModel) = clickListener(dietReminder.id)
}