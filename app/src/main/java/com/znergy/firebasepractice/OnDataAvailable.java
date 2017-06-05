package com.znergy.firebasepractice;

import com.znergy.firebasepractice.models.User;

import java.util.ArrayList;

/**
 * Created by administrator on 6/5/17.
 */

public interface OnDataAvailable {
    void onDataAvailable(ArrayList<User> users);
}
