package Guide.suchelin

import android.content.Context
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
}