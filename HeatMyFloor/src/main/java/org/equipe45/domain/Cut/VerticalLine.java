package org.domain.Cut;



import org.domain.CutLine;
import org.equipe45.dto.VerticalLineDTO;

import java.util.List;
import java.util.UUID;

public class VerticalLine extends Cut {
    private static final int HITBOX_SIZE = 20;
    private int x;
    private Color color;
    private int thickness;
    private UUID tool;

    public VerticalLine(int x, Color color, int thickness, UUID tool) {
        this.id = UUID.randomUUID();

        this.x = x;
        this.color = color;
        this.thickness = thickness;
        this.tool = tool;
    }

    public VerticalLine(UUID id, boolean isSelected, int x, Color color, int thickness, UUID tool)
    {
        this.id = id;
        this.isSelected = isSelected;
        this.x = x;
        this.color = color;
        this.thickness = thickness;
        this.tool = tool;
    }


    public boolean isClicked(int mouseX, int mouseY) {
        return mouseX >= x - this.HITBOX_SIZE && mouseX <= x + this.HITBOX_SIZE;
    }

    public void move(int newX) {
        this.x = newX;
    }

    public VerticalLineDTO toDTO()
    {
        return new VerticalLineDTO(id, isSelected, x, color, thickness, tool);
    }

    @Override
    public List<CutLine> getCutLines() {
        Point start = new Point(x, 0);
        Point end = new Point(x, 99999);
        return List.of(new CutLine(start, end, this));
    }


    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }


    public int getThickness() {
        return thickness;
    }
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public UUID getTool() {
        return tool;
    }
    public void setTool(UUID tool) {
        this.tool = tool;
    }

    /*@Override
    public void draw(Graphics g, int currentGridSize) {


        int adjustedX = (x / initialGridSize) * currentGridSize;

        if (isSelected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(color);
        }

        g.drawLine(adjustedX, 99999, adjustedX, currentGridSize);
    }*/
}