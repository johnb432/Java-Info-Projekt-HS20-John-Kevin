package main;
import hevs.graphics.FunGraphics;
import java.awt.Color;

public class Connect4 {
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 1000;
	private static final int RADIUS = 70;
	private static final int BORDER = 20;
	private static final int DISTANCE_HOLES = 110;
	private static final int BACKGROUND_START = 200;
	private static final int ROWS = 7;
	private static final int COLUMNS = 9;
	private FunGraphics display = new FunGraphics(WIDTH, HEIGHT, 200, 200, "Connect 4", true);
	private int[][] contents = new int [COLUMNS][ROWS];
	private int turn = 1;
	
	public void drawBackground () {
		display.setColor(Color.blue);
		display.drawFillRect(0,BACKGROUND_START,1000,800);
		
		
		for (int i = BORDER; i < HEIGHT - BORDER; i += DISTANCE_HOLES) {
			for (int j = BACKGROUND_START + BORDER; j < WIDTH - BORDER; j += DISTANCE_HOLES) {
				display.setColor(Color.white);
				display.drawFilledOval(i, j, RADIUS, RADIUS);
				display.setColor(Color.black);
				display.drawCircle(i, j, RADIUS+1);
				display.drawCircle(i, j, RADIUS+2);
				display.drawCircle(i, j, RADIUS+3);
			}
		}
		
		this.drawBlankSpaces();
	}
	
	public void drawBlankSpaces () {
		display.setColor(Color.white);
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (contents[i][j] == 0) {
					display.drawFilledOval(i * DISTANCE_HOLES + BORDER, j * DISTANCE_HOLES + BACKGROUND_START + BORDER, RADIUS, RADIUS);
				}
			}
		}
	}
	
	public void drawTakenSpaces () {
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (contents[i][j] == 1) {
					display.setColor(Color.red);
					display.drawFilledOval(i * DISTANCE_HOLES + BORDER, j * DISTANCE_HOLES + BACKGROUND_START + BORDER, RADIUS, RADIUS);
				} else if (contents[i][j] == 2) {
					display.setColor(Color.yellow);
					display.drawFilledOval(i * DISTANCE_HOLES + BORDER, j * DISTANCE_HOLES + BACKGROUND_START + BORDER, RADIUS, RADIUS);
				}
				
			}
		}
	}
	
	private boolean isOccupied (int row, int column) {
		int content = contents[row - 1][column - 1];
		if (content == 1 || content == 2) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setOccupied (int row, int column) {
		if (!this.isOccupied(row, column)) {
			contents[row - 1][column - 1] = this.getCurrentPlayer();
		}
		this.switchPlayer();
	}
	
	public int getCurrentPlayer () {
		return turn;
	}
	
	public int switchPlayer () {
		int player = this.getCurrentPlayer();
		if (player == 1) {
			turn = 2;
		}
		if (player == 2) {
			turn = 1;
		}
		return turn;
	}
	
	public boolean dropPiece (int row) {
		boolean successful = false;
		for (int i = ROWS; i > -1; i--) {
			if (!isOccupied(row, i)) {
				this.setOccupied(row, i);
				successful = true;
				break;
			}
		}
		return successful;
	}
	/*public boolean play () {
	}*/
}
