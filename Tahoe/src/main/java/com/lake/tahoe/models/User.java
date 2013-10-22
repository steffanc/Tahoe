package com.lake.tahoe.models;

import android.content.Context;

import com.orm.SugarRecord;

/**
 * Created on 10/21/13.
 */
public class User extends SugarRecord<User> {
    String name;
    String email;

    public User(Context ctx) {
        super(ctx);
    }

    public User(Context ctx, String name, String email) {
        super(ctx);

        this.name = name;
        this.email = email;
    }

}
