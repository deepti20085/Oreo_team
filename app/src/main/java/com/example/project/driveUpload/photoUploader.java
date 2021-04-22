package com.example.project.driveUpload;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.broadcastreceivers.ReceiveAlarmUploadReceiver;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


public class photoUploader extends Fragment {
    private Button uploader, imgBtn , imgUploader ;
    private int SIGN_REQUEST_CODE = 200, eventId = 0;
    private String TAG = "autodroid";
    public static Drive drive;
    private ArrayList<String> imageArrayList = new ArrayList<String>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public photoUploader() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment photoUploader.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.project.driveUpload.photoUploader newInstance(String param1, String param2) {
        com.example.project.driveUpload.photoUploader fragment = new com.example.project.driveUpload.photoUploader();
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
        View view = inflater.inflate(R.layout.fragment_photo_uploader, container, false);

        imgUploader = (Button) view.findViewById(R.id.Btn_enablePhotoUpload);
        imgUploader.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean internetStatus = checkInternetConnection();
                Log.d(TAG, "internet status: " + internetStatus);
                if(internetStatus){
                    Log.d(TAG, "entered inside the if cond of INTStatus");
                    RequestForGoogleAccountSignUp();
                    startAlarm();
                }else{
                    Toast.makeText(getContext(),
                            "Internet connection is not available. Cannot Enable Feature.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) ;

        return view;
    }

    public boolean checkInternetConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), ReceiveAlarmUploadReceiver.class);
        //intent.putExtra("Drive ", (Serializable) drive);
        //intent.putExtra("frag Context", getActivity());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(),eventId,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        eventId +=1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.MINUTE, 00);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }


    public void RequestForGoogleAccountSignUp(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(new Scope("https://www.googleapis.com/auth/drive"),new Scope("https://www.googleapis.com/auth/drive.appdata"),new Scope("https://www.googleapis.com/auth/drive.file"), new Scope("https://www.googleapis.com/auth/drive.metadata"), new Scope("https://www.googleapis.com/auth/drive.scripts")).build();
        //requestScopes(new Scope(DriveScopes.DRIVE_FILE))  requestScopes(new Scope("https://www.googleapis.com/auth/drive"))
        Log.d("autodroid", "signUpoptions" +String.valueOf(googleSignInOptions));
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        Log.d("autodroid", "signin client" +String.valueOf(googleSignInClient));
        Log.d("autodroid", "signin client intent" +String.valueOf(googleSignInClient.getSignInIntent()));
        startActivityForResult(googleSignInClient.getSignInIntent(), SIGN_REQUEST_CODE);
        Log.d("autodroid","called startActivityfor result");
        googleSignInClient.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("autodroid", String.valueOf(requestCode)+ " "+ String.valueOf(resultCode));
        Log.d("autodroid", String.valueOf(data));
        if(requestCode == SIGN_REQUEST_CODE && resultCode == RESULT_OK){
            GoogleSignIn.getSignedInAccountFromIntent(data).addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                @Override
                public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                    GoogleAccountCredential googleAccountCredential =  GoogleAccountCredential.usingOAuth2(getContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
                    Log.d("autodroid","credential "+ String.valueOf(googleAccountCredential));
                    googleAccountCredential.setSelectedAccount(googleSignInAccount.getAccount());
                    Log.d("autodroid","account "+ String.valueOf(googleSignInAccount.getAccount()));
                    drive = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), googleAccountCredential).setApplicationName("PhotoUploder").build();
                    Log.d("autodroid","dive "+ String.valueOf(drive));
                    Toast.makeText(getContext()," Successfull Sign Up ",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("autodroid","error" + e.getMessage());
                }
            });
        }else{
            Log.d("autodroid","enter in else part of activity result");
        }
    }



}