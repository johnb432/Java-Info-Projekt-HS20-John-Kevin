package main;

public class Main {
	public static void main(String[] args) {
		Connect4 game1 = new Connect4();
		game1.drawBackground();
		/*System.out.println(game1.isOccupied(3, 4));
		
		game1.setOccupied(3, 4);
		
		System.out.println(game1.isOccupied(3, 4));
		
		game1.drawTakenSpaces();
		
		game1.drawBlankSpaces();
		
		game1.setOccupied(4, 4);*/
		
		game1.dropPiece(3);
		game1.drawTakenSpaces();
		game1.dropPiece(3);
		game1.drawTakenSpaces();
		game1.dropPiece(3);
		game1.drawTakenSpaces();
	}

}
