
public class Piece extends Object {


	public static final int RED = 1;
	public static final int BLACK = -1;
	public static final int EMPTY = 0;

	// initial number of pieces
	private int numr = 12;
	private int numb = 12; 

	// Data fields of Piece
	private int color;
	private boolean isKing;
	private int x;
	private int y;
	boolean seen; // (not) used to select next avail piece


	Piece() {
		// default Constructor, not used.
	}

	Piece(int c) {
		this(c, -1, -1);
	}

	Piece(int c, int x, int y) {
		color = c;
		this.x = x;
		this.y = y;
		isKing = false;
		seen = false;
	}

	public String toString(){
		if(color == 1) return "r";
		else if(color == -1) return "b";
		else return "0";
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getColor() {
		return color;
	}

	public boolean getKing() {
		return isKing;
	}

	public String getCoords() {
		return "(" + x + ", " + y + ")";
	}


	// ---------------------------------------------


	public void log(Piece from, Piece to) {
		System.out.println("Moving " + from + " from " + from.getCoords() 
				+ " to " + to.getCoords());
	}

	public void log(Piece from, Piece to, Piece ate) {
		System.out.println("Moving " + from + " from " + from.getCoords() 
				+ " to " + to.getCoords() + " and ate " + ate 
				+ " at " + ate.getCoords());

	}


	// ---------------------------------------------


	// Checks if a NW1 step stays on the board
	public boolean hasNW1() {

		if(x + (1 * color) < 8 && x + (1 * color) >= 0 && 
				y + (1 * color) < 8 && y + (1 * color) >= 0) {
			return true;
		}

		return false;
	}

	// Checks if a NW2 jump stays on the board
	public boolean hasNW2() {

		if(x + (2 * color) < 8 && x + (2 * color) >= 0 && 
				y + (2 * color) < 8 && y + (2 * color) >= 0) {
			return true;
		}

		return false;
	}

	// Checks if a NE1 step stays on the board
	public boolean hasNE1() {

		if(x + (1 * color) < 8 && x + (1 * color) >= 0 &&
				y + (-1 * color) < 8 && y + (-1 * color) >= 0) {
			return true;
		}
		return false;
	}

	// Checks if a NE2 jump stays on the board
	public boolean hasNE2() {

		if(x + (2 * color) < 8 && x + (2 * color) >= 0 &&
				y + (-2 * color) < 8 && y + (-2 * color) >= 0) {
			return true;
		}
		return false;
	}

	// Checks if a SW1 step stays on the board
	public boolean hasSW1() {

		// Same as NW1, but in the opposite direction (color)
		if(x + (1 * -color) < 8 && x + (1 * -color) >= 0 && 
				y + (1 * -color) < 8 && y + (1 * -color) >= 0) {
			return true;
		}

		return false;
	}

	// Checks if a SW2 jump stays on the board
	public boolean hasSW2() {

		if(x + (2 * -color) < 8 && x + (2 * -color) >= 0 && 
				y + (2 * -color) < 8 && y + (2 * -color) >= 0) {
			return true;
		}

		return false;
	}


	// Checks if a SE step stays on the board
	public boolean hasSE1() {

		if(x + (1 * -color) < 8 && x + (1 * -color) >= 0 &&
				y + (-1 * -color) < 8 && y + (-1 * -color) >= 0) {
			return true;
		}

		return false;
	}

	// Checks if a SE jump stays on the board
	public boolean hasSE2() {

		if(x + (2 * -color) < 8 && x + (2 * -color) >= 0 &&
				y + (-2 * -color) < 8 && y + (-2 * -color) >= 0) {
			return true;
		}


		return false;
	}


	// ---------------------------------------------


	// Finds the next free piece and tries moving it.
	// If none are found, game is over.
	public void move() {

		// FIXME: Better algorithm to pick next piece
		// If there are jumps available, I don't always take it 
		// because I encountered another moveable piece before seeing it.
		// TODO: Depending on the color, search from other end.
		// TODO: Introduce randomness.  Currently always plays the same game.


		System.out.println("------------------------------------");

		// Search for a piece to move.  
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {

				if(this.color == Checkers.grid[i][j].color) {

					boolean hasMove = Checkers.grid[i][j].tryAll();
					if(hasMove) return;

				} // end if

			}
		}

		// In current sim, black should be the winner.
		System.out.println(this + " has run out of moves!");
		Checkers.gameOver = true;
		Checkers.printBoard();
		return;

	} // end move()

	// See if a piece can jump, can step. 
	public boolean tryAll() {

		//            System.out.println("Examining " + this + " at " + this.getCoords());

		boolean canJump = this.tryJumps();

		if(canJump) return true;
		else {
			boolean canStep = this.trySteps();
			return canStep;
		}

	} // end tryAll()


	// Try jumping in this order: SW, SE, NW, NE.
	public boolean tryJumps() {

		Piece p = this;

		if(p.isKing && p.hasSW2()) {

			Piece sw1 = Checkers.grid[x + (1 * -color)][y + (1 * -color)];
			Piece sw2 = Checkers.grid[x + (2 * -color)][y + (2 * -color)];

			if(EMPTY == sw2.color 
					&& EMPTY != sw1.color && p.color != sw1.color) {

				System.out.print("SW: ");
				log(p, sw2, sw1);

				// Move to sw2.  Eat sw1.
				Checkers.grid[sw2.x][sw2.y] = new Piece(p.color, sw2.x, sw2.y);
				Checkers.grid[sw1.x][sw1.y] = new Piece(EMPTY, sw1.x, sw1.y);
				Checkers.grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);


				// Decrement num pieces
				if(sw1.color == RED) numr--;
				else numb--;
				if(0 == numr || 0 ==numb) Checkers.gameOver = true;


				// Check for king-ship
				if(Checkers.grid[sw2.x][sw2.y].madeKing() || p.isKing) {
					Checkers.grid[sw2.x][sw2.y].isKing = true;
				}

				// Re-draw
				Checkers.printBoard();
				Checkers.pieces.repaint();

				//                    System.out.println("Keep jumping?" + Checkers.grid[sw2.x][sw2.y] 
				//                        + Checkers.grid[sw2.x][sw2.y].getCoords());

				Checkers.grid[sw2.x][sw2.y].tryJumps();

				//                    System.out.println("--- Stop Jumping.");


				return true;

			}


		} // end SW jump

		if(p.isKing && p.hasSE2()) {

			Piece se1 = Checkers.grid[x + (1 * -color)][ y + (-1 * -color)];
			Piece se2 = Checkers.grid[x + (2 * -color)][y + (-2 * -color)];

			if(EMPTY == se2.color 
					&& EMPTY != se1.color && p.color != se1.color) {
				System.out.print("SW: ");
				log(p, se2, se1);

				// Move to se2.  Eat se1.
				Checkers.grid[se2.x][se2.y] = new Piece(p.color, se2.x, se2.y);
				Checkers.grid[se1.x][se1.y] = new Piece(EMPTY, se1.x, se1.y);
				Checkers.grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);


				// Decrement num pieces
				if(se1.color == RED) numr--;
				else numb--;
				if(0 == numr || 0 ==numb) Checkers.gameOver = true;


				// Check for king-ship
				if(Checkers.grid[se2.x][se2.y].madeKing()|| p.isKing) {
					Checkers.grid[se2.x][se2.y].isKing = true;
				}

				// Re-draw
				Checkers.printBoard();
				Checkers.pieces.repaint();


				//                    System.out.println("Keep jumping?" + Checkers.grid[se2.x][se2.y] 
				//                        + Checkers.grid[se2.x][se2.y].getCoords());

				Checkers.grid[se2.x][se2.y].tryJumps();

				//                    System.out.println("--- Stop Jumping.");


				return true;

			}

		} // end SE jump


		if(p.hasNW2()) {

			Piece nw1 = Checkers.grid[p.x + (1 * color)][p.y + (1 * color)];
			Piece nw2 = Checkers.grid[p.x + (2 * color)][p.y + (2 * color)];

			if(EMPTY == nw2.color 
					&& EMPTY != nw1.color && p.color != nw1.color) {

				System.out.print("NW: ");
				log(p, nw2, nw1);

				// Move to nw2. Eat nw1.
				Checkers.grid[nw2.x][nw2.y] = new Piece(p.color, nw2.x, nw2.y);  
				Checkers.grid[nw1.x][nw1.y] = new Piece(EMPTY, nw1.x ,nw1.y);
				Checkers.grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);


				if(nw1.color == RED) numr--;
				else numb--;
				if(0 == numr || 0 ==numb) Checkers.gameOver = true;


				if(Checkers.grid[nw2.x][nw2.y].madeKing() || p.isKing) {
					Checkers.grid[nw2.x][nw2.y].isKing = true;
				}

				Checkers.printBoard();
				Checkers.pieces.repaint();

				//                    System.out.println("Keep jumping?" + Checkers.grid[nw2.x][nw2.y] 
				//                        + Checkers.grid[nw2.x][nw2.y].getCoords());
				Checkers.grid[nw2.x][nw2.y].tryJumps();
				//                    System.out.println("--- Stop Jumping.");



				return true;
			}

		} // end if NW jump

		if(p.hasNE2()) {

			Piece ne1 = Checkers.grid[p.x + (1 * color)][p.y + (-1 * color)];
			Piece ne2 = Checkers.grid[p.x + (2 * color)][p.y + (-2 * color)];

			if(EMPTY == ne2.color 
					&& EMPTY != ne1.color && p.color != ne1.color) {

				System.out.print("NE: ");
				log(p, ne2, ne1);

				// Move to ne2.  Eat ne1.
				Checkers.grid[ne2.x][ne2.y] = new Piece(p.color, ne2.x, ne2.y);
				Checkers.grid[ne1.x][ne1.y] = new Piece(EMPTY, ne1.x , ne1.y);
				Checkers.grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(RED == ne1.color) numr--;
				else numb--;
				if(0 == numr || 0 ==numb) Checkers.gameOver = true;


				if(Checkers.grid[ne2.x][ne2.y].madeKing() || p.isKing) {
					Checkers.grid[ne2.x][ne2.y].isKing = true;
				}

				Checkers.printBoard();
				Checkers.pieces.repaint();

				//                    System.out.println("Keep jumping?" + Checkers.grid[ne2.x][ne2.y] 
				//                        + Checkers.grid[ne2.x][ne2.y].getCoords());
				Checkers.grid[ne2.x][ne2.y].tryJumps();
				//                    System.out.println("--- Stop Jumping.");

				return true;

			}

		} // end NE jump

		// No jumps possible.
		return false;

	} // end tryJumps

	// Try stepping in this order: SE, SW, NW, NE.
	public boolean trySteps() {

		// FIXME: If a king's the the "earliest" piece (always picked in 
		// current algorithm) and has no neighbors, it gets caught in a loop
		// bc it can only move one step either way.  
		// Should be fixed when a smarter choosing algorithm is introduced.

		Piece p = this;

		if(p.isKing && p.hasSE1()) {

			Piece se1 = Checkers.grid[x + (1 * -color)][y + (-1 * -color)];

			if(EMPTY == se1.color) {

				System.out.print("SE: ");
				log(p, se1);

				Checkers.grid[se1.x][se1.y] = new Piece(p.color, se1.x, se1.y);
				Checkers.grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(Checkers.grid[se1.x][se1.y].madeKing() || p.isKing) {
					Checkers.grid[se1.x][se1.y].isKing = true;
				}

				Checkers.printBoard();
				Checkers.pieces.repaint();
				return true;			

			}

		} // end SE step

		if(p.isKing && p.hasSW1()) {

			Piece sw1 = Checkers.grid[x + (1 * -color)][y + (1 * -color)];

			if(EMPTY == sw1.color) {

				System.out.print("SW: ");
				log(p, sw1);

				Checkers.grid[sw1.x][sw1.y] = new Piece(p.color, sw1.x, sw1.y);
				Checkers.grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(Checkers.grid[sw1.x][sw1.y].madeKing() || p.isKing) {
					Checkers.grid[sw1.x][sw1.y].isKing = true;
				}

				Checkers.printBoard();
				Checkers.pieces.repaint();
				return true;			

			}

		} // end SW step


		if(p.hasNW1()) {

			// NW step
			Piece nw1 = Checkers.grid[p.x + (1 * color)][p.y + (1 * color)];

			if(EMPTY == nw1.color) {

				System.out.print("NW: ");
				log(p, nw1);

				Checkers.grid[nw1.x][nw1.y] = new Piece(p.color, nw1.x, nw1.y);
				Checkers.grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(Checkers.grid[nw1.x][nw1.y].madeKing() || p.isKing) {
					Checkers.grid[nw1.x][nw1.y].isKing = true;
				}

				Checkers.printBoard();
				Checkers.pieces.repaint();
				return true;

			}

		} // end NW step

		if(p.hasNE1()) {

			Piece ne1 = Checkers.grid[p.x + (1 * color)][p.y + (-1 * color)];

			if(EMPTY == ne1.color) {

				System.out.print("NE: ");
				log(p, ne1);

				Checkers.grid[ne1.x][ne1.y] = new Piece(p.color, ne1.x, ne1.y);
				Checkers.grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(Checkers.grid[ne1.x][ne1.y].madeKing() || p.isKing) {
					Checkers.grid[ne1.x][ne1.y].isKing = true;
				}

				Checkers.printBoard();
				Checkers.pieces.repaint();
				return true;			

			}

		} // end NE step


		// No steps possible.
		return false;

	} // end trySteps()


	// Check if a move has resulted in a piece made King
	public boolean madeKing() {

		if(RED == this.color && 7 == this.x) {
			return true;
		}
		else if(BLACK == this.color && 0 == this.x) {
			return true;
		}

		return false;
	} // end madeKing()


} // end nested class Pieces



