import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*; 

public class Checkers extends JApplet implements ActionListener {


	// TODO: Make private fields and methods
	// TODO: Throw exceptions
	// TODO: Refactor


	// Array representing the board
	Piece[][] grid = new Piece[8][8];


	int RED = 1;
	int BLACK = -1;
	int EMPTY = 0;


	// Pale colors for the board
	Color pred = new Color(255, 128, 128);
	Color pblack = new Color(125, 125, 125);

	// Vibrant colors for the pieces
	Color red = Color.red;
	Color black = Color.black;

	int numr, numb = 12; // initial number of pieces

	JLabel turn; // Whose turn is it?
	JLabel winner;
	boolean gameOver = false;

	Display display; 	 // the board
	PiecesLayer piecesLayer; // the pieces
	JLayeredPane lp;	 // wrapper for board and pieces


	class Display extends JPanel {

		Display() {
			// Constructor for the Display class.
			setPreferredSize(new Dimension(504, 504));       
		}

		public void paint(Graphics g){

			/*  Draw a red-and-black checkerboard.
			    Each square is 63 pixels, making for a board of 504 pixels.  
			 */

			int row;   // Row number, from 0 to 7
			int col;   // Column number, from 0 to 7
			int x,y;   // Top-left corner of square

			for ( row = 0;  row < 8;  row++ ) {

				for ( col = 0;  col < 8;  col++) {
					x = col * 63;
					y = row * 63;
					if ( (row % 2) == (col % 2) )
						g.setColor(pred);
					else
						g.setColor(pblack);

					g.fillRect(x, y, 63, 63);
				} 

			} // end for row

		} // end paintComponent

	} // end nested class Display

	class PiecesLayer extends JPanel {

		PiecesLayer() {
			// Constructor
			setPreferredSize(new Dimension(504, 504));  
			setOpaque(false);
		}

		public void paint(Graphics g) {
			int x;
			int y;

			for(int i = 0; i < 8; i++) { // row
				y = (63 * i) + 16;

				for(int j = 0; j < 8; j++) { // column
					x = (63 * j + 16);

					if(RED == grid[i][j].color) {
						g.setColor(red);
					}
					else if(BLACK == grid[i][j].color) {
						g.setColor(black);
					}

					g.fillOval(x, y, 30, 30);
				}
			}
		} // end paint


	} // end nested class PiecesLayer

	// --------------------------------------------------------------
	// --------------------------------------------------------------
	// --------------------------------------------------------------


	class Piece extends Object {

		// What methods should Piece have?

		int color;
		boolean isKing;
		int x;
		int y;

		Piece() {
			// default Constructor.
			// Use to construct empty space?
			this(EMPTY, -1, -1);
		}

		Piece(int c) {
			this(c, -1, -1);
		}

		Piece(int c, int x, int y) {
			color = c;
			this.x = x;
			this.y = y;
			isKing = false;
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



		// ---------------------------------------------

		public void move() {
			// Need to loop through grid back to x-1, not just to end.
			// Start with naive implementation - search to end.
			for(int i = x; i < grid.length; i++) {
				for(int j = y; j < grid.length; j++) {

					if(this.color == grid[x][y].color && 
							grid[x][y].canMove()) {
						// Code to move the piece
						// If it's still your turn, call move()
						// else, return;
					}


				}
			} // end for loops

		} // end move()



		// Determines if a piece can move
		public boolean canMove() {

			// Use color to set what directions you check

			// Check straight ahead
			if(EMPTY == grid[x][y + (1 * color)].color && 
					EMPTY == grid[x][y + (2 * color)].color) 			{
				// Move it forward one step
				grid[x][y + (2 * color)] = grid[x][y]; 
				grid[x][y] = new Piece();
				piecesLayer.repaint();

			}

			// Check left diagonal
			else if(true) {


			}
			// Check right diagonal
			else if(true) {

			}	


			if(this.isKing) {

				// Kings can move backwards as well
				// make the move, return true
				gameOver = true;
				return true;

			}



			// A piece can move if it can jump forward diagonally 
			// OR if it has a (1 or 2?) free space in front of it




			return false;

		} // end canMove()



	} // end nexted class Pieces





	// --------------------------------------------------------------
	// --------------------------------------------------------------
	// --------------------------------------------------------------

	public void drawBoard() {

		// Use a Layered Pane to hold the board
		lp = new JLayeredPane();
		lp.setPreferredSize(new Dimension(504, 504)); 
		// Layout messes with layering, must use absolute positions
		//		lp.setLayout(new BorderLayout()); 
		getContentPane().add(lp, BorderLayout.CENTER);

		// Draw the blank board 
		display = new Display();
		display.setBounds(0,0,504,504);
		lp.add(display, new Integer(0), 0);

		//		lp.add(display, BorderLayout.CENTER, JLayeredPane.DEFAULT_LAYER);
		//		getContentPane().add(display, BorderLayout.CENTER);


		// Draw the pieces
		piecesLayer = new PiecesLayer();
		piecesLayer.setBounds(0,0,504,504);
		lp.add(piecesLayer, new Integer(1), 0);

		//		lp.add(piecesLayer, BorderLayout.CENTER, new Integer(20));
		//		getContentPane().add(piecesLayer, BorderLayout.CENTER);

		System.out.println("--- Current Board:");
		printBoard();

	}

	public void initGrid() {
		// Clear out the grid
		for(int r = 0; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				grid[r][c] = new Piece(EMPTY);
			}
		}

		// Red pieces on top three rows
		for(int r = 0; r < 3; r++) {
			for(int c = 0; c < 8; c++) {
				if ( (r % 2) == (c % 2) )
					grid[r][c] = new Piece(RED, r, c);
			}
		}

		// Black pieces on bottom three rows
		for(int r = 5; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				if ( (r % 2) == (c % 2) )
					grid[r][c] = new Piece(BLACK, r, c);

			}
		}


	}


	public void initControls() {
		// Draw the controls
		JPanel controls = new JPanel();
		controls.setBackground(Color.gray);

		JButton move = new JButton("Move Red");
		move.addActionListener(this);	
		controls.add(move); // add to panel

		/*
		   JButton blackButton = new JButton("Move Black");
		   blackButton.addActionListener(this);
		   controls.add(blackButton);
		 */


		turn = new JLabel("Red starts.");
		controls.add(turn);

		winner = new JLabel("No winner yet.");	
		controls.add(winner);	

		JButton reset = new JButton("Reset");
		reset.addActionListener(this);
		controls.add(reset);

		getContentPane().add(controls, BorderLayout.SOUTH);


	}


	public void printBoard() {
		// Print board to console
		for(int i = 0; i<grid.length; i++) {
			for(int j = 0; j < 8; j++) {
				System.out.print(grid[i][j] + "-");
			}

			System.out.println();
		}



	}


	public void actionPerformed(ActionEvent evt) { 
		// Event listenrs for buttons
		String command = evt.getActionCommand();
		System.out.println("Button: " + command);

		if("Move Red".equals(command)) {
			// Just playing around.  Move the red one at 2, 2 forward one space
			grid[3][0] = grid[2][0];
			grid[2][0] = new Piece(EMPTY);
			// Check validity of move
			piecesLayer.repaint();
			System.out.println("--- Moved a red piece:");
			printBoard();



		}
		else if("Move Black".equals(command)) {

		}
		else if("Reset".equals(command)) {

			initGrid();
			piecesLayer.repaint();
			System.out.println("--- Reset Grid:");
			printBoard();
		}

	}



	// --------------------------------------------------------------
	// --------------------------------------------------------------
	// --------------------------------------------------------------


	public void play() {

		while(!gameOver) {
			grid[0][0].move();			
		}

		System.out.println("Game Over!");
		winner.setText("A player won!");


	}

	public void init() {

		initGrid();
		drawBoard();
		initControls();
		play();

	}


}  // end class
