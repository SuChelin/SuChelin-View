package mingyuk99.suchelin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListToMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_to_map)

        val btnGoToList = findViewById<FloatingActionButton>(R.id.btnGoToList)

        btnGoToList.setOnClickListener {
            finish()
        }
    }
}