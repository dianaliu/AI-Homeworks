import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*; 


public class Checkers extends JApplet implements ActionListener {


    // TODO: Throw exceptions
    // TODO: Makefile.


    final Piece REDP = new Piece(Piece.RED);
    final Piece BLACKP = new Piece(Piece.BLACK);


    private int turn = 1; // red starts
    static boolean gameOver = false;

    // -----------------------------------

    // Pale colors for the board
    Color pred = new Color(255, 128, 128);
    Color pblack = new Color(125, 125, 125);

    // Vibrant colors for the pieces
    Color red = Color.red;
    Color black = Color.black;

    // -----------------------------------

    JLabel turnL; 
    JLabel winner;
    
    JButton move;
    JPanel controls;

    BoardLayer board; 	 		// the board
    static PiecesLayer pieces;		// the pieces
    JLayeredPane wrapper;	 	// wrapper for board and pieces


    class BoardLayer extends JPanel {

        BoardLayer() {

            setPreferredSize(new Dimension(504, 504));  
        }

        public void paint(Graphics g){

        // Draw a 8x8 checkerboard of 504 pixels.
        // Each square is 63 pixels.
    
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

            } // end for loop

        } // end paintComponent

    } // end nested class BoardLayer


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    // --------------------------------------------------------------


    class PiecesLayer extends JPanel {

        PiecesLayer() {

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

                    if(Piece.RED == Piece.grid[i][j].getColor()) {
                        g.setColor(red);
                        g.fillOval(x, y, 30, 30);


                    }
                    else if(Piece.BLACK == Piece.grid[i][j].getColor()) {
                        g.setColor(black);
                        g.fillOval(x, y, 30, 30);

                    }


                    if(Piece.grid[i][j].getKing()) {
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


       // --------------------------------------------------------------
    // --------------------------------------------------------------
    // --------------------------------------------------------------


    public void drawBoard() {

        // Use a Layered Pane to hold the board
        wrapper = new JLayeredPane();
        wrapper.setPreferredSize(new Dimension(504, 504)); 
        // Using a LayoutManager, like BorderLayout, messes with layering. 
        // Must use absolute positions instead.
        //		wrapper.setLayout(new BorderLayout()); 
        getContentPane().add(wrapper, BorderLayout.CENTER);

        // Draw the blank board 
        board = new BoardLayer();
        board.setBounds(0,0,504,504);
        wrapper.add(board, new Integer(0), 0);

        //		wrapper.add(board, BorderLayout.CENTER, JLayeredPane.DEFAULT_LAYER);


        // Draw the pieces
        pieces = new PiecesLayer();
        pieces.setBounds(0,0,504,504);
        wrapper.add(pieces, new Integer(1), 0);

        //		wrapper.add(pieces, BorderLayout.CENTER, new Integer(20));
       
        System.out.println("--- Initial Board:");
        printBoard();

    } // end drawBoard()


    int numSeen = 0;
    // Not used
    public void unseeGrid() {

        // Mark all points as un-visited.  Used to choose next piece to move.
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                Piece.grid[r][c].seen = false;
            }
        }

        numSeen = 0;
    } // end unseeGrid()


    public void initGrid() {

        // Clear out the Piece.grid
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                Piece.grid[r][c] = new Piece(Piece.EMPTY, r, c);
            }
        }

        // Red pieces on top three rows
        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 8; c++) {
                if ( (r % 2) == (c % 2) )
                    Piece.grid[r][c] = new Piece(Piece.RED, r, c);
            }
        }

        // Black pieces on bottom three rows
        for(int r = 5; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                if ( (r % 2) == (c % 2) )
                    Piece.grid[r][c] = new Piece(Piece.BLACK, r, c);

            }
        }


    } // end initGrid()


    public void initControls() {

        controls = new JPanel();
        controls.setBackground(Color.gray);

        move = new JButton("Move");
        move.addActionListener(this);	
        controls.add(move); 

        turnL = new JLabel("Red starts.");
        controls.add(turnL);

        winner = new JLabel("No winner yet.");	
        controls.add(winner);	

        JButton reset = new JButton("Reset");
        reset.addActionListener(this);
        controls.add(reset);

        getContentPane().add(controls, BorderLayout.SOUTH);

    } // end initControls()


    // Used to monitor game via console
    public static void printBoard() {

        for(int i = 0; i<Piece.grid.length; i++) {
            for(int j = 0; j < 8; j++) {
                System.out.print(Piece.grid[i][j] + "-");
            }

            System.out.println();
        }

    } // end printBoard()


    // Event listeners for buttons
    public void actionPerformed(ActionEvent evt) { 
       
       String command = evt.getActionCommand();

        if("Move".equals(command)) {

            if(!gameOver) {

                if(turn == Piece.RED) {
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
		controls.repaint();

                System.out.println("GAME OVER");
                if(Piece.RED == turn) winner.setText("RED WON!");
                else winner.setText("BLACK WON!");
                return;
            }


        }
        else if("Reset".equals(command)) {

            initGrid();
            pieces.repaint();
            System.out.println("--- Reset Grid:");
            printBoard();
        }

    }


    // --------------------------------------------------------------
   

    // Not used.
    // For automated play
    public void play() {

        // TODO: Make this actually work

        while(!gameOver) {
            REDP.move();
            turn *= -1;
            BLACKP.move();
        }
    } // end play()

    public void init() {

        initGrid();
        drawBoard();
        initControls();
        //		play();

    } // end init()


}  // end class
