package com.example.bookhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.squareup.picasso.Picasso
import database.BookDatabase
import database.BookEntity
import org.json.JSONObject
import util.ConnectionManager
import java.lang.Exception

class DescriptionActivity: AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var txtBookRating: TextView
    lateinit var toolbar: Toolbar

   var bookId: String? = "100"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookRating=findViewById(R.id.txtBookRating)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        imgBookImage = findViewById(R.id.img)
        txtBookDesc=findViewById(R.id.txtdesc)
        btnAddToFav = findViewById(R.id.btnfav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout1)
        progressLayout.visibility = View.VISIBLE

        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"


        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "some Unexpected Error Occured",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(this@DescriptionActivity, "some unexpected!", Toast.LENGTH_SHORT).show()
        }

        val queue= Volley.newRequestQueue(this@DescriptionActivity)
        val url="http://13.235.250.119/v1/book/get_book/"

        val jsonParams=JSONObject()
        jsonParams.put("book_id",bookId)

        if (ConnectionManager().checkconnection(this@DescriptionActivity)){
        val jsonRequest= object : JsonObjectRequest(Request.Method.POST,url,jsonParams, Response.Listener {

            try {

                val success = it.getBoolean("success")
                if (success) {
                    val bookJsonObject = it.getJSONObject("book_data")
                    progressLayout.visibility = View.GONE

                    val bookImageUrl = bookJsonObject.getString("image")
                    Picasso.get().load(bookJsonObject.getString("image"))
                        .error(R.drawable.book_app_icon_web).into(imgBookImage)
                    txtBookName.text = bookJsonObject.getString("name")
                    txtBookAuthor.text = bookJsonObject.getString("author")
                    txtBookPrice.text = bookJsonObject.getString("price")
                    txtBookRating.text = bookJsonObject.getString("rating")
                    txtBookDesc.text = bookJsonObject.getString("description")



                    val bookEntity= BookEntity(
                        bookId?.toInt() as Int,
                        txtBookName.text.toString(),
                        txtBookAuthor.text.toString(),
                        txtBookDesc.text.toString(),
                        txtBookPrice.text.toString(),
                        txtBookRating.text.toString(),
                        bookImageUrl
                    )

                    val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                        val isFav= checkFav.get()

                    if (isFav){
                        btnAddToFav.text="Remove From Favourites"
                        val favcolor=ContextCompat.getColor(applicationContext,R.color.Favourites)
                        btnAddToFav.setBackgroundColor(favcolor)
                    }
                    else{
                        btnAddToFav.text="Add To Favourites"
                        val noFAvcolor=ContextCompat.getColor(applicationContext, R.color.navigator)
                        btnAddToFav.setBackgroundColor(noFAvcolor)
                    }
                    btnAddToFav.setOnClickListener {
                        if (!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){

                            val async= DBAsyncTask(applicationContext,bookEntity,2).execute()
                            val result=async.get()
                            if (result){
                                Toast.makeText(this@DescriptionActivity,"Book Add TO Favourites", Toast.LENGTH_SHORT).show()
                                btnAddToFav.text="Remove From Favourites"
                                val favcolor=ContextCompat.getColor(applicationContext, R.color.navigator)
                                btnAddToFav.setBackgroundColor(favcolor)

                            }else{

                                Toast.makeText(this@DescriptionActivity,"Some Error Occurred", Toast.LENGTH_SHORT).show()
                            }


                        }
                        else
                        {
                            val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                            val result=async.get()
                            if (result){

                                Toast.makeText(this@DescriptionActivity,"Book Removed Favourites",Toast.LENGTH_SHORT).show()

                                btnAddToFav.text="Add To Favourites"
                                val favcolor=ContextCompat.getColor(applicationContext, R.color.navigator)
                                btnAddToFav.setBackgroundColor(favcolor)

                            }
                            else{
                                Toast.makeText(this@DescriptionActivity,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
                else{
                    Toast.makeText(this@DescriptionActivity, "some error occured!", Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                Toast.makeText(this@DescriptionActivity, "error $e!", Toast.LENGTH_SHORT).show()

            }
        }

            ,Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity, "Volley Error $it!", Toast.LENGTH_SHORT).show()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "77dd5fb8c7316f"
                return headers
            }
        }

        queue.add(jsonRequest)

    }
        else
        {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Failed")
            dialog.setMessage("No Internet connection found")
            dialog.setPositiveButton("Open Settings") { text, listner ->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }

            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()

        }



    }
    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int): AsyncTask<Void,Void,Boolean>(){

        val db=Room.databaseBuilder(context, BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {


            when(mode)
            {
                1->{
                    val book: BookEntity?=db.bookDao().getBookID(bookEntity.book_id.toString())
                    db.close()
                    return book!=null
                }
                2->{

                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{

                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }

            }

            return false
        }


    }



}









