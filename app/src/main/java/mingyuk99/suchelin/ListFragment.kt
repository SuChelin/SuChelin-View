package mingyuk99.suchelin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController

class ListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val fragMap = view.findViewById<TextView>(R.id.fragMap)
        val fragVote = view.findViewById<TextView>(R.id.fragVote)

        fragMap.setOnClickListener {
            it.findNavController().navigate(R.id.action_listFragment_to_mapFragment)
        }
        fragVote.setOnClickListener {
            it.findNavController().navigate(R.id.action_listFragment_to_voteFragment)
        }
        return view

    }
}