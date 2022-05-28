package Guide.suchelin.List

import Guide.suchelin.DataControl
import Guide.suchelin.R
import Guide.suchelin.StoreDetail.StoreDetailActivity
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.databinding.FragmentListBinding
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

class ListFragment : BaseFragment<FragmentListBinding>(
    FragmentListBinding::bind,
    R.layout.fragment_list
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = DataControl().getStoreDataList(requireContext())
        items.sortBy { it.name }

        val rvAdapter = RvAdapter(context, items)

        rvAdapter.itemClick = object : RvAdapter.ItemClick{
            override fun onClick(view: View, position: Int){
                val intent = Intent(context, StoreDetailActivity::class.java)

                intent.putExtra("name", items[position].name)
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
                    startActivity(intent)
                }
            }

            binding.rv.adapter = rvAdapter
            binding.rv.layoutManager = LinearLayoutManager(context)
        }

    }
}