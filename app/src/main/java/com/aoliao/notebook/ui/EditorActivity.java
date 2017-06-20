
package com.aoliao.notebook.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aoliao.notebook.R;
import com.aoliao.notebook.broadcastreceiver.NetWorkBroadcastReceiver;
import com.aoliao.notebook.contract.EditorContract;
import com.aoliao.notebook.factory.PerformEditable;
import com.aoliao.notebook.widget.NoScrollViewPager;
import com.aoliao.notebook.utils.db.NoteDB;
import com.aoliao.notebook.utils.data.PreferenceData;
import com.aoliao.notebook.presenter.EditorPresenter;
import com.aoliao.notebook.utils.Check;
import com.aoliao.notebook.utils.FileUtils;
import com.aoliao.notebook.utils.PerformInputAfter;
import com.aoliao.notebook.utils.TimeUtils;
import com.aoliao.notebook.utils.ToastUtil;
import com.aoliao.notebook.widget.MarkdownPreviewView;
import com.aoliao.notebook.widget.TabIconView;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ren.qinc.edit.PerformEdit;


public class EditorActivity extends BaseActivity<EditorPresenter> implements EditorContract.View, View.OnClickListener { //BaseFragment<EditorPresenter> implements EditorContract.View, View.OnClickListener {
    @BindView(R.id.pager)
    protected NoScrollViewPager mViewPager;
    @BindView(R.id.action_other_operate)
    protected ExpandableLayout mExpandLayout;
    @BindView(R.id.toolbar_edit)
    Toolbar toolbar;
    @BindView(R.id.tabIconView)
    TabIconView mTabIconView;
    private View editView = null;
    private View mdShowView = null;
    private EditViewHolder mEditViewHolder;
    private MarkdownViewHolder mMarkdownViewHolder = null;
    private ProgressDialog progressDialog;
    private String coverPicture = null;//第一次上传的图片为封面图
    private SweetAlertDialog dialog;
    private SweetAlertDialog compressDialog;
    private NetWorkBroadcastReceiver mNetBroadcastReceiver;
    @BindView(R.id.fab_editModify_1)
    FloatingActionButton fab;
    private SQLiteDatabase mDatabase;
    private Bundle bd;
    private boolean isEditToBack = false;//false为read true为edit
    private boolean isSave = true;//判断点击保存还是返回
    public final int NOTEACTIVITY = 0x1;
    public final int MAINACTIVITY = 0x2;
    public final int RECYCLEACTIVITY = 0x3;

    @Override
    protected void onInit() {
        super.onInit();
        initToolbar();//先加载toolbar
        initTab();

        Intent intent = getIntent();
        bd = intent.getExtras();
        //0x1:NoteActivity  0x2:MainActivity    0x3:RecycleActivity
        //判断该页面是由哪个页面跳转过来
        if (NOTEACTIVITY == bd.getInt("class")) {
            initReadText();
        } else if (MAINACTIVITY == bd.getInt("class")) {
            initEditText();
        } else if (RECYCLEACTIVITY == bd.getInt("class")) {
            initRecycle();
        }
        fab = (FloatingActionButton) findViewById(R.id.fab_editModify_1);
    }

    @Override
    protected void onResume() {
        if (mNetBroadcastReceiver == null) {
            mNetBroadcastReceiver = new NetWorkBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetBroadcastReceiver, filter);
        System.out.println("---------------->注册广播");
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(mNetBroadcastReceiver);
        System.out.println("---------------->注销广播");
        super.onPause();
    }

    //加载回收站阅读笔记页面
    private void initRecycle() {
        initReadText();
        mViewPager.setCanScrollble(false);
        toolbar.getMenu().findItem(R.id.action_edit).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_share).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_helper).setVisible(false);
        fab.setImageResource(R.mipmap.lock);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "无法编辑已删除的笔记", Toast.LENGTH_SHORT).show();
//                fab.setVisibility(View.GONE);
            }
        });
    }

    //加载笔记阅读页面
    private void initReadText() {
        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.mipmap.ic_floating_modify_1);
        toolbar.setNavigationIcon(R.mipmap.ic_action_mode_back);
        initViewPager();
        mEditViewHolder.title.setText(bd.getString("title"));
        mEditViewHolder.content.setText(bd.getString("content"));
        toolbar.setTitle(bd.getString("title"));
        goEdit(false);
        mViewPager.setCurrentItem(1, true);

        mMarkdownViewHolder.parse(mEditViewHolder.title.getText().toString(), mEditViewHolder.content.getText().toString());
        final MarkdownPreviewView markdownPreviewView = (MarkdownPreviewView) mdShowView.findViewById(R.id.markdownView);
        markdownPreviewView.postDelayed(new Runnable() {
            @Override
            public void run() {
                markdownPreviewView.parseMarkdown(mEditViewHolder.content.getText().toString(), true);
            }
        }, 500);
        isEditToBack = false;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditToBack = true;
                mViewPager.setCurrentItem(0, true);
                fab.setVisibility(View.GONE);
            }
        });
    }

    //加载笔记编辑页面
    private void initEditText() {
        goEdit(true);
        fab.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.mipmap.ic_action_mode_done);
        mDatabase = new NoteDB(this).getWritableDatabase();
        initViewPager();
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave = true;
                backEven();
            }
        });
        toolbar.inflateMenu(R.menu.menu_editor_act);
        toolbar.inflateMenu(R.menu.menu_editor_frag);
//        mActionSave = toolbar.getMenu().findItem(R.id.action_save);
        mActionOtherOperate = toolbar.getMenu().findItem(R.id.action_other_operate);
        if (mExpandLayout.isExpanded())
            //展开，设置向上箭头
            mActionOtherOperate.setIcon(R.mipmap.ic_arrow_up);
        else
            mActionOtherOperate.setIcon(R.mipmap.ic_arrow_add);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isSave = false;
            backEven();
        }
        return false;
    }

    //返回事件
    private void backEven() {

        if (NOTEACTIVITY == bd.getInt("class")) {
            if (!isEditToBack) {//read->back
                finish();
            } else {
                if (mEditViewHolder.title.getText().toString().equals(bd.getString("title"))
                        && mEditViewHolder.content.getText().toString().equals(bd.getString("content"))) {//read->edit->back
                    initReadText();
                } else {
                    if (isSave) {
                        saved();
                    } else {
                        showGiveUpEditDialog();
                    }
                }
            }
        } else if (RECYCLEACTIVITY == bd.getInt("class")) {
            finish();
        } else if ("".equals(mEditViewHolder.content.getText().toString().trim())) {//edit->back
            Toast.makeText(getApplication(), "不能保存一条空笔记", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (isSave) {
                saved();
            } else {
                showGiveUpEditDialog();
            }
        }
    }


    private void showGiveUpEditDialog() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.give_up_modification))
                .setContentText(getString(R.string.not_save))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    /**
     * 是否跳转到编辑模式
     *
     * @param isEdit
     */
    private void goEdit(boolean isEdit) {
        if (toolbar.getMenu().findItem(R.id.action_edit) == null) {
            toolbar.inflateMenu(R.menu.menu_editor_preview_frag);
        }

        toolbar.getMenu().findItem(R.id.action_other_operate).setVisible(isEdit);
        toolbar.getMenu().findItem(R.id.action_edit).setVisible(!isEdit);
        toolbar.getMenu().findItem(R.id.action_preview).setVisible(isEdit);
        toolbar.getMenu().findItem(R.id.action_undo).setVisible(isEdit);
        toolbar.getMenu().findItem(R.id.action_redo).setVisible(isEdit);

        if (!isEdit && mExpandLayout.isExpanded()) {
            optionsItemSelected(mActionOtherOperate);
        }
    }

    private void initViewPager() {
        editView = LayoutInflater.from(getApplication()).inflate(R.layout.layout_editor, mViewPager, false);
        mdShowView = LayoutInflater.from(getApplication()).inflate(R.layout.layout_markdown, mViewPager, false);
        mEditViewHolder = new EditViewHolder(editView);
        mMarkdownViewHolder = new MarkdownViewHolder(mdShowView);
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                if (position == 0) {
                    container.addView(editView);
                    return editView;
                }
                container.addView(mdShowView);
                return mdShowView;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    @Override
    protected void onListener() {
        super.onListener();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                optionsItemSelected(item);
                return true;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    toolbar.setTitle("编辑");
                    goEdit(true);
                    fab.setVisibility(View.GONE);
                } else {
                    toolbar.setTitle(mEditViewHolder.title.getText().toString().trim());
                    goEdit(false);
                    mMarkdownViewHolder.parse(mEditViewHolder.title.getText().toString(), mEditViewHolder.content.getText().toString());
                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_editor_parent;
    }

    private void initTab() {
//        mTabIconView = ButterKnife.findById(getRootView(), R.id.tabIconView);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_list_bulleted, R.id.id_shortcut_list_bulleted, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_list_numbers, R.id.id_shortcut_format_numbers, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_insert_link, R.id.id_shortcut_insert_link, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_insert_photo, R.id.id_shortcut_insert_photo, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_console, R.id.id_shortcut_console, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_bold, R.id.id_shortcut_format_bold, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_italic, R.id.id_shortcut_format_italic, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_1, R.id.id_shortcut_format_header_1, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_2, R.id.id_shortcut_format_header_2, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_3, R.id.id_shortcut_format_header_3, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_quote, R.id.id_shortcut_format_quote, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_xml, R.id.id_shortcut_xml, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_minus, R.id.id_shortcut_minus, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_strikethrough, R.id.id_shortcut_format_strikethrough, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_grid, R.id.id_shortcut_grid, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_4, R.id.id_shortcut_format_header_4, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_5, R.id.id_shortcut_format_header_5, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_6, R.id.id_shortcut_format_header_6, this);
    }


    private final int SYSTEM_GALLERY = 1;

    @Override
    public void onClick(View v) {
        if (R.id.id_shortcut_insert_photo == v.getId()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);// Pick an item fromthe
            intent.setType("image/*");// 从所有图片中进行选择
            startActivityForResult(intent, SYSTEM_GALLERY);
            return;
        } else if (R.id.id_shortcut_insert_link == v.getId()) {
            //插入链接
            insertLink();
            return;
        } else if (R.id.id_shortcut_grid == v.getId()) {
            //插入表格
            insertTable();
            return;
        }
        //点击事件分发
        mEditViewHolder.mPerformEditable.onClick(v);

    }


    private MenuItem mActionOtherOperate;

    public void optionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_other_operate://展开和收缩
                if (!mExpandLayout.isExpanded())
                    //没有展开，但是接下来就是展开，设置向上箭头
                    mActionOtherOperate.setIcon(R.mipmap.ic_arrow_up);
                else
                    mActionOtherOperate.setIcon(R.mipmap.ic_arrow_add);
                mExpandLayout.toggle();
                break;
            case R.id.action_preview://预览
                mViewPager.setCurrentItem(1, true);
                break;
            case R.id.action_edit://编辑
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.action_share:
                presenter.uploadArticle(coverPicture, mEditViewHolder.title.getText().toString().trim(), mEditViewHolder.content.getText().toString());
                break;
            case R.id.action_helper:
//                CommonMarkdownActivity.startHelper(this);
                break;
            case R.id.action_undo://撤销
                mEditViewHolder.mPerformEdit.undo();
                break;
            case R.id.action_redo://重做
                mEditViewHolder.mPerformEdit.redo();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == SYSTEM_GALLERY) {
            Uri uri = data.getData();
            String file = FileUtils.getImageAbsolutePath(this, uri);
//            mEditViewHolder.mPerformEditable.perform(R.id.id_shortcut_insert_photo, file);
            presenter.uploadPic(file);
        }

    }


    /**
     * 插入表格
     */
    private void insertTable() {
        View rootView = LayoutInflater.from(EditorActivity.this).inflate(R.layout.view_common_input_table_view, null);

        final AlertDialog dialog = new AlertDialog.Builder(EditorActivity.this)
                .setTitle(R.string.insert_table)
                .setView(rootView)
                .show();

        final TextInputLayout rowNumberHint = (TextInputLayout) rootView.findViewById(R.id.rowNumberHint);
        final TextInputLayout columnNumberHint = (TextInputLayout) rootView.findViewById(R.id.columnNumberHint);
        final EditText rowNumber = (EditText) rootView.findViewById(R.id.rowNumber);
        final EditText columnNumber = (EditText) rootView.findViewById(R.id.columnNumber);

        rootView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rowNumberStr = rowNumber.getText().toString().trim();
                String columnNumberStr = columnNumber.getText().toString().trim();

                if (Check.isEmpty(rowNumberStr)) {
                    rowNumberHint.setError(getString(R.string.no_can_null));
                    return;
                }
                if (Check.isEmpty(columnNumberStr)) {
                    columnNumberHint.setError(getString(R.string.no_can_null));
                    return;
                }


                if (rowNumberHint.isErrorEnabled())
                    rowNumberHint.setErrorEnabled(false);
                if (columnNumberHint.isErrorEnabled())
                    columnNumberHint.setErrorEnabled(false);
                mEditViewHolder.mPerformEditable.perform(R.id.id_shortcut_grid, Integer.parseInt(rowNumberStr), Integer.parseInt(columnNumberStr));
                dialog.dismiss();

            }
        });

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 插入链接
     */
    private void insertLink() {
        View rootView = LayoutInflater.from(EditorActivity.this).inflate(R.layout.view_common_input_link_view, null);

        final AlertDialog dialog = new AlertDialog.Builder(EditorActivity.this, R.style.AppTheme_NoActionBar)
                .setTitle(R.string.insert_link)
                .setView(rootView)
                .show();

        final TextInputLayout titleHint = (TextInputLayout) rootView.findViewById(R.id.inputNameHint);
        final TextInputLayout linkHint = (TextInputLayout) rootView.findViewById(R.id.inputHint);
        final EditText title = (EditText) rootView.findViewById(R.id.name);
        final EditText link = (EditText) rootView.findViewById(R.id.text);

        rootView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString().trim();
                String linkStr = link.getText().toString().trim();

                if (Check.isEmpty(titleStr)) {
                    titleHint.setError(getString(R.string.no_can_null));
                    return;
                }
                if (Check.isEmpty(linkStr)) {
                    linkHint.setError(getString(R.string.no_can_null));
                    return;
                }

                if (titleHint.isErrorEnabled())
                    titleHint.setErrorEnabled(false);
                if (linkHint.isErrorEnabled())
                    linkHint.setErrorEnabled(false);

                mEditViewHolder.mPerformEditable.perform(R.id.id_shortcut_insert_link, titleStr, linkStr);
                dialog.dismiss();
            }
        });

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void saved() {
        String title = mEditViewHolder.title.getText().toString().trim();
        String content = mEditViewHolder.content.getText().toString().trim();
        PreferenceData.saveArticle(title, content);
        Long note_create_at = TimeUtils.getCurrentMillisLong();
        ContentValues cv = new ContentValues();
        cv.put(NoteDB.TITLE, title);
        cv.put(NoteDB.CONTENT, content);
        cv.put(NoteDB.TIME, note_create_at);
        mDatabase.insert(NoteDB.TABLE_NAME, null, cv);
//        presenter.saveArticle(coverPicture, title, content);
        Toast.makeText(getApplicationContext(), "已保存", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void showCompressingPicDialog() {
        compressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        compressDialog.setTitleText("正在压缩图片…");
        compressDialog.show();
    }

    @Override
    public void compressedPicSuccess() {
        if (compressDialog != null && compressDialog.isShowing()) {
            compressDialog.dismissWithAnimation();
        }
    }

    @Override
    public void showUploadArticleProgress() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText(getString(R.string.now_releasing_article));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void showUploadPicProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(EditorActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setTitle(getString(R.string.pic_uploading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {
            progressDialog.setProgress(0);
            progressDialog.show();
        }
    }

    @Override
    public void uploadPicProgress(int progress) {
        if (progressDialog == null) {
            showUploadPicProgress();
        }

        progressDialog.setProgress(progress);

        if (progress >= 100) {
            progressDialog.dismiss();
            ToastUtil.getInstance().showLongT(getString(R.string.upload_success));
        }
    }

    @Override
    public void uploadedPicLink(String link) {
        if (this.coverPicture == null) {
            this.coverPicture = link;
        }
        mEditViewHolder.mPerformEditable.perform(R.id.id_shortcut_insert_photo, link);
    }

    @Override
    public void uploadPicFail(String err) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog.setProgress(0);
        }
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void uploadArticleSuccess() {
        if (dialog != null && dialog.isShowing()) {
            dialog.setTitleText(getString(R.string.release_success));
            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismissWithAnimation();
                    finish();
                }
            });
        }
        ToastUtil.getInstance().showLongT(R.string.release_success);
        PreferenceData.clearPreference();
        mEditViewHolder.title.setText("");
        mEditViewHolder.content.setText("");
    }

    @Override
    public void uploadArticleFail(String err) {
        if (dialog != null && dialog.isShowing()) {
            dialog.setTitleText(getString(R.string.release_fail));
            dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        }
        ToastUtil.getInstance().showShortT(err);
    }

    @Override
    public void saveArticleSuccess() {
        if (dialog != null && dialog.isShowing()) {
            dialog.setTitleText(getString(R.string.save_success));
            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismissWithAnimation();
                    finish();
                }
            });
        }
        ToastUtil.getInstance().showLongT(R.string.save_success);

    }

    @Override
    public void saveArticleFail(String err) {
        if (dialog != null && dialog.isShowing()) {
            dialog.setTitleText(getString(R.string.save_fail));
            dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        }
        ToastUtil.getInstance().showShortT(err);
    }


    static class EditViewHolder {
        @BindView(R.id.title)
        EditText title;
        @BindView(R.id.content)
        EditText content;
        PerformEditable mPerformEditable;
        PerformEdit mPerformEdit;
        PerformEdit mPerformNameEdit;
        private Listener listener;

        EditViewHolder(View view) {
            ButterKnife.bind(this, view);
            //代码格式化或者插入操作
            mPerformEditable = new PerformEditable(content);
            //文本输入监听(用于自动输入)
            PerformInputAfter.start(content);
            //撤销和恢复初始化
            mPerformEdit = new PerformEdit(content) {
                @Override
                protected void onTextChanged(Editable s) {
                    //文本改变
                    if (listener != null) {
                        listener.change();
                    }
                }
            };

            mPerformNameEdit = new PerformEdit(title) {
                @Override
                protected void onTextChanged(Editable s) {
                    //文本改变
                    if (listener != null) {
                        listener.change();
                    }
                }
            };
        }

        public void setListener(Listener listener) {
            this.listener = listener;
        }

        interface Listener {
            void change();
        }
    }

    static class MarkdownViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.markdownView)
        MarkdownPreviewView markdownView;

        MarkdownViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void parse(String title, String content) {
            this.title.setText(title);
            this.markdownView.parseMarkdown(content, true);
        }
    }
}
