package com.aoliao.notebook.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aoliao.notebook.R;
import com.aoliao.notebook.adapter.UserInfoAdapter;
import com.aoliao.notebook.contract.EditUserInfoContract;
import com.aoliao.notebook.factory.FragmentFactory;
import com.aoliao.notebook.config.Config;
import com.aoliao.notebook.presenter.EditUserInfoPresenter;
import com.aoliao.notebook.utils.BottomSheetImagePicker;
import com.aoliao.notebook.utils.FileUtils;
import com.aoliao.notebook.utils.ToastUtil;
import com.aoliao.notebook.utils.data.DataFiller;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.soundcloud.android.crop.Crop;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.zaaach.citypicker.CityPickerActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by 你的奥利奥 on 2017/6/3.
 */

public class EditUserInfoActivity extends BaseActivity<EditUserInfoPresenter> implements EditUserInfoContract.View, DatePickerDialog.OnDateSetListener {
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.rvUserInfo)
    RecyclerView rvUserInfo;
    @BindView(R.id.bottomSheet)
    BottomSheetLayout bottomSheet;
    private UserInfoAdapter adapter;
    private static final int REQUEST_CODE_PICK_CITY = 0;


    @Override
    protected void onInit() {
        super.onInit();
        initToolbar(toolbar);
        initList();
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("个人信息");
    }

    /**
     * 初始化用户信息列表
     */
    private void initList() {
        rvUserInfo.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserInfoAdapter(DataFiller.getLocalUser());
        rvUserInfo.setAdapter(adapter);
    }

    @Override
    protected void onListener() {
        super.onListener();
        rvUserInfo.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (i) {
                    case Config.euii.HEAD:
                        editHead();
                        break;
                    case Config.euii.NICKNAME:
                        editInfo(R.string.edit_nickname);
                        break;
                    case Config.euii.SEX:
                        editSex();
                        break;
                    case Config.euii.PHONE:
                        editInfo(R.string.edit_phone_number);
                        break;
                    case Config.euii.CITY:
                        editCity();
                        break;
                    case Config.euii.BIRTHDAY:
                        editBirthday();
                        break;
                    case Config.euii.EMAIL:
                        editInfo(R.string.edit_email);
                        break;
                    case Config.euii.PASSWORD:
                        editPassword();
                        break;
                    case Config.euii.SIGN:
                        editSign();
                        break;
                }
            }
        });
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_edit_user_info;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                presenter.requestChangeCity(city);
            }
        }

        if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
            return;
        }
        BottomSheetImagePicker.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 修改城市
     */
    private void editCity() {
        startActivityForResult(new Intent(EditUserInfoActivity.this, CityPickerActivity.class), REQUEST_CODE_PICK_CITY);
    }

    /**
     * 修改签名
     */
    private void editSign() {
        new SweetAlertDialog(this, SweetAlertDialog.SIGN_TYPE)
                .setTitleText(getString(R.string.update_sign))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        presenter.requestChangeSign(sweetAlertDialog.getSign());
                    }
                })
                .show();
    }

    /**
     * 更改密码
     */
    private void editPassword() {
        new SweetAlertDialog(this, SweetAlertDialog.PASSWORD_TYPE)
                .setTitleText(getString(R.string.update_password))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        String[] pwd = sweetAlertDialog.getPassword();
                        presenter.requestChangePassword(pwd[0], pwd[1]);
                    }
                }).show();
    }

    /**
     * 编辑生日
     */
    private void editBirthday() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    /**
     * 编辑性别
     */
    private void editSex() {
        new SweetAlertDialog(this, SweetAlertDialog.SEX_TYPE)
                .setTitleText("更改性别")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        presenter.requestChangeSex(sweetAlertDialog.getSex());
                    }
                }).show();
    }

    /**
     * 编辑昵称/手机号
     */
    private void editInfo(final int titleId) {
        new SweetAlertDialog(this, SweetAlertDialog.EDIT_TYPE)
                .setTitleText(getString(titleId))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        switch (titleId) {
                            case R.string.edit_nickname:
                                presenter.requestChangeNickname(sweetAlertDialog.getEditText());
                                break;
                            case R.string.edit_phone_number:
                                presenter.requestChangePhone(sweetAlertDialog.getEditText());
                                break;
                            case R.string.edit_email:
                                presenter.requestChangeEmail(sweetAlertDialog.getEditText());
                                break;
                        }
                    }
                })
                .show();
    }


    /**
     * 编辑头像
     */
    private void editHead() {
        BottomSheetImagePicker
                .getInstance()
                .showImagePicker(BottomSheetImagePicker.PickerType.BOTH,
                        this,
                        bottomSheet,
                        new BottomSheetImagePicker.Listener() {
                            @Override
                            public void onImageArrived(Uri selectedImageUri) {
                                String imgPath = FileUtils.getImageAbsolutePath(EditUserInfoActivity.this, selectedImageUri);
                                if (FileUtils.isGif(imgPath)) {
                                    presenter.requestChangeHeadPic(imgPath);
                                    return;
                                }
                                Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
                                Crop.of(selectedImageUri, destination).asSquare().start(EditUserInfoActivity.this);
                            }
                        });
    }

    @Override
    public void changeHeadPicSuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.update_head_success));
        adapter.update();
        FragmentFactory.updatedUser();
    }

    private void update() {
        adapter.update();
        FragmentFactory.updatedUser();
    }

    @Override
    public void changeHeadPicFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void changeNicknameSuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.update_nickname_success));
        update();
    }

    @Override
    public void changeNicknameFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void changePhoneSuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.update_phone_success));
        update();
    }

    @Override
    public void changePhoneFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void changeSignSuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.update_sign_success));
        update();
    }

    @Override
    public void changeSignFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void changeCitySuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.update_city_success));
        update();
    }

    @Override
    public void changeCityFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void changeSexSuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.update_sex_success));
        update();
    }

    @Override
    public void changeSexFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void changeBirthdaySuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.update_birthday_succes));
        update();
    }

    @Override
    public void changeBirthdayFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void changeEmailSuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.update_email_success));
        update();
    }

    @Override
    public void changeEmailFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void changePasswordSuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.update_password_succss));
        update();
    }

    @Override
    public void changePasswordFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    /**
     * BOTTOM SHEET IMAGE PICKER METHODS
     **/
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        BottomSheetImagePicker.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
//            resultView.setImageURI(Crop.getOutput(result));
            presenter.requestChangeHeadPic(FileUtils.getImageAbsolutePath(this, Crop.getOutput(result)));

        } else if (resultCode == Crop.RESULT_ERROR) {
            ToastUtil.getInstance().showLongT(Crop.getError(result).getMessage());
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        try {
            presenter.requestBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
