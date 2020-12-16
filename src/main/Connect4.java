package main;

import hevs.graphics.FunGraphics;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Connect4 {
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int OFFSET_X = 0;
	private static final int OFFSET_Y = 0;
	private static final int TURN_COUNT_MAX = 42;
	private static final int FPS = 144;

	private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static final int WIDTH = (int) (0.5 * gd.getDisplayMode().getWidth());
	private static final int HEIGHT = (int) (0.75 * gd.getDisplayMode().getHeight());
	private static int RADIUS = (WIDTH + HEIGHT) / 20;
	private static final int BACKGROUND_START = HEIGHT / 6;
	private static int ARROW_WIDTH = RADIUS / 2;
	private static int ARROW_HEIGHT = RADIUS / 4 * 3;
	private static int ARROW_START = (BACKGROUND_START - (ARROW_HEIGHT + ARROW_WIDTH));
	private static int DISTANCE_HOLES_X = WIDTH / (COLUMNS + 1);
	private static int DISTANCE_HOLES_Y = (HEIGHT - BACKGROUND_START) / (ROWS + 1);
	private static int BORDER_X = DISTANCE_HOLES_X - RADIUS / 2;
	private static int BORDER_Y = DISTANCE_HOLES_Y - RADIUS / 2;

	private FunGraphics display = new FunGraphics(WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, "Connect 4", true);
	private Piece[][] contents = new Piece [COLUMNS][ROWS];

	private int player1Wins = 0;
	private int player2Wins = 0;

	private int turnPlayer = 1;
	private int turnCount = 0;
	private boolean playAgain = false;
	private boolean waitingForInput = true;
	private boolean allowInputRestart = false;
	public boolean allowInput = false;

	private int columnCurrentlySelected = 3;
	private int rowCurrentlySelected = 0;
	private boolean isColumnSelected = false;

	private int connectFourColumn1 = -1;
	private int connectFourColumn2 = -1;
	private int connectFourRow1 = -1;
	private int connectFourRow2 = -1;

	/**
	 * Sets up content of arrays, window background and keyboard input
	 */
	public void init() {
		this.setContents();
		this.calcGraphics();
		this.keyboardInput();
		this.drawBackground();
	}

	/**
	 * Makes a new basic Piece in every spot
	 */
	private void setContents() {
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				contents[i][j] = new Piece (i, j);
			}
		}
	}

	/**
	 * Calculates various variables for drawing the game.
	 */
 	private void calcGraphics () {
 		RADIUS = (WIDTH + HEIGHT) / 20;

		if (2 * RADIUS > DISTANCE_HOLES_X) {
			RADIUS = DISTANCE_HOLES_X / 8 * 7;
		}

		if (2 * RADIUS > DISTANCE_HOLES_Y) {
			RADIUS = DISTANCE_HOLES_Y / 8 * 7;
		}

		ARROW_WIDTH = RADIUS / 2;
		ARROW_HEIGHT = RADIUS / 4 * 3;
		ARROW_START = (BACKGROUND_START - (ARROW_HEIGHT + ARROW_WIDTH));

		DISTANCE_HOLES_X = WIDTH / (COLUMNS + 1);
		DISTANCE_HOLES_Y = (HEIGHT - BACKGROUND_START) / (ROWS + 1);
		BORDER_X = DISTANCE_HOLES_X - RADIUS / 2;
		BORDER_Y = DISTANCE_HOLES_Y - RADIUS / 2;
	}

    /**
     * Handles keyboard input
     */
	private void keyboardInput() {
		display.setKeyManager(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT && allowInput) {
					++columnCurrentlySelected;
					if (columnCurrentlySelected > COLUMNS - 1) {
						columnCurrentlySelected = COLUMNS - 1;
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_LEFT && allowInput) {
					--columnCurrentlySelected;
					if (columnCurrentlySelected < 0) {
						columnCurrentlySelected = 0;
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN && allowInput) {
					isColumnSelected = true;
					allowInput = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER && allowInputRestart) {
					playAgain = true;
					waitingForInput = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE && allowInputRestart) {
					playAgain = false;
					waitingForInput = false;
				}
			}
		});
	}

 	/**
 	 * Draws the background of the game
 	 */
	private void drawBackground() {
		display.setColor(Color.blue);
		display.drawFillRect(0, BACKGROUND_START, WIDTH, HEIGHT - BACKGROUND_START);
		this.drawBlankSpaces(true, true);
	}

	/**
	 * Sets FPS every loop
	 */
	public void refreshRate() {
		display.syncGameLogic(FPS);
	}

	/**
	 *
	 * @param overwrite
	 * @param shadow
	 *
	 * Draws the blank spaces. Overwrite allows taken spaces to be overwritten, shadow includes a 3D shadow effect.
	 */
	private void drawBlankSpaces(boolean overwrite, boolean shadow) {
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (overwrite || this.getOccupied(i, j).getPlayer() == 0) {
					display.setColor(Color.white);
					display.drawFilledCircle(i * DISTANCE_HOLES_X + BORDER_X, j * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS);

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

	/**
	 * Draws the space just taken in the current move.
	 */
	public void drawTakenSpaces() {
		if (this.isOccupied(columnCurrentlySelected, rowCurrentlySelected) && !this.getOccupied(columnCurrentlySelected, rowCurrentlySelected).getDrawn()) {
			this.getOccupied(columnCurrentlySelected, rowCurrentlySelected).setDrawn(true);
			display.setColor(this.getOccupied(columnCurrentlySelected, rowCurrentlySelected).getColor());
			display.drawFilledCircle(columnCurrentlySelected * DISTANCE_HOLES_X + BORDER_X, rowCurrentlySelected * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS);
		}
	}

	/**
	 * Draws an arrow over the currently selected column.
	 */
	public void drawArrowSelected() {
		display.setColor(Color.white);
		display.drawFillRect(0, 0, WIDTH, BACKGROUND_START);
		this.drawArrow(columnCurrentlySelected * DISTANCE_HOLES_X + BORDER_X, ARROW_START);
		this.displayScore(Color.black);
	}

	/**
	 *
	 * @param px
	 * @param py
	 *
	 * Draws an arrow at the given position.
	 */
	private void drawArrow(int px, int py) {
		display.setColor(this.getCurrentPlayerColor());
		display.drawFillRect(px + RADIUS / 4, py, ARROW_WIDTH, ARROW_HEIGHT);

		for (int x = 0; x <= ARROW_WIDTH; x++) {
			for (int y = 0; y < x; y++) {
				display.setPixel(px + x, py + ARROW_HEIGHT + y);
				display.setPixel(px + 2 * ARROW_WIDTH - x, py + ARROW_HEIGHT + y);
			}
		}
	}

	/**
	 * Makes a black banner displaying the winner of the game.
	 */
	public void displayWinner() {
		display.setColor(Color.black);
		display.drawFillRect(0, 0, WIDTH, BACKGROUND_START);
		display.drawFancyString(BORDER_X / 2, BACKGROUND_START / 3 * 2, "Player " + turnPlayer + " wins!", this.getCurrentPlayerColor(), BACKGROUND_START / 2);

		// Adds a win to the winner
		if (turnPlayer == 1) {
			player1Wins++;
		} else if (turnPlayer == 2) {
			player2Wins++;
		}

		this.displayScore(Color.green);

		// Draws a line through the connected 4
		int calcX = BORDER_X + RADIUS / 2;
		int calcY = BORDER_Y + BACKGROUND_START + RADIUS / 2;

		display.setPenWidth(4.0f);
		display.drawLine(connectFourColumn1 * DISTANCE_HOLES_X + calcX, connectFourRow1 * DISTANCE_HOLES_Y + calcY, connectFourColumn2 * DISTANCE_HOLES_X + calcX, connectFourRow2 * DISTANCE_HOLES_Y + calcY);
		display.setPenWidth(1.0f);
		
		this.displayPlayAgain();
	}

	/**
	 * Makes a black banner displaying the winner of the game.
	 */
	public void displayNobody() {
		display.setColor(Color.black);
		display.drawFillRect(0, 0, WIDTH, BACKGROUND_START);
		display.drawFancyString(BORDER_X / 2, BACKGROUND_START / 3 * 2, "Nobody wins.", Color.white, BACKGROUND_START / 2);

		this.displayScore(Color.green);
		
		this.displayPlayAgain();
	}
	
	/**
	 * Display text for instructions
	 */
	public void displayPlayAgain() {
		display.drawString(WIDTH / 4 * 3, BACKGROUND_START / 5 * 3, "Press Enter to play again", Color.white, BACKGROUND_START / 8);
		display.drawString(WIDTH / 4 * 3, BACKGROUND_START / 5 * 4, "Press Escape to quit", Color.white, BACKGROUND_START / 8);
	}

	/**
	 *
	 * @param c
	 *
	 * Displays the current score in given color
	 */
	public void displayScore(Color c) {
		display.drawString(WIDTH / 4 * 3, BACKGROUND_START / 5 * 2, "Player 1 " + player1Wins + " : " + player2Wins + " Player 2", c, BACKGROUND_START / 8);
	}

	public boolean columnIsSelected() {
		return isColumnSelected;
	}

	public int isColumnSelected() {
		return columnCurrentlySelected;
	}

	/**
	 *
	 * @param column
	 * @param row
	 * @return boolean - if spot is taken or not
	 */
	private boolean isOccupied(int column, int row) {
		if (this.getOccupied(column, row).getPlayer() != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * @param column
	 * @param row
	 *
	 * Sets a current spot to taken.
	 */
	private void setOccupied(int column, int row) {
		if (!this.isOccupied(column, row)) {
			this.getOccupied(column, row).setPlayer(turnPlayer);
		}
	}

	/**
	 *
	 * @param column
	 * @param row
	 * @return Piece - Piece at given coordinates
	 */
	private Piece getOccupied(int column, int row) {
		return this.contents[column][row];
	}

	public int getTurnCount() {
		return turnCount;
	}

	private int getNextPlayer() {
		return turnPlayer == 1 ? 2 : 1;
	}

	private void setNextPlayer() {
		turnPlayer = getNextPlayer();
	}

	private Color getCurrentPlayerColor() {
		return turnPlayer == 1 ? Color.red : Color.yellow;
	}

	public void switchPlayer() {
		this.setNextPlayer();
		this.drawArrowSelected();
	}

	/**
	 *
	 * @param column
	 * @return boolean - if piece dropped successfully
	 *
	 * Virtually drops a Piece into the bottom most slot.
	 */
	public boolean dropPiece(int column) {
		isColumnSelected = false;
		if (column >= COLUMNS) {
			return false;
		}

		//Finds the first free slot from the bottom
		for (int i = ROWS - 1; i > -1; i--) {
			if (!isOccupied(column, i)) {
				this.getOccupied(column, i).setColor(getCurrentPlayerColor());
				this.setOccupied(column, i);
				rowCurrentlySelected = i;
				turnCount++;
				return true;
			}
		}

		return false;
	}

	/**
	 *
	 * @return boolean - if entire grid is taken
	 */
	public boolean checkFull() {
		return (turnCount == TURN_COUNT_MAX);
	}

	/**
	 *
	 * @return boolean - if a winning condition has been fulfilled
	 *
	 * Checks the grid for a Connect 4
	 */
	public boolean checkFour() {
		int player = turnPlayer;
		int column = this.isColumnSelected();
		int row = rowCurrentlySelected;
		int fourInARow = 0;

		// Checks the currently selected column for 4 in a row
		for (int i = rowCurrentlySelected; i < ROWS; i++) {
			if (this.getOccupied(column, i).getPlayer() == player) {
				fourInARow++;
			} else {
				fourInARow = 0;
			}

			if (fourInARow == 4) {
				connectFourColumn1 = column;
				connectFourColumn2 = column;
				connectFourRow1 = i - 3;
				connectFourRow2 = i;
				return true;
			}
		}

		fourInARow = 0;

		// Checks the currently selected row for 4 in a row
		for (int i = 0; i < COLUMNS; i++) {
			if (this.getOccupied(i, row).getPlayer() == player) {
				fourInARow++;
			} else {
				fourInARow = 0;
			}

			if (fourInARow == 4) {
				connectFourColumn1 = i - 3;
				connectFourColumn2 = i;
				connectFourRow1 = row;
				connectFourRow2 = row;
				return true;
			}
		}

		// Checks all descending diagonals, top down
		int x = column - row;
		int y = 0;
		fourInARow = 0;

		if (x < 0) {
			x = 0;
			y = row - column;
		};

		for (; x < COLUMNS && y < ROWS; x++) {
			if (x >= 0 && y >= 0 && this.getOccupied(x, y).getPlayer() == player) {
				fourInARow++;
			} else {
				fourInARow = 0;
			}

			if (fourInARow == 4) {
				connectFourColumn1 = x - 3;
				connectFourColumn2 = x;
				connectFourRow1 = y - 3;
				connectFourRow2 = y;
				return true;
			}
			y++;
		}

		// Checks all ascending diagonals, top down
		x = column + row;
		y = 0;
		fourInARow = 0;

		if (x > COLUMNS - 1) {
			x = COLUMNS - 1;
			y = row + column - (COLUMNS - 1);
		};

		for (fourInARow = 0; x >= 0 && y < ROWS; x--) {
			if (x >= 0 && y >= 0 && this.getOccupied(x, y).getPlayer() == player) {
				fourInARow++;
			} else {
				fourInARow = 0;
			}

			if (fourInARow == 4) {
				connectFourColumn1 = x + 3;
				connectFourColumn2 = x;
				connectFourRow1 = y - 3;
				connectFourRow2 = y;
				return true;
			}
			y++;
		}

		return false;
	}

	/**
	 *
	 * @return boolean - if wanted to play again
	 *
	 * Asks the player in the console whether they want to play again or not.
	 */
	public boolean playAgain() {
		allowInputRestart = true;

		while (waitingForInput) {
			this.refreshRate();
		}

		waitingForInput = true;
		allowInputRestart = false;

		if (playAgain) {
			display.clear();

			for (int i = 0; i < COLUMNS; i++) {
				for (int j = 0; j < ROWS; j++) {
					this.getOccupied(i, j).setPlayer(0);
					this.getOccupied(i, j).setDrawn(false);
				}
			}

			turnCount = 0;
			columnCurrentlySelected = 3;

			// Refreshes background and arrow
			this.drawBackground();
			this.drawArrowSelected();
			return true;
		}
		return false;
	}
}
