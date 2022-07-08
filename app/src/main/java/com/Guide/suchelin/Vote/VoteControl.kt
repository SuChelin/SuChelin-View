package com.Guide.suchelin.Vote

import android.util.Log
import com.Guide.suchelin.Vote.VoteDialog
import com.Guide.suchelin.Vote.VoteFragment
import com.Guide.suchelin.Vote.VoteRvAdapter
import com.Guide.suchelin.Vote.VoteSharedPreference
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.*

class VoteControl(
    val fragment: VoteFragment,
    val database: DatabaseReference,
    val storeId: Int,
    val storeName: String,
    val rvAdapter: VoteRvAdapter
) {
    // 다이얼로그
    private val voteDialog: VoteDialog = VoteDialog(storeName, this)
    private var score: Long? = null
    private var jobArray = ArrayList<Job>()

    private fun getStoreVoteSource() {
        database.child(storeId.toString()).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            //오류가 계속 나버려서 미리 파이어베이스 db에 다 id - 0 으로 non null 하게 만들기
            if (score != null) {
                // 데이터베이스 동기화 전에 addStoreVoteToDatabase 가 실행된 경우
                jobArray.add(CoroutineScope(Dispatchers.Default).launch {
                    addStoreVoteToDatabase(it.value as Long)
                })
            } else {
                score = it.value as Long? ?: 0L
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    // SharedPreference 에 가게 투표 점수 저장하기
    private fun saveSharedStoreVote(ratingValue: Int) {
        VoteSharedPreference().putVoteStatement(
            storeId.toString(),
            ratingValue,
            fragment.requireContext()
        )
    }

    private suspend fun addStoreVoteToDatabase(addValue: Long) {
        if (score == null) {
            // 아직 데이터베이스 동기화 안됨
            score = addValue
        } else {
            // 데이터베이스 동기화 완료
            score = score!! + addValue

            // 데이터베이스에 추가
            database
                .child(storeId.toString())
                .setValue(score)
        }
    }

    fun showDialog() {
        voteDialog.show(fragment.requireActivity().supportFragmentManager, "VoteDialog")

        // 데이터베이스 동기화
        getStoreVoteSource()
    }

    // 투표 다이얼로그에서 투표를 완료했을 경우
    fun changeStoreVote(ratingValue: Int) {
        // 데이터베이스 동기화
        jobArray.add(CoroutineScope(Dispatchers.Default).launch {
            addStoreVoteToDatabase(ratingValue.toLong())
        })
        rvAdapter.notifyDataSetChanged()

        // 파일에 투표값 저장
        saveSharedStoreVote(ratingValue)
    }
}