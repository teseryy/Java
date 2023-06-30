package thedrake;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public class GameState implements JSONSerializable{
    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy,
            PlayingSide sideOnTurn,
            GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;

        return blueArmy;
    }

    public Tile tileAt(TilePos pos) {
        if (pos == TilePos.OFF_BOARD){
            return BoardTile.EMPTY;
        }

        if (sideOnTurn == PlayingSide.BLUE){
            if (blueArmy.boardTroops().at(pos).isPresent()){
                return blueArmy.boardTroops().at(pos).get();
            }
        }
        else{
            if (orangeArmy.boardTroops().at(pos).isPresent()){
                return orangeArmy.boardTroops().at(pos).get();
            }
        }

        return BoardTile.EMPTY;
    }

    private boolean canStepFrom(TilePos origin) {
        if (origin == TilePos.OFF_BOARD){
            return false;
        }

        if (result != GameResult.IN_PLAY){
            return false;
        }

        if (sideOnTurn == PlayingSide.BLUE){
            if (orangeArmy.boardTroops().at(origin).isPresent()){
                return false;
            }

            if (blueArmy.boardTroops().guards() < 2){
                return false;
            }

            return blueArmy.boardTroops().at(origin).isPresent();
        }
        else{
            if (blueArmy.boardTroops().at(origin).isPresent()){
                return false;
            }

            if (orangeArmy.boardTroops().guards() < 2){
                return false;
            }

            return orangeArmy.boardTroops().at(origin).isPresent();
        }
    }

    private boolean canStepTo(TilePos target) {
        if (target == TilePos.OFF_BOARD){
            return false;
        }

        if (result != GameResult.IN_PLAY){
            return false;
        }

        if (orangeArmy.boardTroops().at(target).isPresent() || blueArmy.boardTroops().at(target).isPresent() || !board.at(target).canStepOn()){
            return false;
        }

        return true;
    }

    private boolean canCaptureOn(TilePos target) {
        if (target == TilePos.OFF_BOARD){
            return false;
        }

        if (result != GameResult.IN_PLAY){
            return false;
        }

        if (sideOnTurn == PlayingSide.BLUE){
            return orangeArmy.boardTroops().at(target).isPresent();
        }
        else{
            return blueArmy.boardTroops().at(target).isPresent();
        }
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) {
        if (target == TilePos.OFF_BOARD){
            return false;
        }

        if (result != GameResult.IN_PLAY || !canStepTo(target)){
            return false;
        }

        if (sideOnTurn == PlayingSide.BLUE){
            if (blueArmy.stack().isEmpty()){
                return false;
            }
            if (blueArmy.boardTroops().leaderPosition() == TilePos.OFF_BOARD){
                return target.row() == 1;
            }
            else if (blueArmy.boardTroops().isPlacingGuards()){
                return blueArmy.boardTroops().leaderPosition().isNextTo(target);
            }
            else {
                List<? extends TilePos> neibs = target.neighbours();

                for (TilePos pos : neibs) {
                    if (blueArmy.boardTroops().at(pos).isPresent()) {
                        return true;
                    }
                }

                return false;
            }
        }
        else{
            if (orangeArmy.stack().isEmpty()){
                return false;
            }
            if (orangeArmy.boardTroops().leaderPosition() == TilePos.OFF_BOARD){
                return target.row() == board.dimension();
            }
            else if (orangeArmy.boardTroops().isPlacingGuards()){
                return orangeArmy.boardTroops().leaderPosition().isNextTo(target);
            }
            else {
                List<? extends TilePos> neibs = target.neighbours();

                for (TilePos pos : neibs) {
                    if (orangeArmy.boardTroops().at(pos).isPresent()) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write("{\"result\":");
        result.toJSON(writer);

        writer.write(",\"board\":");
        board.toJSON(writer);

        writer.write(",\"blueArmy\":");
        blueArmy.toJSON(writer);

        writer.write(",\"orangeArmy\":");
        orangeArmy.toJSON(writer);

        writer.write("}");
    }
}
