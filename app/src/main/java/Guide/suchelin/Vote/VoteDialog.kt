package Guide.suchelin.Vote

import Guide.suchelin.databinding.DialogVoteBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

class VoteDialog(val storeTitle: String, val voteControl: VoteControl) : DialogFragment() {
    private lateinit var binding : DialogVoteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogVoteBinding.inflate(layoutInflater)
        // 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        isCancelable = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 가게 이름 설정
        val storeName = "\u003C ${storeTitle} \u003E"
        binding.voteDialogStoreTitleTextView.text = storeName

        // 취소하기
        binding.voteDialogCancelImageView.setOnClickListener {
            dismiss()
        }

        // 투표 완료
        binding.voteDialogCompleteTextView.setOnClickListener {
            val ratingValue = binding.rating.rating.toInt()
            Log.d("votevalue", "ratingValue : ${ratingValue}")

            // 투표 정보 바꾸기
            if(ratingValue > 0){
                voteControl.changeStoreVote(ratingValue)
            }

            // 다이얼로그 종료하기
            dismiss()
        }
    }

}