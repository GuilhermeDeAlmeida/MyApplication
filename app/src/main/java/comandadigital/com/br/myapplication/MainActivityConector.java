package comandadigital.com.br.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;;import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guilh on 20/05/2017.
 */

public class MainActivityConector {
    OkHttpClient client = new OkHttpClient();


    public ArrayList<String> get(String url, String pId) throws IOException {
        ArrayList<String> lista = new ArrayList<>();
        RequestBody formBody = new FormEncodingBuilder()
                .add("post", pId)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();

        String a;
        String jsonStr = response.body().string();

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
        try{
            JSONArray root = new JSONArray(jsonStr);
            JSONObject item = null;
            for (int i = 0; i < root.length(); i++ ) {
                item = (JSONObject)root.get(i);

                String id = item.getString("post");
                lista.add(new String(id));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }finally {
            if(lista.size() == 0)
                lista.add(new String("VAZIO"));
            //Log.v("CervejaRequester", jsonStr);
        }
        return lista;
    }
    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
