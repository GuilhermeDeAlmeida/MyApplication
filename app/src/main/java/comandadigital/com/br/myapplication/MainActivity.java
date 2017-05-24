package comandadigital.com.br.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button scanBtn;
    String qrCodeContent;
    ArrayList<String> qrCodes;
    MainActivityConector requester = new MainActivityConector();
    String url = "https://techdion.com.br/networkpesquisa/ws";
    TextView respostaSistema;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity activity = this;
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        loadQRCodeIntentIntegrator(intentIntegrator);
    }
    private void loadQRCodeIntentIntegrator(IntentIntegrator intentIntegrator){
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scan");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){
            if (result.getContents() == null){
                Toast.makeText(this, "Você cancelou o scan", Toast.LENGTH_LONG).show();
            }
            else {
                setQrCodeContent(result.getContents());
                Toast.makeText(this, "Valor atualizado no Web Service: " + "' "+ qrCodeContent + " '", Toast.LENGTH_LONG).show();
                finish();
                try {
                    consultarQRCode();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void setQrCodeContent(String qrCodeContent){
        this.qrCodeContent = qrCodeContent;
    }
    public void consultarQRCode(){
        requester = new MainActivityConector();
        if (requester.isConnected(this)){
            //progess bar TODO
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        qrCodes = requester.get(url,qrCodeContent);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Toast toast = Toast.makeText(this, "Rede indisponível!", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
