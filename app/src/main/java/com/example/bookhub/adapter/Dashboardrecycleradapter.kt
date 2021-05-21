package com.example.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.activity.DescriptionActivity
import com.squareup.picasso.Picasso
import model.Book

class Dashboardrecycleradapter(val context: Context, val itemlist: ArrayList<Book>): RecyclerView.Adapter<Dashboardrecycleradapter.Dashboardviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Dashboardviewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dasboard_singlerow,parent,false)
         return Dashboardviewholder(view)
    }

    override fun getItemCount(): Int {
         return itemlist.size
    }

    override fun onBindViewHolder(holder: Dashboardviewholder, position: Int) {
        val book=itemlist[position]
        holder.txtBookPrice.text=book.bookPrice
        holder.txtBookAuthor.text=book.bookAuthor
        holder.txtBookName.text=book.BookName
        holder.txtBookRating.text=book.bookRating
        //holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.book_app_icon_web).into(holder.imgBookImage)
        holder.licontent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)
        }



    }

    class Dashboardviewholder(view: View) : RecyclerView.ViewHolder(view){

        val txtBookName: TextView =view.findViewById(R.id.txtBookName)
        val txtBookAuthor:TextView=view.findViewById(R.id.txtBookAuthor)
        val txtBookRating:TextView=view.findViewById(R.id.txtBookRating)
        val txtBookPrice:TextView=view.findViewById(R.id.txtBookPrice)
        val imgBookImage:ImageView=view.findViewById(R.id.imgBookImage)
        val licontent:LinearLayout=view.findViewById(R.id.licontent)
    }



}




