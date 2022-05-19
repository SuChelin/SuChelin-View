package Guide.suchelin.Vote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Guide.suchelin.R
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.databinding.FragmentMapBinding
import Guide.suchelin.databinding.FragmentVoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class VoteFragment : BaseFragment<FragmentVoteBinding>(
    FragmentVoteBinding::bind,
    R.layout.fragment_vote
) {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

    }
}