package com.example.bookhub.fragments

import android.app.Activity
import android.app.AlertDialog
import com.example.bookhub.adapter.Dashboardrecycleradapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import kotlinx.android.synthetic.main.fragment_dashboard.*
import model.Book
import org.json.JSONException
import util.ConnectionManager
import java.util.*
import kotlin.Comparator

/**
 * A simple [Fragment] subclass.
 */
class FragmentDashboard : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var recyclerAdapter: Dashboardrecycleradapter
   val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{book1, book2 ->

        if (book1.bookRating.compareTo(book2.bookRating, true) == 0) {
            // sort according to name if rating is same
            book1.BookName.compareTo(book2.BookName, true)
        } else {
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)


        progressBar=view.findViewById(R.id.progressbar)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(activity)




        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkconnection(activity as Context))
        {
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

            // Here we will handle the response
            try {
                progressLayout.visibility = View.GONE
                val success = it.getBoolean("success")

                if (success){

                    val data = it.getJSONArray("data")
                    for (i in 0 until data.length()){
                        val bookJsonObject = data.getJSONObject(i)
                        val bookObject = Book(
                            bookJsonObject.getString("book_id"),
                            bookJsonObject.getString("name"),
                            bookJsonObject.getString("author"),
                            bookJsonObject.getString("rating"),
                            bookJsonObject.getString("price"),
                            bookJsonObject.getString("image")
                        )
                        bookInfoList.add(bookObject)
                        recyclerAdapter = Dashboardrecycleradapter(activity as Context, bookInfoList)

                        recyclerDashboard.adapter = recyclerAdapter

                        recyclerDashboard.layoutManager = layoutManager



                    }

                } else {
                    Toast.makeText(activity as Context, "Some Error Occurred!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                Toast.makeText(activity as Context, "Some unexpected error occurred!", Toast.LENGTH_SHORT).show()
            }

        }, Response.ErrorListener {

            //Here we will handle the errors
            if (activity != null){
                Toast.makeText(activity as Context, "Volley error occurred!", Toast.LENGTH_SHORT).show()
            }

        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Token"] = "77dd5fb8c7316f"
                return headers
            }
        }


            queue.add(jsonObjectRequest)

    }
     else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Failed")
            dialog.setMessage("No Internet connection found")
            dialog.setPositiveButton("Open Settings") { text, listner ->
                val settingsIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }

            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }

        return view
}


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_dashboard,menu)




    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id= item.itemId
        if (id==R.id.action_sort){
            Collections.sort(bookInfoList, ratingComparator)
            bookInfoList.reverse()



        }

        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }



}





