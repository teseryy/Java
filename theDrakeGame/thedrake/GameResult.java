package thedrake;

import java.io.PrintWriter;

public enum GameResult implements JSONSerializable{
    VICTORY{
        @Override
        public void toJSON(PrintWriter writer) {
            writer.print("\"" + VICTORY.toString() + "\"");
        }
    }, DRAW{
        @Override
        public void toJSON(PrintWriter writer) {
            writer.print("\"" + DRAW.toString() + "\"");
        }
    }, IN_PLAY {
        @Override
        public void toJSON(PrintWriter writer) {
            writer.print("\"" + IN_PLAY.toString() + "\"");
        }
    };
}
