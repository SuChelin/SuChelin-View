package mingyuk99.suchelin.SuperDetail

import android.os.Bundle
import mingyuk99.suchelin.config.BaseActivity
import mingyuk99.suchelin.databinding.ActivitySuperDetailBinding

class SuperDetailActivity : BaseActivity<ActivitySuperDetailBinding>(ActivitySuperDetailBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val superName = intent.getStringExtra("SuperName") ?: ""

        binding.superNameTextView.text = superName
    }
}