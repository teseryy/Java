package thedrake;

import java.io.PrintWriter;

public enum PlayingSide implements JSONSerializable{
    ORANGE {
        @Override
        public void toJSON(PrintWriter writer) {
            writer.print("\"" + ORANGE.toString() + "\"");
        }
    }, BLUE {
        @Override
        public void toJSON(PrintWriter writer) {
            writer.print("\"" +  BLUE.toString() + "\"");
        }
    };
}
