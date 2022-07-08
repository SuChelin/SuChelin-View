package com.Guide.suchelin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import Guide.suchelin.List.ListFragment
import com.Guide.suchelin.Map.MapsFragment
import com.Guide.suchelin.Vote.VoteFragment
import com.Guide.suchelin.config.BaseActivity
import com.Guide.suchelin.config.MyApplication
import com.Guide.suchelin.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //bottom_navigaion
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomTabBar)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ListFragment()).commitAllowingStateLoss()

        bottomMenu.setOnItemSelectedListener {
            getFragment(it)
        }
        bottomMenu.setOnItemReselectedListener {
            Log.d("Main","Menu Reselected")
        }

        // 데이터베이스 초기화
        MyApplication.dataControl.scoreFromFirebase()
    }

    private fun getFragment(menuItem: MenuItem): Boolean {

        when(menuItem.itemId){
            R.id.menuList -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ListFragment()).commitAllowingStateLoss()
            }
            R.id.menuMap -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, MapsFragment()).commitAllowingStateLoss()
            }
            else -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, VoteFragment()).commitAllowingStateLoss()
            }
        }
        return true
    }
    private var doubleClicked = false

    override fun onBackPressed() {
        Log.d("Main", "BackPressed")
        if (doubleClicked == true){
            finish()
        }
        doubleClicked = true
        Toast.makeText(this, "한 번 더 뒤로가면 종료됩니다",Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleClicked = false
        }, 1500)
    }
}
