package mobapp.maze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ParametersActivity extends AppCompatActivity {

    public static String mazeName = "default";
    public static int mazeSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parameters);
    }

    public void save(View v){
        EditText name = findViewById(R.id.name);
        EditText size = findViewById(R.id.size);

        mazeName = name.getText().toString();
        String sizeStr = size.getText().toString();

        if (sizeStr.isEmpty()) {
            size.setError("Please enter a maze size.");
            return;
        }

        mazeSize = Integer.parseInt(sizeStr);
        if (mazeSize < 10 || mazeSize > 50) {
            size.setError("Maze size must be between 10 and 50.");
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", mazeName);
        resultIntent.putExtra("size", mazeSize);
        setResult(Activity.RESULT_OK, resultIntent);
        this.finish();
    }
}