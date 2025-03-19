
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

//you will need to implement two functions in this file.
public class Piece {
    private final boolean color;
    private BufferedImage img;
    
    public Piece(boolean isWhite, String img_file) {
        this.color = isWhite;
        
        try {
            if (this.img == null) {
              this.img = ImageIO.read(getClass().getResource(img_file));
            }
          } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
          }
    }



    
    public boolean getColor() {
        return color;
    }

    public Image getImage() {
        return img;
    }

    public void draw(Graphics g, Square currentSquare) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();
        g.drawImage(this.img, x, y, null);
    }


    // returns a list of squares controlled by this piece.
    // a square is controlled if the piece can legally capture into it.
    // this piece moves like a knight (L-shape) and like a king (one square any direction).
    // precondition: board[][] must be initialized, start must be a valid square
    // postcondition: returns a list of all squares this piece can attack
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
      ArrayList<Square> controlled = new ArrayList<>();
      int row = start.getRow();
      int col = start.getCol();
  
      // Possible moves like a king (1 step in any direction)
      int[][] kingMoves = { {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1} };
  
      // Possible moves like a knight (L-shape)
      int[][] knightMoves = { {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, 
                               {1, -2}, {1, 2}, {2, -1}, {2, 1} };
  
      // Check all king moves
      for (int[] move : kingMoves) {
          int newRow = row + move[0];
          int newCol = col + move[1];
          if (isValidMove(newRow, newCol)) {
              controlled.add(board[newRow][newCol]);
          }
      }
  
      // Check all knight moves
      for (int[] move : knightMoves) {
          int newRow = row + move[0];
          int newCol = col + move[1];
          if (isValidMove(newRow, newCol)) {
              controlled.add(board[newRow][newCol]);
          }
      }
  
      return controlled;
  }
  
  // Helper function to check if move is on the board
  private boolean isValidMove(int row, int col) {
      return row >= 0 && row < 8 && col >= 0 && col < 8;
  }

  
// returns a list of legal moves for this piece.
// this piece moves like a knight and a king combined.
// It can move in an L-shape like a knight (2 squares in one direction and 1 in another).
// It can also move one square in any direction like a king.
// It can capture an opponent's piece if it lands on an occupied square.
// precondition: board[][] must be initialized, start must be a valid square
// postcondition: returns all legal moves based on movement rules
    public ArrayList<Square> getLegalMoves(Board b, Square start) {
        ArrayList<Square> legalMoves = new ArrayList<>();
        Square[][] board = b.getSquareArray();
        int row = start.getRow();
        int col = start.getCol();

        // Knight move patterns (L-shape)
        int[][] knightMoves = {
            {-2, -1}, {-2, 1}, {2, -1}, {2, 1},
            {-1, -2}, {-1, 2}, {1, -2}, {1, 2}
        };

        // King move patterns (one square in any direction)
        int[][] kingMoves = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},         {0, 1},
            {1, -1}, {1, 0}, {1, 1}
        };

        // Check all knight moves
        for (int[] move : knightMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            addMoveIfValid(board, legalMoves, newRow, newCol);
        }

        // Check all king moves
        for (int[] move : kingMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            addMoveIfValid(board, legalMoves, newRow, newCol);
        }

        return legalMoves;
    }

    /**
     * Helper function to check if a move is valid and add it to the list.
     */
    private void addMoveIfValid(Square[][] board, ArrayList<Square> legalMoves, int newRow, int newCol) {
        if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) { // Check board bounds
            Square targetSquare = board[newRow][newCol];
            if (!targetSquare.isOccupied() || targetSquare.getOccupyingPiece().getColor() != this.color) {
                legalMoves.add(targetSquare);
            }
        }
    }
}