package mvvm.view.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.androidminiproject.R;

import mvvm.view.LoginActivity;

public class PersistSuccessDialog extends AppCompatActivity {

    private String redirect_to, message;
    TextView message_text, btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_persist_success);

        message_text = findViewById(R.id.message);
        btn = findViewById(R.id.proceed_btn);



        Bundle extras = getIntent().getExtras();
        this.redirect_to = extras.getString("REDIRECT_TO");
        this.message = extras.getString("MESSAGE");
        btn.setText(extras.getString("BTN_TEXT"));

        message_text.setText(this.message);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (redirect_to){
                    case "":
                        finish();
                        break;

                    case "LOGIN":
                        Intent loginIntent = new Intent(PersistSuccessDialog.this, LoginActivity.class);
                        startActivity(loginIntent);
                        break;
                }
            }
        });
    }
}
