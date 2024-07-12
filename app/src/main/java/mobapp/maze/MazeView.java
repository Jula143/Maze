package mobapp.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MazeView extends View {

    private MazeGenerator mazeGenerator;
    private boolean isGameActive = false;
    private int playerX, playerY; // Player's position in the maze

    public MazeView(Context context) {
        super(context);
    }
    public void setMazeGenerator(MazeGenerator mazeGenerator) {
        this.mazeGenerator = mazeGenerator;
        invalidate();
    }
    public void setPlayerPosition(int x, int y) {
        this.playerX = x;
        this.playerY = y;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mazeGenerator != null) {
            mazeGenerator.draw(canvas);
            if (isGameActive){
                drawPlayer(canvas);
            }
        }
    }
    private void drawPlayer(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            int cellSize = canvas.getWidth() / mazeGenerator.getCols();
            float radius = cellSize / 3.0f;

            float centerX = playerX * cellSize + cellSize / 2.0f;
            float centerY = playerY * cellSize + cellSize / 2.0f;

            canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public void setGameActive(boolean a) {
        isGameActive=a;
    }
}
