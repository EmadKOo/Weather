package com.emad.weatherapp.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.emad.weatherapp.R
import com.emad.weatherapp.databinding.FragmentDisplayImageBinding
import com.emad.weatherapp.db.models.Photo
import com.emad.weatherapp.ui.DisplayImageFragmentArgs.fromBundle
import kotlinx.android.synthetic.main.fragment_display_image.*

class DisplayImageFragment : Fragment() {
    lateinit var currentPhoto: Photo
    lateinit var mBinding: FragmentDisplayImageBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= FragmentDisplayImageBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentPhoto= arguments?.let { fromBundle(it).photo }!!
        handleViews()

    }

    private fun handleViews(){
        mBinding.displayImageViewFragment.setImageURI(Uri.parse(currentPhoto.photoPath))
    }

}