package main;

import java.awt.Color;

public class Piece {
	private int column;
	private int row;
	private Color color = Color.white;
	private int player = 0;
	private boolean isDrawn = false;
	
	public Piece (int c, int r) {
		this.column = c;
		this.row = r;
		this.color = Color.white;
		this.player = 0;
		this.isDrawn = false;
	}
	
	public void setPiece (int c, int r) {
		this.column = c;
		this.row = r;
	}
	
	public int getPlayer () {
		return this.player;
	}
	
	public void setPlayer (int n) {
		this.player = n;
	}
	
	public boolean getDrawn () {
		return this.isDrawn;
	}
	
	public void setDrawn (boolean b) {
		this.isDrawn = b;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public Color getColor() {
		return this.color;
	}
}
