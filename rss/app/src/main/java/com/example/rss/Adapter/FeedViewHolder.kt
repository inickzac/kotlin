package com.example.rss.Adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.rss.Interface.ItemClickListener
import com.example.rss.Model.RSSObject
import com.example.rss.OneNewsActivity
import com.example.rss.R
import android.graphics.Color
import android.widget.*
import android.view.MenuItem

var adapterPublic : RecyclerView.Adapter<FeedViewHolder>? = null
lateinit var rssObjectPublic : RSSObject


class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
    View.OnClickListener, View.OnLongClickListener {
    var txtTitle: TextView
    var txtPubDate: TextView
    var srcImage: ImageView
    var changeColorBtn : ImageButton
    var linerLoyInRec : LinearLayout
    var sortBtn : ImageButton
    private var itemClickListener: ItemClickListener? = null
    init {
        sortBtn = itemView.findViewById(R.id.sort)
        txtTitle = itemView.findViewById(R.id.textTitle) as TextView
        txtPubDate = itemView.findViewById(R.id.txtPubdate) as TextView
        srcImage = itemView.findViewById(R.id.imageNews) as ImageView
        changeColorBtn =  itemView.findViewById(R.id.changeColorBtn) as ImageButton
        linerLoyInRec = itemView.findViewById(R.id.linerLoyInRec) as LinearLayout
        changeColorBtn.setOnClickListener {
            showPopupColor(changeColorBtn, this.adapterPosition )
        }
        sortBtn.setOnClickListener{showPopupSort(sortBtn)
            adapterPublic?.notifyDataSetChanged()}
        itemView.setOnLongClickListener(this)
        itemView.setOnClickListener(this)
    }
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
    override fun onClick(v: View?) {
        itemClickListener!!.onClick(v, adapterPosition, false)
    }
    override fun onLongClick(v: View?): Boolean {
        itemClickListener!!.onClick(v, adapterPosition, true)
        return true
    }
    private fun showPopupColor(view: View, position: Int) {
       var popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.change_color_menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.menuRed -> {
                    rssObjectPublic.items[position].color = Color.RED
                    adapterPublic?.notifyDataSetChanged()
                }
                R.id.menuBlue -> {
                    rssObjectPublic.items[position].color =  Color.BLUE
                    adapterPublic?.notifyDataSetChanged()
                }
                R.id.menuGreen -> {
                    rssObjectPublic.items[position].color = Color.GREEN
                    adapterPublic?.notifyDataSetChanged()
                }
                R.id.menuYellow-> {
                    rssObjectPublic.items[position].color =  Color.YELLOW
                    adapterPublic?.notifyDataSetChanged()
                }
                R.id.menuWhite-> {
                    rssObjectPublic.items[position].color = Color.WHITE
                    adapterPublic?.notifyDataSetChanged()
                }
            }
            true
        })

        popup.show()
    }
    private fun showPopupSort(view: View) {
        var popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.sort_menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.menuSortByColor -> {
                    rssObjectPublic.items= rssObjectPublic.items.sortedBy {it.color }
                    adapterPublic?.notifyDataSetChanged()
                }
                R.id.menuSortByAuthor -> {
                    rssObjectPublic.items= rssObjectPublic.items.sortedBy {it.author }
                    adapterPublic?.notifyDataSetChanged()
                }
                R.id.menuSortByDate -> {
                    rssObjectPublic.items= rssObjectPublic.items.sortedBy {it.pubDate}
                    adapterPublic?.notifyDataSetChanged()
                }
                R.id.menuYellow-> {
                    rssObjectPublic.items= rssObjectPublic.items.sortedBy {it.content }
                    adapterPublic?.notifyDataSetChanged()
                }
            }
            true
        })

        popup.show()
    }

}
class FeedAdapter(private val rssObject: RSSObject, private val mContext:
Context): RecyclerView.Adapter<FeedViewHolder>() {
    private val inflater = LayoutInflater.from(mContext)
    init {
        rssObjectPublic = rssObject
        rssObjectPublic.items.forEach { o-> o.color= Color.WHITE }
        adapterPublic =this
    }
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.txtTitle.text = rssObject.items[position].title
        holder.txtPubDate.text = rssObject.items[position].pubDate
        holder.itemView.setBackgroundColor(rssObjectPublic.items[position].color)
        Glide.with(mContext).load(rssObject.items[position].thumbnail).into(holder.srcImage)
        holder.setItemClickListener(ItemClickListener { _, pos, isLongClick ->
            if (!isLongClick) {
                val intent = Intent(mContext, OneNewsActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("TITLE", rssObject.items[pos].title)
                intent.putExtra("DATE", rssObject.items[pos].pubDate)
                intent.putExtra("CONTENT", rssObject.items[pos].content)
                intent.putExtra("AUTHOR", rssObject.items[pos].author)
                mContext.startActivity(intent)
            }
        })
    }
    override fun getItemCount(): Int = rssObject.items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            FeedViewHolder {
        val itemView = inflater.inflate(R.layout.row, parent, false)
        return FeedViewHolder(itemView)
    }
}