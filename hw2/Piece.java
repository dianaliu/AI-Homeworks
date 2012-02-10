import java.util.ArrayList;


/**
 * HW2: A game of Checkers with some strategy.
 * Due Monday February 13, 2012.
 *
 * @author Diana Liu
 */

public class Piece extends Object {

    // TODO: Randomize first moves.

    public static final int RED = 1;
    public static final int BLACK = -1;
    public static final int EMPTY = 0;

    // initial number of pieces
    static int numr = 12;
    static int numb = 12; 

    // For game tree, a list of all pieces with their associated moves.
    private static ArrayList allMoves = new ArrayList();

    // Array representing the board
    public static Piece[][] grid = new Piece[8][8];

    // Data fields of a Piece
    private int color;
    private boolean isKing;
    private int x;
    private int y;
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
        moves = new ArrayList();
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

    public String info() {
        return this.toString() + this.getCoords();

    }


    // ---------------------------------------------


    public void logMoves() {

        if(!this.moves.isEmpty()) {

            System.out.println(this.info() + " has move:");

            for(int i = 0; i < moves.size(); i++) {

                Piece [] tmp = (Piece []) moves.get(i);

                for(int j = 0; j < tmp.length; j++) {

                    System.out.print(tmp[j].info() + " ");
                }

                System.out.println();

            }

        }
        else {
            System.out.println(this.info() + " has no moves.");
        }

    } // end logMoves()


    public void log(Piece [] m) {

        System.out.print("Move from " + m[0].info());
        System.out.print(" to " + m[1].info());
        if(m.length > 2) {
            System.out.print(" and ate " + m[2].info());
        }
        System.out.println();

    }


    // ---------------------------------------------


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

        // NOTE: Don't use "this" in move() because it's called by
        // nonsense RED and BLACK pieces.  Only reference color. 

        if(Checkers.gameOver == true) return;

        System.out.println("------------------------------------");

        // Find the first piece that can jump, or step.
        Piece current = this.findMoves();

        if(null != current) {

            makeMove((Piece [])current.moves.get(0));

        }
        else {

            System.out.println(this + " has run out of moves!");
            Checkers.gameOver = true;

        }

        Checkers.printBoard();

    } // end move()


    // Called by a dummy RED/BLACK piece used only for it's color.
    // Returns the piece you ought to move.
    public Piece findMoves() {

        int x = 0;
        int y = 0;

        do {

            do {

                if(this.color == grid[x][y].color) {

                    // Try finding jumps first
                    grid[x][y].addJumps();  
 //                   grid[x][y].logMoves();                 
                    if(!grid[x][y].moves.isEmpty()) { return grid[x][y]; }

                }

                y = (y + 1) % 8;

            } while (y != 0);

            x = (x + 1) % 8;

        } while (x != 0);



        // Is the only way to prioritize jumps, with two loops?
        // TODO: Can combine into one loop if addJumps returns true...
        x = 0;
        y = 0;

        do {

            do {

                if(this.color == grid[x][y].color) {

                    // Then, try finding steps
                    grid[x][y].addSteps();
 //                   grid[x][y].logMoves(); 
                    if(!grid[x][y].moves.isEmpty()) { return grid[x][y]; }

                }

                y = (y + 1) % 8;

            } while (y != 0);

            x = (x + 1) % 8;

        } while (x != 0);


        // No moves found :(
        return null;

    } // end findMoves()


    // Called with dummy RED and BLACK pieces. Don't use "this"
    // Returns the new location of the Piece.
    public void makeMove(Piece [] m) {

        Piece from = m[0];
        Piece to = m[1];
        Piece eaten = this;
        if(m.length > 2) { eaten = m[2]; }


        // Update grid with the move.
        grid[from.x][from.y] = new Piece(EMPTY, from.x, from.y);
        grid[to.x][to.y] = new Piece(from.color, to.x, to.y);
        if(m.length > 2) {

            grid[eaten.x][eaten.y] = new Piece(EMPTY, eaten.x, eaten.y);

            // Update counts
            if(eaten.color == RED) numr--;
            else numb--;
            if(0 == numr || 0 ==numb) Checkers.gameOver = true;

        }


        // Re-draw.
        Checkers.pieces.repaint();
        log(m);


        // Preserve kingship
        if(grid[from.x][from.y].isKing) {
            grid[to.x][to.y].isKing = true;
        }

        // If a piece just made king, turn is over.
        if(grid[to.x][to.y].madeKing()) {

            grid[to.x][to.y].isKing = true;      
            return; 

        }

        // If you just jumped, should you jump again?
        if(m.length > 2) {
            grid[to.x][to.y].addJumps();
            if(!grid[to.x][to.y].moves.isEmpty()) {
                makeMove((Piece [])grid[to.x][to.y].moves.get(0));
            }

        }

    } // end makeMove()



    public void addJumps() {

        Piece p = this;
        p.moves.clear();

        if(p.isKing && p.hasSW2()) {

            Piece sw1 = grid[x + (1 * -color)][y + (1 * -color)];
            Piece sw2 = grid[x + (2 * -color)][y + (2 * -color)];

            if(EMPTY == sw2.color 
                    && EMPTY != sw1.color && p.color != sw1.color) {

                // Add move states from, to, eaten
                p.moves.add(new Piece[] {p, sw2, sw1});

            }

        } // end SW jump

        if(p.isKing && p.hasSE2()) {

            Piece se1 = grid[x + (1 * -color)][ y + (-1 * -color)];
            Piece se2 = grid[x + (2 * -color)][y + (-2 * -color)];

            if(EMPTY == se2.color 
                    && EMPTY != se1.color && p.color != se1.color) {

                // Add move states from, to, eaten
                p.moves.add(new Piece[] {p, se2, se1});

            }

        } // end SE jump


        if(p.hasNW2()) {

            Piece nw1 = grid[p.x + (1 * color)][p.y + (1 * color)];
            Piece nw2 = grid[p.x + (2 * color)][p.y + (2 * color)];

            if(EMPTY == nw2.color 
                    && EMPTY != nw1.color && p.color != nw1.color) {

                // Add move states from, to, eaten
                this.moves.add(new Piece[] {p, nw2, nw1});

            }

        } // end if NW jump


        if(p.hasNE2()) {

            Piece ne1 = grid[p.x + (1 * color)][p.y + (-1 * color)];
            Piece ne2 = grid[p.x + (2 * color)][p.y + (-2 * color)];

            if(EMPTY == ne2.color 
                    && EMPTY != ne1.color && p.color != ne1.color) {

                // Add move states from, to, eaten
                p.moves.add(new Piece[] {p, ne2, ne1});			
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

            }
        } // end SE step


        if(p.isKing && p.hasSW1()) {

            Piece sw1 = grid[x + (1 * -color)][y + (1 * -color)];

            if(EMPTY == sw1.color) {

                // Add move states from, to
                p.moves.add(new Piece[] {p, sw1});

            }

        } // end SW step


        if(p.hasNW1()) {

            // NW step
            Piece nw1 = grid[p.x + (1 * color)][p.y + (1 * color)];

            if(EMPTY == nw1.color) {

                // Add move states from, to
                p.moves.add(new Piece[] {p, nw1});

            }

        } // end NW step


        if(p.hasNE1()) {

            Piece ne1 = grid[p.x + (1 * color)][p.y + (-1 * color)];

            if(EMPTY == ne1.color) {

                // Add move states from, to
                p.moves.add(new Piece[] {p, ne1});
            }

        } // end NE step


    } // end addSteps()


} // end nested class Pieces



