package cs2b01.utec.chat_mobile;

import android.app.Activity;
import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    JSONArray messages = new JSONArray();
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    public Activity getActivity(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        String username = getIntent().getExtras().getString("username");
        setTitle("@"+username);
        mRecyclerView = findViewById(R.id.main_recycler_view);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getMessages();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(true){
                     try{
                         Thread.sleep(1000);
                         getMessages();
                     }
                     catch (InterruptedException e){
                         e.printStackTrace();
                     }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void onClickBtnSend(View v){
        postMessage();
        EditText chatBox = findViewById(R.id.txtMessage);
        chatBox.setText("");
    }

    public void getMessages(){
        final int userFromId = getIntent().getExtras().getInt("user_from_id");
        final int userToId = getIntent().getExtras().getInt("user_to_id");

        String uri = "http://10.0.2.2:5000/messages/"+userFromId+"/"+userToId;
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

    public void postMessage(){
        String uri = "http://10.0.2.2:5000/messages";
        RequestQueue queue = Volley.newRequestQueue(this);
        Map <String,String> params = new HashMap<>();
        final String user_from_id = getIntent().getExtras().get("user_from_id").toString();
        final String user_to_id = getIntent().getExtras().get("user_to_id").toString();
        final String content = ((EditText)findViewById(R.id.txtMessage)).getText().toString();
        params.put("user_from_id",user_from_id);
        params.put("user_to_id",user_to_id);
        params.put("content",content);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                uri,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                error.printStackTrace();

            }
        });
        queue.add(jsonObjectRequest);
    }

}

