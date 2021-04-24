package com.example.project.JackFunc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.Services.MyService;
import com.example.project.TimerFunc.ApkInfoExtractor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mainF#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mainF extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private RecyclerView.Adapter adapter;

    public mainF() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mainF.
     */
    // TODO: Rename and change types and number of parameters
    public static mainF newInstance(String param1, String param2) {
        mainF fragment = new mainF();
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
    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
    Button bStart,bStop;
    boolean Microphone_Plugged_in = false;
    IntentFilter filter = new IntentFilter("android.intent.action.HEADSET_PLUG");
    BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context,"Headphone connect",Toast.LENGTH_LONG).show();
            final String action = intent.getAction();
            int iii=2;
            if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                iii=intent.getIntExtra("state", -1);
                if(Integer.valueOf(iii)==0){
                    Microphone_Plugged_in = false;
                    Toast.makeText(context,"microphone not plugged in",Toast.LENGTH_LONG).show();
                }if(Integer.valueOf(iii)==1){
                    Microphone_Plugged_in = true;
                    Toast.makeText(context,"microphone plugged in",Toast.LENGTH_SHORT).show();
                    String App_name = intent.getStringExtra("App");
                    System.out.println(App_name);
                    Intent inten = context.getPackageManager().getLaunchIntentForPackage(App_name);
                    if(inten != null){

                        context.startActivity(inten);

                    }
                    else {
                        System.out.println("In elseeeeeeeeeeeeeeeeeeeee");
                        Toast.makeText(context,App_name + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
                    }
////                    String mimeType = "audio";
////                    ShareCompat.IntentBuilder
////                            .from(getActivity())
////                            .setType(mimeType)
////                            .setChooserTitle("Open with: ")
////                            .startChooser();
//                    SecondFragment sf = new SecondFragment();
//                    getFragmentManager().beginTransaction().replace(R.id.mainact,sf).commit();
                }
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        bStart=v.findViewById(R.id.b1);
        bStop=v.findViewById(R.id.b2);
        System.out.println("MPARAMMMMMMMMMMMMMMM"+mParam1);
        bStart.setOnClickListener(new View.OnClickListener(){
            Intent serIntent;
            @Override
            public void onClick(View v) {
                serIntent=new Intent(getActivity(), MyService.class);
                if(mParam1!=null){
                    serIntent.putExtra("presence","True");
                    serIntent.putExtra("App",mParam1);
                }else {
                    serIntent.putExtra("presence","False");
                }
                getActivity().startService(serIntent);
                System.out.println(Microphone_Plugged_in);
//                SecondFragment sf = new SecondFragment();
//                getFragmentManager().beginTransaction().replace(R.id.mainact,sf).commit();
            }
        });
        bStop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                Intent ts= new Intent(getContext(),MyService.class);
                Intent ts= new Intent(getActivity(),MyService.class);
                getActivity().stopService(ts);
            }
        });
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view2);
        recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new AppAdap(getActivity(), new ApkInfoExtractor(getActivity()).GetAllInstalledApkInfo());
        recyclerView.setAdapter(adapter);

        return v;
    }


}