package com.tpov.testphoto.first_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tpov.testphoto.CustomTextView
import com.tpov.testphoto.R


class FirstTaskFragment: Fragment() {

    private lateinit var textViewPicture: CustomTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_first_task, container, false)

        textViewPicture =  view.findViewById(R.id.customTextView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewPicture.setText(getString(R.string.input_text))
    }
}