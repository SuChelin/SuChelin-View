package Guide.suchelin

import Guide.suchelin.DataClass.*
import Guide.suchelin.List.ListFragment
import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONTokener
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class DataControl {
    private lateinit var database: DatabaseReference
    private val allScores = hashMapOf<String, Long>()

    companion object {
        private const val FILTER_NAME = 1
        private const val FILTER_GRADE = 2
        private const val FILTER_NEW = 3
        const val STORE_JSON_LENGTH = 40
    }

    // assets 파일 읽어오기
    private fun readFile(fileName: String, context: Context): String? {
        var data: String? = null

        val inputStream: InputStream = context.assets.open(fileName)
        val byteArrayOutputStream = ByteArrayOutputStream()

        var i: Int
        try {
            i = inputStream.read()
            while (i != -1) {
                byteArrayOutputStream.write(i)
                i = inputStream.read()
            }
            data = String(byteArrayOutputStream.toByteArray())
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return data
    }


    fun getStoreDataMap(context: Context): ArrayList<StoreDataClassMap> {
        // 식당 데이터
        val data = readFile("StoreData.json", context)
        val storeDataMap = ArrayList<StoreDataClassMap>()

        val jsonArray = JSONTokener(data).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            storeDataMap.add(
                StoreDataClassMap(
                    jsonArray.getJSONObject(i).getInt("id"),
                    jsonArray.getJSONObject(i).getString("imageUrl"),
                    jsonArray.getJSONObject(i).getString("name"),
                    jsonArray.getJSONObject(i).getString("detail"),
                    jsonArray.getJSONObject(i).getDouble("latitude"),
                    jsonArray.getJSONObject(i).getDouble("longitude")
                )
            )
        }

        return storeDataMap
    }

    fun getStoreDataList(context: Context): ArrayList<StoreDataClass> {

        // 식당 데이터
        val data = readFile("StoreData.json", context)
        val storeData = ArrayList<StoreDataClass>()
        //여기서 점수가 필요한가? getStoreDataList -> VoteFragment에서 사용중
        val scr = 0

        val jsonArray = JSONTokener(data).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val id = jsonArray.getJSONObject(i).getInt("id")
            val imageUrl = jsonArray.getJSONObject(i).getString("imageUrl")
            val name = jsonArray.getJSONObject(i).getString("name")
            val detail = jsonArray.getJSONObject(i).getString("detail")

            /*
            * id 값으로 점수 값 받아오기. key-value가 id-score로 되게
            * */
            storeData.add(StoreDataClass(id, imageUrl, name, detail, scr, scr))
        }

        return storeData
    }
    //ListFragment에 사용중
    fun getStoreDataScoreList(context: Context, score: HashMap<String, Long>): ArrayList<StoreDataScoreClass> {
        // 식당 데이터
        val data = readFile("StoreData.json", context)
        val storeData = ArrayList<StoreDataScoreClass>()

        val jsonArray = JSONTokener(data).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val id = jsonArray.getJSONObject(i).getInt("id")
            val imageUrl = jsonArray.getJSONObject(i).getString("imageUrl")
            val name = jsonArray.getJSONObject(i).getString("name")
            val detail = jsonArray.getJSONObject(i).getString("detail")
            /*
            * id 값으로 점수 값 받아오기. key-value가 id-score로 되게
            * */
            val scr = score.getValue(id.toString())
            storeData.add(StoreDataScoreClass(id, imageUrl, name, detail, scr,))
        }

        return storeData
    }

    fun getStoreDetail(context: Context, storeId: Int): StoreDataClass? {
        val tmpScore = mutableListOf<StoreScore>()

        val range = (1..3)
        for (i in 1..STORE_JSON_LENGTH) {
            tmpScore.add(StoreScore(i, range.random()))
        }

        // 식당 데이터
        val data = readFile("StoreData.json", context)

        val jsonArray = JSONTokener(data).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val id = jsonArray.getJSONObject(i).getInt("id")

            if (id == storeId) {
                val scr = tmpScore[id - 1].score
                return StoreDataClass(
                    id,
                    jsonArray.getJSONObject(i).getString("imageUrl"),
                    jsonArray.getJSONObject(i).getString("name"),
                    jsonArray.getJSONObject(i).getString("detail"),
                    scr
                )
            }
        }

        return null
    }
    //테스트중
    fun getStoreScoreDetail(context: Context, storeId: Int, score: Long): StoreDataScoreClass? {
        // 식당 데이터
        val data = readFile("StoreData.json", context)

        val jsonArray = JSONTokener(data).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val id = jsonArray.getJSONObject(i).getInt("id")

            if (id == storeId) {
                return StoreDataScoreClass(
                    id,
                    jsonArray.getJSONObject(i).getString("imageUrl"),
                    jsonArray.getJSONObject(i).getString("name"),
                    jsonArray.getJSONObject(i).getString("detail"),
                    score
                )
            }
        }

        return null
    }

    fun getStoreMenu(context: Context, storeId: Int): ArrayList<StoreMenuClass> {
        val storeMenuList = ArrayList<StoreMenuClass>()

        // 메뉴 데이터 가져오기
        val data = readFile("StoreMenu.json", context)

        val jsonArray = JSONTokener(data).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val id = jsonArray.getJSONObject(i).getInt("id")

            if (id == storeId) {
                val menu = JSONTokener(
                    jsonArray.getJSONObject(i).getString("menu")
                ).nextValue() as JSONArray

                for (k in 0 until menu.length()) {
                    storeMenuList.add(
                        StoreMenuClass(
                            menu.getJSONObject(k).getString("menuName"),
                            menu.getJSONObject(k).getString("menuPrice")
                        )
                    )
                }

                break
            }
        }
        // Log.d("menuData", "menu : $storeMenuList")
        return storeMenuList
    }

    fun scoreFromFirebase(fragment: ListFragment?) {
        var flag = true
        // 데이터 초기화
        for (id in 1 until STORE_JSON_LENGTH+1) {
            if (allScores.get(key = id.toString()) == null) {
                allScores[id.toString()] = 0
            }
        }

        val database = Firebase.database
        val databaseRef = database.reference

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (id in snapshot.children) {
                    allScores.put(id.key!!, id.value!! as Long)
                }

                // 리스트 설정
                Log.d("score: ", allScores.toString())
                if(flag){
                    fragment?.setListAdapter(allScores = allScores)
                    flag = false
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("dataRef", "DB error")
            }
        })
    }
}