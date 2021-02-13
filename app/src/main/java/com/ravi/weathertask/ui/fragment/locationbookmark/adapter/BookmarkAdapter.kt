package com.ravi.weathertask.ui.fragment.locationbookmark.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ravi.weathertask.databinding.BookmarkItemBinding
import com.ravi.weathertask.repository.local.LocationEntity
import com.ravi.weathertask.ui.fragment.locationbookmark.DeleteBookmarkCallback


class BookmarkAdapter(private var bookmarkList: MutableList<LocationEntity>,private var deleteBookmarkCallback: DeleteBookmarkCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding:BookmarkItemBinding = BookmarkItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewItem(itemBinding,bookmarkList)
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewItem) {
            holder.bind(position,deleteBookmarkCallback)

            holder.itemBinding.bookmarkItem.setOnClickListener {
                deleteBookmarkCallback.bookmarkItemClick(bookmarkList[holder.adapterPosition].latitude.toString(),bookmarkList[holder.adapterPosition].longitude.toString())

            }
        }
    }


    class ViewItem(itemBinding: BookmarkItemBinding,private val data: List<LocationEntity>) : RecyclerView.ViewHolder(itemBinding.root){
         val itemBinding: BookmarkItemBinding = itemBinding

        fun bind(position: Int,deleteBookmarkCallback: DeleteBookmarkCallback) {
            itemBinding.cityName.text = data[position].city
        }

    }

    fun removeAt(position: Int) {
        deleteBookmarkCallback.bookmarkId(bookmarkList[position].id)
        bookmarkList.removeAt(position)
        notifyItemRemoved(position)

    }
}
