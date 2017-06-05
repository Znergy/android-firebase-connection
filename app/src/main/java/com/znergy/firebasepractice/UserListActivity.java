package com.znergy.firebasepractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.znergy.firebasepractice.models.User;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity implements OnDataAvailable {
    private static final String TAG = UserListActivity.class.getSimpleName();
    @Bind(R.id.lvUserAccounts) ListView lvUserAccounts;

    private DatabaseReference userAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        /** Firebase Reference to users section of database */
        userAccounts = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users");

        /** AsyncTask for Firebase */
        Firebase firebase = new Firebase(this, userAccounts);
        firebase.execute();

        /** Firebase userList from users */
        getUsersFromFirebase(userAccounts, this);

//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userList);

//        lvUserAccounts.setAdapter(adapter);

        lvUserAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                Toast.makeText(UserListActivity.this, "Name: " + name, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(UserListActivity.this, UserProfilePage.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onDataAvailable(ArrayList<User> users) {
        Log.d(TAG, "onDataAvailable: users is " + users);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        lvUserAccounts.setAdapter(adapter);
    }

    private void getUsersFromFirebase(DatabaseReference reference, final OnDataAvailable callback) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: starts");
                ArrayList<User> users = new ArrayList<>();
                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    /** How to pull out individual fields */
//                    String firstName = (String) userSnapShot.child("firstName").getValue();
//                    String lastName = (String) userSnapShot.child("lastName").getValue();
//                    String email = (String) userSnapShot.child("email").getValue();
//                    String phoneNumber = (String) userSnapShot.child("phoneNumber").getValue();
//                    User user = new User(firstName, lastName, email, phoneNumber);
//                    Log.d(TAG, "onDataChange: user is " + user);

                    /** Pulling out the entire object */
                    User user = userSnapShot.getValue(User.class);

                    /** Adding to users ArrayList */
                    users.add(user);
                }

                Log.d(TAG, "onDataChange: ends");
                callback.onDataAvailable(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Firebase read failed " + databaseError.getMessage());
            }
        });
    }
}
