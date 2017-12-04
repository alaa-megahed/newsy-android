package guc.edu.eg.newsy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity  implements MessagesListAdapter.OnMessageClickListener {
    protected MessagesListAdapter<Message> adapter;
    protected String uuid = "1234";
    boolean firstTime = true;
    protected User newsy;
    protected String welcomeMessage = "Hey, hey!\nWould you like to see the top stories, most viewed, or latest news?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if(firstTime) {
            System.out.println("FIRST TIME");
            firstTime = false;
            getWelcome();
        }
    }

    void getWelcome () {
        String url = "https://dry-sea-26759.herokuapp.com/welcome";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            uuid = response.get("uuid").toString();
                            initAdapter();
                            RelativeLayout buttons =  findViewById(R.id.buttons);
                            buttons.setVisibility(View.VISIBLE);
                            System.out.println("UUID:" + uuid);
                        } catch (JSONException e) {
                            System.out.println(e.getMessage());
                        }

                        System.out.println("RESPONSE " + response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR: " + error);
                    }
                });
        Singleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsObjRequest);
    }
    void postMessage(String message) throws JSONException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("message", message);
        String url = "https://dry-sea-26759.herokuapp.com/chat";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            MyJSONParser parser = new MyJSONParser(new JSONObject(response.toString()));
                            if (parser.getType().equals("string")) {
                                Message message = new Message(new Random(10000).toString(), newsy, parser.getMessage());
                                adapter.addToStart(message, true);
                            } else if (parser.getType().equals("json")) {
                                JSONArray jsonArray = parser.getJsonArray();
                                for (int i = 0; i < Math.min(jsonArray.length(), 10); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String title = obj.get("title").toString();
                                    Message message = new Message(new Random(10000).toString(), newsy, Html.fromHtml("<u>"+title+"</u>").toString());
                                    message.setUrl(obj.get("url").toString());
                                    if(i == 0)
                                    {
                                        adapter.addToStart(message, true );
                                        System.out.println("i == 0");
                                    }
                                    else
                                        adapter.addToStart(message, false );
                                    if(!(obj.get("image").toString()).equals(""))
                                    {
                                        Message messageImage = new Message(new Random(10000).toString(), newsy, obj.get("image").toString());
                                        messageImage.setImage(obj.get("image").toString());
                                        if(i == 0)
                                            adapter.addToStart(messageImage, true);
                                        else
                                            adapter.addToStart(messageImage, false);
                                    }
                                }

                                RelativeLayout buttons = findViewById(R.id.buttons);
                                buttons.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR: " + error);

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", uuid);
                return params;
            }
        };

        Singleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsObjRequest);
    }
    protected void initAdapter() {
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(MainActivity.this).load(url).into(imageView);
            }
        };
        adapter = new MessagesListAdapter<Message>(uuid, imageLoader);
        adapter.setOnMessageClickListener(this);
        MessagesList messagesList = findViewById(R.id.messagesList);
        messagesList.setAdapter(adapter);

        MessageInput inputView = findViewById(R.id.input);
        newsy = new User("12345", "newsy", null, true);
        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Message message = new Message(new Random(10000).toString(), (new User(uuid, "user", null, true)), input.toString());
                adapter.addToStart(message, true);
                try {
                    postMessage(input.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        //put welcome message
        Message message = new Message(new Random(10000).toString(), newsy, welcomeMessage);
        adapter.addToStart(message, true);
    }

    public void onMessageClick(IMessage message) {
        if(((Message)message).getUrl() != null){
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse(((Message)message).getUrl()));
            startActivity(viewIntent);
        }
    }

    public void quickReply(View view) throws JSONException {
        Button button = (Button) view;
        String msg = "";
        boolean reset = false;
        if(button.getId() == R.id.top_stories) {
            postMessage("top stories");
            msg = "top stories";
        } else if(button.getId() == R.id.most_viewed) {
            postMessage("most viewed");
            msg = "most viewed";
        } else if(button.getId() == R.id.latest){
            postMessage("latest");
            msg = "latest news";
        } else if(button.getId() == R.id.reset){
            postMessage("reset");
            msg = "reset";
            reset = true;
        }
        Message message = new Message(new Random(10000).toString(), (new User(uuid, "user", null, true)), msg);
        adapter.addToStart(message, true);
        RelativeLayout buttons = findViewById(R.id.buttons);
        if(reset)
            buttons.setVisibility(View.VISIBLE);
        else
            buttons.setVisibility(View.GONE);
    }
}