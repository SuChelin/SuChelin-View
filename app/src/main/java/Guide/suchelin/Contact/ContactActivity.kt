package Guide.suchelin.Contact

import Guide.suchelin.config.BaseActivity
import Guide.suchelin.databinding.ActivityContactBinding
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri

class ContactActivity :BaseActivity<ActivityContactBinding>(ActivityContactBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.contactBack.setOnClickListener {
            Log.d("Contact", "Backbtn pressed")
            finish()
        }
        var tag = ""
        binding.contactRg.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                binding.contactReport.id ->{
                    tag = binding.contactReport.text.toString()
//                    Log.d("Rg", i.toString() + "${tag}")
                }
                binding.contactNewStore.id ->{
                    tag = binding.contactNewStore.text.toString()
//                    Log.d("Rg", i.toString() + "${tag}")
                }
            }
        }

        //edittext내용을 이메일 전송
        binding.contactBtnSend.setOnClickListener{
            //이메일 전송
            if(tag.isEmpty()){
                Toast.makeText(this, "문의 유형을 선택해주세요", Toast.LENGTH_SHORT).show()
            }else{
                goEmail(binding.contactEt.text.toString(), tag)
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun goEmail(content: String, tag: String) {
        val emailAddress = "mingyuk1999@gmail.com"
        val title = "[${tag}] 수슐랭 문의입니다"
        startActivity(Intent(Intent.ACTION_SEND).apply {
            selector = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
            }
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, content)
        })
    }
}