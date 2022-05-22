package Guide.suchelin.StoreDetail

import android.os.Bundle
import Guide.suchelin.config.BaseActivity
import Guide.suchelin.databinding.ActivityStoreDetailBinding
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide

class StoreDetailActivity : BaseActivity<ActivityStoreDetailBinding>(ActivityStoreDetailBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        intent.putExtra("name", items[position].name)
//        intent.putExtra("imageUrl", items[position].imageUrl)
//        intent.putExtra("detail", items[position].detail)
//        intent.putExtra("score", items[position].score)
//
        val storeName = intent.getStringExtra("StoreName") ?: intent.getStringExtra("name")
        val nameFromList = intent.getStringExtra("name") ?: ""
        val imageUrlFromList = intent.getStringExtra("imageUrl") ?: ""
        val detailFromList = intent.getStringExtra("detail") ?: ""
        val scoreFromList = intent.getIntExtra("score", 0)

        Glide
            .with(this)
            .load(imageUrlFromList)
            .centerCrop()
            .into(binding.imgStoreInside)

        binding.superNameTextView.text = storeName

        when(scoreFromList){
            1 -> {
                binding.oneStarLayout.visibility = View.VISIBLE
                binding.twoStarLayout.visibility = View.INVISIBLE
                binding.threeStarLayout.visibility = View.INVISIBLE
            }
            2 -> {
                binding.oneStarLayout.visibility = View.INVISIBLE
                binding.twoStarLayout.visibility = View.VISIBLE
                binding.threeStarLayout.visibility = View.INVISIBLE
            }
            3 -> {
                binding.oneStarLayout.visibility = View.INVISIBLE
                binding.twoStarLayout.visibility = View.INVISIBLE
                binding.threeStarLayout.visibility = View.VISIBLE
            }
        }

        //즐겨찾기 기능이 필요한지? 익명인증을 이용해서 고유 UUID로 북마크를 이용 -> 파이어베이스 부하 좀 걸릴수도




    }
}