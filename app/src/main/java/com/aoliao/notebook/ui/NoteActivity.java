package com.aoliao.notebook.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.ReadActicleContract;
import com.aoliao.notebook.adapter.NoteAdapter;
import com.aoliao.notebook.utils.db.NoteDB;
import com.aoliao.notebook.utils.listener.OnItemClickListener;
import com.aoliao.notebook.utils.entity.Comment;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.Reply;
import com.aoliao.notebook.presenter.ReadArticlePresenter;
import com.aoliao.notebook.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 你的奥利奥 on 2017/2/14.
 */

public class NoteActivity extends BaseActivity<ReadArticlePresenter> implements ReadActicleContract.View {
    private Activity mContext;
    private ArrayList<Post> noteList;
    private SQLiteDatabase mDatabase;
    private SwipeMenuRecyclerView mMenuRecyclerView;
    private NoteAdapter noteAdapter;

    String sortOrder = NoteDB.TIME + " DESC";

    @Override
    protected int getContentId() {
        return R.layout.list_notes;
    }

    @Override
    protected void onInit() {
        super.onInit();
        initView();
    }

    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        mMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycle_view);
        mContext = this;
        initToolbar(toolbar);
        mDatabase = new NoteDB(this).getReadableDatabase();
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {

        super.initToolbar(toolbar);
        toolbar.setTitle(" 查看笔记 ");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }

    public void selectDB() {
        noteList = new ArrayList<>();
        Cursor cursor = mDatabase.query(NoteDB.TABLE_NAME, null, null, null, null, null, sortOrder);
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
        mMenuRecyclerView.setSwipeMenuItemClickListener(mOnSwipeMenuItemClickListener);
        noteAdapter.setOnItemClickListener(mOnItemClickListener);
        mMenuRecyclerView.setAdapter(noteAdapter);
        cursor.close();
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            Intent intent = new Intent(mContext, EditorActivity.class);
            TextView title = (TextView) view.findViewById(R.id.note_title);
            TextView content = (TextView) view.findViewById(R.id.note_content);
            Bundle bd = new Bundle();
            bd.putInt("class", 0x1);
            bd.putString("title", title.getText().toString());
            bd.putString("content", content.getText().toString());
            intent.putExtras(bd);
            startActivity(intent);
        }
    };

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
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
        }
    };


    private OnSwipeMenuItemClickListener mOnSwipeMenuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         *
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView
         *                        #RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }
            // TODO 如果是删除：推荐调用Adapter.notifyItemRemoved(position)，不推荐Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 删除按钮被点击。
                Post noteOne = noteList.get(adapterPosition);
                ContentValues cv = new ContentValues();
                cv.put(NoteDB.ID, noteOne.getId());
                cv.put(NoteDB.TITLE, noteOne.getTitle());
                cv.put(NoteDB.CONTENT, noteOne.getContent());
                cv.put(NoteDB.TIME, System.currentTimeMillis());
                mDatabase.insert(NoteDB.RECYCLE_TABLE, null, cv);
                mDatabase.delete(NoteDB.TABLE_NAME, "id=?", new String[]{noteOne.getId() + ""});
                noteList.remove(adapterPosition);
                noteAdapter.notifyItemRemoved(adapterPosition);
            }
        }
    };

    @Override
    public void showArticle(Post post) {

    }

    @Override
    public void showComment(List<Comment> comments, List<Reply> replyList) {

    }

    @Override
    public void addCommentSuccess() {

    }

    @Override
    public void addCommentFail(String err) {

    }

    @Override
    public void replyCommentSuccess() {

    }

    @Override
    public void replyCommentFail(String err) {

    }

    @Override
    public void likePostSuccess() {

    }

    @Override
    public void likePostFail(String err) {

    }

    @Override
    public void followUserSuccess() {

    }

    @Override
    public void followUserFail(String err) {

    }
}