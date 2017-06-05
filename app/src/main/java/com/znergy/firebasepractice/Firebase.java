package com.znergy.firebasepractice;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.znergy.firebasepractice.models.User;

import java.util.ArrayList;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Not In Use.. There are issues with improper return types.
 */

public class Firebase extends AsyncTask<String, Void, ArrayList<User>> {

    private OnDataAvailable callback;
    private DatabaseReference reference;

    private boolean downloadComplete = false;

    public Firebase(OnDataAvailable callback, DatabaseReference reference) {
        this.callback = callback;
        this.reference = reference;
    }

    @Override
    protected void onPostExecute(ArrayList<User> users) {
        if(callback != null) {
            callback.onDataAvailable(users);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    /** AsyncTask doesn't work, there is an issue with the inner async that is happening with firebase
     * when onDataChange is run it's not run prior to the return statemtent. Since I can't return
     * inside the onDataChange method, and even if I use a callback, I then don't have an
     * ArrayList to return to the onPostExecute method.. */
    @Override
    protected ArrayList<User> doInBackground(String... params) {
        final ArrayList<User> users = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: starts");
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Firebase read failed " + databaseError.getMessage());
            }
        });
        return users;
    }
}
