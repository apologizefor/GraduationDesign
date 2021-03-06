package com.tm.book_of_exercises.main.otherPage;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import com.tm.book_of_exercises.main.mainPage.MeFragment;
import com.tm.book_of_exercises.tools.AnnotateUtils;
import com.tm.book_of_exercises.tools.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tm.book_of_exercises.constant.Constant.username;

/**
 * Created by T M on 2018/3/16.
 */

public class UserInfo extends AppCompatActivity {
    private LinearLayout changeLogoLinearLayout,classLinearLayout,gradeLinearLayout,lineLinearLayout;
    private ImageView logoImageView;
    private TextView accountTV;
    private EditText nicknameEV;
    private EditText phoneEV;
    private EditText schoolEV;
    private EditText occupationEV;
    private EditText classEV,gradeEV;

    @ViewInject(R.id.user_info_modify)
    private Button btn_modify;
    private String resulCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        AnnotateUtils.injectViews(this);

        classLinearLayout = findViewById(R.id.infoClassLinearLayout);
        changeLogoLinearLayout = findViewById(R.id.changeLogo);
        logoImageView = findViewById(R.id.logo);
        accountTV = findViewById(R.id.account);
        nicknameEV = findViewById(R.id.nickname);
        phoneEV = findViewById(R.id.phone);
        schoolEV = findViewById(R.id.school);
        occupationEV = findViewById(R.id.occupation);
        classEV = findViewById(R.id.class_);
        gradeEV = findViewById(R.id.grade);
        //btn_modify = findViewById(R.id.user_info_modify);
        gradeLinearLayout = findViewById(R.id.infoGradeLinearLayout);
        lineLinearLayout = findViewById(R.id.line_view);

        occupationEV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if("学生".equals(occupationEV.getText().toString())){
                    classLinearLayout.setVisibility(View.VISIBLE);
                    gradeLinearLayout.setVisibility(View.VISIBLE);
                    lineLinearLayout.setVisibility(View.VISIBLE);
                }else {
                    lineLinearLayout.setVisibility(View.INVISIBLE);
                    classLinearLayout.setVisibility(View.INVISIBLE);
                    gradeLinearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        ExtraSet();

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        changeLogoLinearLayout.setOnClickListener(myOnClickListener);
        logoImageView.setOnClickListener(myOnClickListener);
        btn_modify.setOnClickListener(myOnClickListener);
    }

    void ExtraSet(){
        accountTV.setText(username);
        Intent intent = getIntent();
        String logo = intent.getStringExtra("userLogo");
        nicknameEV.setText(intent.getStringExtra("userNickname"));
        phoneEV.setText(intent.getStringExtra("userPhone"));
        schoolEV.setText(intent.getStringExtra("userSchool"));
        occupationEV.setText(intent.getStringExtra("userOccupation"));
        classEV.setText(intent.getStringExtra("userClass"));
        gradeEV.setText(intent.getStringExtra("userGrade"));
        if ("教师".equals(intent.getStringExtra("userOccupation"))){
            classLinearLayout.setVisibility(View.INVISIBLE);
            gradeLinearLayout.setVisibility(View.INVISIBLE);
            lineLinearLayout.setVisibility(View.INVISIBLE);
        }
    }

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.changeLogo:
                    break;
                case R.id.logo:
                    break;
                case R.id.user_info_modify:
                    infoModify();
                    break;
            }
        }
    }

    void infoModify(){
        HashMap<String,Object> infoMap = new HashMap<>();
        infoMap.put("action","modify");
        infoMap.put("username",accountTV.getText().toString());
        infoMap.put("nickname",nicknameEV.getText().toString());
        infoMap.put("phone",phoneEV.getText().toString());
        infoMap.put("school",schoolEV.getText().toString());
        infoMap.put("occupation",occupationEV.getText().toString());
        infoMap.put("class_",classEV.getText().toString());
        infoMap.put("grade",gradeEV.getText().toString());
        Constant constant = new Constant();
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/userInfo/");
        retrofitBuilder.isConnected(UserInfo.this);
        retrofitBuilder.params(infoMap);
        retrofitBuilder.post();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    resulCode = jsonObject.getString("code");
                    if("200".equals(resulCode)){
                        //new MeFragment().query(accountTV.getText().toString());
                        Toast.makeText(UserInfo.this,"修改成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(UserInfo.this,"修改失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(UserInfo.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            //new MeFragment().query(accountTV.getText().toString());
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
