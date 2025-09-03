package org.equipe39.domain.Cut;



import org.equipe39.domain.CutLine;
import org.equipe39.domain.Outil;
import org.equipe39.dto.CutLineDTO;
import org.equipe39.dto.HorizontalLineDTO;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HorizontalLine extends Cut {
    private List<CutLine> cutLines;

    public static final double HITBOX_SIZE = 20;
    private double y;
    private double width;
    private double profondeur;

    //TODO: UUID or tool object? 
    private Outil tool;

    public HorizontalLine(double y, double profondeur, Outil tool/*, double width*/) {
        this.id = UUID.randomUUID();
        this.y = y;
        //this.width = width;
        this.profondeur = profondeur;
        this.tool = tool;

        this.cutLines = getCutLines();
    }

    public HorizontalLine(UUID id, boolean isSelected, double y, double profondeur, Outil tool/*, double width*/) {
        this.id = id;
        this.isSelected = isSelected;
        this.y = y;
        //this.width = width;
        this.profondeur = profondeur;
        this.tool = tool;
    }

    @Override
    public boolean isClicked(double mouseX, double mouseY) {
        return mouseY >= y - HITBOX_SIZE && mouseY <= y + HITBOX_SIZE;
    }

    public void move(double newY) {
        this.y = newY;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
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

    

    @Override
    public HorizontalLineDTO toDTO() {
        List<CutLine> cutLines = getCutLines();
        return ((this.tool == null) ?  new HorizontalLineDTO(id, isSelected, y, profondeur, null) : new HorizontalLineDTO(id, isSelected, y, profondeur, tool.toDTO()));
    }

    @Override
    public List<CutLine> getCutLines() {
        Point2D.Double start = new Point2D.Double(0, y);
        //Point2D.Double end = new Point2D.Double(width, y);
        Point2D.Double end = new Point2D.Double(999999999, y);
        this.cutLines = List.of(new CutLine(1, start, end, this));
        return this.cutLines;
    }

    //TODO: FINISH
    @Override
    public boolean isTouching(Cut cut) {
        return false;
    }


    /*@Override
    //tostring
    public String toString() {
        return "HorizontalLine{" +
                "y=" + y +
                ", profondeur=" + profondeur +
                ", tool=" + tool +
                ", selected=" + isSelected +
                '}';
    }*/

    /*@Override
    //tostring
    public String toString() {
        return "HorizontalLine{" +
                "y=" + y +
                ", profondeur=" + profondeur +
                ", tool=" + tool +
                ", selected=" + isSelected +
                '}';
    }*/

    @Override
    //tostring
    public String toString() {
        return "HorizontalLine{" +
                    "id" + id +
                '}';
    }

    //equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HorizontalLine horizontalLine = (HorizontalLine) o;

        return id.equals(horizontalLine.id ) && horizontalLine.y == y && horizontalLine.profondeur == profondeur && horizontalLine.tool.equals(tool);
    }

    //clone
    @Override
    public HorizontalLine clone() {
        return new HorizontalLine(this.id, this.isSelected, this.y, this.profondeur, this.tool/*, this.width*/);
    }


}