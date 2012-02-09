import java.util.ArrayList;


/**
 * HW2: A game of Checkers with some strategy.
 * Due Monday February 13, 2012.
 *
 * @author Diana Liu
*/

public class Piece extends Object {

    // TODO: Randomize first moves.
    // FIXME: Once again, doesn't always jump when it has the chance to


	public static final int RED = 1;
	public static final int BLACK = -1;
	public static final int EMPTY = 0;

	// initial number of pieces
    static int numr = 12;
	static int numb = 12; 

    // Array representing the board
    public static Piece[][] grid = new Piece[8][8];

	// Data fields of a Piece
	private int color;
	private boolean isKing;
	private int x;
	private int y;
    // FIXME: Used to detect multiple jumps.  "continue" was reserved :(
    private boolean cont;
    // Track of all possible moves for this piece
    private static ArrayList moves;


	Piece() {
		// default Constructor, not used.
	}

	Piece(int c) {
		this(c, -1, -1);
	}

	Piece(int c, int x, int y) {
		this(c, x, y, false);
  	}

    Piece(int c, int x, int y, boolean isKing) {
        this.color = c;
        this.x = x;
        this.y = y;
        this.isKing = isKing;
        cont = false;
        moves = new ArrayList();
    }

	public String toString(){
        // FIXME: Include getCoords()?

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

    public String info() {
        return this.toString() + this.getCoords();

    }

    public void logMoves() {

        System.out.println("All possible moves for " + this.info() + ":");
        
        if(!this.moves.isEmpty()) {

            for(int i = 0; i < moves.size(); i++) {
            
                Piece [] tmp = (Piece []) moves.get(i);

                for(int j = 0; j < tmp.length; j++) {

                    System.out.print(tmp[j].info() + " ");
                }

                System.out.println();

            }


        }
        else {
            System.out.println("this.moves() is empty");
        }

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

	// FIXME: Replace these with error catching DUH. <sheepish look>
    
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

        // FIXME: Immediate detection of gameOver, 
        // currently needs a few clicks from the other player 
        // before detection.

        if(Checkers.gameOver == true) return;
	
		System.out.println("------------------------------------");

        // Find all moves possible and save to list allMoves
        this.findMoves();

        this.logMoves();

        if(!this.moves.isEmpty()) {
            
            makeMove((Piece [])this.moves.get(0));

        }
        else {

            System.out.println(this + " has run out of moves!");
		    Checkers.gameOver = true;

        }

		Checkers.printBoard();
		return;

	} // end move()


    public void findMoves() {

        int x = 0;
        int y = 0;


        // Ok to clear??  Don't know if we'll be passing around yet.
        this.moves.clear();

        do {

            do {

                if(this.color == grid[x][y].color) {
                        // Try finding jumps first
                        grid[x][y].addJumps();

                        // Then, try finding steps
                        grid[x][y].addSteps();
                }

                y = (y + 1) % 8;

            } while (y != 0);

            x = (x + 1) % 8;

        } while (x != 0);


    } // end findMoves()

    
    public void addJumps() {

        // TODO: Check for multiple jumps

        Piece p = this;

        if(p.isKing && p.hasSW2()) {

            Piece sw1 = grid[x + (1 * -color)][y + (1 * -color)];
            Piece sw2 = grid[x + (2 * -color)][y + (2 * -color)];

            if(EMPTY == sw2.color 
                    && EMPTY != sw1.color && p.color != sw1.color) {


                // Add move states from, to, eaten
                p.moves.add(new Piece[] {p, sw2, sw1});

//                System.out.println(p.info() + " has jump:");
//                System.out.println(p.moves.get(moves.size()-1));

                // Check for multiple jumps
                sw2.addJumps();
                if(!sw2.moves.isEmpty()) {
                    p.cont = true;
                }

            }

        } // end SW jump

        if(p.isKing && p.hasSE2()) {

            Piece se1 = grid[x + (1 * -color)][ y + (-1 * -color)];
            Piece se2 = grid[x + (2 * -color)][y + (-2 * -color)];

            if(EMPTY == se2.color 
                    && EMPTY != se1.color && p.color != se1.color) {

                // Add move states from, to, eaten
                p.moves.add(new Piece[] {p, se2, se1});

//                System.out.println(p.info() + " has jump:");
//                System.out.println(p.moves.get(moves.size()-1));

                // Check for multiple jumps
                se2.addJumps();
                if(!se2.moves.isEmpty()) {
                    p.cont = true;
                }

            }

        } // end SE jump


		if(p.hasNW2()) {

			Piece nw1 = grid[p.x + (1 * color)][p.y + (1 * color)];
			Piece nw2 = grid[p.x + (2 * color)][p.y + (2 * color)];

			if(EMPTY == nw2.color 
					&& EMPTY != nw1.color && p.color != nw1.color) {

                    // Add move states from, to, eaten
                    p.moves.add(new Piece[] {p, nw2, nw1});

                    // Check for multiple jumps
                    nw2.addJumps();
                    if(!nw2.moves.isEmpty()) {
                        p.cont = true;
                    }

				}

		} // end if NW jump


	if(p.hasNE2()) {

			Piece ne1 = grid[p.x + (1 * color)][p.y + (-1 * color)];
			Piece ne2 = grid[p.x + (2 * color)][p.y + (-2 * color)];

			if(EMPTY == ne2.color 
					&& EMPTY != ne1.color && p.color != ne1.color) {

                    // Add move states from, to, eaten
                    p.moves.add(new Piece[] {p, ne2, ne1});

                    // Check for multiple jumps
                    ne2.addJumps();
                    if(!ne2.moves.isEmpty()) {
                        p.cont = true;
                    }
			
			}

		} // end NE jump


    } // end addJumps()

    public void addSteps() {

        Piece p = this;

        if(p.isKing && p.hasSE1()) {

            Piece se1 = grid[x + (1 * -color)][y + (-1 * -color)];

            if(EMPTY == se1.color) {

                // Add move states from, to
                p.moves.add(new Piece[] {p, se1});

//                System.out.println(p.info() + " has step:");
//                System.out.println(p.moves.get(moves.size()-1));

            }
        } // end SE step


        if(p.isKing && p.hasSW1()) {

            Piece sw1 = grid[x + (1 * -color)][y + (1 * -color)];

            if(EMPTY == sw1.color) {

                // Add move states from, to
                p.moves.add(new Piece[] {p, sw1});

//                System.out.println(p.info() + " has step:");
//                System.out.println(p.moves.get(moves.size()-1));

            }

        } // end SW step


        if(p.hasNW1()) {

            // NW step
            Piece nw1 = grid[p.x + (1 * color)][p.y + (1 * color)];

            if(EMPTY == nw1.color) {

                // Add move states from, to
                p.moves.add(new Piece[] {p, nw1});

//                System.out.println(p.info() + " has step:");
//                System.out.println(p.moves.get(moves.size()-1));

            }

        } // end NW step


        if(p.hasNE1()) {

            Piece ne1 = grid[p.x + (1 * color)][p.y + (-1 * color)];

            if(EMPTY == ne1.color) {

                // Add move states from, to
                p.moves.add(new Piece[] {p, ne1});

//                System.out.println(p.info() + " has step:");
//                System.out.println(p.moves.get(moves.size()-1));
            }

        } // end NE step


    } // end addSteps()


    public void makeMove(Piece [] m) {


        Piece from = m[0];
        Piece to = m[1];
        Piece eaten = this;
        if(m.length > 2) { eaten = m[2]; }


        // Update grid to match move.
        grid[from.x][from.y] = new Piece(EMPTY, from.x, from.y);
        grid[to.x][to.y] = new Piece(from.color, to.x, to.y);
        if(m.length > 2) {

            grid[eaten.x][eaten.y] = new Piece(EMPTY, eaten.x, eaten.y);

            // Update counts
            if(eaten.color == RED) numr--;
            else numb--;
            if(0 == numr || 0 ==numb) Checkers.gameOver = true;

        }


        // Check for kingship
        if(grid[to.x][to.y].madeKing()) {

            // FIXME: Not sure kingship and continuation check is perfect...

            this.isKing = true;
            // If a piece just made king, turn is over. 
            this.cont = false;

        }

        // Preserve kingship
        if(this.isKing) {
            grid[to.x][to.y].isKing = true;
        }


        // Re-draw
//        Checkers.printBoard();
        Checkers.pieces.repaint();

        System.out.print("Moved:");
        System.out.print("\tFrom " + from.info());
        System.out.print(" To " + to.info() );
        if(m.length > 2) { System.out.print(" Ate " + eaten.info()); }
        System.out.println();

    } // end makeMove()


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

			Piece sw1 = grid[x + (1 * -color)][y + (1 * -color)];
			Piece sw2 = grid[x + (2 * -color)][y + (2 * -color)];

			if(EMPTY == sw2.color 
					&& EMPTY != sw1.color && p.color != sw1.color) {

				System.out.print("SW: ");
				log(p, sw2, sw1);

				// Move to sw2.  Eat sw1.
				grid[sw2.x][sw2.y] = new Piece(p.color, sw2.x, sw2.y);
				grid[sw1.x][sw1.y] = new Piece(EMPTY, sw1.x, sw1.y);
				grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);


				// Decrement num pieces
				if(sw1.color == RED) numr--;
				else numb--;
				if(0 == numr || 0 ==numb) Checkers.gameOver = true;



				// Re-draw
				Checkers.printBoard();
				Checkers.pieces.repaint();

				//                    System.out.println("Keep jumping?" + grid[sw2.x][sw2.y] 
				//                        + grid[sw2.x][sw2.y].getCoords());

				// Check for king-ship
				if(grid[sw2.x][sw2.y].madeKing() || p.isKing) {
					grid[sw2.x][sw2.y].isKing = true;
                    return true;
				}

				grid[sw2.x][sw2.y].tryJumps();

				//                    System.out.println("--- Stop Jumping.");


				return true;

			}


		} // end SW jump

		if(p.isKing && p.hasSE2()) {


            // FIXME: Do I need p.x and p.y here?
			Piece se1 = grid[x + (1 * -color)][ y + (-1 * -color)];
			Piece se2 = grid[x + (2 * -color)][y + (-2 * -color)];

			if(EMPTY == se2.color 
					&& EMPTY != se1.color && p.color != se1.color) {
				System.out.print("SW: ");
				log(p, se2, se1);

				// Move to se2.  Eat se1.
				grid[se2.x][se2.y] = new Piece(p.color, se2.x, se2.y);
				grid[se1.x][se1.y] = new Piece(EMPTY, se1.x, se1.y);
				grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);


				// Decrement num pieces
				if(se1.color == RED) numr--;
				else numb--;
				if(0 == numr || 0 ==numb) Checkers.gameOver = true;



				// Re-draw
				Checkers.printBoard();
				Checkers.pieces.repaint();

				// Check for king-ship
				if(grid[se2.x][se2.y].madeKing()|| p.isKing) {
					grid[se2.x][se2.y].isKing = true;
                    return true;
				}



				//                    System.out.println("Keep jumping?" + grid[se2.x][se2.y] 
				//                        + grid[se2.x][se2.y].getCoords());

				grid[se2.x][se2.y].tryJumps();

				//                    System.out.println("--- Stop Jumping.");


				return true;

			}

		} // end SE jump


		if(p.hasNW2()) {

			Piece nw1 = grid[p.x + (1 * color)][p.y + (1 * color)];
			Piece nw2 = grid[p.x + (2 * color)][p.y + (2 * color)];

			if(EMPTY == nw2.color 
					&& EMPTY != nw1.color && p.color != nw1.color) {

				System.out.print("NW: ");
				log(p, nw2, nw1);

				// Move to nw2. Eat nw1.
				grid[nw2.x][nw2.y] = new Piece(p.color, nw2.x, nw2.y);  
				grid[nw1.x][nw1.y] = new Piece(EMPTY, nw1.x ,nw1.y);
				grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);


				if(nw1.color == RED) numr--;
				else numb--;
				if(0 == numr || 0 ==numb) Checkers.gameOver = true;



				Checkers.printBoard();
				Checkers.pieces.repaint();

               	if(grid[nw2.x][nw2.y].madeKing() || p.isKing) {
					grid[nw2.x][nw2.y].isKing = true;
                    return true;
				}


				//                    System.out.println("Keep jumping?" + grid[nw2.x][nw2.y] 
				//                        + grid[nw2.x][nw2.y].getCoords());
				grid[nw2.x][nw2.y].tryJumps();
				//                    System.out.println("--- Stop Jumping.");



				return true;
			}

		} // end if NW jump

		if(p.hasNE2()) {

			Piece ne1 = grid[p.x + (1 * color)][p.y + (-1 * color)];
			Piece ne2 = grid[p.x + (2 * color)][p.y + (-2 * color)];

			if(EMPTY == ne2.color 
					&& EMPTY != ne1.color && p.color != ne1.color) {

				System.out.print("NE: ");
				log(p, ne2, ne1);

				// Move to ne2.  Eat ne1.
				grid[ne2.x][ne2.y] = new Piece(p.color, ne2.x, ne2.y);
				grid[ne1.x][ne1.y] = new Piece(EMPTY, ne1.x , ne1.y);
				grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(RED == ne1.color) numr--;
				else numb--;
				if(0 == numr || 0 ==numb) Checkers.gameOver = true;



				Checkers.printBoard();
				Checkers.pieces.repaint();


               	if(grid[ne2.x][ne2.y].madeKing() || p.isKing) {
					grid[ne2.x][ne2.y].isKing = true;
                    return true;
				}


				//                    System.out.println("Keep jumping?" + grid[ne2.x][ne2.y] 
				//                        + grid[ne2.x][ne2.y].getCoords());
				grid[ne2.x][ne2.y].tryJumps();
				//                    System.out.println("--- Stop Jumping.");

				return true;

			}

		} // end NE jump

		// No jumps possible.
		return false;

	} // end tryJumps

	// Try stepping in this order: SE, SW, NW, NE.
	public boolean trySteps() {

		// FIXME: An isolated King can get caught in a loop, repeating the same moves.

		Piece p = this;

		if(p.isKing && p.hasSE1()) {

			Piece se1 = grid[x + (1 * -color)][y + (-1 * -color)];

			if(EMPTY == se1.color) {

				System.out.print("SE: ");
				log(p, se1);

				grid[se1.x][se1.y] = new Piece(p.color, se1.x, se1.y);
				grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(grid[se1.x][se1.y].madeKing() || p.isKing) {
					grid[se1.x][se1.y].isKing = true;
				}

				Checkers.printBoard();
				Checkers.pieces.repaint();
				return true;			

			}

		} // end SE step

		if(p.isKing && p.hasSW1()) {

			Piece sw1 = grid[x + (1 * -color)][y + (1 * -color)];

			if(EMPTY == sw1.color) {

				System.out.print("SW: ");
				log(p, sw1);

				grid[sw1.x][sw1.y] = new Piece(p.color, sw1.x, sw1.y);
				grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(grid[sw1.x][sw1.y].madeKing() || p.isKing) {
					grid[sw1.x][sw1.y].isKing = true;
				}

				Checkers.printBoard();
				Checkers.pieces.repaint();
				return true;			

			}

		} // end SW step


		if(p.hasNW1()) {

			// NW step
			Piece nw1 = grid[p.x + (1 * color)][p.y + (1 * color)];

			if(EMPTY == nw1.color) {

				System.out.print("NW: ");
				log(p, nw1);

				grid[nw1.x][nw1.y] = new Piece(p.color, nw1.x, nw1.y);
				grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(grid[nw1.x][nw1.y].madeKing() || p.isKing) {
					grid[nw1.x][nw1.y].isKing = true;
				}

				Checkers.printBoard();
				Checkers.pieces.repaint();
				return true;

			}

		} // end NW step

		if(p.hasNE1()) {

			Piece ne1 = grid[p.x + (1 * color)][p.y + (-1 * color)];

			if(EMPTY == ne1.color) {

				System.out.print("NE: ");
				log(p, ne1);

				grid[ne1.x][ne1.y] = new Piece(p.color, ne1.x, ne1.y);
				grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

				if(grid[ne1.x][ne1.y].madeKing() || p.isKing) {
					grid[ne1.x][ne1.y].isKing = true;
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



