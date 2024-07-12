package mobapp.maze;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor accSensor;
    private MazeView mazeView;
    private MazeGenerator mazeGenerator;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        String mazeString = getIntent().getStringExtra("mazeGenerator");
        mazeGenerator = new MazeGenerator(mazeString);

        mazeView = new MazeView(this);
        mazeView.setGameActive(true);
        mazeView.setMazeGenerator(mazeGenerator);

        LinearLayout mainLayout = findViewById(R.id.main);
        mainLayout.addView(mazeView);

        game = new Game(mazeGenerator,mazeView);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void quit(View v){
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];

        if (Math.abs(x) > Math.abs(y)) {
            if (x < 0) {
                game.movePlayerRight();
            } else {
                game.movePlayerLeft();
            }
        } else {
            if (y < 0) {
                game.movePlayerUp();
            } else {
                game.movePlayerDown();
            }
        }

        if (mazeView.isGameActive() && game.isEndPoint()){
            gameEnd();
        }
    }

    private void gameEnd(){
        mazeView.setGameActive(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Congratulations! You have reached the exit of the maze.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}
