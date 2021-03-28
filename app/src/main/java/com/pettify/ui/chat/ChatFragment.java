package com.pettify.ui.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pettify.R;
import com.pettify.model.user.User;
import com.pettify.ui.auth.AuthViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference myDatabase;
    EditText myEditText;
    Button myButton;

    ListView lvDiscussion;
    ArrayList<String> listConversation = new ArrayList<String>();
    ArrayAdapter arrayAdpt;

    String UserName, user_msg_key, SelectedTopic;

    EditText etMsg;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View root;



    private AuthViewModel authViewModel;
    User currentUser;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  myDatabase= FirebaseDatabase.getInstance().getReference("Message");
         root = inflater.inflate(R.layout.fragment_chat, container, false);
        TextView myText=root.findViewById(R.id.textbox);
         myButton=root.findViewById(R.id.button1);
      //  UserName = getIntent().getExtras().get("user_name").toString();

        lvDiscussion = (ListView) root.findViewById(R.id.lvConversation);
        arrayAdpt=new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);//getActivity().GetApplicationContext()

        lvDiscussion.setAdapter(arrayAdpt);
        etMsg=root.findViewById(R.id.etMessage);


        if (user != null) {
            // Name, email address etc
            String name = user.getDisplayName();
            String email = user.getEmail();
//            Log.d("TAG", email);
//            Log.d("TAG", name);
            UserName=name;
            String uid = user.getUid();
            Log.d("TAG", uid);
        }
        else{
            Log.d("TAG","Not working to");
            UserName="Guest";
        }

        DatabaseReference  dbr = FirebaseDatabase.getInstance().getReference().child("mainRoom");

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                user_msg_key = dbr.push().getKey();
                dbr.updateChildren(map);

                DatabaseReference dbr2 = dbr.child(user_msg_key);
                Map<String, Object> map2 = new HashMap<String, Object>();

                String msg=etMsg.getText().toString();
                if(etMsg!=null) {
                    map2.put("msg", etMsg.getText().toString());
                    map2.put("user", UserName);
                    dbr2.updateChildren(map2);
                }
                etMsg.setText("");
            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return root;
    }

    public void updateConversation(DataSnapshot dataSnapshot){
        String msg, user, conversation;
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            msg = (String) ((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();

            conversation = user + ": " + msg;
            arrayAdpt.insert(conversation, arrayAdpt.getCount());
            arrayAdpt.notifyDataSetChanged();
        }
    }

}