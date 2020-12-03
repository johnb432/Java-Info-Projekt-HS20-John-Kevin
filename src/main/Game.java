package main;

public class Game {
	public static void makeGame() {
		Connect4 game = new Connect4();
		game.init();

		int oldSelected = -1;

		while (true) {
			game.refreshRate();

			while (!game.rowIsSelected()) {
				if (game.rowSelected() != oldSelected) {
					game.drawArrowSelected();
					oldSelected = game.rowSelected();
				}
				System.out.print("");
			}
			game.dropPiece(game.rowSelected());
			game.drawTakenSpaces();

			if (game.checkFour()) {
				game.displayWinner();
				if (!game.playAgain()) {
					break;
				}
			}

			if (game.checkFull()) {
				System.out.println("Nobody wins.");
				if (!game.playAgain()) {
					break;
				}
			}
		}
	}
}
