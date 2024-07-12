package mobapp.maze;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Stack;

public class MazeGenerator  {

    private int cols, rows;
    private int cellSize; // size of 1 cell


    public enum CellState {
        WALL,
        EMPTY,
        PATH,
        PLAYER_PATH
    }

    public CellState[][] maze;

    // start and stop positions of the maze
    private Point start;
    private Point stop;

    public MazeGenerator(int size) {
        cols = size;
        rows = size;

        cols = cols - 1 + cols % 2;
        rows = rows - 1 + rows % 2;

        start = new Point(0, 1);
        stop = new Point(cols - 1, rows - 2);

        initialize();
        generate();
        setCellState(cols-1,rows-2,CellState.PATH); //set endpoint to path
    }

    public MazeGenerator(String mazeString) {
        String[] r = mazeString.split("\n");

        cols = r[0].length();
        rows = r.length;

        start = new Point(0, 1);
        stop = new Point(cols - 1, rows - 2);

        maze = new CellState[cols][rows];
        loadMazeFromString(mazeString);
    }



    private void initialize() {
        maze = new CellState[cols][rows];

        int i, j;

        for (j = 0; j < rows; j++) {
            maze[0][j] = CellState.WALL;
            maze[cols - 1][j] = CellState.WALL;
        }
        for (i = 0; i < cols; i++) {
            maze[i][0] = CellState.WALL;
            maze[i][rows - 1] = CellState.WALL;
        }

        // inside walls and empty cells
        for (i = 1; i < cols - 1; i += 2) {
            for (j = 1; j < rows - 1; j += 2) {
                maze[i][j] = CellState.EMPTY;
                maze[i + 1][j] = CellState.WALL;
                maze[i][j + 1] = CellState.WALL;
            }
        }
        for (i = 2; i < cols - 2; i += 2) {
            for (j = 2; j < rows - 2; j += 2) {
                maze[i][j] = CellState.WALL;
            }
        }
    }

    private void generate() {
        Point current, next;
        Stack<Point> history = new Stack<Point>();

        int nToVisit = (cols - 1) * (rows - 1) / 4;
        int nVisited = 1;

        current = new Point(start.x + 1, start.y);
        maze[current.x][current.y] = CellState.PATH;

        while (nVisited < nToVisit) {

            next = checkNext(current, CellState.EMPTY, 2);
            if (next != null) {
                int x = (current.x + next.x) / 2;
                int y = (current.y + next.y) / 2;
                maze[x][y] = CellState.PATH;

                history.push(current);
                current = next;
                maze[current.x][current.y] = CellState.PATH;

                nVisited++;
            } else if (!history.empty()) {
                current = history.pop();
            }
        }
    }

    private Point checkNext(Point current, CellState target, int dist) {

        final int n = 4; // number of neighbors

        // neigbour points
        Point[] options = {new Point(current.x, current.y + dist), new Point(current.x, current.y - dist),
                new Point(current.x + dist, current.y), new Point(current.x - dist, current.y)};

        boolean[] goodIndices = new boolean[n];
        int nGood = 0;

        for (int i = 0; i < n; i++) {
            Point c = options[i];

            boolean good = c.x >= 0 && c.x < cols && c.y >= 0 && c.y < rows && maze[c.x][c.y] == target;
            goodIndices[i] = good;

            if (good)
                nGood++;
        }

        if (nGood == 0)
            return null; // when no neighbors

        int rand = (int) (Math.random() * n);
        while (!goodIndices[rand]) {
            rand = (int) (Math.random() * n);
        }

        return options[rand];

    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        cellSize = canvas.getWidth() / cols;

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                CellState state = maze[i][j];
                int color;

                int x = i * cellSize;
                int y = j * cellSize;

                switch (state) {
                    case EMPTY:
                        color = Color.YELLOW;
                        break;
                    case PATH:
                        color = Color.WHITE;
                        break;
                    case PLAYER_PATH:
                        color = Color.argb(255, 173, 216, 230);;
                        break;
                    default:
                        color = Color.BLACK; // wall
                        break;
                }

                if (i == start.x && j == start.y)
                    color = Color.YELLOW;
                else if (i == stop.x && j == stop.y)
                    color = Color.GREEN;

                paint.setColor(color);
                canvas.drawRect(x, y, x + cellSize, y + cellSize, paint);
            }
        }
    }


    public String mazeToString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if(maze[i][j]==CellState.PATH){
                    stringBuilder.append("P");
                }
                else if (maze[i][j]==CellState.WALL){
                    stringBuilder.append("W");
                }
                else if (maze[i][j]==CellState.EMPTY){
                    stringBuilder.append("E");
                }
            }
            if (i < maze.length - 1) {
                stringBuilder.append("\n"); // row separator
            }
        }

        return stringBuilder.toString();
    }

    public void loadMazeFromString(String mazeString) {
        String[] rows = mazeString.split("\n");

        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            for (int j = 0; j < row.length(); j++) {
                char cell = row.charAt(j);
                switch (cell) {
                    case 'P':
                        maze[i][j] = CellState.PATH;
                        break;
                    case 'W':
                        maze[i][j] = CellState.WALL;
                        break;
                    case 'E':
                        maze[i][j] = CellState.EMPTY;
                        break;
                }
            }
        }
    }

    public int getCols() {
        return cols;
    }
    public int getRows() {
        return rows;
    }

    public CellState getCellState(int x, int y) {
        return maze[x][y];
    }

    public void setCellState(int playerX, int playerY, CellState cellState) {
        maze[playerX][playerY] = cellState;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return stop;
    }
}