package com.example.practice19;

import java.util.ArrayList;
import java.util.List;

public class UserStaticInfo {

    public final static String POSITION = "position";

    public final static String USERS_SIGN_IN_INFO = "UsersSignInInfo";
    public final static String USERS_PROFILE_INFO = "UsersProfileInfo";
    public final static String PASSWORD = "password";
    public final static String PROFILE_ID = "profileId";
    public final static String NAME = "name";
    public final static String AGE = "age";
    public final static String STATE = "state";

    public static String profileId;

    public static List<User> users = new ArrayList<>();

    public UserStaticInfo() {
        if (users.size() == 0) {
            users.add(new User("Софья Сергеева", "В сети", 18, 1));
            users.add(new User("Анна Миронова", "Занята", 20, 2));
            users.add(new User("Максим Орлов", "На занятиях", 18, 0));
            users.add(new User("Екатерина Волкова", "Доступна", 19, 1));
        }
    }
}
