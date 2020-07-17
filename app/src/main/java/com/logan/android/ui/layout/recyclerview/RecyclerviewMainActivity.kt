package com.logan.android.ui.layout.recyclerview

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.entity.ButtonModel
import com.logan.android.ui.layout.recyclerview.adapter.MyRVAdapter2
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_recyclerview_main.*

/**
 * desc: Recyclerview 案例主入口 <br/>
 * time: 2020/7/17 11:15 AM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class RecyclerviewMainActivity : BaseActivity() {

    var decor: RecyclerView.ItemDecoration? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_main)

        // StaggeredGridLayoutManager	在分散对齐网格中显示项目 TODO

        collectButtons().forEach {
            showButtons(this, fl_view_container, it)
        }
    }


    private fun collectButtons(): Array<ButtonModel> {
        return arrayOf(
            ButtonModel("简单用法", View.OnClickListener {
                removeItemDecoration()

                // LinearLayoutManager 以垂直或水平滚动列表方式显示项目
                val linearLayoutManager = LinearLayoutManager(this)
                rv_main.layoutManager = linearLayoutManager
                rv_main.adapter = MyRVAdapter2(this)
            }),

            ButtonModel("分割线(横着)", View.OnClickListener {
                removeItemDecoration()

                rv_main.layoutManager = LinearLayoutManager(this)
                rv_main.adapter = MyRVAdapter2(this)

                // 分割线
                // decor = HorizontalDividerItemDecoration.Builder(this).build()
                decor = HorizontalDividerItemDecoration.Builder(this)
                    .color(Color.RED)
                    .margin(80, 30)
                    .size(5)
                    .build()
                rv_main.addItemDecoration(decor!!)
            }),

            ButtonModel("分割线(竖着)", View.OnClickListener {
                removeItemDecoration()

                // GridLayoutManager 在网格中显示项目
                rv_main.layoutManager = GridLayoutManager(this, 2)
                rv_main.adapter = MyRVAdapter2(this)
                rv_main.addItemDecoration( // 竖着的分割线
                    VerticalDividerItemDecoration.Builder(this).color(Color.BLUE).build()
                )
                rv_main.addItemDecoration( // 横着的分割线
                    HorizontalDividerItemDecoration.Builder(this).color(Color.RED).build()
                )
            }),

            ButtonModel("监听事件(点击、监听)", View.OnClickListener {
                with(rv_main) {
                    val context = this@RecyclerviewMainActivity
                    layoutManager = GridLayoutManager(context, 2)
                    addItemDecoration(
                        VerticalDividerItemDecoration.Builder(context).color(Color.BLUE).build()
                    )
                    addItemDecoration(
                        HorizontalDividerItemDecoration.Builder(context).color(Color.RED).build()
                    )
                    // 点击事件
                    adapter = MyRVAdapter2(context).apply {
                        setOnItemClickListener(createOnItemClickListener())
                    }
                }
            })

        )
    }

    private fun removeItemDecoration() {
        decor?.let {
            rv_main.removeItemDecoration(it)
        }
    }

    private fun createOnItemClickListener(): MyRVAdapter2.OnItemClickListener {
        return object : MyRVAdapter2.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                showMsg("点击 ${position + 1} ")
            }

            override fun onItemLongClick(view: View, position: Int) {
                showMsg("长按 ${position + 1} ")
            }
        }
    }

}
