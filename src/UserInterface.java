import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class UserInterface extends JPanel implements MouseListener, MouseMotionListener {
    static int x = 0,y = 0;
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.yellow);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        g.setColor(Color.BLUE);
        g.fillRect(x-20,y-20,40,40);
        g.setColor(new Color(190,80,120));
        g.fillRect(40,20,80,50);
        g.drawString("Karan",x,y);
        Image chessPiecesImage;
        chessPiecesImage = new ImageIcon("ChessPieces.png").getImage();
        g.drawImage(chessPiecesImage, x, y,x+64, y+64, 0, 0, 64, 64, this);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        repaint();
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
