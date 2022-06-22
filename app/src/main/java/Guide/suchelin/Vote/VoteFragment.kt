package Guide.suchelin.Vote

import Guide.suchelin.DataClass.StoreDataClass
import Guide.suchelin.DataClass.StoreScore
import Guide.suchelin.DataControl
import Guide.suchelin.List.ListFragment
import android.os.Bundle
import android.view.View
import Guide.suchelin.R
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.databinding.FragmentVoteBinding
import android.util.Log
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class VoteFragment : BaseFragment<FragmentVoteBinding>(
    FragmentVoteBinding::bind,
    R.layout.fragment_vote
) {
    companion object {
        private const val FILTER_NAME = 1
        private const val FILTER_GRADE = 2
        private const val FILTER_NEW = 3
        private const val FILTER_VOTED = 4
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var rvAdapter: VoteRvAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        auth.signInAnonymously()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("MainActivity", user!!.uid)
                    //로그인됐으면 uid값 찍힘 !!는 not null임을 명시하는 것.
                } else {
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }

        database = Firebase.database.reference

        //고유 uid기반으로 데이터가 들어감
        // val myRef = database.getReference("StoreVote").child(Firebase.auth.currentUser!!.uid)
        val items = DataControl().getStoreDataList(requireContext())
        items.sortBy { it.name }

        // rvAdatper 설정
        rvAdapter = VoteRvAdapter(items)

        // rvAdatper 설정
        setRvAdapter(items)

        // 필터 설정
        changeFilter(FILTER_NAME)

        // 투표한 가게 필터 부분
        binding.sortSelectVoted.setOnClickListener {
            rvAdapter = VoteRvAdapter(items)
            setRvAdapter(items)
            // 필터 바꾸기
            changeFilter(FILTER_VOTED)

            // 내용...!
        }
        //검색
        binding.voteSearchImageView.setOnClickListener {
            //검색버튼 누르면 키보드 내려가게
            var imm:InputMethodManager? = null
            imm = requireActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(it.windowToken,0)

            val searchText = binding.voteSearchEditTextView.text.toString()
            val searchItem = mutableListOf<StoreDataClass>()
            var searchComplete = false
            //일부만 같으려면 contains, 완전히 같으려면 eqauls나 contentEquals
            for(i in items.indices){
                if(items[i].name.contains(searchText,true)){
                    searchItem.add(items[i])
                    rvAdapter = VoteRvAdapter(searchItem)

                    setRvAdapter(searchItem as ArrayList<StoreDataClass>)
                    searchComplete = true
                    // rvAdatper 설정
                    break
                }
            }
            if(searchComplete==false){
                Toast.makeText(context, "검색결과가 없습니다", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sortNameVote.setOnClickListener {
            rvAdapter = VoteRvAdapter(items)
            setRvAdapter(items)

            items.sortBy {
                it.name
            }
            rvAdapter.notifyDataSetChanged()

            // rvAdatper 설정
            setRvAdapter(items)

            // 필터 바꾸기
            changeFilter(FILTER_NAME)
        }

        binding.sortDistanceVote.setOnClickListener {
            rvAdapter = VoteRvAdapter(items)
            setRvAdapter(items)

            items.sortBy {
                it.id
            }

            rvAdapter.notifyDataSetChanged()

            // rvAdatper 설정
            setRvAdapter(items)

            // 필터 바꾸기
            changeFilter(FILTER_NEW)
        }
        binding.sortScoreVote.setOnClickListener {
            rvAdapter = VoteRvAdapter(items)
            setRvAdapter(items)

            items.apply {
                sortBy { it.score }
                reverse()
            }

            rvAdapter.notifyDataSetChanged()

            // rvAdatper 설정
            setRvAdapter(items)

            // 필터 바꾸기
            changeFilter(FILTER_GRADE)
        }
    }

    private fun setRvAdapter(items: ArrayList<StoreDataClass>) {
        binding.rvVote.adapter = rvAdapter
        binding.rvVote.layoutManager = GridLayoutManager(context, 2)

        rvAdapter.itemClick = object : VoteRvAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val storeId = items[position].id
                val storeName = items[position].name
                if(VoteSharedPreference().getVoteStatement(storeId.toString(), requireActivity()) == 0){
                    // 처음 투표한 가게일 경우
                    VoteControl(this@VoteFragment, database, storeId, storeName).showDialog()
                } else {
                    // 이미 투표한 가게일 경우
                    Toast.makeText(this@VoteFragment.requireContext(), "이미 투표한 가게입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun changeFilter(filter: Int){
        when(filter){
            FILTER_NAME -> {
                binding.sortScoreVote.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.sortNameVote.setBackgroundResource(R.drawable.filter_check_box)
                binding.sortDistanceVote.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.sortSelectVoted.setBackgroundResource(R.drawable.filter_uncheck_box)
            }
            FILTER_GRADE -> {
                binding.sortScoreVote.setBackgroundResource(R.drawable.filter_check_box)
                binding.sortNameVote.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.sortDistanceVote.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.sortSelectVoted.setBackgroundResource(R.drawable.filter_uncheck_box)
            }
            FILTER_NEW -> {
                binding.sortScoreVote.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.sortNameVote.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.sortDistanceVote.setBackgroundResource(R.drawable.filter_check_box)
                binding.sortSelectVoted.setBackgroundResource(R.drawable.filter_uncheck_box)
            }
            FILTER_VOTED -> {
                binding.sortScoreVote.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.sortNameVote.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.sortDistanceVote.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.sortSelectVoted.setBackgroundResource(R.drawable.filter_check_box)
            }
        }

    }
}
