package com.oneseries.starter

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.oneseries.starter.adapter.IconsAdapter
import com.oneseries.starter.bean.CategoryBean
import com.oneseries.starter.bean.IconBean
import com.oneseries.starter.databinding.ActivityDashboardBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.xmlpull.v1.XmlPullParser
import java.io.Console
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: IconsAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        initView()
        setOnClickListener()
        setContentView(view)
        adapter.clear()
        CoroutineScope(Dispatchers.Main).launch {
            val iconFlow = getAllIcons()
            iconFlow.collect(){
                adapter.attach(it)
            }
        }
    }

    private fun initView() {
        adapter = IconsAdapter(this)
        adapter.setClickListener(object :IconsAdapter.OnItemClickListener{
            override fun onClick(iconView: ImageView, icon: Int, name: String?) {
                Toast.makeText(this@DashboardActivity,name, Toast.LENGTH_SHORT).show()
            }
        })
        layoutManager = GridLayoutManager(this,calculateGridNum())
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.isCate(position)) layoutManager.spanCount else 1
            }
        }
        binding.IconList.setHasFixedSize(true)
        binding.IconList.layoutManager = layoutManager
        binding.IconList.adapter = adapter
    }

    private fun setOnClickListener(){
        binding.AppBarControls.setNavigationOnClickListener {
            finish()
        }
    }

    private fun calculateGridNum(): Int {
        val displayMetrics = resources.displayMetrics
        val minGridSize = (72 * displayMetrics.density).toInt()
        val totalWidth = displayMetrics.widthPixels
        return totalWidth / minGridSize
    }

    private fun getAllIcons(): Flow<List<IconBean>> = flow<List<IconBean>> {
        val iconList: MutableList<IconBean> = ArrayList()
        val cateList: MutableList<CategoryBean> = ArrayList()

        val defCate = CategoryBean(null)
        val parser = resources.getXml(R.xml.drawable)
        val dateNow = LocalDate.now().dayOfMonth

        suspend fun endCategory(index: Int) {
            if (cateList.isEmpty()) return
            iconList.clear()
            val subIconList: List<String> = cateList[index].listIcons()
            // Add category
            iconList.add(IconBean(cateList[index].name + "..." + subIconList.size.toString(),0,null))
            // Add Icon
            for (iconName in subIconList) {
                val drawableId = resources.getIdentifier(iconName,"drawable",packageName)
                iconList.add(IconBean(cateList[index].name,drawableId,iconName))
            }
            if (cateList.size == 1 && cateList[0].name == null) { // 无任何分类
                iconList.removeAt(0)
            }
            emit(ArrayList(iconList))
        }

        try {
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.eventType != XmlPullParser.START_TAG) continue
                when (parser.name) {
                    "category" -> {
                        endCategory(cateList.size-1)
                        cateList.add(CategoryBean(parser.getAttributeValue(null, "title")))
                    }
                    "item" -> {
                        var iconName = parser.getAttributeValue(null, "drawable")
                        if (iconName.endsWith("calendar_")) iconName += dateNow
                        if (cateList.isEmpty()) {
                            defCate.pushIcon(iconName)
                        } else {
                            cateList[cateList.size - 1].pushIcon(iconName)
                        }
                    }
                }
            }
            if (!defCate.isEmpty) {
                cateList.add(defCate)
            }
            endCategory(cateList.size-1)
        } catch (ex: Exception) {
            ex.printStackTrace();
        }
    }.flowOn(Dispatchers.IO)
}