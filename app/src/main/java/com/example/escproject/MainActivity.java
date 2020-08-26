package com.example.escproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    //멤버 변수
    private ImageButton addContact;
    private ImageButton contact;
    private TextView phoneNum;
    private TextView[] dials = new TextView[10];
    private TextView star;
    private TextView sharp;
    private ImageButton message;
    private ImageButton call;
    private ImageButton backspace;

    //전화번호 검색
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        setUpUI();

        // 아무것도 없을 때
        if (phoneNum.getText().length() == 0) { //메세지, 벡스페이스 안보이게하기
            message.setVisibility(View.INVISIBLE);
            backspace.setVisibility(View.INVISIBLE);
        }

    }
    // permission 함수
    private void checkPermissions(){
        int resultCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int resultSms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(resultCall == PackageManager.PERMISSION_DENIED || resultSms == PackageManager.PERMISSION_DENIED){ //승인거절 시
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS},1004); //요청 보내기

        }
        
    }
    
    // permission 요청 보내기
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if(requestCode == 1004){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED  && grantResults[1] == PackageManager.PERMISSION_GRANTED){ //허용 되었을 떄
                Toast.makeText(this, "권한 허용 됨 ", Toast.LENGTH_SHORT).show();
            }
            else{//허용 안될때
                Toast.makeText(this, "권한 허용이 필요합니다. 설정에서 허용을 설정해주세요.", Toast.LENGTH_SHORT).show(); //토스트
                Log.d("PermissionDenied", "권한이 거부되어 앱을 종료됩니다."); //로그
                finish(); //앱 종료

            }
        }
    }

    //시작할 때 화면
    private void setUpUI() {
        //변수와 레이아웃 연결
        addContact = findViewById(R.id.main_ibtn_add);
        contact = findViewById(R.id.main_ibtn_contact);
        phoneNum = findViewById(R.id.main_tv_phone);

        for (int i = 0; i < dials.length; i++) {
            dials[i] = findViewById(getResourceID("main_tv_" + i, "id", this));
        }

        star = findViewById(R.id.main_tv_star);
        sharp = findViewById(R.id.main_tv_sharp);
        message = findViewById(R.id.main_ibtn_message);
        call = findViewById(R.id.main_ibtn_call);
        backspace = findViewById(R.id.main_ibtn_backspace);

        name = findViewById(R.id.main_tv_name);

        //이벤트 설정
        addContact.setOnClickListener(new View.OnClickListener() {//addContact 저장
            @Override
            public void onClick(View v) {

                Intent addIntent = new Intent(MainActivity.this, AddEditActivity.class);
                addIntent.putExtra("phone_num", phoneNum.getText().toString());
                addIntent.putExtra("add_edit","add");
                startActivity(addIntent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {//contact 연락처
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(MainActivity.this, ContactActivity.class);

                startActivity(contactIntent);
            }
        });

        //dial 눌렀을 때 이벤트
        setOnClickDial(star, "*");
        setOnClickDial(sharp, "#");


        for (int i = 0; i < 10; i++) {
            setOnClickDial(dials[i], String.valueOf(i));
        }

        //message 눌렀을 때
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메시지로 화면 넘어가기
                Intent messageIntent = new Intent(MainActivity.this, MessageActivity.class);
                messageIntent.putExtra("phone_num",phoneNum.getText().toString()); // 화면이 넘어갈때 번호 넘기기
                startActivity(messageIntent);
            }
        });

        //call 눌렀을 때
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.getText()));
                startActivity(callIntent);

            }
        });

        //backspace 눌렀을 때
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (phoneNum.getText().length() > 0) { // 아무것도 없을 때 예외처리
//                    String formatPhoneNum = PhoneNumberUtils.formatNumber(phoneNum.getText().subSequence(0, phoneNum.getText().length() - 1).toString(),
//                            Locale.getDefault().getCountry()); //숫자 하나 지우기, 하이픈 자동 입력
//                    phoneNum.setText(formatPhoneNum);//기존번호 + 입력한것

                phoneNum.setText(changeToDial(phoneNum.getText().subSequence(0, phoneNum.getText().length() - 1).toString())); //숫자 하나 지우기
//
                // 다 지웠을 때
                if (phoneNum.getText().length() == 0) { //메세지, 벡스페이스 안보이게하기
                    message.setVisibility(View.GONE);
                    backspace.setVisibility(View.GONE);
                }
                findPhone();

            }

        });


        //backspace 길게 눌렀을 때
        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                phoneNum.setText("");


                //메세지, 벡스페이스 안보이게하기
                message.setVisibility(View.INVISIBLE);
                backspace.setVisibility(View.INVISIBLE);

                findPhone();
                return true;
            }

        });


    }


    //dial 눌렀을 때 이벤트를 위한 함수
    private void setOnClickDial(View view, final String input) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String formatPhoneNum = PhoneNumberUtils.formatNumber(phoneNum.getText() + input, Locale.getDefault().getCountry()); //하이픈 자동 입력
                //삼성 폰은 가능하지만 애뮬은 국가 지정이안되어있어 오류가 생김
                //phoneNum.setText(formatPhoneNum);//기존번호 + 입력한것

                phoneNum.setText(changeToDial(phoneNum.getText() + input));

                //메세지, 벡스페이스 보이게하기
                message.setVisibility(View.VISIBLE);
                backspace.setVisibility(View.VISIBLE);

                findPhone();


            }
        });
    }

    private void findPhone(){

        //존재하는 번호일 때
        String find = phoneNum.getText().toString().replaceAll("-","");

        int i = 0;
        int num = 0;

        name.setText("");
        StringBuffer s = new StringBuffer("");

        for(i = 0; i<DummyData.contacts.size(); i++){
            if(DummyData.contacts.get(i).getPhone().replaceAll("-","").contains(find)){
                name.setText(DummyData.contacts.get(i).getName() + " " + DummyData.contacts.get(i).getPhone());
                s.append(DummyData.contacts.get(i).getName() + " " + DummyData.contacts.get(i).getPhone());
                s.append("\n");
                num++;

            }
        }

        if(num == 0){
            name.setText("");
        }
        else if(num >1){
            name.setText(s);
        }
        num = 0;




    }

    //dial id 호출을 위한 함수
    private int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType, ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + resName);
        } else {
            return ResourceID;
        }
    }

    //하이픈 입력되는 함수
    private String changeToDial(String phoneNum) {
        //전화번호 기준
        //4글자 이상일 때 3번째 숫자 다음에 -  (010-3
        // 8글자 이상일 때 3번째 다음과 7번째 다음에 - (010-3744-0
        //12글자 이상이면 - 전부 제거
        //특수문자 * # 있으면 - 전부 제거


        phoneNum = phoneNum.replaceAll("-", "");
        if (phoneNum.length() >= 4 && phoneNum.length() <= 7) {//010539
            phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3);
        } else if (phoneNum.length() >= 8 && phoneNum.length() <= 11) {
            phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7);
        } else if (phoneNum.length() > 11) {
            phoneNum = phoneNum.replaceAll("-", "");

        }

        if (phoneNum.contains("*") || phoneNum.contains("#") || phoneNum.contains("+")) {
            phoneNum = phoneNum.replaceAll("-", "");

        }

        return phoneNum;

    }


}
