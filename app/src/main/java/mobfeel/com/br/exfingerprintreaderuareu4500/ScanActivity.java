package mobfeel.com.br.exfingerprintreaderuareu4500;

import android.content.Intent;
import android.os.Looper;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import asia.kanopi.fingerscan.Fingerprint;
import asia.kanopi.fingerscan.Status;

public class ScanActivity extends AppCompatActivity {

    private TextView tvStatus;

    private Fingerprint fingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        init();

    }

    @Override
    protected void onStart() {
        fingerprint.scan(ScanActivity.this, printHandler, updateHandler);
        super.onStart();
    }

    @Override
    protected void onStop() {
        fingerprint.turnOffReader();
        super.onStop();
    }

    Handler updateHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int status = msg.getData().getInt("status");
            switch (status) {
                case Status.INITIALISED:
                    tvStatus.setText("Setting up reader");
                    break;
                case Status.SCANNER_POWERED_ON:
                    tvStatus.setText("Reader powered on");
                    break;
                case Status.READY_TO_SCAN:
                    tvStatus.setText("Ready to scan finger");
                    break;
                case Status.FINGER_DETECTED:
                    tvStatus.setText("Finger detected");
                    break;
                case Status.RECEIVING_IMAGE:
                    tvStatus.setText("Receiving image");
                    break;
                case Status.FINGER_LIFTED:
                    tvStatus.setText("Finger has been lifted off reader");
                    break;
                case Status.SCANNER_POWERED_OFF:
                    tvStatus.setText("Reader is off");
                    break;
                case Status.SUCCESS:
                    tvStatus.setText("Fingerprint successfully captured");
                    break;
                case Status.ERROR:
                    tvStatus.setText("Error");
                    toast(msg.getData().getString("errorMessage"));
                    break;
                default:
                    tvStatus.setText(String.valueOf(status));
                    toast(msg.getData().getString("errorMessage"));
                    break;
            }
        }
    };

    Handler printHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            byte[] image;
            String errorMessage = "";
            int status = msg.getData().getInt("status");

            Intent intent = new Intent();
            intent.putExtra("status", status);

            if (status == Status.SUCCESS) {
                intent.putExtra("img", msg.getData().getByteArray("img"));
            } else {
                intent.putExtra("errorMessage", msg.getData().getString("errorMessage"));
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private void toast(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void init() {
        tvStatus = findViewById(R.id.sa_tv_status);
        fingerprint = new Fingerprint();
    }
}
