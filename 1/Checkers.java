import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*; 

public class Checkers extends JApplet implements ActionListener {


    // TODO: Make private fields and methods
    // TODO: Throw exceptions
    // TODO: Refactor


    // Array representing the board
    public static Piece[][] grid = new Piece[8][8];

    int RED = 1;
    int BLACK = -1;
    int EMPTY = 0;

    // Pale colors for the board
    Color pred = new Color(255, 128, 128);
    Color pblack = new Color(125, 125, 125);

    // Vibrant colors for the pieces
    Color red = Color.red;
    Color black = Color.black;

    int numr = 12;
    int numb = 12; // initial number of pieces

    int turn = 1; // red starts
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


        // Checks if a NW move stays on the board
        public boolean hasNW1() {

            if(x + (1 * color) < 8 && x + (1 * color) >= 0 && 
                    y + (1 * color) < 8 && y + (1 * color) >= 0) {
                return true;
            }

            return false;
        }

        // Checks if a NW move stays on the board
        public boolean hasNW2() {

            if(x + (2 * color) < 8 && x + (2 * color) >= 0 && 
                    y + (2 * color) < 8 && y + (2 * color) >= 0) {
                return true;
            }

            return false;
        }

        // Checks if a NE move stays on the board
        public boolean hasNE1() {

            if(x + (1 * color) < 8 && x + (1 * color) >= 0 &&
                    y + (-1 * color) < 8 && y + (-1 * color) >= 0) {
                return true;
            }
            return false;
        }

        // Checks if a NE move stays on the board
        public boolean hasNE2() {

            if(x + (2 * color) < 8 && x + (2 * color) >= 0 &&
                    y + (-2 * color) < 8 && y + (-2 * color) >= 0) {
                return true;
            }
            return false;
        }

        // Checks if a SW step stays on the board
        public boolean hasSW1() {

            // As if NW1, but in the opposite color/direction
            if(x + (1 * -color) < 8 && x + (1 * -color) >= 0 && 
                    y + (1 * -color) < 8 && y + (1 * -color) >= 0) {
                return true;
            }

            return false;
        }

        // Checks if a SW jump stays on the board
        public boolean hasSW2() {

            if(x + (2 * -color) < 8 && x + (2 * -color) >= 0 && 
                    y + (2 * -color) < 8 && y + (2 * -color) >= 0) {
                return true;
            }

            return false;
        }


        // Checks if a SE move stays on the board
        public boolean hasSE1() {

            if(x + (1 * -color) < 8 && x + (1 * -color) >= 0 &&
                    y + (-1 * -color) < 8 && y + (-1 * -color) >= 0) {
                return true;
            }

            return false;
        }

        // Checks if a SE move stays on the board
        public boolean hasSE2() {

            if(x + (2 * -color) < 8 && x + (2 * -color) >= 0 &&
                    y + (-2 * -color) < 8 && y + (-2 * -color) >= 0) {
                return true;
            }


            return false;
        }


        // Used to alternate turns.  Only valid colors matter. 
        public void move() {

            System.out.println("------------------------------------");

            // Methodically search for a piece to move.  
            // Of course, it always plays the same game. 
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {

                    if(this.color == grid[i][j].color) {

                        boolean hasMove = grid[i][j].tryAll();
                        if(hasMove) return;

                    } // end if

                }
            }

            // As of now, black should be the winner.
            System.out.println(this + " has run out of moves!");
            gameOver = true;
            printBoard();
            return;

            // FIXME: How do you know when there are no more moves available?
        } // end move()



        // try all moves for a piece
        public boolean tryAll() {

            System.out.println("Examining " + this + " at " + this.getCoords());

            boolean canJump = this.tryJumps();
            System.out.println("--- canJump = " + canJump);


            if(canJump) return true;
            else {
                boolean canStep = this.trySteps();
                System.out.println("--- canStep = " + canStep);
                return canStep;
            }

        } // end tryAll

        public boolean tryJumps() {

            Piece p = this;


            // FIXME: Multiple jumps work, however doesn't always detect a 
            // jump is possible.  check if else statements.
            // Ex: would rather step forward then jump backwards.
            // rather step west then jump east when king.

            // -------------------------------------------------------
            // ------------------- Try all jumps --------------------- 
            // -------------------------------------------------------

            if(p.isKing && p.hasSW2()) {

                Piece sw1 = grid[x + (1 * -color)][y + (1 * -color)];
                Piece sw2 = grid[x + (2 * -color)][y + (2 * -color)];

                if(EMPTY == sw2.color && EMPTY != sw1.color && p.color != sw1.color) {
                    System.out.print("SW: ");
                    log(p, sw2, sw1);

                    // Move to sw2.  Eat sw1.
                    grid[sw2.x][sw2.y] = new Piece(p.color, sw2.x, sw2.y);
                    grid[sw1.x][sw1.y] = new Piece(EMPTY, sw1.x, sw1.y);
                    grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);


                    // Decrement num pieces
                    if(sw1.color == RED) numr--;
                    else numb--;
                    if(0 == numr || 0 ==numb) gameOver = true;


                    // Check for king-ship
                    if(grid[sw2.x][sw2.y].madeKing() || p.isKing) {
                        grid[sw2.x][sw2.y].isKing = true;
                    }

                    // Re-draw
                    printBoard();
                    piecesLayer.repaint();

                    // You could jump multiple times:
                    // UNTESTED!
                    System.out.println("Keep jumping?" + grid[sw2.x][sw2.y] 
                        + grid[sw2.x][sw2.y].getCoords());

                    grid[sw2.x][sw2.y].tryJumps();

                    System.out.println("--- Stop Jumping.");
                    

                    return true;

                }


            } // end SW jump

            if(p.isKing && p.hasSE2()) {

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
                    if(0 == numr || 0 ==numb) gameOver = true;


                    // Check for king-ship
                    if(grid[se2.x][se2.y].madeKing()|| p.isKing) {
                        grid[se2.x][se2.y].isKing = true;
                    }

                    // Re-draw
                    printBoard();
                    piecesLayer.repaint();


                    // If it can jump, try jumping again!
                    // UNTESTED!
                    System.out.println("Keep jumping?" + grid[se2.x][se2.y] 
                        + grid[se2.x][se2.y].getCoords());

                    grid[se2.x][se2.y].tryJumps();

                    System.out.println("--- Stop Jumping.");
                    

                    return true;

                }

            } // end SE jump


            if(p.hasNW2()) {

                Piece nw1 = grid[p.x + (1 * color)][p.y + (1 * color)];
                Piece nw2 = grid[p.x + (2 * color)][p.y + (2 * color)];

                if(EMPTY == nw2.color && EMPTY != nw1.color 
                        && p.color != nw1.color) {

                    System.out.print("NW: ");
                    log(p, nw2, nw1);

                    // Move to nw2. Eat nw1.
                    grid[nw2.x][nw2.y] = new Piece(p.color, nw2.x, nw2.y);  
                    grid[nw1.x][nw1.y] = new Piece(EMPTY, nw1.x ,nw1.y);
                    grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);


                    if(nw1.color == RED) numr--;
                    else numb--;
                    if(0 == numr || 0 ==numb) gameOver = true;


                    if(grid[nw2.x][nw2.y].madeKing() || p.isKing) {
                        grid[nw2.x][nw2.y].isKing = true;
                    }

                    printBoard();
                    piecesLayer.repaint();


                    // You could jump multiple times:
                    // UNTESTED!
                    System.out.println("Keep jumping?" + grid[nw2.x][nw2.y] 
                        + grid[nw2.x][nw2.y].getCoords());
                    grid[nw2.x][nw2.y].tryJumps();
                    System.out.println("--- Stop Jumping.");



                    return true;
                }

            } // end if NW jump

            if(p.hasNE2()) {

                Piece ne1 = grid[p.x + (1 * color)][p.y + (-1 * color)];
                Piece ne2 = grid[p.x + (2 * color)][p.y + (-2 * color)];

                if(EMPTY == ne2.color && EMPTY != ne1.color && p.color != ne1.color) {
                    System.out.print("NE: ");
                    log(p, ne2, ne1);

                    // Move to ne2.  Eat ne1.
                    grid[ne2.x][ne2.y] = new Piece(p.color, ne2.x, ne2.y);
                    grid[ne1.x][ne1.y] = new Piece(EMPTY, ne1.x , ne1.y);
                    grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

                    if(RED == ne1.color) numr--;
                    else numb--;
                    if(0 == numr || 0 ==numb) gameOver = true;


                    if(grid[ne2.x][ne2.y].madeKing() || p.isKing) {
                        grid[ne2.x][ne2.y].isKing = true;
                    }

                    printBoard();
                    piecesLayer.repaint();

                    // You could jump multiple times:
                    // UNTESTED!
                    System.out.println("Keep jumping?" + grid[ne2.x][ne2.y] 
                        + grid[ne2.x][ne2.y].getCoords());
                    grid[ne2.x][ne2.y].tryJumps();
                    System.out.println("--- Stop Jumping.");

                    return true;

                }

            } // end NE jump




            // No jumps possible.
            return false;

        } // end tryJumps


        public boolean trySteps() {


            // FIXME: If a king the the "earliest" piece, it gets caught in a 
            // loop bc it can only move one step either way.

            Piece p = this;

            // -------------------------------------------------------
            // ------------------- Try all steps --------------------- 
            // -------------------------------------------------------



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

                    printBoard();
                    piecesLayer.repaint();
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

                    printBoard();
                    piecesLayer.repaint();
                    return true;			

                }


            } // end SW step


            if(p.hasNW1()) {

                // NW step
                Piece nw1 = grid[p.x + (1 * color)][p.y + (1 * color)];

                 System.out.println("--- Found nw1: " + nw1 + " at " + nw1.getCoords());

                if(EMPTY == nw1.color) {

                    System.out.print("NW: ");
                    log(p, nw1);

                    grid[nw1.x][nw1.y] = new Piece(p.color, nw1.x, nw1.y);
                    grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

                    if(grid[nw1.x][nw1.y].madeKing() || p.isKing) {
                        grid[nw1.x][nw1.y].isKing = true;
                    }

                    printBoard();
                    piecesLayer.repaint();
                    return true;

                }

            } // end NW step

            if(p.hasNE1()) {

                Piece ne1 = grid[p.x + (1 * color)][p.y + (-1 * color)];

            // NE STEP IS NOT WORKING
            // r at (2,4) should be returning true for a ne step!
                System.out.println("--- Found ne1: " + ne1 + " at " + ne1.getCoords());

                if(EMPTY == ne1.color) {

                    System.out.print("NE: ");
                    log(p, ne1);

                    grid[ne1.x][ne1.y] = new Piece(p.color, ne1.x, ne1.y);
                    grid[p.x][p.y] = new Piece(EMPTY, p.x, p.y);

                    if(grid[ne1.x][ne1.y].madeKing() || p.isKing) {
                        grid[ne1.x][ne1.y].isKing = true;
                    }

                    printBoard();
                    piecesLayer.repaint();
                    return true;			

                }

            } // end NE step


            // No steps possible.
            return false;

        } // end trySteps


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

        Piece REDP = new Piece(RED);
        Piece BLACKP = new Piece(BLACK);

        if("Move".equals(command)) {

            if(!gameOver) {

                //           unseeGrid();

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
