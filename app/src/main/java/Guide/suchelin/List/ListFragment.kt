package Guide.suchelin.List

import Guide.suchelin.R
import Guide.suchelin.StoreDataClass
import Guide.suchelin.StoreDetail.StoreDetailActivity
import Guide.suchelin.StoreScore
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.databinding.FragmentListBinding
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.json.JSONArray
import org.json.JSONTokener

class ListFragment : BaseFragment<FragmentListBinding>(
    FragmentListBinding::bind,
    R.layout.fragment_list
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//json 적용 블럭
        val items = mutableListOf<StoreDataClass>()
        val jsonString = requireActivity().assets.open("StoreData.json").reader().readText()
        val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray

        //tmpScore가 파이어베이스로 구성될 예정
        val tmpScore = mutableListOf<StoreScore>()
        tmpScore.add(StoreScore(1,1))
        tmpScore.add(StoreScore(2,2))
        tmpScore.add(StoreScore(3,3))
        tmpScore.add(StoreScore(4,5))

        for (i in 0 until jsonArray.length()) {

            val id = jsonArray.getJSONObject(i).getInt("id")
            val imageUrl = jsonArray.getJSONObject(i).getString("imageUrl")
            val name = jsonArray.getJSONObject(i).getString("name")
            val detail = jsonArray.getJSONObject(i).getString("detail")
            /*
            * id 값으로 점수 값 받아오기. key-value가 id-score로 되게
            * */
            val scr = tmpScore[id-1].score
            Log.d("ListFragment", "$scr")
            items.add(StoreDataClass(id, imageUrl, name, detail, scr))
        }
        items.sortBy { it.name }
//json 적용 블럭

        val rvAdapter = RvAdapter(context, items)

        rvAdapter.itemClick = object : RvAdapter.ItemClick{
            override fun onClick(view: View, position: Int){
                val intent = Intent(context, StoreDetailActivity::class.java)

                intent.putExtra("name", items[position].name)
                intent.putExtra("imageUrl", items[position].imageUrl)
                intent.putExtra("detail", items[position].detail)
                intent.putExtra("score", items[position].score)

                startActivity(intent)
            }
        }

        binding.rv.apply{
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.sortName.setOnClickListener{
            items.sortBy {
                it.name
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : RvAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    val intent = Intent(context, StoreDetailActivity::class.java)

                    intent.putExtra("name", items[position].name)
                    intent.putExtra("imageUrl", items[position].imageUrl)
                    intent.putExtra("detail", items[position].detail)
                    intent.putExtra("score", items[position].score)

                    startActivity(intent)
                }
            }

            binding.rv.adapter = rvAdapter
            binding.rv.layoutManager = LinearLayoutManager(context)
        }
        binding.sortDistance.setOnClickListener{
            items.sortBy {
                it.id
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : RvAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    val intent = Intent(context, StoreDetailActivity::class.java)

                    intent.putExtra("name", items[position].name)
                    intent.putExtra("imageUrl", items[position].imageUrl)
                    intent.putExtra("detail", items[position].detail)
                    intent.putExtra("score", items[position].score)
                    //latitude, longitude는 store detail표시에 필요없음. 지도에만 필요
                    startActivity(intent)
                }
            }

            binding.rv.adapter = rvAdapter
            binding.rv.layoutManager = LinearLayoutManager(context)
        }
        binding.sortScore.setOnClickListener {
            items.apply {
                sortBy { it.score }
                reverse()
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : RvAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    val intent = Intent(context, StoreDetailActivity::class.java)

                    intent.putExtra("name", items[position].name)
                    intent.putExtra("imageUrl", items[position].imageUrl)
                    intent.putExtra("detail", items[position].detail)
                    intent.putExtra("score", items[position].score)
                    //latitude, longitude는 store detail표시에 필요없음. 지도에만 필요
                    startActivity(intent)
                }
            }

            binding.rv.adapter = rvAdapter
            binding.rv.layoutManager = LinearLayoutManager(context)
        }

    }
}