package com.Guide.suchelin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SpalshActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mPreferences: SharedPreferences

    companion object {
        const val sharedPrefFile = "datetime"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)
        //deprecated method solved
        auth = Firebase.auth

        try {
            Log.d("Splash", auth.currentUser!!.uid)
            Handler(Looper.getMainLooper()).postDelayed({
                val previousDate = getDate()
//        val previousDate = "2022-09-01"
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ISO_DATE
                val formatted = current.format(formatter)
//                testcode
//                val format = "2022-11-02"
                if (previousDate != formatted) {
                    // 날짜가 바뀐 것임.
                    saveDate(formatted)
                    Log.d("CurrentDate", "$formatted 날짜바뀜")
                    // 랜덤추천 액티비티로 이동시킴
                    startActivity(Intent(this, RandomActivity::class.java))
                    finish()
                } else {
                    //이전 날짜가 없다는 의미이므로 지금날짜 저장해줌.
                    if (previousDate.isNullOrEmpty()) {
                        Log.d("CurrentDate", "$formatted 비어있음")
                        saveDate(formatted)
                        //testcode
//                        startActivity(Intent(this, RandomActivity::class.java))
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else{
                        Log.d("CurrentDate", "$formatted 날짜가 동일")
                        //testcode
//                        startActivity(Intent(this, RandomActivity::class.java))
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }, 400)
        } catch (e: Exception) {
            Log.d("Splash", "need to sign in")
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        Handler(Looper.getMainLooper()).postDelayed({
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }, 400)

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, "로그인 실패.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun saveDate(date: String) {
        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
        preferencesEditor.putString("date", date)
        Log.d("sharedPref", preferencesEditor.putString("date", date).toString())
        //commit은 sync, apply는 async 적으로 동작함
        preferencesEditor.apply()
    }

    private fun getDate(): String? {
        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        Log.d("sharedPref", mPreferences.getString("date","").toString())
        return mPreferences.getString("date", "")
    }
}