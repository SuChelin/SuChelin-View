package mingyuk99.suchelin.List

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import mingyuk99.suchelin.dataSet
import mingyuk99.suchelin.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //ViewBinding
        _binding = FragmentListBinding.inflate(inflater,container,false)
        val view = binding.root
//        val view = inflater.inflate(R.layout.fragment_list, container, false)
//        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val items = mutableListOf<dataSet>()

        val dummyDB = mutableListOf<dataSet>()
        dummyDB.add(dataSet("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAGJ28R7vVAaVouy37LhbNlptqTJQwl208Vg&usqp=CAU","던킨도너츠 수원대점","바바리안 도넛",37.214523,126.978058))
        dummyDB.add(dataSet("https://search.pstatic.net/common/?autoRotate=true&quality=95&type=f180_180&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210302_125%2F161464487124061agC_JPEG%2FK15utTFWXeuNEny1JMXiV57W.jpg","할리스 수원대학교점","토피넛 라떼",37.2143929, 126.978974))
        dummyDB.add(dataSet("https://mblogthumb-phinf.pstatic.net/MjAyMDAzMTdfOTAg/MDAxNTg0NDI4ODk4MTE3.Sbya0oZBVcS1uXbtrwTNdetrqjvx0Y3FHpBkdjCEELkg.olDkDm0beJhdPP3hDzesjGT4HX20mN4ILAISylHrccUg.JPEG.hyesun0305/1584428897220.jpg?type=w800","와우당","main"))

        items.add(dummyDB[0])
        items.add(dummyDB[1])
        items.add(dummyDB[2])
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))
        items.add(dataSet("imageUrl","dummy","main"))

        val rvAdapter = RvAdapter(context, items)

        binding.rv.adapter = rvAdapter
        binding.rv.layoutManager = LinearLayoutManager(context)

        return view
    }
    //Fragment LifeCycle Caution
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}