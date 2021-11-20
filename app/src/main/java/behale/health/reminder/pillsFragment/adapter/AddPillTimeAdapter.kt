package behale.health.reminder.pillsFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import behale.health.reminder.databinding.ListItemForAddPillTimeBinding

import java.util.*
import kotlin.collections.ArrayList

class AddPillTimeAdapter: RecyclerView.Adapter<AddPillTimeAdapter.ViewHolder>() {

    private var time: List<Calendar> = ArrayList()
    fun setData(time: List<Calendar>){
        this.time = time
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(time[position])
    }

    override fun getItemCount(): Int {
        return time.size
    }

    class ViewHolder(private val binding: ListItemForAddPillTimeBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemForAddPillTimeBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(singleTime: Calendar){
            binding.time = singleTime
            binding.executePendingBindings()
        }

    }

}