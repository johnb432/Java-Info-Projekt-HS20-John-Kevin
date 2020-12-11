package main;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

import hevs.graphics.FunGraphics;

public class Connect4 extends JPanel implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static final int WIDTH = (int) (0.5 * gd.getDisplayMode().getWidth());
	private static final int HEIGHT = (int) (0.75 * gd.getDisplayMode().getHeight());
	private static int RADIUS = 100;
	private static int ARROW_WIDTH = RADIUS / 2;
	private static int ARROW_HEIGHT = RADIUS / 4 * 3;
	private static int ARROW_START = 20;
	private static final int BORDER = 20;
	private static final int SAFETY_MARGIN = 20;
	private static int BORDER_X = 20;
	private static int BORDER_Y = 20;
	private static int DISTANCE_HOLES_X = 140;
	private static int DISTANCE_HOLES_Y = 140;
	private static int BACKGROUND_START = 150;
	
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int OFFSET_X = 0;
	private static final int OFFSET_Y = 0;
	private static final int TURN_COUNT_MAX = 42;
	private static final int FPS = 144;
	
	private FunGraphics display = new FunGraphics(WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, "Connect 4", true);
	private Piece[][] contents = new Piece [COLUMNS][ROWS];
	
	private int turnPlayer = 1;
	private int turnCount = 0;
	private int rowCurrentlySelected = 3;
	private boolean rowSelected = false;
	private int connectFourColumn1 = 0;
	private int connectFourColumn2 = 0;
	private int connectFourRow1 = 0;
	private int connectFourRow2 = 0;

	public void init() {
		this.setContents();
		this.drawBackground();
		this.keyboardInput();
	}
	
	private void setContents() {		
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				this.contents[i][j] = new Piece (i, j);
			}
		}
	}

	public void refreshRate() {
		display.syncGameLogic(FPS);
	}
	
	/*public Connect4() {
		display.addMouseListener(this);
		addMouseListener(this);
		
		display.addMouseMotionListener(this);
		addMouseMotionListener(this);
	}*/

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

	public void keyboardInput() {
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

 	private void calcGraphics () { // remember that circles don't start at circle centers!!!!!!!!
		DISTANCE_HOLES_X = WIDTH / (COLUMNS + 1);
		DISTANCE_HOLES_Y = (HEIGHT - BACKGROUND_START) / (ROWS + 1);
		
		RADIUS = (WIDTH + HEIGHT) / 20; // TODO
		
		if (2 * RADIUS > DISTANCE_HOLES_X) {
			RADIUS = DISTANCE_HOLES_X / 6 * 5;
		}
		 
		if (2 * RADIUS > DISTANCE_HOLES_Y) {
			RADIUS = DISTANCE_HOLES_Y / 6 * 5;
		}
		
		BORDER_X = DISTANCE_HOLES_X - RADIUS / 2;
		BORDER_Y = DISTANCE_HOLES_Y - RADIUS / 2;
		
		ARROW_WIDTH = RADIUS / 2;
		ARROW_HEIGHT = RADIUS / 4 * 3;
		
		BACKGROUND_START = HEIGHT / 6;
		
		ARROW_START = (BACKGROUND_START - (ARROW_HEIGHT + ARROW_WIDTH));
	}

	public void drawBackground() {
		this.calcGraphics();
		display.setColor(Color.blue);
		display.drawFillRect(0, BACKGROUND_START, WIDTH, HEIGHT - BACKGROUND_START);
		this.drawBlankSpaces(true, true);
	}

	public void drawBlankSpaces(boolean overwrite, boolean shadow) { // could be used for animations
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

	public void drawTakenSpaces() {
		for (int j = 0; j < ROWS; j++) {
			if (this.isOccupied(rowCurrentlySelected, j) && !this.getOccupied(rowCurrentlySelected, j).getDrawn()) {
				this.getOccupied(rowCurrentlySelected, j).setDrawn(true);
				display.setColor(this.getOccupied(rowCurrentlySelected, j).getColor());
				display.drawFilledCircle(rowCurrentlySelected * DISTANCE_HOLES_X + BORDER_X, j * DISTANCE_HOLES_Y + BACKGROUND_START + BORDER_Y, RADIUS);
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

	public void drawArrow(int px, int py) { // optimize?
		display.setColor(this.getCurrentPlayerColor());
		display.drawFillRect(px + RADIUS / 4, py, ARROW_WIDTH, ARROW_HEIGHT);

		for (int x = 0; x <= ARROW_WIDTH; x++) {
			for (int y = 0; y < x; y++) {
				display.setPixel(px + x, py + ARROW_HEIGHT + y);
				display.setPixel(px + 2 * ARROW_WIDTH - x, py + ARROW_HEIGHT + y);
			}
		}
	}

	public void displayWinner() {
		display.setColor(Color.black);
		display.drawFillRect(0, 0, WIDTH, BACKGROUND_START);
		display.drawFancyString(BORDER, 100, "Player " + this.getNextPlayer() + " wins!", this.getNextPlayerColor(), 100);
		
		int calcX = BORDER_X + RADIUS / 2;
		int calcY = BORDER_Y + BACKGROUND_START + RADIUS / 2;
		
		display.setPenWidth(4.0f);
		display.drawLine(connectFourColumn1 * DISTANCE_HOLES_X + calcX, connectFourRow1 * DISTANCE_HOLES_Y + calcY, connectFourColumn2 * DISTANCE_HOLES_X + calcX, connectFourRow2 * DISTANCE_HOLES_Y + calcY);
		display.setPenWidth(1.0f);
	}

	public boolean rowIsSelected() {
		return this.rowSelected;
	}

	public int rowSelected() {
		return this.rowCurrentlySelected;
	}

	private boolean isOccupied(int column, int row) {
		if (this.getOccupied(column, row).getPlayer() != 0) {
			return true;
		} else {
			return false;
		}
	}

	private void setOccupied(int column, int row) {
		if (!this.isOccupied(column, row)) {
			this.getOccupied(column, row).setPlayer(this.getCurrentPlayer());
		}
	}
	
	private Piece getOccupied(int column, int row) {
		return contents[column][row];
	}

	private int getCurrentPlayer() {
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
				this.getOccupied(column, i).setColor(getCurrentPlayerColor());
				this.setOccupied(column, i);
				this.switchPlayer();
				this.turnCount++;
				return true;
			}
		}

		return false;
	}

	public boolean checkFull() {
		return (turnCount == TURN_COUNT_MAX);
	}

	public boolean checkFour() { // OPTIMIZE
		int player = this.getNextPlayer();
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (i < COLUMNS - 3 && this.getOccupied(i, j).getPlayer() == player && this.getOccupied(i + 1, j).getPlayer() == player && this.getOccupied(i + 2, j).getPlayer() == player && this.getOccupied(i + 3, j).getPlayer() == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i + 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j;
					return true;
				} else if (i > 2 && this.getOccupied(i, j).getPlayer() == player && this.getOccupied(i - 1, j).getPlayer() == player && this.getOccupied(i - 2, j).getPlayer() == player && this.getOccupied(i - 3, j).getPlayer() == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i - 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j;
					return true;
				} else if (j < ROWS - 3 && this.getOccupied(i, j).getPlayer() == player && this.getOccupied(i, j + 1).getPlayer() == player && this.getOccupied(i, j + 2).getPlayer() == player && this.getOccupied(i, j + 3).getPlayer() == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j + 3;
					return true;
				} else if (j > 2 && this.getOccupied(i, j).getPlayer() == player && this.getOccupied(i, j - 1).getPlayer() == player && this.getOccupied(i, j - 2).getPlayer() == player && this.getOccupied(i, j - 3).getPlayer() == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j - 3;
					return true;
				} else if (i < COLUMNS - 3 && j > 2 && this.getOccupied(i, j).getPlayer() == player && this.getOccupied(i + 1, j - 1).getPlayer() == player && this.getOccupied(i + 2, j - 2).getPlayer() == player && this.getOccupied(i + 3, j - 3).getPlayer() == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i + 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j - 3;
					return true;
				} else if (i < COLUMNS - 3 && j < ROWS - 3 && this.getOccupied(i, j).getPlayer() == player && this.getOccupied(i + 1, j + 1).getPlayer() == player && this.getOccupied(i + 2, j + 2).getPlayer() == player && this.getOccupied(i + 3, j + 3).getPlayer() == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i + 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j + 3;
					return true;
				} else if (i > 2 && j < ROWS - 3 && this.getOccupied(i, j).getPlayer() == player && this.getOccupied(i - 1, j + 1).getPlayer() == player && this.getOccupied(i - 2, j + 2).getPlayer() == player && this.getOccupied(i - 3, j + 3).getPlayer() == player) {
					this.connectFourColumn1 = i;
					this.connectFourColumn2 = i - 3;
					this.connectFourRow1 = j;
					this.connectFourRow2 = j + 3;
					return true;
				} else if (i > 2 && j > 2 && this.getOccupied(i, j).getPlayer() == player && this.getOccupied(i - 1, j - 1).getPlayer() == player && this.getOccupied(i - 2, j - 2).getPlayer() == player && this.getOccupied(i - 3, j - 3).getPlayer() == player) {
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
		System.out.println("Do you want to play again?\nTo replay, type 'y' or 'Y'");

		char in = Input.readChar();

		if (in == 'y' || in == 'Y') {
			display.clear();

			for (int i = 0; i < COLUMNS; i++) {
				for (int j = 0; j < ROWS; j++) {
					this.getOccupied(i, j).setPlayer(0);
					this.getOccupied(i, j).setDrawn(false);
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
