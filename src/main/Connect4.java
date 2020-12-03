package main;
import hevs.graphics.FunGraphics;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Connect4 {
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 1000;
	private static int RADIUS = 100;
	private static final int ARROW_WIDTH = RADIUS / 2;
	private static final int ARROW_HEIGHT = RADIUS / 4 * 3;
	private static final int ARROW_START = 20;
	private static final int BORDER = 20;
	private static final int SAFETY_MARGIN = 50;
	private static int BORDER_X = 0;
	private static int BORDER_Y = 0;
	private static int DISTANCE_HOLES_X = 140;
	private static int DISTANCE_HOLES_Y = 140;
	private static final int BACKGROUND_START = 150;
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int OFFSET_X = 200;
	private static final int OFFSET_Y = 200;
	private FunGraphics display = new FunGraphics(WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, "Connect 4", true);
	
	private int[][] contents = new int[COLUMNS][ROWS];
	private int turn = 1;
	private int turnCount = 0;
	private static final int TURN_COUNT_MAX = 42;
	private static final int FPS = 60;
	public int rowSelected = 4;
	public boolean selectRow = false;
	
	public void init () {
		this.drawBackground();
		this.keyboardInput();
	}
	
	public void refreshRate () {
		display.syncGameLogic(FPS);
	}
	
	public void keyboardInput () {
		display.setKeyManager (new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode () == KeyEvent.VK_RIGHT) {
			    	++rowSelected;
			    	if (rowSelected > COLUMNS - 1) {
			    		rowSelected = COLUMNS - 1;
			    	}
			    }
			    
			    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			    	--rowSelected;
			    	if (rowSelected < 0) {
			    		rowSelected = 0;
			    	}
			    }
			    
			    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			    	selectRow = true;
			    }
		  	}
	    });
	}
	
	private void calcGraphics () {
		DISTANCE_HOLES_X = (WIDTH) / (COLUMNS + 1);
		DISTANCE_HOLES_Y = (HEIGHT) / (ROWS + 1);
		BORDER_X = (WIDTH - ((COLUMNS - 1) * DISTANCE_HOLES_X)) / 2 - SAFETY_MARGIN;
		BORDER_Y = (HEIGHT - BACKGROUND_START - ((ROWS - 1) * DISTANCE_HOLES_Y)) / 2 - SAFETY_MARGIN;
		RADIUS = (WIDTH + HEIGHT) / 20;
		
		if (2 * RADIUS> DISTANCE_HOLES_X) {
			RADIUS = DISTANCE_HOLES_X - 10;
		}
		if (2 * RADIUS> DISTANCE_HOLES_Y) {
			RADIUS = DISTANCE_HOLES_Y - 10;
		}
		/*System.out.println(DISTANCE_HOLES_X);
		System.out.println(DISTANCE_HOLES_Y);
		System.out.println(BORDER_X);
		System.out.println(BORDER_Y);
		
		System.out.println(display.getFrameWidth());
		System.out.println(display.getFrameHeight());*/
	}
	
	public void drawBackground () {
		this.calcGraphics();
		display.setColor(Color.blue);
		display.drawFillRect(0, BACKGROUND_START, WIDTH, HEIGHT - BACKGROUND_START);
		
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (contents[i][j] == 0) {
					display.setColor(Color.white);
					display.drawFilledOval(i * DISTANCE_HOLES_X + BORDER_X, j * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS, RADIUS);
					
					display.setColor(Color.black);
					for (int k = 1; k < 4; k++) {
						display.drawCircle(i * DISTANCE_HOLES_X + BORDER_X, j * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS + k);
					}
				}
			}
		}
	}
	
	/*public void drawBlankSpaces () { // could be used for animations
		display.setColor(Color.white);
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (contents[i][j] == 0) {
					//display.drawFilledOval(i * DISTANCE_HOLES + BORDER_X, j * DISTANCE_HOLES + BACKGROUND_START + BORDER_Y, RADIUS, RADIUS);
				}
			}
		}
	}*/
	
	public void drawTakenSpaces () {
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (contents[i][j] == 1) {
					display.setColor(Color.red);
					display.drawFilledOval(i * DISTANCE_HOLES_X + BORDER_X, j * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS, RADIUS);
				} else if (contents[i][j] == 2) {
					display.setColor(Color.yellow);
					display.drawFilledOval(i * DISTANCE_HOLES_X + BORDER_X, j * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS, RADIUS);
				}
			}
		}
	}
	
	public void drawArrowSelected () {
		display.setColor(Color.white);
		display.drawFillRect(0, 0, WIDTH, BACKGROUND_START);
		int y = ARROW_START;
		int x = rowSelected * DISTANCE_HOLES_X + BORDER_X;
		this.drawArrow(x, y);
	}
	
	public void drawArrow (int px, int py) {
		display.setColor(this.getCurrentPlayerColor());
		display.drawFillRect(px + RADIUS / 4, py, ARROW_WIDTH, ARROW_HEIGHT);
		
		for (int x = 0; x < ARROW_WIDTH; x++) {
			for (int y = 0; y < x; y++) {
				display.setPixel(px + x, py + ARROW_HEIGHT + y);
			}
		}
		for (int x = ARROW_WIDTH; x <= 2 * ARROW_WIDTH; x++) {
			for (int y = 0; y < 2 * ARROW_WIDTH - x ; y++) {
				display.setPixel(px + x, py + ARROW_HEIGHT + y);
			}
		}
	}
	
	public void displayWinner () {
		FunGraphics displayWinner = new FunGraphics(WIDTH, HEIGHT / 4, OFFSET_X, OFFSET_Y, "Winner!", true);
		String s = "Player " + this.getNextPlayer() + " wins!";
		displayWinner.setColor(Color.black);
		displayWinner.drawFillRect(0, 0, WIDTH, HEIGHT / 4);
		displayWinner.drawFancyString(BORDER, 100, s, this.getNextPlayerColor(), 100);
	}
	
	private boolean isOccupied (int column, int row) {
		if (column < 0) {
			return true;
		}
		
		int content = contents[column][row];
		
		if (content == 1 || content == 2) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setOccupied (int column, int row) {
		if (!this.isOccupied(column, row)) {
			contents[column][row] = this.getCurrentPlayer();
		}
	}
	
	public int getCurrentPlayer () {
		return turn;
	}
	
	public int getNextPlayer() {
		return this.getCurrentPlayer() == 1 ? 2 : 1;
	}
	
	public Color getCurrentPlayerColor () {
		return this.getCurrentPlayer() == 1 ? Color.red : Color.yellow;
	}
	
	public Color getNextPlayerColor () {
		return this.getCurrentPlayer() == 1 ? Color.yellow : Color.red;
	}
	
	public int switchPlayer () {
		int player = this.getCurrentPlayer();
		if (player == 1) {
			turn = 2;
		}
		
		if (player == 2) {
			turn = 1;
		}
		
		this.drawArrowSelected();
		
		return turn;
	}
	
	public boolean dropPiece (int column) {
		if (column >= COLUMNS) {
			return false;
		}
		
		for (int i = ROWS - 1; i > -1; i--) {
			if (!isOccupied(column, i)) {
				this.setOccupied(column, i);
				this.switchPlayer();
				return true;
			}
		}
		
		this.turnCount++;
		
		return false;
	}
	
	public boolean checkFull () {
		return (turnCount == TURN_COUNT_MAX);
	}
	
	public boolean checkFour () {		
		int player = this.getNextPlayer();
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (i < COLUMNS - 3 && contents[i][j] == player && contents[i+1][j] == player && contents[i+2][j] == player && contents[i+3][j] == player) {
					return true;
				} else if (i > 2 && contents[i][j] == player && contents[i-1][j] == player && contents[i-2][j] == player && contents[i-3][j] == player) {
					return true;
				} else if (j < ROWS - 3 && contents[i][j] == player && contents[i][j+1] == player && contents[i][j+2] == player && contents[i][j+3] == player) {
					return true;
				} else if (j > 2 && contents[i][j] == player && contents[i][j-1] == player && contents[i][j-2] == player && contents[i][j-3] == player) {
					return true;
				} else if (i < COLUMNS - 3 && j < ROWS - 3 && contents[i][j] == player && contents[i+1][j-1] == player && contents[i+2][j-2] == player && contents[i+3][j-3] == player) {
					return true;
				} else if (i < COLUMNS - 3 && j < ROWS - 3 && contents[i][j] == player && contents[i+1][j+1] == player && contents[i+2][j+2] == player && contents[i+3][j+3] == player) {
					return true;
				} else if (i < COLUMNS - 3 && j < ROWS - 3 && contents[i][j] == player && contents[i-1][j+1] == player && contents[i-2][j+2] == player && contents[i-3][j+3] == player) {
					return true;
				} else if (i < COLUMNS - 3 && j < ROWS - 3 && contents[i][j] == player && contents[i-1][j-1] == player && contents[i-2][j-2] == player && contents[i-3][j-3] == player) {
					return true;
				}
			}
		}
		
		return false;
	}
}
