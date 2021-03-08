package com.emad.weatherapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.emad.weatherapp.R
import com.emad.weatherapp.db.models.Photo
import com.emad.weatherapp.ui.SavedPhotosFragmentDirections
import kotlinx.android.synthetic.main.photo_item_holder.view.*
import javax.inject.Inject

class AllPhotosAdapter @Inject constructor(): RecyclerView.Adapter<AllPhotosAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    var photosList= ArrayList<Photo>();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.getContext())
        val view: View = inflater.inflate(R.layout.photo_item_holder, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.item_image_thumbnail.setImageURI(Uri.parse(photosList[position].photoPath))
        holder.itemView.item_city_name.text= photosList[position].cityName
        holder.itemView.item_description.text= photosList[position].description
        holder.itemView.item_temp.text= "${photosList[position].temp} \u2103"
        holder.itemView.setOnClickListener {
            navigateToDisplayImage(photosList[position], it)
        }
    }

    fun setList(newList: ArrayList<Photo>){
        photosList= newList
        notifyDataSetChanged()
    }

    fun getPhotoID(position: Int):Int{
        return photosList[position].id
    }
    fun navigateToDisplayImage(photo: Photo, view: View){
        val action= SavedPhotosFragmentDirections.actionSavedPhotosFragmentToDisplayImageFragment(photo)
        view.findNavController().navigate(action)
    }
}
