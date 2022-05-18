package Guide.suchelin.List

import Guide.suchelin.R
import Guide.suchelin.databinding.FragmentListBinding
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.DataSet

class ListFragment : BaseFragment<FragmentListBinding>(
    FragmentListBinding::bind,
    R.layout.fragment_list
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = mutableListOf<DataSet>()

        val dummyDB = mutableListOf<DataSet>()
        dummyDB.add(DataSet("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAGJ28R7vVAaVouy37LhbNlptqTJQwl208Vg&usqp=CAU","던킨도너츠 수원대점","바바리안 도넛",37.214523,126.978058,1))
        dummyDB.add(DataSet("https://search.pstatic.net/common/?autoRotate=true&quality=95&type=f180_180&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210302_125%2F161464487124061agC_JPEG%2FK15utTFWXeuNEny1JMXiV57W.jpg","할리스 수원대학교점","토피넛 라떼",37.2143929, 126.978974,2))
        dummyDB.add(DataSet("https://mblogthumb-phinf.pstatic.net/MjAyMDAzMTdfOTAg/MDAxNTg0NDI4ODk4MTE3.Sbya0oZBVcS1uXbtrwTNdetrqjvx0Y3FHpBkdjCEELkg.olDkDm0beJhdPP3hDzesjGT4HX20mN4ILAISylHrccUg.JPEG.hyesun0305/1584428897220.jpg?type=w800","와우당","main",37.2140056,126.979786,3))
        items.add(dummyDB[0])
        items.add(dummyDB[1])
        items.add(dummyDB[2])
        items.add(DataSet("imageUrl","dummy1","main"))
        items.add(DataSet("imageUrl","dummy4","main"))
        items.add(DataSet("imageUrl","dummy3","main"))
        items.add(DataSet("imageUrl","dummy2","main"))
        items.sortBy { it.name }

        val rvAdapter = RvAdapter(context, items)

        binding.rv.apply{
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }
//        binding.rv.adapter = rvAdapter
//        binding.rv.layoutManager = LinearLayoutManager(context)

        binding.sortName.setOnClickListener{
            items.sortBy {
                it.name
            }
            val rvAdapterName = RvAdapter(context, items)
            binding.rv.adapter = rvAdapterName
            binding.rv.layoutManager = LinearLayoutManager(context)
        }
        binding.sortDistance.setOnClickListener{

        }
        binding.sortScore.setOnClickListener {
            items.apply {
                sortBy { it.score }
                reverse()
            }

            val rvAdapterScore = RvAdapter(context, items)

            binding.rv.adapter = rvAdapterScore
            binding.rv.layoutManager = LinearLayoutManager(context)
        }
    }


}