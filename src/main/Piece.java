package main;

import java.awt.Color;

public class Piece {
	private int column = 0;
	private int row = 0;
	private Color color = Color.white;
	private int player = 0;
	private boolean isDrawn = false;
	
	public Piece (int c, int r) {
		column = c;
		row = r;
		color = Color.white;
		player = 0;
		isDrawn = false;
	}
	
	public void setPiece (int c, int r) {
		column = c;
		row = r;
	}
	
	public int getPlayer () {
		return player;
	}
	
	public void setPlayer (int n) {
		player = n;
	}
	
	public boolean getDrawn () {
		return isDrawn;
	}
	
	public void setDrawn (boolean b) {
		isDrawn = b;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public Color getColor() {
		return color;
	}
}
