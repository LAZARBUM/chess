import java.util.ArrayList;

//Steve Thomas
//KingKnight
//Description: A chess piece that moves like both a King and a Knight. It moves in an L shape and one square in any direction.

public class KingKnight extends Piece {

    // Constructor for KingKnight.
    // Precondition: `img_file` must be a valid file path.
    // Postcondition: A KingKnight piece is created with the given color and image.
    public KingKnight(boolean isWhite, String img_file) {
        super(isWhite, img_file);
    }

    // Returns a list of legal moves for the KingKnight piece.
    // Precondition: `board` is initialized, `start` is a valid square.
    // Postcondition: Returns all legal moves based on movement rules.
    @Override
    public ArrayList<Square> getLegalMoves(Board board, Square start) {
        ArrayList<Square> legalMoves = new ArrayList<>();
        Square[][] squares = board.getSquareArray();
        int row = start.getRow();
        int col = start.getCol();

        int[][] kingMoves = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},         {0, 1},
            {1, -1}, {1, 0}, {1, 1}
        };

        int[][] knightMoves = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        addValidMoves(legalMoves, squares, row, col, kingMoves);
        addValidMoves(legalMoves, squares, row, col, knightMoves);

        return legalMoves;
    }

    // Returns a list of squares controlled by the KingKnight piece.
    // A square is controlled if the piece can legally capture into it.
    // Precondition: `board` is initialized, `start` is a valid square.
    // Postcondition: Returns a list of all squares this piece can attack.
    @Override
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
        ArrayList<Square> controlledSquares = new ArrayList<>();
        int row = start.getRow();
        int col = start.getCol();

        int[][] attackOffsets = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},         {0, 1},
            {1, -1}, {1, 0}, {1, 1},
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] offset : attackOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                controlledSquares.add(board[newRow][newCol]);
            }
        }
        return controlledSquares;
    }

    @Override
    public String toString() {
        return "A " + (getColor() ? "white" : "black") + " KingKnight";
    }

    // Helper method to add valid moves to a list.
    // Precondition: `board` is initialized.
    // Postcondition: Adds valid moves to the list.
    private void addValidMoves(ArrayList<Square> moves, Square[][] board, int row, int col, int[][] moveSet) {
        for (int[] move : moveSet) {
            int newRow = row + move[0];
            int newCol = col + move[1];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Square target = board[newRow][newCol];
                if (!target.isOccupied() || target.getOccupyingPiece().getColor() != this.getColor()) {
                    moves.add(target);
                }
            }
        }
    }
}