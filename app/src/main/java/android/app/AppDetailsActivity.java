package android.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mytestapp.R;

public class AppDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);
    }
}