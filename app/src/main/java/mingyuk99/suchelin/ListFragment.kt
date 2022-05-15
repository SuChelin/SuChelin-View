package mingyuk99.suchelin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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


        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val items = mutableListOf<dataSet>()

        items.add(dataSet("https://search.pstatic.net/common/?autoRotate=true&quality=95&type=f180_180&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210302_125%2F161464487124061agC_JPEG%2FK15utTFWXeuNEny1JMXiV57W.jpg","할리스 수원대학교점","main"))
        items.add(dataSet("https://mblogthumb-phinf.pstatic.net/MjAyMDAzMTdfOTAg/MDAxNTg0NDI4ODk4MTE3.Sbya0oZBVcS1uXbtrwTNdetrqjvx0Y3FHpBkdjCEELkg.olDkDm0beJhdPP3hDzesjGT4HX20mN4ILAISylHrccUg.JPEG.hyesun0305/1584428897220.jpg?type=w800","와우당","main"))
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))


        val rvAdapter = RvAdapter(context, items)
        rv.adapter = rvAdapter

        rv.layoutManager = LinearLayoutManager(context)

        return view
//        val fragMap = view.findViewById<TextView>(R.id.fragMap)
//        val fragVote = view.findViewById<TextView>(R.id.fragVote)
//        fragMap.setOnClickListener {
//            it.findNavController().navigate(R.id.action_listFragment_to_mapFragment)
//        }
//        fragVote.setOnClickListener {
//            it.findNavController().navigate(R.id.action_listFragment_to_voteFragment)
//        }
    }
}