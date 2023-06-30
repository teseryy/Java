package thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction{

    protected SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    public SlideAction(Offset2D offset) {
        super(offset);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        while (target != TilePos.OFF_BOARD){
            int len = result.size();

            if (state.canStep(origin, target)) {
                result.add(new StepOnly(origin, (BoardPos) target));
            } else if (state.canCapture(origin, target)) {
                result.add(new StepAndCapture(origin, (BoardPos) target));
                break;
            }

            if (len == result.size()){
                break;
            }

            target = target.stepByPlayingSide(offset(), side);
        }

        return result;
    }
}
