package ui;

import game.BoardPos;
import game.Parameters;
import game.Puyo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PuyoComponent extends JComponent {
    Puyo puyo;
    BoardPos pos;
    BoardComponent parent;

    public PuyoComponent(Puyo puyo, BoardComponent parent){
        this.puyo = puyo;
        this.parent = parent;
    }

    @Override
    public void paintComponent(Graphics gfx) {
        Graphics2D g = (Graphics2D) gfx;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(rh);
        int w = getWidth();
        int h = getHeight();
        int d = Math.min(w, h);
        g.setColor(Puyo.COLOR_MAP.get(puyo.color));
        g.fillOval(w / 2 - d / 2, h / 2 - d / 2, d, d);
        g.setColor(new Color(255, 255, 255, 50));
        g.setStroke(new BasicStroke(3));
        g.drawOval(w / 2 - d / 2, h / 2 - d / 2, d, d);
    }

    public void updatePos(BoardPos pos){
        if(this.pos != pos){
            this.pos = pos;
            int rowInc = parent.getHeight()/ Parameters.ROWS;
            int colInc = parent.getWidth()/ Parameters.COLUMNS;
            setSize(colInc, rowInc);
            move(new Point(colInc*pos.col, rowInc*(Parameters.ROWS-1-pos.row)), 500);
        }
    }


    public void move(Point targetPosition, int duration) {
        setLocation(targetPosition);
        return;
//        // Get the current position of the component
//        Point currentPosition = getLocation();
//
//        // Calculate the distance to move the component in the x and y directions
//        int dx = targetPosition.x - currentPosition.x;
//        int dy = targetPosition.y - currentPosition.y;
//
//        // Determine the number of steps needed to move the component
//        int steps = duration / 20;
//
//        // Calculate the amount to move the component in each step
//        int stepX = dx / steps;
//        int stepY = dy / steps;
//
//        // Create a Timer to perform the animation
//        Timer timer = new Timer(20, new ActionListener() {
//            private int counter = 0;
//
//            public void actionPerformed(ActionEvent e) {
//                // Move the component by the appropriate amount
//                currentPosition.x += stepX;
//                currentPosition.y += stepY;
//                setLocation(currentPosition);
//
//                // Increment the counter
//                counter++;
//
//                // Stop the Timer when the desired number of steps has been reached
//                if (counter == steps) {
//                    ((Timer) e.getSource()).stop();
//
//                    // Set the final position of the component to ensure it is exactly at the target position
//                    setLocation(targetPosition);
//                }
//            }
//        });
//
//        timer.start();
    }
}
