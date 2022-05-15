package mingyuk99.suchelin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import mingyuk99.suchelin.List.ListFragment
import mingyuk99.suchelin.Map.MapsFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //일단 첫 화면 list로 임의 결정
        //bottom_navigaion
        val bottom_menu = findViewById<BottomNavigationView>(R.id.bottomTabBar)

// 첫 화면 중복의 원인이었음
//        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView, mapsFragment).commit()

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
}
