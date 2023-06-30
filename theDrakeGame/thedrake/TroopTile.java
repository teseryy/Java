package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile, JSONSerializable{
    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    public PlayingSide side(){
        return side;
    }

    public TroopFace face(){
        return face;
    }

    public Troop troop(){
        return troop;
    }

    @Override
    public boolean canStepOn() {
        return false;
    }

    @Override
    public boolean hasTroop() {
        return true;
    }

    public TroopTile flipped(){
        if (face == TroopFace.AVERS) {
            return new TroopTile(troop, side, TroopFace.REVERS);
        }
        else{
            return new TroopTile(troop, side, TroopFace.AVERS);
        }
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<TroopAction> actions = new ArrayList<>();
        actions = troop.actions(face);

        List<Move> result = new ArrayList<>();

        for (TroopAction action : actions){
            result.addAll(action.movesFrom(pos, side, state));
        }

        return result;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"troop\":");
        troop.toJSON(writer);

        writer.print(",\"side\":");
        side.toJSON(writer);

        writer.print(",\"face\":");
        face.toJSON(writer);
        writer.print("}");
    }
}
