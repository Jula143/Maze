package mobapp.maze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class GeneratorActivity extends AppCompatActivity {

    private MazeGenerator mazeGenerator;
    private MazeView mazeView;

    private static final int PARAMETERS_CODE = 1;
    private static final int LOAD_MAZE_CODE = 2;
    String mazeName = "default";
    int mazeSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        mazeGenerator = new MazeGenerator(mazeSize);
        mazeView = new MazeView(this);
        mazeView.setMazeGenerator(mazeGenerator);

        LinearLayout mainLayout = findViewById(R.id.main);
        mainLayout.addView(mazeView);
    }

    public void parameters(View v){
        Intent intent = new Intent(this,ParametersActivity.class);
        startActivityForResult(intent,PARAMETERS_CODE);
    }

    @Override
    public void onActivityResult(int code, int returnCode, Intent results) {
        super.onActivityResult(code, returnCode, results);
        if((code == PARAMETERS_CODE) && (returnCode == RESULT_OK)) {
            mazeSize = results.getIntExtra("size",20);
            mazeName = results.getStringExtra("name");
        }
        else if((code == LOAD_MAZE_CODE) && (returnCode == RESULT_OK)) {
            String name = results.getStringExtra("name");
            loadFromSharedPref(name);
        }
    }

    public void generate(View v) {
        LinearLayout mainLayout = findViewById(R.id.main);
        mainLayout.removeView(mazeView);

        mazeGenerator = new MazeGenerator(mazeSize);
        mazeView = new MazeView(this);
        mazeView.setMazeGenerator(mazeGenerator);

        mainLayout.addView(mazeView);
    }

    public void play(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mazeGenerator", mazeGenerator.mazeToString());
        startActivity(intent);
    }

    public void saveMaze(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("MazePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String mazeString = mazeGenerator.mazeToString();
        editor.putString(mazeName, mazeString);
        editor.apply();

        Toast.makeText(this, "Maze saved", Toast.LENGTH_SHORT).show();
    }

    public void loadMaze(View v){
        Intent intent = new Intent(this,LoadMaze.class);
        startActivityForResult(intent,LOAD_MAZE_CODE);

    }

    public void loadFromSharedPref(String name){
        SharedPreferences sharedPreferences = getSharedPreferences("MazePreferences", Context.MODE_PRIVATE);
        String mazeString = sharedPreferences.getString(name, "");

        if (!mazeString.isEmpty()) {
            mazeGenerator = new MazeGenerator(mazeString);
            mazeView.setMazeGenerator(mazeGenerator);

            LinearLayout mainLayout = findViewById(R.id.main);
            mainLayout.removeView(mazeView);

            mainLayout.addView(mazeView);
            Toast.makeText(this, "Maze loaded", Toast.LENGTH_SHORT).show();

            mazeName = name;
        } else {
            Toast.makeText(this, "Maze not found in preferences", Toast.LENGTH_SHORT).show();
        }
    }

}


