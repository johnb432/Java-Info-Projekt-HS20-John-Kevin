package main;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

import hevs.graphics.FunGraphics;

public class Connect4 extends JPanel implements MouseMotionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 1000;
	private static int RADIUS = 100;
	private static final int ARROW_WIDTH = RADIUS / 2;
	private static final int ARROW_HEIGHT = RADIUS / 4 * 3;
	private static final int ARROW_START = 20;
	private static final int BORDER = 20;
	private static final int SAFETY_MARGIN = 20;
	private static int BORDER_X = 20;
	private static int BORDER_Y = 20;
	private static int DISTANCE_HOLES_X = 140;
	private static int DISTANCE_HOLES_Y = 140;
	private static final int BACKGROUND_START = 150;
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int OFFSET_X = 200;
	private static final int OFFSET_Y = 200;
	private FunGraphics display = new FunGraphics(WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, "Connect 4", true);

	private int[][] contents = new int[COLUMNS][ROWS];
	private int turnPlayer = 1;
	private int turnCount = 0;
	private static final int TURN_COUNT_MAX = 42;
	private static final int FPS = 144;
	private int rowCurrentlySelected = 3;
	private boolean rowSelected = false;
	private int connectFourColumn1 = 0;
	private int connectFourColumn2 = 0;
	private int connectFourRow1 = 0;
	private int connectFourRow2 = 0;

	public void init() {
		this.drawBackground();
		//this.keyboardInput();
	}

	public void refreshRate() {
		display.syncGameLogic(FPS);
	}
	
	public Connect4() {
		display.addMouseListener(this);
		addMouseListener(this);
		
		display.addMouseMotionListener(this);
		addMouseMotionListener(this);
	}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		for (int i = 0; i < COLUMNS; i++) {
			if (e.getX() <= i * DISTANCE_HOLES_X + BORDER_X + RADIUS + ARROW_WIDTH - SAFETY_MARGIN && e.getX() >= i * DISTANCE_HOLES_X + BORDER_X + ARROW_WIDTH - RADIUS + SAFETY_MARGIN) {
				rowCurrentlySelected = i;
				this.drawArrowSelected();
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {}
     
    public void mouseReleased(MouseEvent e) {
    	rowSelected = true;
    }
     
    public void mouseEntered(MouseEvent e) {}
     
    public void mouseExited(MouseEvent e) {}
     
    public void mouseClicked(MouseEvent e) {}

	public void keyboardInput() { // currently not in use
		display.setKeyManager(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					++rowCurrentlySelected;
					if (rowCurrentlySelected > COLUMNS - 1) {
						rowCurrentlySelected = COLUMNS - 1;
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					--rowCurrentlySelected;
					if (rowCurrentlySelected < 0) {
						rowCurrentlySelected = 0;
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					rowSelected = true;
				}
			}
		});
	}

	/*
 	private void calcGraphics () {
		DISTANCE_HOLES_X = (WIDTH) / (COLUMNS + 1);
		DISTANCE_HOLES_Y = (HEIGHT) / (ROWS + 1); BORDER_X = (WIDTH - ((COLUMNS - 1) * DISTANCE_HOLES_X)) / 2 - SAFETY_MARGIN;
		BORDER_Y = (HEIGHT -
		BACKGROUND_START - ((ROWS - 1) * DISTANCE_HOLES_Y)) / 2 - SAFETY_MARGIN;
		RADIUS = (WIDTH + HEIGHT) / 20;
		 
		if (2 * RADIUS> DISTANCE_HOLES_X) {
			RADIUS = DISTANCE_HOLES_X - 10;
		}
		 
		if (2 * RADIUS> DISTANCE_HOLES_Y) {
			RADIUS = DISTANCE_HOLES_Y - 10;
		}
	}
	*/

	public void drawBackground() {
		// this.calcGraphics();
		display.setColor(Color.blue);
		display.drawFillRect(0, BACKGROUND_START, WIDTH, HEIGHT - BACKGROUND_START);
		this.drawBlankSpaces(true, true);
	}

	public void drawBlankSpaces(boolean overwrite, boolean shadow) { // could be used for animations
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (overwrite || contents[i][j] == 0) {
					display.setColor(Color.white);
					display.drawFilledOval(i * DISTANCE_HOLES_X + BORDER_X, j * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS, RADIUS);

					if (shadow) {
						display.setColor(Color.black);
						for (int k = 1; k < 4; k++) {
							display.drawCircle(i * DISTANCE_HOLES_X + BORDER_X, j * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS + k);
						}
					}
				}
			}
		}
	}

	public void drawTakenSpaces() {
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (this.isOccupied(i, j)) {
					if (contents[i][j] == 1) {
						display.setColor(Color.red);
					} else if (contents[i][j] == 2) {
						display.setColor(Color.yellow);
					}

					display.drawFilledCircle(i * DISTANCE_HOLES_X + BORDER_X, j * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS);
				}
			}
		}
	}

	public void drawArrowSelected() {
		display.setColor(Color.white);
		display.drawFillRect(0, 0, WIDTH, BACKGROUND_START);
		int y = ARROW_START;
		int x = rowCurrentlySelected * DISTANCE_HOLES_X + BORDER_X;
		this.drawArrow(x, y);
	}

	public void drawArrow(int px, int py) {
		display.setColor(this.getCurrentPlayerColor());
		display.drawFillRect(px + RADIUS / 4, py, ARROW_WIDTH, ARROW_HEIGHT);

		for (int x = 0; x < ARROW_WIDTH; x++) {
			for (int y = 0; y < x; y++) {
				display.setPixel(px + x, py + ARROW_HEIGHT + y);
			}
		}

		for (int x = ARROW_WIDTH; x <= 2 * ARROW_WIDTH; x++) {
			for (int y = 0; y < 2 * ARROW_WIDTH - x; y++) {
				display.setPixel(px + x, py + ARROW_HEIGHT + y);
			}
		}
	}

	public void displayWinner() {
		// FunGraphics displayWinner = new FunGraphics(WIDTH, HEIGHT / 4, OFFSET_X, OFFSET_Y, "Winner!", true);
		String s = "Player " + this.getNextPlayer() + " wins!";
		display.setColor(Color.black);
		display.drawFillRect(0, 0, WIDTH, BACKGROUND_START);
		display.drawFancyString(BORDER, 100, s, this.getNextPlayerColor(), 100);
		
		int calcX = BORDER_X + RADIUS / 2;
		int calcY = BORDER_Y + BACKGROUND_START + RADIUS / 2;
		
		display.drawLine(connectFourColumn1 * DISTANCE_HOLES_X + calcX, connectFourRow1 * DISTANCE_HOLES_Y + calcY, connectFourColumn2 * DISTANCE_HOLES_X + calcX, connectFourRow2 * DISTANCE_HOLES_Y + calcY);
	}

	public boolean rowIsSelected() {
		return this.rowSelected;
	}

	public int rowSelected() {
		return this.rowCurrentlySelected;
	}

	private boolean isOccupied(int column, int row) {
		int content = contents[column][row];

		if (content == 1 || content == 2) {
			return true;
		} else {
			return false;
		}
	}

	private void setOccupied(int column, int row) {
		if (!this.isOccupied(column, row)) {
			contents[column][row] = this.getCurrentPlayer();
		}
	}

	public int getCurrentPlayer() {
		return turnPlayer;
	}

	public int getNextPlayer() {
		return this.getCurrentPlayer() == 1 ? 2 : 1;
	}

	public Color getCurrentPlayerColor() {
		return this.getCurrentPlayer() == 1 ? Color.red : Color.yellow;
	}

	public Color getNextPlayerColor() {
		return this.getCurrentPlayer() == 1 ? Color.yellow : Color.red;
	}

	public int switchPlayer() {
		int player = this.getCurrentPlayer();
		if (player == 1) {
			turnPlayer = 2;
		}

		if (player == 2) {
			turnPlayer = 1;
		}

		this.drawArrowSelected();

		return turnPlayer;
	}

	public boolean dropPiece(int column) {
		this.rowSelected = false;
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

	public boolean checkFull() {
		int k = 0;
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (isOccupied(i, j)) {
					k++;
				}
			}
		}
		return (turnCount == TURN_COUNT_MAX) || (k == TURN_COUNT_MAX);
	}

	public boolean checkFour() {
		int player = this.getNextPlayer();
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (i < COLUMNS - 3 && contents[i][j] == player && contents[i + 1][j] == player && contents[i + 2][j] == player && contents[i + 3][j] == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i + 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j;
					return true;
				} else if (i > 2 && contents[i][j] == player && contents[i - 1][j] == player && contents[i - 2][j] == player && contents[i - 3][j] == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i - 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j;
					return true;
				} else if (j < ROWS - 3 && contents[i][j] == player && contents[i][j + 1] == player && contents[i][j + 2] == player && contents[i][j + 3] == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j + 3;
					return true;
				} else if (j > 2 && contents[i][j] == player && contents[i][j - 1] == player && contents[i][j - 2] == player && contents[i][j - 3] == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j - 3;
					return true;
				} else if (i < COLUMNS - 3 && j > 2 && contents[i][j] == player && contents[i + 1][j - 1] == player && contents[i + 2][j - 2] == player && contents[i + 3][j - 3] == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i + 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j - 3;
					return true;
				} else if (i < COLUMNS - 3 && j < ROWS - 3 && contents[i][j] == player && contents[i + 1][j + 1] == player && contents[i + 2][j + 2] == player && contents[i + 3][j + 3] == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i + 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j + 3;
					return true;
				} else if (i > 2 && j < ROWS - 3 && contents[i][j] == player && contents[i - 1][j + 1] == player && contents[i - 2][j + 2] == player && contents[i - 3][j + 3] == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i - 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j + 3;
					return true;
				} else if (i > 2 && j > 2 && contents[i][j] == player && contents[i - 1][j - 1] == player && contents[i - 2][j - 2] == player && contents[i - 3][j - 3] == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i - 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j - 3;
					return true;
				}
			}
		}

		return false;
	}

	public boolean playAgain() {
		System.out.println("Do you want to play again?");
		System.out.println("To replay, type 'y' or 'Y'");

		char in = Input.readChar();

		if (in == 'y' || in == 'Y') {
			display.clear();

			for (int i = 0; i < COLUMNS; i++) {
				for (int j = 0; j < ROWS; j++) {
					contents[i][j] = 0;
				}
			}

			turnPlayer = 1;
			turnCount = 0;
			rowCurrentlySelected = 3;
			this.drawBackground();
			this.drawArrowSelected();
			return true;
		}
		return false;
	}
}
