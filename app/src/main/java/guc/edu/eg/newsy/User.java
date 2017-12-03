package guc.edu.eg.newsy;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by alaa on 12/1/17.
 */

public class User implements IUser {
    String id;
    String name;
    String avatar;
    boolean online;
    public User (String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}