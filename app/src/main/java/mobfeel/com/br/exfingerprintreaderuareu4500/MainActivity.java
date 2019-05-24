package mobfeel.com.br.exfingerprintreaderuareu4500;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import asia.kanopi.fingerscan.Status;

public class MainActivity extends AppCompatActivity {

    private ImageView ivFingerprint;
    private Button btScan;

    private static final int SCAN_FINGERPRINT = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, SCAN_FINGERPRINT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SCAN_FINGERPRINT:
                if (resultCode == RESULT_OK) {

                    int status = data.getIntExtra("status", Status.ERROR);

                    if (status == Status.SUCCESS) {
                        toast("Fingerprint OK!");

                        byte[] img = data.getByteArrayExtra("img");
                        Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        ivFingerprint.setImageBitmap(bm);
                        return;
                    }
                    toast(data.getStringExtra("errorMessage"));
                }
                break;
        }

    }

    private void toast(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void init(){
        ivFingerprint = findViewById(R.id.ma_iv_fingerprint);
        btScan = findViewById(R.id.ma_bt_scan);
    }
}