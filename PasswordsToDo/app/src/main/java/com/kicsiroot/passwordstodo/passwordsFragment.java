package com.kicsiroot.passwordstodo;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link passwordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class passwordsFragment extends Fragment {

    PostAdapter postAdapter;
    RecyclerView postRV;
    FirebaseDatabase fDb;
    DatabaseReference dbRef;
    List<Post> postList;
    FloatingActionButton fb_pwd;
    Passwords activity;
    ArrayList list;
    //TextView popUrl, popEmail, popUsername, popPassword;
    Toolbar pwdToolbar;
    Dialog popAddPwd;
    EditText popUrl, popEmail, popUsername, popPassword;
    Button popButt;
    FirebaseUser currentUser;
    FirebaseAuth auth;
    String postKey;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public passwordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment passwordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static passwordsFragment newInstance(String param1, String param2) {
        passwordsFragment fragment = new passwordsFragment();
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
        View pwdView =  inflater.inflate(R.layout.fragment_passwords, container, false);
        postRV = pwdView.findViewById(R.id.postRv);
        postRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        fDb = FirebaseDatabase.getInstance();
        dbRef = fDb.getReference("Post");
        return pwdView;
    }

    @Override
    public void onStart() {
        super.onStart();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap: snapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter = new PostAdapter(getActivity(), postList);
                postRV.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}