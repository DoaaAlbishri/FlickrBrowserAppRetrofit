package com.example.flickrbrowserappretrofit

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    //xml
    lateinit var editText: EditText
    lateinit var button: Button
    lateinit var myRv : RecyclerView
    lateinit var imageView: ImageView
    lateinit var llBottom : LinearLayout
    // array of images -- Image class
    var images = ArrayList<Image>()
  //  var apiInterface: APIInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //xml
        editText = findViewById(R.id.editText)
        button = findViewById(R.id.button)
        imageView = findViewById(R.id.imageView)
        llBottom = findViewById(R.id.llBottom)
        myRv = findViewById(R.id.recyclerView)
        // recycler view
        //context , array
        myRv.adapter = RecyclerViewAdapter(this,images)
        myRv.layoutManager = LinearLayoutManager(this)

        button.setOnClickListener {
            // fetch data
            fetchData(editText.text.toString())
        }
        // close image
        imageView.setOnClickListener { closeImage() }

    }
    fun fetchData(searchWord : String){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        // clear previous search
        images.clear()
        //progress
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.show()
        val call: Call<ImageDetails?>? = apiInterface!!.getPhoto(searchWord)
    //another ways
        /*
        val call: Call<ImageDetails?>? = apiInterface!!.getPhoto("flickr.photos.search","9cf6eae618774b19d20c9c0d747c820a",searchWord,"json")
        */
        /*
        val call: Call<ImageDetails?>? = apiInterface!!.getPhoto("flickr.photos.search","9cf6eae618774b19d20c9c0d747c820a",searchWord,"json","1")
         */
        call?.enqueue(object : Callback<ImageDetails?> {
            override fun onResponse(
                call: Call<ImageDetails?>,
                response: Response<ImageDetails?>
            )
            {
                progressDialog.dismiss()
                val resource = response.body()
                for(i in resource!!.photos?.photo!!){
                    images.add (Image(i.title!!, "https://farm${i.farm}.staticflickr.com/${i.server}/${i.id}_${i.secret}.jpg"))
                }
                myRv.adapter?.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<ImageDetails?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, ""+t.message, Toast.LENGTH_SHORT).show();
            }
        })
    }
    //open image glide
    fun openImage(link: String){
        Glide.with(this)
            .load(link)
            .into(imageView)
        imageView.isVisible = true
        myRv.isVisible = false
        llBottom.isVisible = false
    }
    // close image
    private fun closeImage(){
        imageView.isVisible = false
        myRv.isVisible = true
        llBottom.isVisible = true
    }

}