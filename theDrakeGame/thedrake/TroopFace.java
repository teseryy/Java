package thedrake;

import java.io.PrintWriter;

public enum TroopFace implements JSONSerializable{
    AVERS{
        @Override
        public void toJSON(PrintWriter writer) {
            writer.print("\"" + AVERS.toString() + "\"");
        }
    }, REVERS {
        @Override
        public void toJSON(PrintWriter writer) {
            writer.print("\"" + REVERS.toString() + "\"");
        }
    };
}
