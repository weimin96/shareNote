package com.aoliao.notebook.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aoliao.notebook.R;
import com.aoliao.notebook.adapter.NoteAdapter;
import com.aoliao.notebook.utils.db.NoteDB;
import com.aoliao.notebook.utils.listener.OnItemClickListener;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

/**
 * Created by 你的奥利奥 on 2017/4/18.
 */

public class RecycleActivity extends NoteActivity {
    private Activity mContext;
    private ArrayList<Post> noteList;
    private SQLiteDatabase mDatabase;
    private NoteAdapter noteAdapter;
    private SwipeMenuRecyclerView mMenuRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle(" 回收站 ");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycle_view);
        mDatabase = new NoteDB(this).getReadableDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void selectDB() {
        super.selectDB();
        noteList = new ArrayList<>();
        Cursor cursor = mDatabase.query(NoteDB.RECYCLE_TABLE, null, null, null, null, null, sortOrder);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            Long time = cursor.getLong(cursor.getColumnIndex("time"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            Post note = new Post(title, content, time, id);
            noteList.add(note);
        }
        noteAdapter = new NoteAdapter(this, noteList);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));//布局管理器
        mMenuRecyclerView.addItemDecoration(new ListViewDecoration(this));//添加分割线
        mMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        //设置菜单item点击监听
        mMenuRecyclerView.setSwipeMenuItemClickListener(onSwipeMenuItem);
        noteAdapter.setOnItemClickListener(onItemClick);
        mMenuRecyclerView.setAdapter(noteAdapter);
        cursor.close();
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_delete_black)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.BLACK)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
            }

            {
                SwipeMenuItem restoreItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_delete_black)
                        .setText("恢复") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.BLACK)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(restoreItem);// 添加一个按钮到右侧侧菜单。
            }
        }
    };

    private OnSwipeMenuItemClickListener onSwipeMenuItem = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。
            Post noteOne = noteList.get(adapterPosition);
            if (menuPosition == 0) {// 删除按钮被点击。
                mDatabase.delete(NoteDB.RECYCLE_TABLE, "id=?", new String[]{noteOne.getId() + ""});
                noteList.remove(adapterPosition);
                noteAdapter.notifyItemRemoved(adapterPosition);
                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }
            if (menuPosition == 1) {//恢复按钮
                ContentValues cv = new ContentValues();
                cv.put(NoteDB.ID, noteOne.getId());
                cv.put(NoteDB.TITLE, noteOne.getTitle());
                cv.put(NoteDB.CONTENT, noteOne.getContent());
                cv.put(NoteDB.TIME, noteOne.getTime());
                mDatabase.insert(NoteDB.TABLE_NAME, null, cv);
                mDatabase.delete(NoteDB.RECYCLE_TABLE, "id=?", new String[]{noteOne.getId() + ""});
                noteList.remove(adapterPosition);
                noteAdapter.notifyItemRemoved(adapterPosition);
            }
        }
    };

    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            Intent intent = new Intent(mContext, EditorActivity.class);
            TextView title = (TextView) view.findViewById(R.id.note_title);
            TextView content = (TextView) view.findViewById(R.id.note_content);
            Bundle bd = new Bundle();
            bd.putInt("class", 0x3);
            bd.putString("title", title.getText().toString());
            bd.putString("content", content.getText().toString());
            intent.putExtras(bd);
            startActivity(intent);
        }
    };
}
