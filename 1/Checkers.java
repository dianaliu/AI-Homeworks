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

    int turn = 1; // red starts
    int tries = 0; // lame attempt at detecting when no moves exist

    // Pale colors for the board
    Color pred = new Color(255, 128, 128);
    Color pblack = new Color(125, 125, 125);

    // Vibrant colors for the pieces
    Color red = Color.red;
    Color black = Color.black;

    int numr = 12;
    int numb = 12; // initial number of pieces

    JLabel turnL; // Whose turn is it?
    JLabel winner;
    boolean gameOver = false;

    JButton move;
    JPanel controls;

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
			g.fillOval(x, y, 30, 30);


		    }
		    else if(BLACK == grid[i][j].color) {
			g.setColor(black);
			g.fillOval(x, y, 30, 30);

		    }

		    
		    if(grid[i][j].isKing) {
			g.setColor(Color.white);
			g.drawString("K", x, y);
		    }
			

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
	boolean seen; // used to select next avail piece

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
			       + " to " + to.getCoords() + " and ate " + ate + " at " + ate.getCoords());

	}



	// Checks if a NW move is possible
	public boolean hasNW1() {

	    if(x + (1 * color) < 8 && x + (1 * color) >= 0 && 
	       y + (1 * color) < 8 && y + (1 * color) >= 0) {
		return true;
	    }

	    return false;
	}

	// Checks if a NW move is possible
	public boolean hasNW2() {

	    // "this" is not necessary, instance method.
	    if(x + (2 * color) < 8 && x + (2 * color) >= 0 && 
	       y + (2 * color) < 8 && y + (2 * color) >= 0) {
		return true;
	    }

	    return false;
	}

	// Checks if a NE move is possible
	public boolean hasNE1() {

	    if(x + (1 * color) < 8 && x + (1 * color) >= 0 &&
	       y + (-1 * color) < 8 && y + (-1 * color) >= 0) {
		return true;
	    }
	    return false;
	}

	// Checks if a NE move is possible
	public boolean hasNE2() {

	    if(x + (2 * color) < 8 && x + (2 * color) >= 0 &&
	       y + (-2 * color) < 8 && y + (-2 * color) >= 0) {
		return true;
	    }
	    return false;
	}

	// Checks if a SW move is possible
	public boolean hasSW1() {
	    return false;
	}

	// Checks if a SW move is possible
	public boolean hasSW2() {
	    return false;
	}


	// Checks if a SE move is possible
	public boolean hasSE1() {
	    return false;
	}

	// Checks if a SE move is possible
	public boolean hasSE2() {
	    return false;
	}

	int num = 12;
	public void move() {

	    Piece p = new Piece(EMPTY, 0, 0);

	    if(RED == color) num = numr;
	    else num = numb;

	    // FIXME: Loop through grid back to x-1, not just to end
	    // Start with naive implementation - search all

	    // Find any same colored piece
	    if(numSeen < num) {
		while((this.color != p.color)) {
		    p = grid[(int)(8.0 * Math.random())][(int)(8.0 * Math.random())];
		} // end while
	    
		if(!grid[p.x][p.y].seen) p.tryAll();
		else this.move();

	    }
	    else gameOver = true;

	    // FIXME: A turn can be more than one move.
       	    // FIXME: How do you know when there are no more moves available?
	} // end move()



	// try all moves for a piece
	public void tryAll() {

	    // FIXME: Pieces move up to the middle line, then stop.
	    // No jumping!
	    
	    Piece p = this;
	    grid[p.x][p.y].seen = true;
	    numSeen++;
	    
	    System.out.println("--- Current = " + p + " at " + p.getCoords());

	    // --- Eliglble for NW move?
        
	    if(p.hasNW1()) {
		System.out.print("WEST : ");
		Piece nw1 = grid[p.x + (1 * color)][p.y + (1 * color)];

		if(EMPTY == nw1.color) {

		    log(p, nw1);

		    // move to nw1
		    grid[nw1.x][nw1.y] = p;
		    grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);


		    if(nw1.madeKing()) grid[nw1.x][nw1.y].isKing = true;
				
		    printBoard();
		    piecesLayer.repaint();
		    return;

		}
		else if(p.hasNW2()) {

		    Piece nw2 = grid[p.x + (2 * color)][p.y + (2 * color)];

		    // nw1 is occupied. Can we jump?
		    if(EMPTY == nw2.color && this.color != nw1.color) {

			log(p, nw2, nw1);

			// move to nw2. Eat nw1.
			grid[nw2.x][nw2.y] = p;	
			grid[nw1.x][nw1.y] = new Piece(EMPTY, nw1.x ,nw1.y);
			grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);
							

			if(nw1.color == RED) numr--;
			else numb--;

			if(0 == numr || 0 ==numb) gameOver = true;


			if(nw2.madeKing()) grid[nw2.x][nw2.y].isKing = true;

			printBoard();
			piecesLayer.repaint();
			return;


		    }

		}

	    } // end NW check


	    // --- Eligible for NE move?
	    else if(p.hasNE1()) {
		System.out.print("EAST : ");

		Piece ne1 = grid[p.x + (1 * color)][p.y + (-1 * color)];
		if(EMPTY == ne1.color) {
				
		    // move to ne1
		    log(p, ne1);
		    grid[ne1.x][ne1.y] = p;
		    grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

		    if(ne1.madeKing()) grid[ne1.x][ne1.y].isKing = true;
				
		    printBoard();
		    piecesLayer.repaint();
		    return;			    
		}
		else if(p.hasNE2()) {

		    Piece ne2 = grid[p.x + (2 * color)][p.y + (-2 * color) ];
		    // ne1 is occupied.  Can we jump?
		    if(EMPTY == ne2.color && this.color != ne1.color) {
			// Move to ne2. Eat ne1.
			log(p, ne2, ne1);
			grid[ne2.x][ne2.y] = p;
			grid[ne1.x][ne1.y] = new Piece(EMPTY, ne1.x , ne1.y);
			grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

			if(RED == ne1.color) numr--;
			else numb--;
			if(0 == numr || 0 ==numb) gameOver = true;

			
			if(ne2.madeKing()) grid[ne2.x][ne2.y].isKing = true;

			printBoard();
			piecesLayer.repaint();
			return;
		    }
		    
		}
			    
	    } // end NE check


	    // --- Eligible to move backwards?
	    else if(p.isKing && p.hasSE1()) {

	    }

	    else if(p.isKing && p.hasSW1()) {

	    }


	    if(numSeen <= num) this.move();
	    else gameOver = true;
	    
	} // end tryAll


	public boolean madeKing() {

	    if(RED == this.color && 7 == this.x) {
		return true;
	    }
	    else if(BLACK == this.color && 0 == this.x) {
		return true;
	    }


	    return false;
	}

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


    int numSeen = 0;
    public void unseeGrid() {

	// Mark all points as un-visited.  Used to choose next piece to move.
	for(int r = 0; r < 8; r++) {
	    for(int c = 0; c < 8; c++) {
		grid[r][c].seen = false;
	    }
	}

	numSeen = 0;
    } // end unseeGrid


    public void initGrid() {
	// Clear out the grid
	for(int r = 0; r < 8; r++) {
	    for(int c = 0; c < 8; c++) {
		grid[r][c] = new Piece(EMPTY, r, c);
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
	controls = new JPanel();
	controls.setBackground(Color.gray);

	move = new JButton("Move");
	move.addActionListener(this);	
	controls.add(move); // add to panel

	turnL = new JLabel("Red starts.");
	controls.add(turnL);

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

	Piece REDP = new Piece(RED);
	Piece BLACKP = new Piece(BLACK);

	if("Move".equals(command)) {
	    tries = 0;
		    
	    if(!gameOver) {

		unseeGrid();

		if(turn == RED) {
		    REDP.move();
		    turnL.setText("Black's turn.");
		}
		else {
		    BLACKP.move();
		    turnL.setText("Red's turn.");
		} 
		turn *= -1;

	    }
	    else {
		controls.remove(move);
		System.out.println("GAME OVER");
		if(RED == turn) winner.setText("RED WON!");
		else winner.setText("BLACK WON!");
		return;
	    }


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


    // For automated play
    public void play() {

	Piece REDP = new Piece(RED);
	Piece BLACKP = new Piece(BLACK);


	while(!gameOver) {
	    REDP.move();
	    turn *= -1;
	    BLACKP.move();
	}
    }

    public void init() {

	initGrid();
	drawBoard();
	initControls();
	//		play();

    }


}  // end class
