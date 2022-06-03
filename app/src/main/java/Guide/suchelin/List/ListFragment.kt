package Guide.suchelin.List

import Guide.suchelin.DataControl
import Guide.suchelin.R
import Guide.suchelin.StoreDetail.StoreDetailActivity
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.databinding.FragmentListBinding
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : BaseFragment<FragmentListBinding>(
    FragmentListBinding::bind,
    R.layout.fragment_list
) {
    companion object {
        private const val FILTER_NAME = 1
        private const val FILTER_GRADE = 2
        private const val FILTER_NEW = 3
    }
    // 그림자 효과
    private var scrolledY = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = DataControl().getStoreDataList(requireContext())
        items.sortBy { it.name }

        val rvAdapter = RvAdapter(context, items)

        rvAdapter.itemClick = object : RvAdapter.ItemClick{
            override fun onClick(view: View, position: Int){
                val intent = Intent(context, StoreDetailActivity::class.java)

                intent.putExtra("StoreName", items[position].id)
                startActivity(intent)
            }
        }

        binding.rv.apply{
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.listFilterNameTextView.setOnClickListener {
            // 필터 바꾸기
            changeFilter(FILTER_NAME)

            items.sortBy {
                it.name
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : RvAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    val intent = Intent(context, StoreDetailActivity::class.java)

                    intent.putExtra("StoreName", items[position].id)
                    startActivity(intent)
                }
            }

            binding.rv.adapter = rvAdapter
            binding.rv.layoutManager = LinearLayoutManager(context)
        }
        binding.listFilterDistanceTextView.setOnClickListener{
            // 필터 바꾸기
            changeFilter(FILTER_NEW)
            //신규니까 id가 큰게 나중에 추가된 가게
            items.apply{
                sortBy {
                    it.id
                }
                reverse()
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : RvAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    val intent = Intent(context, StoreDetailActivity::class.java)

                    intent.putExtra("StoreName", items[position].id)
                    startActivity(intent)
                }
            }

            binding.rv.adapter = rvAdapter
            binding.rv.layoutManager = LinearLayoutManager(context)
        }
        binding.listFilterGradeTextView.setOnClickListener {
            // 필터 바꾸기
            changeFilter(FILTER_GRADE)

            items.apply {
                sortBy { it.score }
                reverse()
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : RvAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    val intent = Intent(context, StoreDetailActivity::class.java)

                    intent.putExtra("StoreName", items[position].id)
                    startActivity(intent)
                }
            }

            binding.rv.adapter = rvAdapter
            binding.rv.layoutManager = LinearLayoutManager(context)
        }

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrolledY += dy

                if(scrolledY > 5 && binding.listShadowView.visibility == View.GONE){
                    binding.listShadowView.visibility = View.VISIBLE
                } else if(scrolledY < 5 && binding.listShadowView.visibility == View.VISIBLE){
                    binding.listShadowView.visibility = View.GONE
                }
            }
        })
    }

    private fun changeFilter(filter: Int){
        when(filter){
            FILTER_NAME -> {
                binding.listFilterGradeTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.listFilterNameTextView.setBackgroundResource(R.drawable.filter_check_box)
                binding.listFilterDistanceTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
            }
            FILTER_GRADE -> {
                binding.listFilterGradeTextView.setBackgroundResource(R.drawable.filter_check_box)
                binding.listFilterNameTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.listFilterDistanceTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
            }
            FILTER_NEW -> {
                binding.listFilterGradeTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.listFilterNameTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.listFilterDistanceTextView.setBackgroundResource(R.drawable.filter_check_box)
            }
        }

        scrolledY = 0
    }
}