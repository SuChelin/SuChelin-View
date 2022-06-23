package Guide.suchelin.StoreDetail

import Guide.suchelin.DataControl
import Guide.suchelin.R
import android.os.Bundle
import Guide.suchelin.config.BaseActivity
import Guide.suchelin.databinding.ActivityStoreDetailBinding
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class StoreDetailActivity : BaseActivity<ActivityStoreDetailBinding>(ActivityStoreDetailBinding::inflate) {
    private var storeId: Int = -1
    private var score: Long = 0
    var michelin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        storeId = intent.getIntExtra("StoreName", -1)
        score = intent.getLongExtra("Score",0)
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
        //테스트
        //test
        //5점이상이면 별 세개
        //5~3이면 별 두개
        //1~2면 별 한개
        //0 이면 별 없음
        Log.d("BackImage",score.toLong().toString())
        michelin = when(score.toInt()){
            5 -> 3
            in 3..4 -> 2
            in 1..2 -> 1
            else -> 0
        }
        binding.storeDetailBackgroundImageView.visibility =
            if(michelin == 0) View.GONE
            else {
                Log.d("BackImage",setMichelinBackgroundImage(michelin)!!.toString())
                binding.storeDetailBackgroundImageView.setImageResource(setMichelinBackgroundImage(michelin)!!)
                View.VISIBLE
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

    private fun setMichelinBackgroundImage(michelin: Int): Int?{
        return when(michelin){
            1 -> R.drawable.ic_michelin_one_background
            2 -> R.drawable.ic_michelin_two_background
            3 -> R.drawable.ic_michelin_three_background
            else -> null
        }
    }
}