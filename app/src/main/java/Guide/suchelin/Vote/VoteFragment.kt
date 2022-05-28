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
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.RatingBar
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

        val database = Firebase.database.reference

        //고유 uid기반으로 데이터가 들어감
//        val myRef = database.getReference("StoreVote").child(Firebase.auth.currentUser!!.uid)
        val items = DataControl().getStoreDataList(requireContext())
        items.sortBy { it.name }
//json 적용 블럭

        val rvAdapter = VoteRvAdapter(context, items)

        rvAdapter.itemClick = object : VoteRvAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.vote_dialog, null)
                val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                var ratingScore = 0
                mAlertDialog.setCancelable(false);
                ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    ratingScore = rating.toInt()

                    database
                        .child(Firebase.auth.currentUser!!.uid)
                        .child(items[position].id.toString())
                        .setValue(ratingScore)

                    mAlertDialog.dismiss()
//                    Log.d("Vote", "" + ratingScore)
                }
            }
        }

        binding.rvVote.apply {
            adapter = rvAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        binding.sortNameVote.setOnClickListener {
            items.sortBy {
                it.name
            }
            rvAdapter.notifyDataSetChanged()
            rvAdapter.itemClick = object : VoteRvAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.vote_dialog, null)
                    val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                    val mAlertDialog = mBuilder.show()
                    val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                    var ratingScore = 0
                    mAlertDialog.setCancelable(false);
                    ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                        ratingScore = rating.toInt()

                        database
                            .child(Firebase.auth.currentUser!!.uid)
                            .child(items[position].id.toString())
                            .setValue(ratingScore)

                        mAlertDialog.dismiss()
//                    Log.d("Vote", "" + ratingScore)
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
                    val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.vote_dialog, null)
                    val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                    val mAlertDialog = mBuilder.show()
                    val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                    var ratingScore = 0
                    mAlertDialog.setCancelable(false);
                    ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                        ratingScore = rating.toInt()
                        database
                            .child(Firebase.auth.currentUser!!.uid)
                            .child(items[position].id.toString())
                            .setValue(ratingScore)
                        mAlertDialog.dismiss()
//                    Log.d("Vote", "" + ratingScore)
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
                    val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.vote_dialog, null)
                    val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                    val mAlertDialog = mBuilder.show()
                    val ratingBar = mAlertDialog.findViewById<RatingBar>(R.id.rating)
                    var ratingScore = 0
                    mAlertDialog.setCancelable(false);
                    ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                        ratingScore = rating.toInt()
                        database
                            .child(Firebase.auth.currentUser!!.uid)
                            .child(items[position].id.toString())
                            .setValue(ratingScore)
                        mAlertDialog.dismiss()
//                    Log.d("Vote", "" + ratingScore)
                    }
                }
            }

            binding.rvVote.adapter = rvAdapter
            binding.rvVote.layoutManager = GridLayoutManager(context, 2)
        }
    }
}