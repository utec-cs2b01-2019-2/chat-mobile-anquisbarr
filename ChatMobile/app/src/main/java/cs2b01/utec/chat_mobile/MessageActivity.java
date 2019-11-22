package cs2b01.utec.chat_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        String username = getIntent().getExtras().getString("username");
        setTitle("@"+username);
    }

    @Override
    protected void onResume(){
        super.onResume();
        getMessages();
    }

    public void getMessages(){
        final int userFromId = getIntent().getExtras().getInt("user_from_id");
        final int userToId = getIntent().getExtras().getInt("user_to_id");

        String uri = "http://10.0.2.2:8000/messages/"+userFromId+"/"+userToId;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                uri,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response){
                        //TODO process response
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
