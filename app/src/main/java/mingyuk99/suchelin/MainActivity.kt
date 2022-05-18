package mingyuk99.suchelin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import mingyuk99.suchelin.List.ListFragment
import mingyuk99.suchelin.Map.MapsFragment
import mingyuk99.suchelin.Vote.VoteFragment
import mingyuk99.suchelin.config.BaseActivity
import mingyuk99.suchelin.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //일단 첫 화면 list로 임의 결정
        //bottom_navigaion
        val bottom_menu = findViewById<BottomNavigationView>(R.id.bottomTabBar)

        bottom_menu.setOnItemSelectedListener {
            getFragment(it)
        }
        bottom_menu.setOnItemReselectedListener {
            Log.d("Reselcted","true")
        }
    }

    private fun getFragment(menuItem: MenuItem): Boolean {

        when(menuItem.itemId){
            R.id.menuList -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ListFragment()).commit()
            }
            R.id.menuMap -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, MapsFragment()).commit()
            }
            else -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, VoteFragment()).commit()
            }
        }
        return true
    }
    private var doubleClicked = false

    override fun onBackPressed() {
        Log.d("Main", "Back pressed")
        if (doubleClicked == true){
            finish()
        }
        doubleClicked = true
        Toast.makeText(this, "한 번 더 뒤로가면 종료됩니다",Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleClicked = false
        }, 1500)
    }
}
