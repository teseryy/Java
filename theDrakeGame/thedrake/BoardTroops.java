package thedrake;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable{
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        troopMap = Collections.emptyMap();
        leaderPosition = TilePos.OFF_BOARD;
        guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        /* for (Map.Entry<BoardPos, TroopTile> entry : troopMap.entrySet()) {
            if (entry.getKey() == pos) {
                obj = Optional.ofNullable(entry.getValue());
            }
        }*/
        return Optional.ofNullable(troopMap.get(pos));
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        return isLeaderPlaced() && guards < 2;
    }

    public Set<BoardPos> troopPositions() {
        Set<BoardPos> set = new HashSet<BoardPos>();
        for (Map.Entry<BoardPos, TroopTile> entry : troopMap.entrySet()) {
            set.add(entry.getKey());
        }

        return set;
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (at(target).isPresent())
            throw new IllegalArgumentException();

        TroopTile newTile = new TroopTile(troop, playingSide(), TroopFace.AVERS);
        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.put(target, newTile);
        if (!isLeaderPlaced()) {
            return new BoardTroops(playingSide(), newTroopMap, target, 0);
        } else if (guards() < 2) {
            return new BoardTroops(playingSide(), newTroopMap, leaderPosition, guards + 1);
        }
        else{
            return new BoardTroops(playingSide(), newTroopMap, leaderPosition, guards);
        }

    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent() || at(target).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        TroopTile value = newTroopMap.get(origin);

        newTroopMap.remove(origin);
        newTroopMap.put(target, value.flipped());

        if (origin.equals(leaderPosition)) {
            return new BoardTroops(playingSide(), newTroopMap, target, guards);
        }
        else {
            return new BoardTroops(playingSide(), newTroopMap, leaderPosition, guards);
        }

    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(target).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);

        if (target.equals(leaderPosition)) {
            return new BoardTroops(playingSide(), newTroops, TilePos.OFF_BOARD, guards);
        } else {
            return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"side\":");
        playingSide.toJSON(writer);

        writer.print(",\"leaderPosition\":");
        try {
            leaderPosition.toJSON(writer);
        }
        catch (UnsupportedOperationException e){
            writer.print("\"off-board\"");
        }


        writer.print(",\"guards\":" + guards());

        //List<BoardPos> characters = new ArrayList<>(troopMap.keySet());
        Set<BoardPos> setKeys = troopMap.keySet();
        TreeMap<BoardPos, TroopTile> sorted = new TreeMap<>();

        writer.print(",\"troopMap\":{");
        for (BoardPos k : setKeys){
            sorted.put(k, troopMap.get(k));
        }

        boolean fFlag = false;
        for (BoardPos key: sorted.keySet()){
            if (!fFlag) {
                writer.print("\"" + key + "\":");
                sorted.get(key).toJSON(writer);
                fFlag = true;
            }
            else {
                writer.print(",\"" + key + "\":");
                sorted.get(key).toJSON(writer);
            }
        }
        writer.print("}}");

    }
}
