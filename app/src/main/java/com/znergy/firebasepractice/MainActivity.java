package com.znergy.firebasepractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.znergy.firebasepractice.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.btnSave) Button btnSave;
    @Bind(R.id.etFirstName) EditText etFirstName;
    @Bind(R.id.etLastName) EditText etLastName;
    @Bind(R.id.etEmail) EditText etEmail;
    @Bind(R.id.etPhoneNumber) EditText etPhoneNumber;


    /** Declaring Firebase variable */
    private DatabaseReference userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /** Assigning the Firebase Reference, basically we are telling Firebase where are
         * starting point should be inside our database. For this use case, we want the
         * userAccount variable to hold the reference to the users section of our database. */
        userAccount = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users");

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSave) {
            /** We pull the fields and create a User object */
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String email = etEmail.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            User user = new User(firstName, lastName, email, phoneNumber);

            /** After creating a user object, we then save to firebase by calling the
             * saveToFirebase() method */
            saveToFirebase(user);

            Intent intent = new Intent(MainActivity.this, UserListActivity.class);
            startActivity(intent);
        }
    }

    private void saveToFirebase(User user) {
        /** Store the user object in our users section of our database */
        userAccount.push().setValue(user);
    }
}
