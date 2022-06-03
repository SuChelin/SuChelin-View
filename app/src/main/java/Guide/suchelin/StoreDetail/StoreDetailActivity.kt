package Guide.suchelin.StoreDetail

import Guide.suchelin.DataControl
import android.os.Bundle
import Guide.suchelin.config.BaseActivity
import Guide.suchelin.databinding.ActivityStoreDetailBinding
import android.provider.ContactsContract
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

class StoreDetailActivity : BaseActivity<ActivityStoreDetailBinding>(ActivityStoreDetailBinding::inflate) {
    private var storeId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storeId = intent.getIntExtra("StoreName", -1)

        init()

        binding.storeDetailBack.setOnClickListener {
            finish()
        }
    }

    private fun init(){
        val data = DataControl().getStoreDetail(baseContext, storeId)
        if(data == null) {
            Toast.makeText(this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        binding.storeDetailTitleTextView.text = data.name
        Glide.with(this)
            .load(data.imageUrl)
            .circleCrop()
            .into(binding.storeDetailImageView)

        // 메뉴 리사이클러뷰 설정
        val menuData = DataControl().getStoreMenu(this, storeId)

        binding.storeDetailMenuRecyclerView.adapter = MenuAdapter(menuData)
        binding.storeDetailMenuRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}