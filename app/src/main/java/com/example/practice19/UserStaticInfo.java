package com.example.practice19;

import java.util.ArrayList;
import java.util.List;

public class UserStaticInfo {

    public final static String POSITION = "position";

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
