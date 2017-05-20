package comandadigital.com.br.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private Button scanBtn;
    String qrCodeContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (Button) findViewById(R.id.scan_btn);

        final Activity activity = this;
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                loadQRCodeIntentIntegrator(intentIntegrator);
            }
        });
    }
    private void loadQRCodeIntentIntegrator(IntentIntegrator intentIntegrator){
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scan");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }
    private void getText() throws UnsupportedEncodingException {
        String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(qrCodeContent, "UTF-8");
        String text = "";
        BufferedReader reader = null;
        try {
            URL url = new URL("https://techdion.com.br/ws/index.php");
            Toast.makeText(this, "Chegou a colocar a URL", Toast.LENGTH_LONG).show();
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                Toast.makeText(this, "Fechou a conexão", Toast.LENGTH_LONG).show();
                reader.close();
            }

            catch(Exception ex) {}
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){
            if (result.getContents() == null){
                Toast.makeText(this, "Você cancelou o scan", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                setQrCodeContent(result.getContents());
                try {
                    getText();
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
    public void test(){
        StringRequest request
    }
}
