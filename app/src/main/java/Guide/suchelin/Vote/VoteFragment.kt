package Guide.suchelin.Vote

import Guide.suchelin.DataClass.StoreScore
import Guide.suchelin.DataControl
import android.os.Bundle
import android.view.View
import Guide.suchelin.R
import Guide.suchelin.StoreDetail.StoreDetailActivity
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.databinding.FragmentVoteBinding
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.widget.RatingBar
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        val database = Firebase.database.reference


        //고유 uid기반으로 데이터가 들어감
//        val myRef = database.getReference("StoreVote").child(Firebase.auth.currentUser!!.uid)
        val items = DataControl().getStoreDataList(requireContext())
        items.sortBy { it.name }
//json 적용 블럭

        val rvAdapter = VoteRvAdapter(context, items)

        binding.rvVote.apply {
            adapter = rvAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

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
                        VoteSharedPreference().putVoteStatement(itemId, rating.toInt(), requireActivity())
                        mAlertDialog.dismiss()
                    }
                } else {
                    Toast.makeText(context, "이미 투표한 가게입니다", Toast.LENGTH_SHORT).show()
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
                            LayoutInflater.from(requireContext()).inflate(R.layout.vote_dialog, null)
                        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                        var ratingScore = 0
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
                            score += rating.toLong()
                            //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                            database
                                .child(itemId)
                                .setValue(score)
                            VoteSharedPreference().putVoteStatement(itemId, rating.toInt(), requireActivity())
                            mAlertDialog.dismiss()
                        }
                    } else {
                        Toast.makeText(context, "이미 투표한 가게입니다", Toast.LENGTH_SHORT).show()
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
                    if (VoteSharedPreference().getVoteStatement(itemId, requireActivity()) == 0) {
                        val mDialogView =
                            LayoutInflater.from(requireContext()).inflate(R.layout.vote_dialog, null)
                        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                        var ratingScore = 0
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
                            score += rating.toLong()
                            //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                            database
                                .child(itemId)
                                .setValue(score)
                            VoteSharedPreference().putVoteStatement(itemId, rating.toInt(), requireActivity())
                            mAlertDialog.dismiss()
                        }
                    } else {
                        Toast.makeText(context, "이미 투표한 가게입니다", Toast.LENGTH_SHORT).show()
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
                    if (VoteSharedPreference().getVoteStatement(itemId, requireActivity()) == 0) {
                        val mDialogView =
                            LayoutInflater.from(requireContext()).inflate(R.layout.vote_dialog, null)
                        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                        val mAlertDialog = mBuilder.show()
                        val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                        var ratingScore = 0
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
                            score += rating.toLong()
                            //sharedpreference로 한번 투표하면 클릭못하게 바꿀예정
                            database
                                .child(itemId)
                                .setValue(score)
                            VoteSharedPreference().putVoteStatement(itemId, rating.toInt(), requireActivity())
                            mAlertDialog.dismiss()
                        }
                    } else {
                        Toast.makeText(context, "이미 투표한 가게입니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding.rvVote.adapter = rvAdapter
            binding.rvVote.layoutManager = GridLayoutManager(context, 2)
        }
    }
}