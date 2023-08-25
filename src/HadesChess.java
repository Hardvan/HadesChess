import java.util.*;
import javax.swing.*;

/*
 * Naming convention:
 * PIECE = WHITE/black
 * pawn = P/p
 * Knight (horse) = K/k
 * bishop = B/b
 * Rook (castle) = R/r
 * Queen = Q/q
 * King = A/a

 * Strategy: Create an alpha-beta tree diagram which returns the best outcome

 * (1234b represents row1, column2 moves to row3, column4 which captured
 * b (a space represents no capture))
 */

public class HadesChess {

    // 2D array of Strings representing the chess board
    static String[][] chessBoard = {
            {"r", "k", "b", "q", "a", "b", "k", "r"},
            {"p", "p", "p", "p", "p", "p", "p", "p"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"P", "P", "P", "P", "P", "P", "P", "P"},
            {"R", "K", "B", "Q", "A", "B", "K", "R"}};

    // Location of the kings (C = computer, L = human)
    static int kingPositionC, kingPositionL;
    static int humanAsWhite = -1; // 1 = human as white, 0 = human as black
    static int globalDepth = 4; // depth of the alpha-beta tree

    public static void main(String[] args) {
        // Get King's location
        while (!"A".equals(chessBoard[kingPositionC / 8][kingPositionC % 8])) {
            kingPositionC++;
        }
        // Get King's location
        while (!"a".equals(chessBoard[kingPositionL / 8][kingPositionL % 8])) {
            kingPositionL++;
        }

        // Create the GUI window
        JFrame frame = new JFrame("Chess Tutorial");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui = new UserInterface();
        frame.add(ui);
        frame.setSize(1000, 1000); // 1000x1000 pixel window
        frame.setLocationRelativeTo(null); // center the window
        frame.setVisible(true); // show the window

        System.out.println(sortMoves(possibleMoves())); // print out the possible moves

        // Ask the user if they want to play as white or black
        Object[] option = {"Computer", "Human"}; // options for the JOptionPane
        humanAsWhite = JOptionPane.showOptionDialog(null, "Who should play as white?",
                "ABC Options", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
        // If the user chooses to play as black, make the first move
        if (humanAsWhite == 0) {
            long startTime = System.currentTimeMillis(); // start the timer

            // Make the move by calling the alphaBeta function
            makeMove(alphaBeta(globalDepth, 1000000, -1000000, "", 0));

            long endTime = System.currentTimeMillis(); // end the timer
            System.out.println("That took " + (endTime - startTime) + " milliseconds"); // print out the time it took

            // Flip the board and repaint the GUI
            flipBoard();
            frame.repaint();
        }
        makeMove("7655 ");
        undoMove("7655 ");

        // Print out the chess board to the console
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }

    public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {

        // Return in the form of 1234b##########

        String list = possibleMoves(); // get the list of possible moves

        // If no moves are possible or the depth is 0, return the move and the rating
        if (depth == 0 || list.length() == 0) {
            return move + (Rating.rating(list.length(), depth) * (player * 2 - 1));
        }

        list = sortMoves(list); // sort the moves
        player = 1 - player; // either 1 or 0

        // Loop through the list of moves
        for (int i = 0; i < list.length(); i += 5) {
            makeMove(list.substring(i, i + 5)); // make the move
            flipBoard();

            // Recursively call the alphaBeta function to get the rating
            String returnString = alphaBeta(depth - 1, beta, alpha, list.substring(i, i + 5), player);
            int value = Integer.valueOf(returnString.substring(5)); // get the rating

            flipBoard();
            undoMove(list.substring(i, i + 5)); // undo the move

            // If the rating is less than beta, set beta to the rating
            if (player == 0) {
                if (value <= beta) {
                    beta = value;
                    // If the depth is the global depth, set the move to the move
                    if (depth == globalDepth) {
                        move = returnString.substring(0, 5);
                    }
                }
            }
            // If the rating is greater than alpha, set alpha to the rating
            else {
                if (value > alpha) {
                    alpha = value;
                    if (depth == globalDepth) {
                        move = returnString.substring(0, 5);
                    }
                }
            }
            // If alpha is greater than or equal to beta, return the move and the rating
            if (alpha >= beta) {
                if (player == 0) {
                    return move + beta;
                } else {
                    return move + alpha;
                }
            }
        }
        // Return the move and the rating
        if (player == 0) {
            return move + beta;
        } else {
            return move + alpha;
        }
    }

    public static void flipBoard() {
        String temp;
        // Loop through the chess board
        for (int i = 0; i < 32; i++) {
            int r = i / 8, c = i % 8;
            // Turn uppercase to lowercase and vice versa
            if (Character.isUpperCase(chessBoard[r][c].charAt(0))) {
                temp = chessBoard[r][c].toLowerCase();
            } else {
                temp = chessBoard[r][c].toUpperCase();
            }
            // Swap the pieces
            if (Character.isUpperCase(chessBoard[7 - r][7 - c].charAt(0))) {
                chessBoard[r][c] = chessBoard[7 - r][7 - c].toLowerCase();
            } else {
                chessBoard[r][c] = chessBoard[7 - r][7 - c].toUpperCase();
            }
            chessBoard[7 - r][7 - c] = temp;
        }

        // Swap the king positions
        int kingTemp = kingPositionC;
        kingPositionC = 63 - kingPositionL;
        kingPositionL = 63 - kingTemp;
    }

    public static void makeMove(String move) {

        if (move.charAt(4) != 'P') {
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
            if ("A".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
                kingPositionC = 8 * Character.getNumericValue(move.charAt(2)) + Character.getNumericValue(move.charAt(3));
            }
        } else {
            // if pawn promotion
            chessBoard[1][Character.getNumericValue(move.charAt(0))] = " ";
            chessBoard[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));
        }
    }

    public static void undoMove(String move) {
        if (move.charAt(4) != 'P') {
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = String.valueOf(move.charAt(4));
            if ("A".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
                kingPositionC = 8 * Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
            }
        } else {
            // if pawn promotion
            chessBoard[1][Character.getNumericValue(move.charAt(0))] = "P";
            chessBoard[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(2));
        }
    }

    public static String possibleMoves() {
        StringBuilder list = new StringBuilder();

        // Loop through the chess board
        for (int i = 0; i < 64; i++) {
            // Switch statement for each piece
            switch (chessBoard[i / 8][i % 8]) {
                case "P":
                    list.append(possibleP(i));
                    break;
                case "R":
                    list.append(possibleR(i));
                    break;
                case "K":
                    list.append(possibleK(i));
                    break;
                case "B":
                    list.append(possibleB(i));
                    break;
                case "Q":
                    list.append(possibleQ(i));
                    break;
                case "A":
                    list.append(possibleA(i));
                    break;
            }
        }

        return list.toString(); // x1,y1,x2,y2,captured piece
    }

    public static String possibleP(int i) {
        String list = "", oldPiece;
        int r = i / 8, c = i % 8; // row and column
        // Loop through the possible captures
        for (int j = -1; j <= 1; j += 2) {
            try {//capture
                if (Character.isLowerCase(chessBoard[r - 1][c + j].charAt(0)) && i >= 16) {
                    oldPiece = chessBoard[r - 1][c + j];
                    chessBoard[r][c] = " ";
                    chessBoard[r - 1][c + j] = "P";
                    if (kingSafe()) {
                        list = list + r + c + (r - 1) + (c + j) + oldPiece;
                    }
                    chessBoard[r][c] = "P";
                    chessBoard[r - 1][c + j] = oldPiece;
                }
            } catch (Exception e) {
            }
            try {//promotion && capture
                if (Character.isLowerCase(chessBoard[r - 1][c + j].charAt(0)) && i < 16) {
                    String[] temp = {"Q", "R", "B", "K"};
                    for (int k = 0; k < 4; k++) {
                        oldPiece = chessBoard[r - 1][c + j];
                        chessBoard[r][c] = " ";
                        chessBoard[r - 1][c + j] = temp[k];
                        if (kingSafe()) {
                            //column1,column2,captured-piece,new-piece,P
                            list = list + c + (c + j) + oldPiece + temp[k] + "P";
                        }
                        chessBoard[r][c] = "P";
                        chessBoard[r - 1][c + j] = oldPiece;
                    }
                }
            } catch (Exception e) {
            }
        }
        try {//move one up
            if (" ".equals(chessBoard[r - 1][c]) && i >= 16) {
                oldPiece = chessBoard[r - 1][c];
                chessBoard[r][c] = " ";
                chessBoard[r - 1][c] = "P";
                if (kingSafe()) {
                    list = list + r + c + (r - 1) + c + oldPiece;
                }
                chessBoard[r][c] = "P";
                chessBoard[r - 1][c] = oldPiece;
            }
        } catch (Exception e) {
        }
        try {//promotion && no capture
            if (" ".equals(chessBoard[r - 1][c]) && i < 16) {
                String[] temp = {"Q", "R", "B", "K"};
                for (int k = 0; k < 4; k++) {
                    oldPiece = chessBoard[r - 1][c];
                    chessBoard[r][c] = " ";
                    chessBoard[r - 1][c] = temp[k];
                    if (kingSafe()) {
                        //column1,column2,captured-piece,new-piece,P
                        list = list + c + c + oldPiece + temp[k] + "P";
                    }
                    chessBoard[r][c] = "P";
                    chessBoard[r - 1][c] = oldPiece;
                }
            }
        } catch (Exception e) {
        }
        try {//move two up
            if (" ".equals(chessBoard[r - 1][c]) && " ".equals(chessBoard[r - 2][c]) && i >= 48) {
                oldPiece = chessBoard[r - 2][c];
                chessBoard[r][c] = " ";
                chessBoard[r - 2][c] = "P";
                if (kingSafe()) {
                    list = list + r + c + (r - 2) + c + oldPiece;
                }
                chessBoard[r][c] = "P";
                chessBoard[r - 2][c] = oldPiece;
            }
        } catch (Exception e) {
        }
        return list;
    }

    public static String possibleR(int i) {
        String list = "", oldPiece;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j += 2) {
            try {
                while (" ".equals(chessBoard[r][c + temp * j])) {
                    oldPiece = chessBoard[r][c + temp * j];
                    chessBoard[r][c] = " ";
                    chessBoard[r][c + temp * j] = "R";
                    if (kingSafe()) {
                        list = list + r + c + r + (c + temp * j) + oldPiece;
                    }
                    chessBoard[r][c] = "R";
                    chessBoard[r][c + temp * j] = oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(chessBoard[r][c + temp * j].charAt(0))) {
                    oldPiece = chessBoard[r][c + temp * j];
                    chessBoard[r][c] = " ";
                    chessBoard[r][c + temp * j] = "R";
                    if (kingSafe()) {
                        list = list + r + c + r + (c + temp * j) + oldPiece;
                    }
                    chessBoard[r][c] = "R";
                    chessBoard[r][c + temp * j] = oldPiece;
                }
            } catch (Exception e) {
            }
            temp = 1;
            try {
                while (" ".equals(chessBoard[r + temp * j][c])) {
                    oldPiece = chessBoard[r + temp * j][c];
                    chessBoard[r][c] = " ";
                    chessBoard[r + temp * j][c] = "R";
                    if (kingSafe()) {
                        list = list + r + c + (r + temp * j) + c + oldPiece;
                    }
                    chessBoard[r][c] = "R";
                    chessBoard[r + temp * j][c] = oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(chessBoard[r + temp * j][c].charAt(0))) {
                    oldPiece = chessBoard[r + temp * j][c];
                    chessBoard[r][c] = " ";
                    chessBoard[r + temp * j][c] = "R";
                    if (kingSafe()) {
                        list = list + r + c + (r + temp * j) + c + oldPiece;
                    }
                    chessBoard[r][c] = "R";
                    chessBoard[r + temp * j][c] = oldPiece;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }
        return list;
    }

    public static String possibleK(int i) {
        String list = "", oldPiece;
        int r = i / 8, c = i % 8;
        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                try {
                    if (Character.isLowerCase(chessBoard[r + j][c + k * 2].charAt(0)) || " ".equals(chessBoard[r + j][c + k * 2])) {
                        oldPiece = chessBoard[r + j][c + k * 2];
                        chessBoard[r][c] = " ";
                        if (kingSafe()) {
                            list = list + r + c + (r + j) + (c + k * 2) + oldPiece;
                        }
                        chessBoard[r][c] = "K";
                        chessBoard[r + j][c + k * 2] = oldPiece;
                    }
                } catch (Exception e) {
                }
                try {
                    if (Character.isLowerCase(chessBoard[r + j * 2][c + k].charAt(0)) || " ".equals(chessBoard[r + j * 2][c + k])) {
                        oldPiece = chessBoard[r + j * 2][c + k];
                        chessBoard[r][c] = " ";
                        if (kingSafe()) {
                            list = list + r + c + (r + j * 2) + (c + k) + oldPiece;
                        }
                        chessBoard[r][c] = "K";
                        chessBoard[r + j * 2][c + k] = oldPiece;
                    }
                } catch (Exception e) {
                }
            }
        }
        return list;
    }

    public static String possibleB(int i) {
        String list = "", oldPiece;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                try {
                    while (" ".equals(chessBoard[r + temp * j][c + temp * k])) {
                        oldPiece = chessBoard[r + temp * j][c + temp * k];
                        chessBoard[r][c] = " ";
                        chessBoard[r + temp * j][c + temp * k] = "B";
                        if (kingSafe()) {
                            list = list + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
                        }
                        chessBoard[r][c] = "B";
                        chessBoard[r + temp * j][c + temp * k] = oldPiece;
                        temp++;
                    }
                    if (Character.isLowerCase(chessBoard[r + temp * j][c + temp * k].charAt(0))) {
                        oldPiece = chessBoard[r + temp * j][c + temp * k];
                        chessBoard[r][c] = " ";
                        chessBoard[r + temp * j][c + temp * k] = "B";
                        if (kingSafe()) {
                            list = list + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
                        }
                        chessBoard[r][c] = "B";
                        chessBoard[r + temp * j][c + temp * k] = oldPiece;
                    }
                } catch (Exception e) {
                }
                temp = 1;
            }
        }
        return list;
    }

    public static String possibleQ(int i) {
        String list = "", oldPiece;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
                if (j != 0 || k != 0) {
                    try {
                        while (" ".equals(chessBoard[r + temp * j][c + temp * k])) {
                            oldPiece = chessBoard[r + temp * j][c + temp * k];
                            chessBoard[r][c] = " ";
                            chessBoard[r + temp * j][c + temp * k] = "Q";
                            if (kingSafe()) {
                                list = list + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
                            }
                            chessBoard[r][c] = "Q";
                            chessBoard[r + temp * j][c + temp * k] = oldPiece;
                            temp++;
                        }
                        if (Character.isLowerCase(chessBoard[r + temp * j][c + temp * k].charAt(0))) {
                            oldPiece = chessBoard[r + temp * j][c + temp * k];
                            chessBoard[r][c] = " ";
                            chessBoard[r + temp * j][c + temp * k] = "Q";
                            if (kingSafe()) {
                                list = list + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
                            }
                            chessBoard[r][c] = "Q";
                            chessBoard[r + temp * j][c + temp * k] = oldPiece;
                        }
                    } catch (Exception e) {
                    }
                    temp = 1;
                }
            }
        }
        return list;
    }

    public static String possibleA(int i) {
        String list = "", oldPiece;
        int r = i / 8, c = i % 8;
        for (int j = 0; j < 9; j++) {
            if (j != 4) {
                try {
                    if (Character.isLowerCase(chessBoard[r - 1 + j / 3][c - 1 + j % 3].charAt(0)) || " ".equals(chessBoard[r - 1 + j / 3][c - 1 + j % 3])) {
                        oldPiece = chessBoard[r - 1 + j / 3][c - 1 + j % 3];
                        chessBoard[r][c] = " ";
                        chessBoard[r - 1 + j / 3][c - 1 + j % 3] = "A";
                        int kingTemp = kingPositionC;
                        kingPositionC = i + (j / 3) * 8 + j % 3 - 9;
                        if (kingSafe()) {
                            list = list + r + c + (r - 1 + j / 3) + (c - 1 + j % 3) + oldPiece;
                        }
                        chessBoard[r][c] = "A";
                        chessBoard[r - 1 + j / 3][c - 1 + j % 3] = oldPiece;
                        kingPositionC = kingTemp;
                    }
                } catch (Exception e) {
                }
            }
        }
        //need to add casting later
        return list;
    }

    // Sort the moves by rating in descending order
    public static String sortMoves(String list) {
        int[] score = new int[list.length() / 5];
        for (int i = 0; i < list.length(); i += 5) {
            makeMove(list.substring(i, i + 5));
            score[i / 5] = -Rating.rating(-1, 0);
            undoMove(list.substring(i, i + 5));
        }

        String newListA = "", newListB = list;
        for (int i = 0; i < Math.min(6, list.length() / 5); i++) {//first few moves only
            int max = -1000000, maxLocation = 0;
            for (int j = 0; j < list.length() / 5; j++) {
                if (score[j] > max) {
                    max = score[j];
                    maxLocation = j;
                }
            }
            score[maxLocation] = -1000000;
            newListA += list.substring(maxLocation * 5, maxLocation * 5 + 5);
            newListB = newListB.replace(list.substring(maxLocation * 5, maxLocation * 5 + 5), "");
        }

        return newListA + newListB;
    }

    // Check if the king is safe
    public static boolean kingSafe() {
        //bishop/queen
        int temp = 1;
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    while (" ".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8 + temp * j])) {
                        temp++;
                    }
                    if ("b".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8 + temp * j]) ||
                            "q".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8 + temp * j])) {
                        return false;
                    }
                } catch (Exception e) {
                }
                temp = 1;
            }
        }
        //rook/queen
        for (int i = -1; i <= 1; i += 2) {
            try {
                while (" ".equals(chessBoard[kingPositionC / 8][kingPositionC % 8 + temp * i])) {
                    temp++;
                }
                if ("r".equals(chessBoard[kingPositionC / 8][kingPositionC % 8 + temp * i]) ||
                        "q".equals(chessBoard[kingPositionC / 8][kingPositionC % 8 + temp * i])) {
                    return false;
                }
            } catch (Exception e) {
            }
            temp = 1;
            try {
                while (" ".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8])) {
                    temp++;
                }
                if ("r".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8]) ||
                        "q".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8])) {
                    return false;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }
        //knight
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    if ("k".equals(chessBoard[kingPositionC / 8 + i][kingPositionC % 8 + j * 2])) {
                        return false;
                    }
                } catch (Exception e) {
                }
                try {
                    if ("k".equals(chessBoard[kingPositionC / 8 + i * 2][kingPositionC % 8 + j])) {
                        return false;
                    }
                } catch (Exception e) {
                }
            }
        }
        //pawn
        if (kingPositionC >= 16) {
            try {
                if ("p".equals(chessBoard[kingPositionC / 8 - 1][kingPositionC % 8 - 1])) {
                    return false;
                }
            } catch (Exception e) {
            }
            try {
                if ("p".equals(chessBoard[kingPositionC / 8 - 1][kingPositionC % 8 + 1])) {
                    return false;
                }
            } catch (Exception e) {
            }
            //king
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        try {
                            if ("a".equals(chessBoard[kingPositionC / 8 + i][kingPositionC % 8 + j])) {
                                return false;
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }

        return true;
    }
}