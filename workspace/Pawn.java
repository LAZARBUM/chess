import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

//NAME: Nailah George
//PIECE NAME: widow (pawn)
//DESCRIPTION: A widow that moves and attacks every 2 squares in front and behind it if the space is available
//(if it's not occupied by its own color)

public class Pawn extends Piece {

    public Pawn(boolean isWhite, String img_file) {
        super(isWhite, img_file);

        try {
            if (this.img == null) {
                this.img = ImageIO.read(getClass().getResource(img_file));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    // pre-condition: a starting square and a piece
    // post-condition: highlighted squares
    @Override
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
        ArrayList<Square> controlledSquares = new ArrayList<>();
        int row = start.getRow();
        int col = start.getCol();

        // widow attacks 2 rows ahead and behind (if not occupied by own color)
        int[] attackOffsets = {-2, 2};  // Attack 2 squares ahead and behind

        for (int offset : attackOffsets) {
            int newRow = row + offset;

            // Check if the target square is within bounds
            if (newRow >= 0 && newRow < 8) {
                Square target = board[newRow][col];

                if (target.isOccupied()) {
                    // If the piece is an opponent's piece, it's controlled
                    if (target.getOccupyingPiece().getColor() != this.getColor()) {
                        controlledSquares.add(target); // Enemy piece
                    }
                } else {
                    // If the square is empty, it's still controlled
                    controlledSquares.add(target); 
                }
            }
        }

        return controlledSquares;
    }

    // pre-condition: a row and a column
    // post-condition: highlighted squares of only where the piece can attack when available
    @Override
    public ArrayList<Square> getLegalMoves(Board b, Square start) {
        ArrayList<Square> moves = new ArrayList<>();
        int row = start.getRow();
        int col = start.getCol();

        // widow moves 2 squares forward or 2 squares backward if not blocked by own color
        int[] moveOffsets = {-2, 2};  // Moves 2 squares forward or backward

        for (int offset : moveOffsets) {
            int newRow = row + offset;

            // Check if the move is within bounds
            if (newRow >= 0 && newRow < 8) {
                Square target = b.getSquareArray()[newRow][col];

                // Check if the target is unoccupied or contains an opponent's piece
                if (!target.isOccupied() || target.getOccupyingPiece().getColor() != this.getColor()) {
                    moves.add(target); // Move or capture
                }
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return this.getColor() ? "white widow" : "black widow";
    }
}