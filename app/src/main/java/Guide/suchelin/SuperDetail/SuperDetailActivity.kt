package Guide.suchelin.SuperDetail

import android.os.Bundle
import Guide.suchelin.config.BaseActivity
import Guide.suchelin.databinding.ActivitySuperDetailBinding

class SuperDetailActivity : BaseActivity<ActivitySuperDetailBinding>(ActivitySuperDetailBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val superName = intent.getStringExtra("SuperName") ?: ""

        binding.superNameTextView.text = superName
    }
}