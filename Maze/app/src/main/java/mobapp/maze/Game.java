package mobapp.maze;


public class Game {
    private MazeGenerator mazeGenerator;
    private MazeView mazeView;
    private int playerX, playerY; // Player's position in the maze
    private int endX, endY;

    public Game(MazeGenerator mazeGenerator, MazeView mv) {
        this.mazeGenerator = mazeGenerator;
        this.mazeView = mv;
        this.playerX = mazeGenerator.getStart().x;
        this.playerY = mazeGenerator.getStart().y;
        this.endX = mazeGenerator.getEnd().x;
        this.endY = mazeGenerator.getEnd().y;
        mazeGenerator.setCellState(playerX, playerY, MazeGenerator.CellState.PLAYER_PATH);
        mazeView.setPlayerPosition(playerX,playerY);
    }

    public void movePlayerRight() {
        if (canMove(playerX + 1, playerY)) {
            playerX++;
            mazeGenerator.setCellState(playerX, playerY, MazeGenerator.CellState.PLAYER_PATH);
            mazeView.setPlayerPosition(playerX,playerY);
            mazeView.invalidate();
        }
    }

    public void movePlayerLeft() {
        if (canMove(playerX - 1, playerY)) {
            playerX--;
            mazeGenerator.setCellState(playerX, playerY, MazeGenerator.CellState.PLAYER_PATH);
            mazeView.setPlayerPosition(playerX,playerY);
            mazeView.invalidate();
        }
    }

    public void movePlayerUp() {
        if (canMove(playerX, playerY - 1)) {
            playerY--;
            mazeGenerator.setCellState(playerX, playerY, MazeGenerator.CellState.PLAYER_PATH);
            mazeView.setPlayerPosition(playerX,playerY);
            mazeView.invalidate();
        }
    }

    public void movePlayerDown() {
        if (canMove(playerX, playerY + 1)) {
            playerY++;
            mazeGenerator.setCellState(playerX, playerY, MazeGenerator.CellState.PLAYER_PATH);
            mazeView.setPlayerPosition(playerX,playerY);
            mazeView.invalidate();
        }
    }

    private boolean canMove(int x, int y) {
        if (isEndPoint()) return false;
        return x >= 0 && y >= 0 && y < mazeGenerator.getRows() &&
                mazeGenerator.getCellState(x, y) != MazeGenerator.CellState.WALL;
    }

    public boolean isEndPoint(){
        if(playerX==endX && playerY==endY) {
            return true;
        }
        else{
            return false;
        }
    }

}
