package org.domain.Cut;



import org.domain.CutLine;
import org.equipe45.dto.HorizontalLineDTO;

import java.util.List;
import java.util.UUID;

public class HorizontalLine extends Cut {
    public static final int HITBOX_SIZE = 20;
    private int y;
    private Color color;
    //public int initialGridSize;
    private int thickness;

    //TODO: UUID or tool object? 
    private UUID tool;

    public HorizontalLine(int y, Color color, int thickness, UUID tool) {
        this.id = UUID.randomUUID();
        this.y = y;
        this.color = color;
        this.thickness = thickness;
        this.tool = tool;
    }

    public HorizontalLine(UUID id, boolean isSelected, int y, Color color, int thickness, UUID tool) {
        this.id = id;
        this.isSelected = isSelected;
        this.y = y;
        this.color = color;
        this.thickness = thickness;
        this.tool = tool;
    }

    @Override
    public boolean isClicked(int mouseX, int mouseY) {
        return mouseY >= y - HITBOX_SIZE && mouseY <= y + HITBOX_SIZE;
    }

    /*@Override
    public void draw(Graphics g, int currentGridSize, int panelWidth) {
        int adjustedY = (y / initialGridSize) * currentGridSize;

        g.setColor(color);
        g.drawLine(0, adjustedY, panelWidth, adjustedY);
    }*/

    public void move(int newY) {
        this.y = newY;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /*public int getInitialGridSize() {
        return initialGridSize;
    }

    public void setInitialGridSize(int initialGridSize) {
        this.initialGridSize = initialGridSize;
    }*/

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

    

    @Override
    public HorizontalLineDTO toDTO() {
        return new HorizontalLineDTO(id, isSelected, y, color, /*initialGridSize,*/ thickness, tool);
    }

    @Override
    public List<CutLine> getCutLines() {
        Point start = new Point(0, y);
        Point end = new Point(99999, y);
        return List.of(new CutLine(start, end, this));
    }


}