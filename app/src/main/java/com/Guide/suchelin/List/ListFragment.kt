package Guide.suchelin.List

import android.app.AlertDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Guide.suchelin.ContactActivity
import com.Guide.suchelin.DataClass.StoreDataClass
import com.Guide.suchelin.DataClass.StoreDataScoreClass
import com.Guide.suchelin.List.RvAdapter
import com.Guide.suchelin.R
import com.Guide.suchelin.StoreDetail.StoreDetailActivity
import com.Guide.suchelin.Vote.VoteRvAdapter
import com.Guide.suchelin.config.BaseFragment
import com.Guide.suchelin.config.MyApplication
import com.Guide.suchelin.databinding.FragmentListBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.android.awaitFrame
import java.lang.Runnable

class ListFragment : BaseFragment<FragmentListBinding>(
    FragmentListBinding::bind,
    R.layout.fragment_list
) {
    companion object {
        private const val FILTER_NAME = 1
        private const val FILTER_GRADE = 2
        private const val FILTER_NEW = 3
    }
    val topThreeId = ArrayList<Int>()

    // 그림자 효과
    private var scrolledY = 0
    // list item
    private var items = ArrayList<StoreDataScoreClass>()
    // job
    private var job : Job? = null
    private var rvAdapter : RvAdapter? = null
    private var finishFlag = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 로딩중 보여주기
        changeLoadingContent(true)

        // 초기화
        init()

        binding.listSearchImageView.setOnClickListener {
            binding.listDefaultBar.visibility = View.GONE
            binding.listSearchBar.visibility = View.VISIBLE

        }

        binding.voteSearchEditTextView.setOnKeyListener{ v, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                //엔터키 누르면 검색되게
                binding.listSearchImageViewClicked.performClick()
            }
            true
        }


        //고객센터
        binding.listIvContact.setOnClickListener {
            val intent = Intent(context, ContactActivity::class.java)
            startActivity(intent)
        }
        //info
        binding.listIvInfo.setOnClickListener {
            //dialog
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_info, null)
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setTitle("앱소개")
            val mAlertDialog = mBuilder.show()
        }
    }

    private fun init() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if(MyApplication.dataControl.initFlag()){
                job = CoroutineScope(Dispatchers.Main).launch {
                    // adapter 설정
                    setAdapter(MyApplication.dataControl.allScores)

                    // 필터 설정
                    setFilter()

                    // loading 없애기
                    changeLoadingContent(false)
                }
            } else {
                Log.d("dataControl", "init 실행됨")
                if(!finishFlag) init()
            }
        }, 300)
    }

    private fun setAdapter(allScores: HashMap<String, Long>) {
        items = MyApplication.dataControl.getStoreDataScoreList(MyApplication.ApplicationContext(), allScores)

        items.apply {
            sortBy { it.score }
            reverse()
        }
        topThreeId.add(items[0].id)
        topThreeId.add(items[1].id)
        topThreeId.add(items[2].id)

        items.sortBy { it.name }

        rvAdapter = RvAdapter(context, items, topThreeId)

        rvAdapter?.itemClick = object : RvAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Log.d("ClickedItem","${items[position]}")

                val intent = Intent(context, StoreDetailActivity::class.java)
                intent.putExtra("StoreName", items[position].name)
                intent.putExtra("StoreId", items[position].id)
                intent.putExtra("score", items[position].score)
                intent.putExtra("latitude", items[position].latitude)
                intent.putExtra("longitude", items[position].longitude)
                startActivity(intent)
            }
        }

        binding.rv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrolledY += dy

                if (scrolledY > 5 && binding.listShadowView.visibility == View.GONE) {
                    binding.listShadowView.visibility = View.VISIBLE
                } else if (scrolledY < 5 && binding.listShadowView.visibility == View.VISIBLE) {
                    binding.listShadowView.visibility = View.GONE
                }
            }
        })

        binding.listSearchImageViewClicked.setOnClickListener {
            binding.listDefaultBar.visibility = View.VISIBLE
            binding.listSearchBar.visibility = View.GONE
            //검색버튼 누르면 키보드 내려가게
            var imm: InputMethodManager? = null
            imm = requireActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(it.windowToken,0)

            val searchText = binding.voteSearchEditTextView.text.toString()
            val searchItem = mutableListOf<StoreDataScoreClass>()
            binding.voteSearchEditTextView.text.clear()
            var searchComplete = false

            //adapter 설정해야됨
            //일부만 같으려면 contains, 완전히 같으려면 eqauls나 contentEquals
            for(i in items.indices){
                if(items[i].name.contains(searchText,true)){
                    searchItem.add(items[i])
                    searchComplete = true
                }
            }
            Log.d("ListSearch","${searchItem}")
            //searchitem이 잘 들어오긴함.
            val searchAdapter = RvAdapter(context, searchItem as ArrayList<StoreDataScoreClass>, topThreeId)

            searchAdapter?.itemClick = object : RvAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    Log.d("ClickedItemSearch","${searchItem[position]}")
                    val intent = Intent(context, StoreDetailActivity::class.java)
                    intent.putExtra("StoreName", searchItem[position].name)
                    intent.putExtra("StoreId", searchItem[position].id)
                    intent.putExtra("score", searchItem[position].score)
                    intent.putExtra("latitude", searchItem[position].latitude)
                    intent.putExtra("longitude", searchItem[position].longitude)
                    CoroutineScope(Dispatchers.Main).launch {
                        startActivity(intent)
                        delay(500)
                        binding.rv.apply {
                            adapter = rvAdapter
                            layoutManager = LinearLayoutManager(context)
                        }
                    }
                }
            }

            binding.rv.apply {
                adapter = searchAdapter
                layoutManager = LinearLayoutManager(context)
            }


//            rvAdapter = VoteRvAdapter(context, searchItem)
//            setRvAdapter(searchItem as ArrayList<StoreDataClass>)

            if(searchComplete==false){
                Toast.makeText(context, "검색결과가 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeLoadingContent(flag: Boolean) {
        if(flag){
            // loading 띄우기
            binding.listContentParent.visibility = View.GONE
            binding.listLoadingParent.visibility = View.VISIBLE
        } else {
            // content 내용 보여주기
            binding.listContentParent.visibility = View.VISIBLE
            binding.listLoadingParent.visibility = View.GONE
        }
    }

    private fun setFilter() {
        binding.listFilterNameTextView.setOnClickListener {
            // 필터 바꾸기
            changeFilter(FILTER_NAME)

            binding.rv.apply {
                adapter = rvAdapter
                layoutManager = LinearLayoutManager(context)
            }

            items.sortBy {
                it.name
            }
            // Log.d("score storedfilter name: ", allScores.toString())
            rvAdapter?.refreshItems(items)
        }
        binding.listFilterDistanceTextView.setOnClickListener {
            // 필터 바꾸기
            changeFilter(FILTER_NEW)
            //신규니까 id가 큰게 나중에 추가된 가게

            binding.rv.apply {
                adapter = rvAdapter
                layoutManager = LinearLayoutManager(context)
            }

            items.apply {
                sortBy {
                    it.id
                }
                reverse()
            }
            // Log.d("score storedfilter new: ", allScores.toString())

            rvAdapter?.refreshItems(items)
        }

        binding.listFilterGradeTextView.setOnClickListener {
            // 필터 바꾸기
            changeFilter(FILTER_GRADE)

            binding.rv.apply {
                adapter = rvAdapter
                layoutManager = LinearLayoutManager(context)
            }

            items.apply {
                sortBy { it.score }
                reverse()
            }

            val topThreeId = ArrayList<Int>()
            topThreeId.add(items[0].id)
            topThreeId.add(items[1].id)
            topThreeId.add(items[2].id)

            rvAdapter?.refresh(items, topThreeId)
        }
    }

    private fun changeFilter(filter: Int) {
        when (filter) {
            FILTER_NAME -> {
                binding.listFilterGradeTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.listFilterNameTextView.setBackgroundResource(R.drawable.filter_check_box)
                binding.listFilterDistanceTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
            }
            FILTER_GRADE -> {
                binding.listFilterGradeTextView.setBackgroundResource(R.drawable.filter_check_box)
                binding.listFilterNameTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.listFilterDistanceTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
            }
            FILTER_NEW -> {
                binding.listFilterGradeTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.listFilterNameTextView.setBackgroundResource(R.drawable.filter_uncheck_box)
                binding.listFilterDistanceTextView.setBackgroundResource(R.drawable.filter_check_box)
            }
        }

        scrolledY = 0
    }

    override fun onDestroy() {
        job?.cancel()
        finishFlag = true
        super.onDestroy()
    }
}