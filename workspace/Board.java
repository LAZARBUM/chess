import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;

// You will be implementing a part of a function and a whole function in this document. 
// Please follow the directions for the suggested order of completion that should make testing easier.

@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {
    // Resource location constants for piece images
    private static final String RESOURCES_WPAWN_PNG = "wpawn.png";
    private static final String RESOURCES_BPAWN_PNG = "bpawn.png";
    private static final String RESOURCES_WROOK_PNG = "wrook.png";
    private static final String RESOURCES_BROOK_PNG = "brook.png";
    private static final String RESOURCES_WBISHOP_PNG = "wbishop.png";
    private static final String RESOURCES_BBISHOP_PNG = "bbishop.png";
    private static final String RESOURCES_WQUEEN_PNG = "wqueen.png";
    private static final String RESOURCES_BQUEEN_PNG = "bqueen.png";
    private static final String RESOURCES_WKING_PNG = "wking.png";
    private static final String RESOURCES_BKING_PNG = "bking.png";
    private static final String RESOURCES_WKNIGHT_PNG = "wknightKing.png";
    private static final String RESOURCES_BKNIGHT_PNG = "bknightKing.png";

    // Logical and graphical representations of board
    private final Square[][] board;
    private final GameWindow g;

    // Contains true if it's white's turn.
    private boolean whiteTurn;

    // If the player is currently dragging a piece, this variable contains it.
    private Piece currPiece;
    private Square fromMoveSquare;

    // Used to keep track of the x/y coordinates of the mouse.
    private int currX;
    private int currY;

    public Board(GameWindow g) {
        this.g = g;
        board = new Square[8][8];
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Populating the board with squares. The board consists of 64 squares alternating from white to black.
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boolean isWhite = (row + col) % 2 == 0; // Alternating white/black pattern
                board[row][col] = new Square(this, isWhite, row, col);
                this.add(board[row][col]); // Add the square to the board
            }
        }

        initializePieces();

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;
    }

    // Sets up the board with all pieces in their standard chess positions.
    private void initializePieces() {
        // Place pawns
        for (int col = 0; col < 8; col++) {
            board[1][col].put(new Pawn(false, RESOURCES_BPAWN_PNG));
            board[6][col].put(new Pawn(true, RESOURCES_WPAWN_PNG));
        }

        // Place rooks
        board[0][0].put(new Rook(false, RESOURCES_BROOK_PNG));
        board[0][7].put(new Rook(false, RESOURCES_BROOK_PNG));
        board[7][0].put(new Rook(true, RESOURCES_WROOK_PNG));
        board[7][7].put(new Rook(true, RESOURCES_WROOK_PNG));

        // Place bishops
        board[0][2].put(new Bishop(false, RESOURCES_BBISHOP_PNG));
        board[0][5].put(new Bishop(false, RESOURCES_BBISHOP_PNG));
        board[7][2].put(new Bishop(true, RESOURCES_WBISHOP_PNG));
        board[7][5].put(new Bishop(true, RESOURCES_WBISHOP_PNG));

        // Place queens
        board[0][3].put(new Queen(false, RESOURCES_BQUEEN_PNG));
        board[7][3].put(new Queen(true, RESOURCES_WQUEEN_PNG));

        // Place kings
        board[0][4].put(new King(false, RESOURCES_BKING_PNG));
        board[7][4].put(new King(true, RESOURCES_WKING_PNG));

        // Place KingKnights (replacing the standard knights)
        board[0][1].put(new KingKnight(false, RESOURCES_BKNIGHT_PNG));
        board[0][6].put(new KingKnight(false, RESOURCES_BKNIGHT_PNG));
        board[7][1].put(new KingKnight(true, RESOURCES_WKNIGHT_PNG));
        board[7][6].put(new KingKnight(true, RESOURCES_WKNIGHT_PNG));
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[x][y];
                if (sq == fromMoveSquare)
                    sq.setBorder(BorderFactory.createLineBorder(Color.blue));
                sq.paintComponent(g);
            }
        }
        if (currPiece != null) {
            if ((currPiece.getColor() && whiteTurn) || (!currPiece.getColor() && !whiteTurn)) {
                final Image img = currPiece.getImage();
                g.drawImage(img, currX, currY, null);
            }
        }
    }

    public boolean isInCheck(boolean kingColor) {
        // Traverse the board to find the king of the specified color
        Square kingSquare = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = board[i][j]; // Directly access the 2D array
                Piece piece = square.getOccupyingPiece();
    
                // Look for the king
                if (piece instanceof King && piece.getColor() == kingColor) {
                    kingSquare = square;
                    break;
                }
            }
            if (kingSquare != null) {
                break; // Exit the outer loop once w found the king
            }
        }
    
        if (kingSquare == null) {
            return false;
        }
    
        // Traverse the board looking for opponent's pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = board[i][j]; // Directly access the 2D array
                Piece piece = square.getOccupyingPiece();
    
                // Only consider the opponent's pieces
                if (piece != null && piece.getColor() != kingColor) {
                    // Get the squares controlled by this piece
                    ArrayList<Square> controlledSquares = piece.getControlledSquares(board, square);
    
                    // If the king's square is in the controlled squares, the king is in check
                    if (controlledSquares.contains(kingSquare)) {
                        return true;
                    }
                }
            }
        }
    
        // If no opponent's piece can attack the king, return false
        return false;
    }




    @Override
    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();

        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));
        // Precondition: sq must be a valid square on the board
        // Postcondition: currPiece is set if a piece is on that square

        if (sq.isOccupied()) {
            currPiece = sq.getOccupyingPiece();
            fromMoveSquare = sq;
            if (!currPiece.getColor() && whiteTurn)
                return;
            if (currPiece.getColor() && !whiteTurn)
                return;
            sq.setDisplay(false);
        }
        repaint();
    }

    // should move the piece to the desired location only if this is a legal move.
    // uses the piece's "legal move" function to determine if the move is valid.
    @Override
    public void mouseReleased(MouseEvent e) {
        if (currPiece == null || fromMoveSquare == null) {
            return;
        }

        Square endSquare = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if ((currPiece.getColor() && !whiteTurn) || (!currPiece.getColor() && whiteTurn)) {
            fromMoveSquare.setDisplay(true);
            currPiece = null;
            repaint();
            return;
        }

        // get all the legal moves for the current piece
        ArrayList<Square> legalMoves = currPiece.getLegalMoves(this, fromMoveSquare);

        if (legalMoves.contains(endSquare)) {
            // save the piece currently on the end squar
            Piece capturedPiece = endSquare.getOccupyingPiece();
    
            // temporarily make the move on the board
            endSquare.put(currPiece);
            fromMoveSquare.removePiece();
    
            // check if the move would pt the king in check
            if (isInCheck(currPiece.getColor())) {
                // rndo the move put piece back where it came from
                fromMoveSquare.put(currPiece);
                endSquare.removePiece();
                // eestore the carptured piece if there was one
                if (capturedPiece != null) {
                    endSquare.put(capturedPiece);
                }
            } else {
                whiteTurn = !whiteTurn;
            }
        } else {
            fromMoveSquare.put(currPiece);
        }

        fromMoveSquare.setDisplay(true);
        currPiece = null;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currX = e.getX() - 24;
        currY = e.getY() - 24;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}