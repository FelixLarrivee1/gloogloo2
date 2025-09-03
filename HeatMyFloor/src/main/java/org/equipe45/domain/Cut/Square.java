package org.domain.Cut;


import org.domain.CutLine;
import org.equipe45.domain.PointReference;
import org.equipe45.dto.CutDTO;
import org.equipe45.dto.SquareDTO;

import java.util.List;
import java.util.UUID;

public class Square extends Cut {

    private PointReference referencePoint;
    private int x, y;
    private double widthFactor, heightFactor;
    private Color color;
    private UUID tool;
    private int thickness;
    public Square(int x, int y, double widthFactor, double heightFactor, Color color, UUID tool, int thickness, PointReference referencePoint) {
        this.id = UUID.randomUUID();

        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.color = color;
        this.tool = tool;
        this.thickness = thickness;
        this.referencePoint = referencePoint;
    }



    public Square(UUID id, boolean isSelected, int x, int y, double widthFactor, double heightFactor, Color color, UUID tool, int thickness, PointReference referencePoint) {
        this.id = id;
        this.isSelected = isSelected;
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.color = color;
        this.tool = tool;
        this.thickness = thickness;
        this.referencePoint = referencePoint;
    }

    //getters and setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getWidthFactor() {
        return widthFactor;
    }

    public double getHeightFactor() {
        return heightFactor;
    }

    public Color getColor() {
        return color;
    }

    public UUID getTool() {
        return tool;
    }

    public int getThickness() {
        return thickness;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidthFactor(double widthFactor) {
        this.widthFactor = widthFactor;
    }

    public void setHeightFactor(double heightFactor) {
        this.heightFactor = heightFactor;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setTool(UUID tool) {
        this.tool = tool;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public void changeSize(double width, double height)
    {
        this.widthFactor = width;
        this.heightFactor = height;
    }


    public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public PointReference getReferencePoint()
    {
        return this.referencePoint;
    }

    @Override
    public boolean isClicked(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + widthFactor && mouseY >= y && mouseY <= y + heightFactor;
    }

    @Override
    public CutDTO toDTO() {
        return new SquareDTO(id, isSelected, x, y, widthFactor, heightFactor, color, tool, thickness, referencePoint.toDTO());
    }

    public List<CutLine> getCutLines() {
        Point start = new Point(x, y);
        Point end = new Point(x + (int) (widthFactor), y);
        Point start2 = new Point(x, y);
        Point end2 = new Point(x, y + (int) (heightFactor));
        Point start3 = new Point(x + (int) (widthFactor), y);
        Point end3 = new Point(x + (int) (widthFactor), y + (int) (heightFactor));
        Point start4 = new Point(x, y + (int) (heightFactor));
        Point end4 = new Point(x + (int) (widthFactor), y + (int) (heightFactor));
        return List.of(new CutLine(start, end, this), new CutLine(start2, end2, this), new CutLine(start3, end3, this), new CutLine(start4, end4, this));

    }
}