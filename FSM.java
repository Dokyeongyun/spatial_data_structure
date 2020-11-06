package Spatial_Data_Structure;

public class FSM {
    public static int LU = 100;
    public static int U = 101;
    public static int RU = 102;
    public static int L = 103;
    public static int R = 104;
    public static int LD = 105;
    public static int D = 106;
    public static int RD = 107;

    String code;
    int direction;
    boolean halt;

    FSM(String code, int direction){
        this.code = code;
        this.direction = direction;
        this.halt = false;
    }
    FSM(String code){
        this.code = code;
        this.halt = true;
    }

    public static FSM[][] fsmArr = new FSM[][]{
            {new FSM("3", LU), new FSM("2", U), new FSM("1", L), new FSM("0")},
            {new FSM("2", U), new FSM("3", U), new FSM("0"), new FSM("1")},
            {new FSM("3", U), new FSM("2", RU), new FSM("1"), new FSM("0", R)},
            {new FSM("1", L), new FSM("0"), new FSM("3", L), new FSM("2")},
            {new FSM("1"), new FSM("0", R), new FSM("3"), new FSM("2", R)},
            {new FSM("3", L), new FSM("2"), new FSM("1", LD), new FSM("0", D)},
            {new FSM("2"), new FSM("3"), new FSM("0", D), new FSM("1", D)},
            {new FSM("3"), new FSM("2", R), new FSM("1", D), new FSM("0", RD)}
    };
}
