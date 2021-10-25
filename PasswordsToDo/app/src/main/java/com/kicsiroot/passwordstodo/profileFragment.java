package com.kicsiroot.passwordstodo;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;


public class profileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    private View profileView;
    private RecyclerView profileRecycle;
    private DatabaseReference dbRef, UserRef;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    ImageView profilimg;
    TextView profilText;


    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    /*public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
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
    }*/
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_profile, container, false); //pass the correct layout name for the fragment

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        TextView text = (TextView) view.findViewById(R.id.profileEmail);
        text.setText(currentUser.getEmail());





        return profileView;
    }

    /*@Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions rvOptions = new FirebaseRecyclerOptions.Builder<UserProfile>().setQuery(dbRef, UserProfile.class).build();

        FirebaseRecyclerAdapter<UserProfile, UserProfileViewHolder> adapter =
                new FirebaseRecyclerAdapter<UserProfile, UserProfileViewHolder>(rvOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final UserProfileViewHolder holder, int i, @NonNull UserProfile userProfile) {
                        String userID = getRef(i).getKey().toString();
                        if (userID != null)
                        UserRef.child(userID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("image"))
                                {
                                    String profileImage = snapshot.child("image").getValue().toString();
                                    String profileEmail = snapshot.child("Email").getValue().toString();

                                    holder.userEmail.setText(profileEmail);
                                    Picasso.get().load(profileImage).placeholder(R.drawable.profile_pic).into(holder.profileIMG);
                                } else {
                                    String profileEmail = snapshot.child("Email").getValue().toString();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public UserProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile, parent, false);
                        UserProfileViewHolder viewHolder = new UserProfileViewHolder(view);
                        return viewHolder;
                    }
                };
        profileRecycle.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserProfileViewHolder extends RecyclerView.ViewHolder{

        TextView userEmail;
        RoundedImageView profileIMG;

        public UserProfileViewHolder(@NonNull View itemView) {
            super(itemView);

            userEmail = itemView.findViewById(R.id.user_email);
            profileIMG = itemView.findViewById(R.id.user_img);
        }
    }*/
}