package com.bauble_app.bauble;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bauble_app.bauble.auth.AuthChoiceFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by princ on 4/19/2017.
 */

public class ProfileFragment extends Fragment {
    private FragmentManager fragManager;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        fragManager = ((MainNavActivity) getActivity()).getMyFragManager();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        showName(v, database.getReference(), mAuth.getCurrentUser());
        attachButtonListener(v, R.id.profile_logout, new AuthChoiceFragment());
        return v;
    }

    private void showName(final View v, DatabaseReference FirebaseRef, FirebaseUser
            user) {
        FirebaseRef.child("users/" + user.getUid() + "/name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.getValue().toString();
                        ((TextView) v.findViewById(R.id.profile_username))
                                .setText(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //TODO: handle error
                    }
                });
    }

    public void attachButtonListener(View v, int resId, final Fragment fragment) {
        Button btn = (Button) v.findViewById(resId);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View btn) {
                mAuth.signOut();
                fragManager.beginTransaction().replace(R.id.content, fragment)
                        .commit();
            }
        });
    }
}