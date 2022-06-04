package Guide.suchelin.Vote

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class VoteSharedPreference {
    companion object {
        const val sharedPrefFile = "vote_statement"
    }

    private lateinit var mPreferences: SharedPreferences

    fun putVoteStatement(id: String, score: Int, context: Context) {
        mPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
        //id 값 받아서 true로 변경
        preferencesEditor.putInt(id, score)
        Log.d("sharedPref", preferencesEditor.putInt(id, score).toString())
        //commit은 sync, apply는 async 적으로 동작함
        preferencesEditor.apply()
    }

    fun getVoteStatement(id: String, context: Context): Int {
        mPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        Log.d("sharedPref", mPreferences.getInt(id, 0).toString())
        val savedScore = mPreferences.getInt(id, 0)
        return savedScore
    }
}