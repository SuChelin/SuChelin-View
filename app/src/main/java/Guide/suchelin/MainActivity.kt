package Guide.suchelin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import Guide.suchelin.List.ListFragment
import Guide.suchelin.Map.MapsFragment
import Guide.suchelin.Vote.VoteFragment
import Guide.suchelin.config.BaseActivity
import Guide.suchelin.databinding.ActivityMainBinding
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


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
