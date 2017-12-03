package guc.edu.eg.newsy;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by alaa on 12/1/17.
 */

public class Singleton {
    public static Singleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private Singleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static  synchronized Singleton getInstance(Context context) {
        if(instance == null) {
            instance = new Singleton(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}

