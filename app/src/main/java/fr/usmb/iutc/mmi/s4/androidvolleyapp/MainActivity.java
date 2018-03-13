package fr.usmb.iutc.mmi.s4.androidvolleyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private JsonObjectRequest rq;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)this.findViewById(R.id.text);
        // creation de la queue de requetes volley
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textView.setText(response.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Erreur : "+error.getMessage());
            }
        };

        String url = "https://api.qwant.com/egp/search/web?count=10&q=mmi+chambery&offset=0";
        rq = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
        requestQueue.add(rq);
    }
}
