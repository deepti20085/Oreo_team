package com.example.project.greetingswisher;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.broadcastreceivers.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class SmsWisher extends Fragment {
    private EditText TextEditor_mobileNo, TextEditor_date, TextEditor_message;
    private String mobileNo ,occasionType,msg;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button saveButton;
    public static int eId= 0;
    Calendar calendar = Calendar.getInstance();
    int PERMISSION_REQUEST_SEND_SMS = 1;
    private OnItemSelectedListener itemSelectedListener;
    public static final String[] monthlist = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final String[] occasionList = {"Birthday", "Anniversary", "Other"};
    public static final String[] wishList = {"Happy birthday!! I hope your day is filled with lots of love and laughter! May all of your birthday wishes come true.", "Congratulations on another year of falling deeper in love with each other. Happy anniversary!!"};
    static final int REQUEST_SELECT_CONTACT = 200;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SmsWisher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SmsWisher.
     */
    // TODO: Rename and change types and number of parameters
    public static SmsWisher newInstance(String param1, String param2) {
        SmsWisher fragment = new SmsWisher();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_sms_wisher, container, false);
        ImageButton b = (ImageButton) view.findViewById(R.id.open_phoneBook);
        TextEditor_mobileNo = (EditText) view.findViewById(R.id.editText_mobileNo);
        TextEditor_date = (EditText) view.findViewById(R.id.editText_date);
        TextEditor_date.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showCalendar(calendar, v);
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
                        TextEditor_date.setText(dayOfMonth + " " + monthlist[month] );
                    }
                };
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openPhoneBook(v);
            }
        });
        TextEditor_mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mobileNo = TextEditor_mobileNo.getText().toString().trim();
                if(validateMobileNo())
                setMobileNo();
                else
                    TextEditor_mobileNo.setError("Please enter  valid 10 digit mobile number");

            }


        });

        TextEditor_message = (EditText) view.findViewById(R.id.editText_message);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_occasion);

        List<String> occasions = new ArrayList<String>();
        occasions.add(0,"Select Occasion");
        for (int i = 0;i< occasionList.length; ++i)
            occasions.add(occasionList[i]);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, occasions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select Occasion");
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occasionType = (String) parent.getItemAtPosition(position);
                if(occasionType.equals("Select Occasion")){
                    TextEditor_message.setVisibility(View.INVISIBLE);
                   // TextEditor_message.setError("Please Select an occasion");
                }else{
                    TextEditor_message.setVisibility(View.VISIBLE);
                    if(occasionType.equals("Birthday")){
                        TextEditor_message.setText(wishList[0]);
                    }else if(occasionType.equals("Anniversary")){
                        TextEditor_message.setText(wishList[1]);
                    }else if(occasionType.equals("Other")){
                        TextEditor_message.setText("");
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextEditor_message.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                msg = TextEditor_message.getText().toString();
                if(msg.length()== 0)
                TextEditor_message.setError("Please enter a message");
            }


        });

        saveButton = (Button) view.findViewById(R.id.smsWisher_SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "save button clicked", Toast.LENGTH_SHORT).show();
                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        sendSMS();
                    }
                } else {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
                }
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_SEND_SMS){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sendSMS();
            }
        }
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(getActivity(), AlarmReceiver.class);
        String msg = TextEditor_message.getText().toString();
        String no = TextEditor_mobileNo.getText().toString();
        no = no.replaceAll("\\+91", "");
        intent.putExtra("sms", msg );
        intent.putExtra("receiver_number", no);
        Log.d("autodroid", no);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(),12345,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendSMS(){
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,2);
        calendar.set(Calendar.SECOND,0);
        startAlarm(calendar);
    }

    //validate mobileNo
    public boolean validateMobileNo(){
        String number = TextEditor_mobileNo.getText().toString();
        if(number.isEmpty() || number.length()<14){
            return false;
        }
        return true;
    }

    // display popup calendar
    public void showCalendar(Calendar calendar, View view){
        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);
        datePickerDialog.show();
    }

    // creating implicit intent to open contact list.
    public void openPhoneBook(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == Activity.RESULT_OK) {
            if(data!=null){
                //getting data from intent.
                Uri uri = data.getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    mobileNo = cursor.getString(numberIndex);
                    mobileNo = PhoneNumberUtils.formatNumber(mobileNo);
                    mobileNo = mobileNo.replaceAll("\\s", "");
                    setMobileNo();
                }
            }

        }
    }
    //setting mobile no in edittext
    public void setMobileNo(){
        if(mobileNo.startsWith("+91") && mobileNo.length() == 13) {
            mobileNo = mobileNo.replaceAll("\\+91", "");
            TextEditor_mobileNo.setText(mobileNo);
            Selection.setSelection(TextEditor_mobileNo.getText(), TextEditor_mobileNo.getText().length());
        }else if(mobileNo.startsWith("0") && mobileNo.length() == 11) {
            mobileNo = mobileNo.replaceAll("0", "");
            TextEditor_mobileNo.setText(mobileNo);
            Selection.setSelection(TextEditor_mobileNo.getText(), TextEditor_mobileNo.getText().length());
        }else if(!mobileNo.startsWith("+91") && mobileNo.length() == 10){
            TextEditor_mobileNo.setText(mobileNo);
            Selection.setSelection(TextEditor_mobileNo.getText(), TextEditor_mobileNo.getText().length());
        }


    }




}