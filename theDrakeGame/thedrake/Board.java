package thedrake;


import java.io.PrintWriter;
import java.util.Arrays;

public class Board implements JSONSerializable {
    private final BoardTile[][] boardTiles;
    private final int dimension;

    // Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
    public Board(int dimension) {
        boardTiles = new BoardTile[dimension][dimension];
        for (int i = 0; i < dimension; i++){
            Arrays.fill(boardTiles[i], BoardTile.EMPTY);
        }
        this.dimension = dimension;
    }

    private Board(int dimension, BoardTile[][] oldBoardTiles, Board.TileAt... ats){
        boardTiles = new BoardTile[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
                boardTiles[i] = oldBoardTiles[i].clone();
        }
        for (TileAt tile : ats){
            boardTiles[tile.pos.i()][tile.pos.j()] = tile.tile;
        }
        this.dimension = dimension;
    }

    // Rozměr hrací desky
    public int dimension() {
        return dimension;
    }

    // Vrací dlaždici na zvolené pozici.
    public BoardTile at(TilePos pos) {
        return boardTiles[pos.i()][pos.j()];
    }

    // Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(Board.TileAt... ats) {
        return new Board(dimension, boardTiles, ats);
    }

    // Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
    public PositionFactory positionFactory() {
        return new PositionFactory(dimension);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"dimension\":" + dimension);

        writer.print(",\"tiles\":[");
        boolean fFlag = false;
        /*for (BoardTile[] tiles: boardTiles){
            for (BoardTile tile : tiles){
                if (!fFlag) {
                    tile.toJSON(writer);
                    fFlag = true;
                }
                else {
                    writer.write(",");
                    tile.toJSON(writer);
                }
            }
        }*/
        for (int i = 0; i < boardTiles.length; i++){
            for (int j = 0; j < boardTiles[i].length; j++){
                if (!fFlag) {
                    boardTiles[j][i].toJSON(writer);
                    fFlag = true;
                }
                else {
                    writer.write(",");
                    boardTiles[j][i].toJSON(writer);
                }
            }
        }
        writer.print("]}");
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }
}

