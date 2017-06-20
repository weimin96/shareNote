package com.aoliao.notebook.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aoliao.notebook.R;
import com.aoliao.notebook.presenter.RegisterPresenter;
import com.aoliao.notebook.utils.CodeUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnTextChanged;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class MobSMSCodeActivity extends BaseActivity<RegisterPresenter> implements View.OnClickListener {
    private static final String TAG = "SmsYanzheng";
    private ImageView image;
    private EditText et;
    private TextView cannotSee;
    private String codeStr;
    private CodeUtils codeUtils;
    EditText mEditTextPhoneNumber;
    EditText mEditTextCode;
    Button mButtonGetCode;
    Button mButtonLogin;
    String APPKEY = "1d9d83c7ab3a1";
    String APPSECRET = "1035ca1a1edac05a1fa0dd80449e3f67";
    EventHandler eventHandler;
    String strPhoneNumber;

    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.layout_send)
    LinearLayout mLayout_send;

    @Override
    protected int getContentId() {
        return R.layout.activity_smscode;
    }


    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("注册");
    }

    @Override
    protected void onInit() {
        super.onInit();
        initView();
    }


    private void initView() {
        initToolbar(toolbar);
        mEditTextPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mEditTextCode = (EditText) findViewById(R.id.code);
        mButtonGetCode = (Button) findViewById(R.id.get_code);
        mButtonLogin = (Button) findViewById(R.id.register);
        image = (ImageView) findViewById(R.id.image);
        et = (EditText) findViewById(R.id.et);
        cannotSee = (TextView) findViewById(R.id.btn);
        cannotSee.setOnClickListener(this);
        mButtonGetCode.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);

        mLayout_send.setVisibility(View.GONE);
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        image.setImageBitmap(bitmap);
        //初始化sdk
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        eventHandler = new EventHandler() {
            /**
             * 在操作之后被触发
             *
             * @param event  参数1
             * @param result 参数2 SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.RESULT_ERROR表示操作失败
             * @param data   事件操作的结果
             */
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = myHandler.obtainMessage(0x00);
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                myHandler.sendMessage(message);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                String strCode = mEditTextCode.getText().toString();
                if (strCode != null && strCode.length() == 4) {
                    Log.d(TAG, mEditTextCode.getText().toString());
                    SMSSDK.submitVerificationCode("86", strPhoneNumber, mEditTextCode.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "验证码长度不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.get_code:
                strPhoneNumber = mEditTextPhoneNumber.getText().toString();
                if (strPhoneNumber == null || "".equals(strPhoneNumber) || strPhoneNumber.length() != 11) {
                    Toast.makeText(getApplicationContext(), "电话号码输入有误", Toast.LENGTH_SHORT).show();
                }
                codeStr = et.getText().toString().trim();
                if (null == codeStr || TextUtils.isEmpty(codeStr)) {
                    Toast.makeText(getApplicationContext(), "请输入图形验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = codeUtils.getCode();
                if (code.equalsIgnoreCase(codeStr)) {
                    SMSSDK.getVerificationCode("86", strPhoneNumber);
                    mButtonGetCode.setClickable(false);
                    //开启线程去更新button的text
                    new Thread() {
                        @Override
                        public void run() {
                            int totalTime = 60;
                            for (int i = 0; i < totalTime; i++) {
                                Message message = myHandler.obtainMessage(0x01);
                                message.arg1 = totalTime - i;
                                myHandler.sendMessage(message);
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            myHandler.sendEmptyMessage(0x02);
                        }
                    }.start();
                    mLayout_send.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn:
                codeUtils = CodeUtils.getInstance();
                Bitmap bitmap = codeUtils.createBitmap();
                image.setImageBitmap(bitmap);

                break;
        }
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.e(TAG, "result : " + result + ", event: " + event + ", data : " + data);
                    if (result == SMSSDK.RESULT_COMPLETE) { //回调  当返回的结果是complete
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码
                            Toast.makeText(getApplicationContext(), "发送验证码成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "get verification code successful.");
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码
                            Log.d(TAG, "submit code successful");
                            Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MobSMSCodeActivity.this, RegisterActivity.class);

                            Bundle bd = new Bundle();
                            bd.putString("phone", mEditTextPhoneNumber.getText().toString());
                            intent.putExtras(bd);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d(TAG, data.toString());
                        }
                    } else { //进行操作出错，通过下面的信息区分析错误原因
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            //错误代码：  http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
                            Log.e(TAG, "status: " + status + ", detail: " + des);
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(getApplicationContext(), des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x01:
                    mButtonGetCode.setText("重新发送(" + msg.arg1 + ")");
                    break;
                case 0x02:
                    mButtonGetCode.setText("获取验证码");
                    mButtonGetCode.setClickable(true);
                    break;
            }
        }
    };
}
