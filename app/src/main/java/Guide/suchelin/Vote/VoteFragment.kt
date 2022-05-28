package Guide.suchelin.Vote

import Guide.suchelin.DataControl
import android.os.Bundle
import android.view.View
import Guide.suchelin.R
import Guide.suchelin.StoreDetail.StoreDetailActivity
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.databinding.FragmentVoteBinding
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class VoteFragment : BaseFragment<FragmentVoteBinding>(
    FragmentVoteBinding::bind,
    R.layout.fragment_vote
) {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val items = DataControl().getStoreDataList(requireContext())
        items.sortBy { it.name }
//json 적용 블럭

        val rvAdapter = VoteRvAdapter(context, items)

        rvAdapter.itemClick = object : VoteRvAdapter.ItemClick{
            override fun onClick(view: View, position: Int){
                val intent = Intent(context, StoreDetailActivity::class.java)

                intent.putExtra("name", items[position].name)
                startActivity(intent)
            }
        }

        binding.rvVote.apply{
            adapter = rvAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        binding.sortNameVote.setOnClickListener{
            items.sortBy {
                it.name
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : VoteRvAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    val intent = Intent(context, StoreDetailActivity::class.java)

                    intent.putExtra("name", items[position].name)
                    startActivity(intent)
                }
            }

            binding.rvVote.adapter = rvAdapter
            binding.rvVote.layoutManager = GridLayoutManager(context, 2)
        }
        binding.sortDistanceVote.setOnClickListener{
            items.sortBy {
                it.id
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : VoteRvAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    val intent = Intent(context, StoreDetailActivity::class.java)

                    intent.putExtra("name", items[position].name)
                    //latitude, longitude는 store detail표시에 필요없음. 지도에만 필요
                    startActivity(intent)
                }
            }

            binding.rvVote.adapter = rvAdapter
            binding.rvVote.layoutManager = GridLayoutManager(context, 2)
        }
        binding.sortScoreVote.setOnClickListener {
            items.apply {
                sortBy { it.score }
                reverse()
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : VoteRvAdapter.ItemClick{
                override fun onClick(view: View, position: Int){
                    val intent = Intent(context, StoreDetailActivity::class.java)

                    intent.putExtra("name", items[position].name)
                    //latitude, longitude는 store detail표시에 필요없음. 지도에만 필요
                    startActivity(intent)
                }
            }

            binding.rvVote.adapter = rvAdapter
            binding.rvVote.layoutManager = GridLayoutManager(context, 2)
        }
    }
}