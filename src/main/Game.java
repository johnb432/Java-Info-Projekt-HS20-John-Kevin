package main;

public class Game {
	public static void makeGame() {
		Connect4 game = new Connect4();
		game.init();

		int oldSelected = -1;

		while (true) {
			// Sets the refresh rate
			game.refreshRate();
			
			// Unlocks keyboard input
			game.allowInput = true;
			
			// Looks for a row to be selected
			while (!game.columnIsSelected()) {
				game.refreshRate();
				
				// Prevents unnecessary updating of graphics; only responds to change
				if (game.isColumnSelected() != oldSelected) {
					game.drawArrowSelected();
					oldSelected = game.isColumnSelected();
				}
			}
			
			// If a piece is successfully dropped, it will switch players at the end of the loop
			boolean success = game.dropPiece(game.isColumnSelected());
			
			// Draws the taken space in this move
			game.drawTakenSpaces();

			// End game checks
			if (game.getTurnCount() > 6 && game.checkFour()) {
				game.displayWinner();
				if (!game.playAgain()) {
					break;
				}
			} else if (game.checkFull()) {
				game.displayNobody();
				if (!game.playAgain()) {
					break;
				}
			}
			
			if (success) {
				game.switchPlayer();
			}
		}
	}
}
