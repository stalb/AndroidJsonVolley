package fr.usmb.iutc.mmi.s4.androidvolleyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.PasswordAuthentication;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private JsonObjectRequest rq;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)this.findViewById(R.id.text);

        // Gestion des cookies
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        // Authentification HTTP
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("stalb", "password".toCharArray());
            }
        };
        Authenticator.setDefault(auth);

        // creation de la queue de requetes volley
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray tabRes = null;
                try {
                    tabRes = response.getJSONObject("data").getJSONObject("result").getJSONArray("items");
                    System.out.println(response.getJSONObject("data"));
                    System.out.println(response.getJSONObject("data").getJSONObject("result"));
                    System.out.println(tabRes);
                    StringBuilder text = new StringBuilder();
                    for(int i = 0; i < tabRes.length(); i++){
                        JSONObject res = tabRes.getJSONObject(i);
                        text.append("<h2>").append(res.getString("title")).append("</h2>");
                        text.append(res.getString("desc")).append("<br/>");
                        text.append(res.getString("url"));
                        text.append("<br/>").append("<br/>");
                    }
                    textView.setText(Html.fromHtml(text.toString(), Html.FROM_HTML_MODE_COMPACT));
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        rq.setTag(this);
        requestQueue.add(rq);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        rq.cancel();
        requestQueue.cancelAll(this);
    }
}
