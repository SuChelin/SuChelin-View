package Guide.suchelin.StoreDetail

import Guide.suchelin.DataControl
import Guide.suchelin.MapStore.MapStoreActivity
import Guide.suchelin.R
import android.os.Bundle
import Guide.suchelin.config.BaseActivity
import Guide.suchelin.databinding.ActivityStoreDetailBinding
import android.content.Intent
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
    private var storeName: String = ""
    private var score: Long = 0
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    var michelin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        storeId = intent.getIntExtra("StoreId", -1)
        storeName = intent.getStringExtra("StoreName") ?: ""
        score = intent.getLongExtra("score",0)
        latitude = intent.getDoubleExtra("latitude", 37.214185)
        longitude = intent.getDoubleExtra("longitude", 126.978792)

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
            in 10..100 -> 3
            in 5..9 -> 2
            in 1..4 -> 1
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

        // 주소 보기
        binding.storeDetailAddressTitleTextView.setOnClickListener {
            Log.d("naverM", "store : ${storeName}, $score $latitude $longitude")
            startActivity(Intent(this, MapStoreActivity::class.java).apply {
                putExtra("StoreName", storeName)
                putExtra("Score", score)
                putExtra("latitude", latitude)
                putExtra("longitude", longitude)
            })
        }
        binding.marker.setOnClickListener {
            binding.storeDetailAddressTitleTextView.performClick()
        }
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