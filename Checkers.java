import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*; 

public class Checkers extends JApplet implements ActionListener {


	// TODO: Make private fields and methods
	// TODO: Throw exceptions
	// TODO: Refactor


	// Array representing the board
	Object[][] grid = new Object[8][8];

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
				y = 31 * (i+1);

				for(int j = 0; j < 8; j++) { // column
					x = 31 * (j+1);
					if(grid[i][j].toString().equals("r")) {
						g.setColor(red);
						g.fillOval(x, y, 30, 30);					}
					else if(grid[i][j].toString().equals("b")) {
						g.setColor(black);
						g.fillOval(x, y, 30, 30);
					}

				
				
				}
			}
		} // end paint

	
	} // end nested class PiecesLayer

	class Piece extends Object {

		String color;
		boolean isKing = false;

		Piece() {
			// default Constructor
			color = "red";  
		}

		Piece(String c) {
			color = c;
		}

		public String toString(){
			return color;
		}

	} // end nexted class Pieces



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
				grid[r][c] = 0;
			}
		}

		// Red pieces on top three rows
		for(int r = 0; r < 3; r++) {
			for(int c = 0; c < 8; c++) {
				if ( (r % 2) == (c % 2) )
					grid[r][c] = new Piece("r");
			}
		}

		// Black pieces on bottom three rows
		for(int r = 5; r < 8; r++) {
			for(int c = 0; c < 8; c++) {
				if ( (r % 2) == (c % 2) )
					grid[r][c] = new Piece("b");

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
			grid[2][0] = 0;
			// Check validity of move
			grid[3][0] = "r";
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


	public void play() {

		while(!gameOver) {
			moveRed();
			moveBlack();
		}

		System.out.println("Game Over!");
		winner.setText("A player won!");



	}

	public void moveRed() {
		turn.setText("It's red's turn");
		move("r");
	}

	public void moveBlack() {
		turn.setText("It's black's turn");

		move("b");

		if(true) {
			gameOver = true;
		}
	}


	public void move(String s) {
	// Where most of the game play occurs

	// Move through the grid looking for the first piece of your color with a viable move
	// Make the move
	// Check for jumps and kings
	// Update pieces count if necessary
	// Check if you won yet.
	// Keep moving until your turn is over.
	// Require clicks to advance the game, otherwise it'd be too fast!

	}


	public void init() {

		initGrid();
		drawBoard();
		initControls();
//		play();


	}


}  // end class
