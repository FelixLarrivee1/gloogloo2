package org.drawing;
import java.awt.Graphics;

public interface ViewDrawer {
    public void draw(Graphics g,  int currentGridSize);
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY);

}