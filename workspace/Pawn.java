import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

//NAME: Nailah George
//PIECE NAME: widow (pawn)
//DESCRIPTION:  a widow that moves and attacks every 2 squares in front and behind it if the space is available
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

        int direction = this.getColor() ? -1 : 1;  // White moves up (-1), Black moves down (+1)

        // Attacking diagonally one square ahead in both directions
        int[][] attackOffsets = {
            {direction, -1}, {direction, 1}
        };

        for (int[] offset : attackOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];

            // Ensure within board boundaries
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                controlledSquares.add(board[newRow][newCol]);
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

        int direction = this.getColor() ? -1 : 1;  // White moves up (-1), Black moves down (+1)

        // Regular move one square forward
        int[][] moveOffsets = {
            {direction, 0}  // Move one square forward
        };

        // Check for standard one-square moves
        for (int[] offset : moveOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Square targetSquare = b.getSquareArray()[newRow][newCol];

                if (!targetSquare.isOccupied()) {
                    moves.add(targetSquare);
                }
            }
        }

        // Check for the initial two-square move
        if (row == (this.getColor() ? 6 : 1)) {  // Starting row for white is 6, black is 1
            int[] doubleMoveOffset = {direction * 2, 0};
            int newRow = row + doubleMoveOffset[0];
            int newCol = col + doubleMoveOffset[1];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Square targetSquare = b.getSquareArray()[newRow][newCol];

                if (!targetSquare.isOccupied()) {
                    moves.add(targetSquare);
                }
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return this.getColor() ? "white pawn" : "black pawn";
    }
}