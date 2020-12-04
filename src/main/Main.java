package main;

public class Main {
	public static void main(String[] args) {
		/*Connect4 game1 = new Connect4();
		game1.init();

		int oldSelected = -1;
		
		while (true)  {
			game1.refreshRate();
			
			while (!game1.rowIsSelected()) {
				if (game1.rowSelected() != oldSelected) {
					game1.drawArrowSelected();
					oldSelected = game1.rowSelected();
				}
				System.out.print("");
			}
			game1.dropPiece(game1.rowSelected());
			game1.drawTakenSpaces();
			
			if (game1.checkFour()) {
				game1.displayWinner();
				break;
			}
			
			if (game1.checkFull()) {
				System.out.println("Nobody wins.");
				break;
			}
		}*/
		
		Game.makeGame();
		
		/*
		Connect4 game2 = new Connect4();
		game2.drawBackground();
		game2.drawTakenSpaces();
		*/
	}
}
