package mingyuk99.suchelin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //일단 첫 화면 list로 임의 결정
        //bottom_navigaion

        val bottom_menu = findViewById<BottomNavigationView>(R.id.bottomTabBar)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView, ListFragment()).commit()

        bottom_menu.setOnItemSelectedListener{
            replaceFragment(
                when(it.itemId){
                    R.id.menuList -> ListFragment()
                    R.id.menuMap -> MapFragment()
                    else -> VoteFragment()
                }
            )
        }
    }
    private fun replaceFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragment).commit()
        return true
    }
}
