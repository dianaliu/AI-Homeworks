import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*; 

/**
 * HW1: A legal game of Checkers.
 * Due Tuesday January 31, 2012.
 *
 * @author Diana Liu
*/

public class Checkers extends JApplet implements ActionListener {


    // TODO: Throw exceptions
    // TODO: Makefile or learn ant.
    // TODO: Take user inputted moves if available.
    // TODO: Allow custom initialization of grid. 


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

    JLabel notify; 
    JLabel winner;
    
    JButton move;
    JPanel controls;

    JTextField fromx, fromy, tox, toy;

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


    public void modifyGrid() {
        // Do nothing.

    } // end modifyGrid()


    // Allows initialization of grid to specific states and to over ride computer moves.
    // Shouldn't be called directly since much validation is needed beforehand.
    // @param color Piece.RED, Piece.BLACK, or Piece.EMPTY
    // @param x,y The coordinates you are setting
    public void modifyGrid(int fromx, int fromy, int tox, int toy) {
        
        // TODO: Check for out of bounds and illegal moves
        // TODO: Error handling and notify.setText("ERR: Invalid " + color + "move.");
        // NOTE: Should users be allowed to override both black and red? Yes.


        // Set the new space
        int color = Piece.grid[fromx][fromy].getColor();
        Piece.grid[tox][toy] = new Piece(color, tox, toy);

        // Clear the old space
        Piece.grid[fromx][fromy] = new Piece(Piece.EMPTY, fromx, fromy);

    } // end modifyGrid()


    // Check if a TextField is valid : right length, a number between 0 - 7..
    public boolean isValidInput() {

        // TODO: Restrict data entered http://docs.oracle.com/javase/1.4.2/docs/api/java/text/NumberFormat.html
        // FIXME: Use built-in formatting and validation

        String x1 = fromx.getText();
        String y1 = fromy.getText();
        String x2 = tox.getText();
        String y2 = toy.getText();

        if(x1.length() == 1 && y1.length() == 1 && x2.length() == 1 && y2.length() == 1) {

            // Convert to numbers
            // FIXME: Catch errors!!! UGH.
            int ix1 = Integer.parseInt(x1);
            int iy1 = Integer.parseInt(y1);
            int ix2 = Integer.parseInt(x2);
            int iy2 = Integer.parseInt(y2);

            if(inRange(ix1, iy1) && inRange(ix2, iy2)) {
                System.out.println("On the grid...");
                return true;
            }

        } 
        
        notify.setText("ERR: Invalid coordinates.");
        System.out.println("ERR: Invalid coordinates."); 
             
        return false;

    }

    // Checks if a user entered move is legal.
    public boolean isMove() {

        return true;
    }


    // Tells if a coordinate is on the grid
    public boolean inRange(int x, int y) {

       if(x < 8 && x >= 0 && y < 8 && y >= 0) return true;

       return false;

    }


    public void initControls() {

        controls = new JPanel();
        controls.setBackground(Color.gray);

        JLabel from = new JLabel("Move from ");
        controls.add(from);

        fromx = new JTextField(1);
        fromy = new JTextField(1);
        controls.add(fromx);
        controls.add(fromy);

        JLabel to = new JLabel("to");
        controls.add(to);

        tox = new JTextField(1);
        toy = new JTextField(1);
        controls.add(tox);
        controls.add(toy);

        move = new JButton("Move");
        move.addActionListener(this);	
        controls.add(move); 

        notify = new JLabel("Red starts.");
        controls.add(notify);

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


       // TODO: Selectively display TextFields
       // TODO: If all TextFields have valid data, Move calls modifyGrid 

        if("Move".equals(command)) {

            if(!gameOver) {

                if(isValidInput()) {
                    // If the textFields have valid input, make that move instead.
                    // FIXME: Make more robust.  Current implementation trusts the user and is just
                    // enough for the demo on Monday.
                    
                    modifyGrid();

                }
                else if(!isValidInput()) {
                    notify.setText("ERR: Invalid coords.");
                }
                else if(turn == Piece.RED) {
                    REDP.move();
                    notify.setText("Black's turn.");
                }
                else {
                    BLACKP.move();
                    notify.setText("Red's turn.");
                } 

                turn *= -1;
            }
            else {

                controls.remove(move);
		        controls.repaint();

                System.out.println("GAME OVER");
                if(Piece.RED == turn) notify.setText("RED WON!");
                else notify.setText("BLACK WON!");
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

    public void init() {

        initGrid();
        // Pass two sets of coordinates if you wish to change the initial state.
        modifyGrid(0,0,4,2);
        drawBoard();
        initControls();

    } // end init()


}  // end class
