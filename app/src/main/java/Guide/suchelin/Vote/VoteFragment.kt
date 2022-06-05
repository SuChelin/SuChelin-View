package Guide.suchelin.Vote

import Guide.suchelin.DataClass.StoreDataClass
import Guide.suchelin.DataControl
import android.os.Bundle
import android.view.View
import Guide.suchelin.R
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.databinding.FragmentVoteBinding
import android.util.Log
import android.widget.Toast
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
        rvAdapter = VoteRvAdapter(context, items)

        // rvAdatper 설정
        setRvAdapter(items)

        // 필터에 따른 설정
        binding.sortNameVote.setOnClickListener {
            items.sortBy {
                it.name
            }
            rvAdapter.notifyDataSetChanged()

            // rvAdatper 설정
            setRvAdapter(items)
        }

        binding.sortDistanceVote.setOnClickListener {
            items.sortBy {
                it.id
            }

            rvAdapter.notifyDataSetChanged()

            // rvAdatper 설정
            setRvAdapter(items)
        }
        binding.sortScoreVote.setOnClickListener {
            items.apply {
                sortBy { it.score }
                reverse()
            }

            rvAdapter.notifyDataSetChanged()

            // rvAdatper 설정
            setRvAdapter(items)
        }
        /*
        rvAdapter.itemClick = object : VoteRvAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val itemId = items[position].id.toString()
                //sharedpreference에 값이 없으면 정상작동
                if (VoteSharedPreference().getVoteStatement(itemId, requireActivity()) == 0) {
                    val mDialogView =
                        LayoutInflater.from(requireContext()).inflate(R.layout.vote_dialog, null)
                    val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                    val mAlertDialog = mBuilder.show()
                    val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                    var ratingScore = 0
                    mAlertDialog.setCancelable(false);
                    val explain = mAlertDialog.findViewById<TextView>(R.id.explain)
                    explain.visibility = View.VISIBLE
                    var score = 0L;

                    database.child(itemId).get().addOnSuccessListener {
                        Log.i("firebase", "Got value ${it.value}")
                        //오류가 계속 나버려서 미리 파이어베이스 db에 다 id - 0 으로 non null 하게 만들기
                        score = it.value as Long
                        //기존 값 가져오기
                    }.addOnFailureListener {
                        Log.e("firebase", "Error getting data", it)
                    }

                    ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                        score += rating.toLong()
                        //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                        database
                            .child(itemId)
                            .setValue(score)
                        VoteSharedPreference().putVoteStatement(
                            itemId,
                            rating.toInt(),
                            requireActivity()
                        )
                        mAlertDialog.dismiss()
                    }
                } else {
                    Log.d("VoteStatement", "이미 투표한 가게입니다")

                    val mDialogView =
                        LayoutInflater.from(requireContext()).inflate(R.layout.vote_dialog, null)
                    val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                    val mAlertDialog = mBuilder.show()
                    val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                    //저장된 점수 가져오기
                    var savedScore =
                        VoteSharedPreference().getVoteStatement(itemId, requireActivity())
                    val votedMessage = mAlertDialog.findViewById<TextView>(R.id.votedMessage)
                    votedMessage.visibility = View.VISIBLE
                    mAlertDialog.setCancelable(false);

                    var score = 0L;

                    database.child(itemId).get().addOnSuccessListener {
                        Log.i("firebase", "Got value ${it.value}")
                        //오류가 계속 나버려서 미리 파이어베이스 db에 다 id - 0 으로 non null 하게 만들기
                        score = it.value as Long
                        //기존 값 가져오기
                    }.addOnFailureListener {
                        Log.e("firebase", "Error getting data", it)
                    }

                    ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                        //저장된 점수 빼고 새 점수 더하기
                        score = score - savedScore + rating.toLong()
                        //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                        database
                            .child(itemId)
                            .setValue(score)
                        VoteSharedPreference().putVoteStatement(
                            itemId,
                            rating.toInt(),
                            requireActivity()
                        )
                        mAlertDialog.dismiss()
                    }
                }
            }
        }

        binding.sortNameVote.setOnClickListener {
            items.sortBy {
                it.name
            }
            rvAdapter.notifyDataSetChanged()

            rvAdapter.itemClick = object : VoteRvAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    val itemId = items[position].id.toString()
                    //sharedpreference에 값이 없으면 정상작동
                    if (VoteSharedPreference().getVoteStatement(itemId, requireActivity()) == 0) {
                        val mDialogView =
                            LayoutInflater.from(requireContext())
                                .inflate(R.layout.vote_dialog, null)
                        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                        var ratingScore = 0
                        mAlertDialog.setCancelable(false);
                        val explain = mAlertDialog.findViewById<TextView>(R.id.explain)
                        explain.visibility = View.VISIBLE
                        var score = 0L;

                        database.child(itemId).get().addOnSuccessListener {
                            Log.i("firebase", "Got value ${it.value}")
                            //오류가 계속 나버려서 미리 파이어베이스 db에 다 id - 0 으로 non null 하게 만들기
                            score = it.value as Long
                            //기존 값 가져오기
                        }.addOnFailureListener {
                            Log.e("firebase", "Error getting data", it)
                        }

                        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            score += rating.toLong()
                            //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                            database
                                .child(itemId)
                                .setValue(score)
                            VoteSharedPreference().putVoteStatement(
                                itemId,
                                rating.toInt(),
                                requireActivity()
                            )
                            mAlertDialog.dismiss()
                        }
                    } else {
                        Log.d("VoteStatement", "이미 투표한 가게입니다")

                        val mDialogView =
                            LayoutInflater.from(requireContext())
                                .inflate(R.layout.vote_dialog, null)
                        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                        //저장된 점수 가져오기
                        var savedScore =
                            VoteSharedPreference().getVoteStatement(itemId, requireActivity())
                        val votedMessage = mAlertDialog.findViewById<TextView>(R.id.votedMessage)
                        votedMessage.visibility = View.VISIBLE
                        mAlertDialog.setCancelable(false);

                        var score = 0L;

                        database.child(itemId).get().addOnSuccessListener {
                            Log.i("firebase", "Got value ${it.value}")
                            //오류가 계속 나버려서 미리 파이어베이스 db에 다 id - 0 으로 non null 하게 만들기
                            score = it.value as Long
                            //기존 값 가져오기
                        }.addOnFailureListener {
                            Log.e("firebase", "Error getting data", it)
                        }

                        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            //저장된 점수 빼고 새 점수 더하기
                            score = score - savedScore + rating.toLong()
                            //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                            database
                                .child(itemId)
                                .setValue(score)
                            VoteSharedPreference().putVoteStatement(
                                itemId,
                                rating.toInt(),
                                requireActivity()
                            )
                            mAlertDialog.dismiss()
                        }
                    }
                }
            }
            binding.rvVote.adapter = rvAdapter
            binding.rvVote.layoutManager = GridLayoutManager(context, 2)
        }
        binding.sortDistanceVote.setOnClickListener {
            items.sortBy {
                it.id
            }

            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : VoteRvAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    val itemId = items[position].id.toString()
                    //sharedpreference에 값이 0이면 투표를 안한 것 -> 정상작동
                    if (VoteSharedPreference().getVoteStatement(
                            itemId,
                            requireActivity()
                        ) == 0
                    ) {
                        val mDialogView =
                            LayoutInflater.from(requireContext())
                                .inflate(R.layout.vote_dialog, null)
                        val mBuilder =
                            AlertDialog.Builder(requireContext()).setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                        var ratingScore = 0
                        mAlertDialog.setCancelable(false);
                        val explain = mAlertDialog.findViewById<TextView>(R.id.explain)
                        explain.visibility = View.VISIBLE
                        var score = 0L;

                        database.child(itemId).get().addOnSuccessListener {
                            Log.i("firebase", "Got value ${it.value}")
                            //오류가 계속 나버려서 미리 파이어베이스 db에 다 id - 0 으로 non null 하게 만들기
                            score = it.value as Long
                            //기존 값 가져오기
                        }.addOnFailureListener {
                            Log.e("firebase", "Error getting data", it)
                        }

                        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            score += rating.toLong()
                            //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                            database
                                .child(itemId)
                                .setValue(score)
                            VoteSharedPreference().putVoteStatement(
                                itemId,
                                rating.toInt(),
                                requireActivity()
                            )
                            mAlertDialog.dismiss()
                        }
                    } else {
                        Log.d("VoteStatement", "이미 투표한 가게입니다")

                        val mDialogView =
                            LayoutInflater.from(requireContext())
                                .inflate(R.layout.vote_dialog, null)
                        val mBuilder =
                            AlertDialog.Builder(requireContext()).setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                        //저장된 점수 가져오기
                        var savedScore =
                            VoteSharedPreference().getVoteStatement(itemId, requireActivity())
                        val votedMessage =
                            mAlertDialog.findViewById<TextView>(R.id.votedMessage)
                        votedMessage.visibility = View.VISIBLE
                        mAlertDialog.setCancelable(false);

                        var score = 0L;

                        database.child(itemId).get().addOnSuccessListener {
                            Log.i("firebase", "Got value ${it.value}")
                            //오류가 계속 나버려서 미리 파이어베이스 db에 다 id - 0 으로 non null 하게 만들기
                            score = it.value as Long
                            //기존 값 가져오기
                        }.addOnFailureListener {
                            Log.e("firebase", "Error getting data", it)
                        }

                        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            //저장된 점수 빼고 새 점수 더하기
                            score = score - savedScore + rating.toLong()
                            //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                            database
                                .child(itemId)
                                .setValue(score)
                            VoteSharedPreference().putVoteStatement(
                                itemId,
                                rating.toInt(),
                                requireActivity()
                            )
                            mAlertDialog.dismiss()
                        }
                    }
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
            rvAdapter.itemClick = object : VoteRvAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    val itemId = items[position].id.toString()
                    //sharedpreference에 값이 없으면 정상작동
                    if (VoteSharedPreference().getVoteStatement(
                            itemId,
                            requireActivity()
                        ) == 0
                    ) {
                        val mDialogView =
                            LayoutInflater.from(requireContext())
                                .inflate(R.layout.vote_dialog, null)
                        val mBuilder =
                            AlertDialog.Builder(requireContext()).setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                        var ratingScore = 0
                        mAlertDialog.setCancelable(false);
                        val explain = mAlertDialog.findViewById<TextView>(R.id.explain)
                        explain.visibility = View.VISIBLE
                        var score = 0L;

                        database.child(itemId).get().addOnSuccessListener {
                            Log.i("firebase", "Got value ${it.value}")
                            //오류가 계속 나버려서 미리 파이어베이스 db에 다 id - 0 으로 non null 하게 만들기
                            score = it.value as Long
                            //기존 값 가져오기
                        }.addOnFailureListener {
                            Log.e("firebase", "Error getting data", it)
                        }

                        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            score += rating.toLong()
                            //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                            database
                                .child(itemId)
                                .setValue(score)
                            VoteSharedPreference().putVoteStatement(
                                itemId,
                                rating.toInt(),
                                requireActivity()
                            )
                            mAlertDialog.dismiss()
                        }
                    } else {
                        Log.d("VoteStatement", "이미 투표한 가게입니다")

                        val mDialogView =
                            LayoutInflater.from(requireContext())
                                .inflate(R.layout.vote_dialog, null)
                        val mBuilder =
                            AlertDialog.Builder(requireContext()).setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                        //저장된 점수 가져오기
                        var savedScore =
                            VoteSharedPreference().getVoteStatement(itemId, requireActivity())
                        val votedMessage =
                            mAlertDialog.findViewById<TextView>(R.id.votedMessage)
                        votedMessage.visibility = View.VISIBLE
                        mAlertDialog.setCancelable(false);

                        var score = 0L;

                        database.child(itemId).get().addOnSuccessListener {
                            Log.i("firebase", "Got value ${it.value}")
                            //오류가 계속 나버려서 미리 파이어베이스 db에 다 id - 0 으로 non null 하게 만들기
                            score = it.value as Long
                            //기존 값 가져오기
                        }.addOnFailureListener {
                            Log.e("firebase", "Error getting data", it)
                        }

                        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            //저장된 점수 빼고 새 점수 더하기
                            score = score - savedScore + rating.toLong()
                            //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                            database
                                .child(itemId)
                                .setValue(score)
                            VoteSharedPreference().putVoteStatement(
                                itemId,
                                rating.toInt(),
                                requireActivity()
                            )
                            mAlertDialog.dismiss()
                        }
                    }
                }
            }

            binding.rvVote.adapter = rvAdapter
            binding.rvVote.layoutManager = GridLayoutManager(context, 2)
        }

         */
    }

    fun setRvAdapter(items: ArrayList<StoreDataClass>) {
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
}
