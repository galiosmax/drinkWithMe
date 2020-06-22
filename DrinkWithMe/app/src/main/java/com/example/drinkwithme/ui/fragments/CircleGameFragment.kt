package com.example.drinkwithme.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import com.example.drinkwithme.R
import com.example.drinkwithme.ui.drawing.DrawCircleView


class CircleGameFragment : Fragment() {

    companion object {
        fun newInstance() =
            CircleGameFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // initialize binding
        val rootView = inflater.inflate(R.layout.circle_game_fragment, container, false)
        val layout = rootView.findViewById<RelativeLayout>(R.id.rect)

        layout.addView(DrawCircleView(requireContext()))

        setHasOptionsMenu(true)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.default_menu, menu)
    }
}
