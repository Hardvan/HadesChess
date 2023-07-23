import javax.swing.*;

public class HadesChess {
    static String chessBoard[][]={
            {"r","k","b","q","a","b","k","r"},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {"R","K","B","Q","A","B","K","R"}};
    static int kingPositionC, kingPositionL;
    public static void main(String []args){
//        JFrame f = new JFrame("My title goes here lol!");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        UserInterface ui = new UserInterface();
//        f.add(ui);
//        f.setSize(500, 500);
//        f.setVisible(true);
    }

    public static String possibleMoves() {
        String list="";
        for (int i=0; i<64; i++) {
            switch (chessBoard[i/8][i%8]) {
                case "P": list+=posibleP(i);
                    break;
                case "R": list+=posibleR(i);
                    break;
                case "K": list+=posibleK(i);
                    break;
                case "B": list+=posibleB(i);
                    break;
                case "Q": list+=posibleQ(i);
                    break;
                case "A": list+=posibleA(i);
                    break;
            }
        }
        return "";//x1,y1,x2,y2, captured piece
    }

    public static String posibleP(int i){
        String list="";
        return list;
    }

    public static String posibleR(int i){
        String list="";
        return list;
    }

    public static String posibleK(int i){
        String list="";
        return list;
    }

    public static String posibleB(int i){
        String list="";
        return list;
    }

    public static String posibleQ(int i){
        String list="";
        return list;
    }

    public static String posibleA(int i){
        String list="", oldPiece;
        int r=i/8, c=i%8;
        for(int j=0;j<9;j++){
            if(j!=4){
                if(Character.isLowerCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || "".equals(chessBoard[r-1+j/3][c-1+j%3])){
                    oldPiece = chessBoard[r-1+j/3][c-1+j%3];
                    chessBoard[r][c] = "";
                    chessBoard[r-1+j/3][c-1+j%3] = "A";
                    kingPositionC = i+(j/3)*8+j%3+-9;
                    if (kingSafe()){

                    }
                }
            }
        }
        return list;
    }

    public static boolean kingSafe(){
        return true;
    }
}
