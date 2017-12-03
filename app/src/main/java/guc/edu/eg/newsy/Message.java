package guc.edu.eg.newsy;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;



/**
 * Created by alaa on 12/1/17.
 */

public class Message implements IMessage, MessageContentType.Image {
    private String id;
    private String text;
    private IUser user;
    private Date createdAt;
    private String url;
    private String image;

    public Message(String id, IUser user, String text){
        this.id = id;
        this.user = user;
        this.text = text;
        this.createdAt = new Date();
    }


    public String getUrl(){
        return url;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setText(String text) {
        this.text = text;
    }

    public void setUrl(String url){
        this.url = url;
    }
    public String getImageUrl(){ return image;}
    public void setImage(String image){
        this.image = image;
    }

}