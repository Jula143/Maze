package mobapp.maze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Map;
import java.util.Set;

public class LoadMaze extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView mazeNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_load_maze);

        mazeNames = findViewById(R.id.mazeNames);

        sp = getSharedPreferences("MazePreferences", Context.MODE_PRIVATE);
        editor = sp.edit();

        showAllNames();
    }
    private void showAllNames(){
        Map<String, ?> allEntries = sp.getAll();

        StringBuilder stringBuilder = new StringBuilder();
        Set<String> keySet = allEntries.keySet();

        for (String key : keySet) {
            stringBuilder.append(key).append("\n");
        }
        mazeNames.setText("List of saved mazes:\n"+stringBuilder.toString());
    }

    public void loadMaze(View v){
        EditText name = findViewById(R.id.name);
        String mazeName = name.getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", mazeName);
        setResult(Activity.RESULT_OK, resultIntent);
        this.finish();
    }
}