package Guide.suchelin

import Guide.suchelin.DataClass.StoreDataClass
import Guide.suchelin.DataClass.StoreDataClassMap
import Guide.suchelin.DataClass.StoreMenuClass
import Guide.suchelin.DataClass.StoreScore
import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONTokener
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class DataControl {

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


    fun getStoreDataMap(context: Context): ArrayList<StoreDataClassMap>{
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

    fun getStoreDataList(context: Context): ArrayList<StoreDataClass>{

        val tmpScore = mutableListOf<StoreScore>()

        val range = (1..3)
        for(i in 1..16){
            tmpScore.add(StoreScore(i,range.random()))
        }

        // 식당 데이터
        val data = readFile("StoreData.json", context)
        val storeData = ArrayList<StoreDataClass>()

        val jsonArray = JSONTokener(data).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val id = jsonArray.getJSONObject(i).getInt("id")
            val imageUrl = jsonArray.getJSONObject(i).getString("imageUrl")
            val name = jsonArray.getJSONObject(i).getString("name")
            val detail = jsonArray.getJSONObject(i).getString("detail")
            /*
            * id 값으로 점수 값 받아오기. key-value가 id-score로 되게
            * */
            val scr = tmpScore[id-1].score
            storeData.add(StoreDataClass(id, imageUrl, name, detail, scr))
        }

        return storeData
    }
    fun getStoreDetail(context: Context, storeId: Int): StoreDataClass?{
        val tmpScore = mutableListOf<StoreScore>()

        val range = (1..3)
        for(i in 1..16){
            tmpScore.add(StoreScore(i,range.random()))
        }

        // 식당 데이터
        val data = readFile("StoreData.json", context)

        val jsonArray = JSONTokener(data).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val id = jsonArray.getJSONObject(i).getInt("id")

            if(id == storeId){
                val scr = tmpScore[id-1].score
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

    fun getStoreMenu(context: Context, storeId: Int): ArrayList<StoreMenuClass>{
        val storeMenuList = ArrayList<StoreMenuClass>()

        // 메뉴 데이터 가져오기
        val data = readFile("StoreMenu.json", context)

        val jsonArray = JSONTokener(data).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val id = jsonArray.getJSONObject(i).getInt("id")

            if(id == storeId){
                val menu = JSONTokener(jsonArray.getJSONObject(i).getString("menu")).nextValue() as JSONArray

                for (k in 0 until menu.length()) {
                    storeMenuList.add(
                        StoreMenuClass(
                            menu.getJSONObject(k).getString("menuName"),
                            menu.getJSONObject(k).getString("menuPrice")
                    ))
                }

                break
            }
        }
        // Log.d("menuData", "menu : $storeMenuList")
        return storeMenuList
    }
}