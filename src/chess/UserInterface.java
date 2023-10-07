package chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener {
    static int mouseX, mouseY, newMouseX, newMouseY;
    static int squareSize = 64;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background color
        Color backgroundColor = new Color(240, 230, 200);
        this.setBackground(backgroundColor);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Calculate the centering offset for x and y axes
        int xOffset = (getWidth() - 8 * squareSize) / 2;
        int yOffset = (getHeight() - 8 * squareSize) / 2;

        // Draw the chess board
        for (int i = 0; i < 64; i += 2) {
            // For the first square
            int x = (i % 8 + (i / 8) % 2) * squareSize + xOffset;
            int y = (i / 8) * squareSize + yOffset;
            g.setColor(new Color(255, 200, 100));
            g.fillRect(x, y, squareSize, squareSize);

            // For the second square
            int x2 = ((i + 1) % 8 - ((i + 1) / 8) % 2) * squareSize + xOffset;
            int y2 = ((i + 1) / 8) * squareSize + yOffset;
            g.setColor(new Color(150, 50, 30));
            g.fillRect(x2, y2, squareSize, squareSize);
        }
        Image chessPiecesImage;
        chessPiecesImage = new ImageIcon("ChessPieces.png").getImage();
        for (int i = 0; i < 64; i++) {
            int j = -1, k = -1;
            switch (HadesChess.chessBoard[i / 8][i % 8]) {
                case "P":
                    j = 5;
                    k = 0;
                    break;
                case "p":
                    j = 5;
                    k = 1;
                    break;
                case "R":
                    j = 2;
                    k = 0;
                    break;
                case "r":
                    j = 2;
                    k = 1;
                    break;
                case "K":
                    j = 4;
                    k = 0;
                    break;
                case "k":
                    j = 4;
                    k = 1;
                    break;
                case "B":
                    j = 3;
                    k = 0;
                    break;
                case "b":
                    j = 3;
                    k = 1;
                    break;
                case "Q":
                    j = 1;
                    k = 0;
                    break;
                case "q":
                    j = 1;
                    k = 1;
                    break;
                case "A":
                    j = 0;
                    k = 0;
                    break;
                case "a":
                    j = 0;
                    k = 1;
                    break;
            }

            if (j != -1 && k != -1) {
                // Apply the xOffset and yOffset to the coordinates
                int x = (i % 8) * squareSize + xOffset;
                int y = (i / 8) * squareSize + yOffset;

                // Draw the chess piece
                g.drawImage(chessPiecesImage, x, y, x + squareSize, y + squareSize,
                        j * 64, k * 64,
                        (j + 1) * 64, (k + 1) * 64, this);
            }
        }
        /*g.setColor(Color.BLUE);
        g.fillRect(x-20, y-20, 40, 40);
        g.setColor(new Color(190,81,215));
        g.fillRect(40, 20, 80, 50);
        g.drawString("Jonathan", x, y);
        */
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int xOffset = (getWidth() - 8 * squareSize) / 2;
        int yOffset = (getHeight() - 8 * squareSize) / 2;

        // if inside the board
        if (e.getX() < 8 * squareSize + xOffset && e.getY() < 8 * squareSize + yOffset) {

            mouseX = e.getX() - xOffset;
            mouseY = e.getY() - yOffset;
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int xOffset = (getWidth() - 8 * squareSize) / 2;
        int yOffset = (getHeight() - 8 * squareSize) / 2;

        // if inside the board
        if (e.getX() < 8 * squareSize + xOffset && e.getY() < 8 * squareSize + yOffset) {

            newMouseX = e.getX() - xOffset;
            newMouseY = e.getY() - yOffset;
            if (e.getButton() == MouseEvent.BUTTON1) {
                String dragMove;
                if (newMouseY / squareSize == 0 && mouseY / squareSize == 1 && "P".equals(HadesChess.chessBoard[mouseY / squareSize][mouseX / squareSize])) {
                    //pawn promotion
                    dragMove = String.valueOf(mouseX / squareSize) + newMouseX / squareSize + HadesChess.chessBoard[newMouseY / squareSize][newMouseX / squareSize] + "QP";
                } else {
                    //regular move
                    dragMove = String.valueOf(mouseY / squareSize) + mouseX / squareSize + newMouseY / squareSize + newMouseX / squareSize + HadesChess.chessBoard[newMouseY / squareSize][newMouseX / squareSize];
                }
                String userPossibilities = HadesChess.possibleMoves();
                if (userPossibilities.replaceAll(dragMove, "").length() < userPossibilities.length()) {
                    //if valid move
                    HadesChess.makeMove(dragMove);
                    HadesChess.flipBoard();
                    HadesChess.makeMove(HadesChess.alphaBeta(HadesChess.globalDepth, 1000000, -1000000, "", 0));
                    HadesChess.flipBoard();
                    repaint();
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}