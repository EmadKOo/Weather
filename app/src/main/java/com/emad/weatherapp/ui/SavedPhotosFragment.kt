package com.emad.weatherapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emad.weatherapp.R
import com.emad.weatherapp.adapters.AllPhotosAdapter
import com.emad.weatherapp.databinding.FragmentSavedPhotosBinding
import com.emad.weatherapp.db.models.Photo
import com.emad.weatherapp.utils.Fonts
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedPhotosFragment : BaseFragment() {

    @Inject
    lateinit var adapter: AllPhotosAdapter
    @Inject
    lateinit var fonts: Fonts
    lateinit var mBinding:FragmentSavedPhotosBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= FragmentSavedPhotosBinding.inflate(inflater, container, false)
        mBinding.fonts= fonts
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        homeViewModel.getAllPhotos()
        homeViewModel.allPhotosLiveData().observe(viewLifecycleOwner, Observer{
            if(it.size>0){
                var photosList= ArrayList<Photo>()
                photosList.addAll(it)
                adapter.setList(photosList)
            }else{
                mBinding.noSavedItems.visibility = VISIBLE
                mBinding.savedToolbar.visibility = GONE
                mBinding.allSavedImagesRecyclerView.visibility= GONE
            }
        })
    }

    fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        mBinding.allSavedImagesRecyclerView.layoutManager = layoutManager
        mBinding.allSavedImagesRecyclerView.adapter = adapter
        handleSwipe()
    }

    fun handleSwipe(){
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    homeViewModel.removePhoto(adapter.getPhotoID(viewHolder.adapterPosition))
                    adapter.notifyDataSetChanged()
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView( mBinding.allSavedImagesRecyclerView)
    }
}