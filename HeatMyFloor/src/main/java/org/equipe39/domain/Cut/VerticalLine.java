package org.equipe39.domain.Cut;


import org.equipe39.domain.CutLine;
import org.equipe39.domain.Outil;
import org.equipe39.dto.VerticalLineDTO;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.UUID;

public class VerticalLine extends Cut {
    private static final double HITBOX_SIZE = 20;
    private double x;
    private double profondeur;
    private Outil tool;
    //private double height;

    public VerticalLine(double x, double profondeur, Outil tool/*, double height*/) {
        this.id = UUID.randomUUID();
        //this.height = height;
        this.x = x;
        this.profondeur = profondeur;
        this.tool = tool;
    }

    public VerticalLine(UUID id, boolean isSelected, double x, double profondeur, Outil tool/*, double height*/)
    {
        this.id = id;
        this.isSelected = isSelected;
        //this.height = height;
        this.x = x;
        this.profondeur = profondeur;
        this.tool = tool;
    }
//

    public boolean isClicked(double mouseX, double mouseY) {
        return mouseX >= x - this.HITBOX_SIZE && mouseX <= x + this.HITBOX_SIZE;
    }

    public void move(double newX) {
        this.x = newX;
    }

    public VerticalLineDTO toDTO()
    {
        //name = ((city.getName() == null) ? "N/A" : city.getName());
        //((this.tool == null) ?  VerticalLineDTO(id, isSelected, x, profondeur, null) : VerticalLineDTO(id, isSelected, x, profondeur, tool.toDTO()));
        return ((this.tool == null) ?  new VerticalLineDTO(id, isSelected, x, profondeur, null) : new VerticalLineDTO(id, isSelected, x, profondeur, tool.toDTO()));
        //return new VerticalLineDTO(id, isSelected, x, profondeur, tool.toDTO());
    }

    @Override
    public List<CutLine> getCutLines() {
        Point2D.Double start = new Point2D.Double(x, 0);
        //Point2D.Double end = new Point2D.Double(x, height);
        Point2D.Double end = new Point2D.Double(x, 999999999);
        return List.of(new CutLine(1, start, end, this));
    }

    @Override
    public boolean isTouching(Cut cut) {
        return false;
    }


    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }


    public double getProfondeur() {
        return profondeur;
    }
    public void setProfondeur(double profondeur) {
        this.profondeur = profondeur;
    }

    public Outil getTool() {
        return tool;
    }
    public void setTool(Outil tool) {
        this.tool = tool;
    }

    //toString
    /*@Override
    public String toString() {
        return "VerticalLine{" +
                "x=" + x +
                ", profondeur=" + profondeur +
                ", tool=" + tool +
                ", selected=" + isSelected +
                '}';
    }*/

    @Override
    //tostring
    public String toString() {
        return "VerticalLine{" +
                "id" + id +
                '}';
    }

    //equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerticalLine verticalLine = (VerticalLine) o;

        return  id.equals(verticalLine.id) && verticalLine.x == x && verticalLine.profondeur == profondeur && verticalLine.tool.equals(tool);
    }

    

    @Override
    public VerticalLine clone() {
        return new VerticalLine(this.id, this.isSelected, this.x, this.profondeur, this.tool/*, this.height*/);
    }
}