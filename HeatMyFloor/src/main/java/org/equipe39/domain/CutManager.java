package org.equipe39.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.equipe39.domain.Cut.*;
import org.equipe39.dto.*;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;



public class CutManager {



    private Point2D.Double startPositionAfterDrag;
    private List<Cut> cuts;
    private CNCController cncController;
    public List<Cut> temporaryCloneCutForMovement = new ArrayList<>();


    public CutManager(CNCController cncController) {
        this.cncController = cncController;
        this.cuts = new ArrayList<>();

    }


    //getCuts
    public List<Cut> getCuts() {
        return cuts;
    }


    public void updateAllAfterAnAction() {
        //this.cncController.updateAllAfterAnAction();

    }


    public void setTemporaryClonePointReferenceForMovement() {
        //this.temporaryClonePointReferenceForMovement = pointReference;
        //has to be cloned because the reference points are being modified
        this.temporaryCloneCutForMovement = new ArrayList<>();
        for(Cut pointRef : this.cuts)
        {
            this.temporaryCloneCutForMovement.add(pointRef.clone());
        }
    }


    //public




    public void addCut(Cut cut) {
        //this.cuts.add(cut);
        //add to top so we can select the newest cut
        this.cuts.add(0, cut);
        this.cncController.updateAllAfterAnAction();
    }

    public void addCutAtLoad(Cut cut) {
        this.cuts.add(0, cut);
    }

    /*public void updateCut(Cut cut) {
        for (int i = 0; i < this.cuts.size(); i++) {
            if (cuts.get(i).isSelected()) {
                cuts.set(i, cut);
            }
        }
        this.cncController.updateAllAfterAnAction();
    }*/
    public void updateCut(Cut cut) {
        /*for (int i = 0; i < this.cuts.size(); i++) {
            if (cuts.get(i).isSelected()) {
                cuts.set(i, cut);
            }
        }*/

        /*for(Cut cut1 : this.cuts) {
            if(cut.id == cut1.id)
            {
                cut1 = cut;
            }
        }*/
        for (int i = 0; i < this.cuts.size(); i++) {
            //if (cuts.get(i).id == cut.id) {
            if (cuts.get(i).id.equals(cut.id)) {
                System.out.println("FOUND THE CUT");
                cuts.set(i, cut);
            }
        }

        this.cncController.updateAllAfterAnAction();
    }


    public void deleteCut(Cut cut) {
        this.cuts.remove(cut);
        this.cncController.updateAllAfterAnAction();
    }






    public void handleCutCreation(double x, double y, String selectedCuttingMethod, Outil tool, double profondeur) {
       //todo: adapt isClickInsideRestrictedArea to overcome cuts with one parametres like lines
        if (!this.cncController.panelManager.isClickInsidePanel(x, y) || this.isClickInsideRestrictedArea(x,y)) {
            System.out.println("Click outside panel, ignoring.");
            return;
        }
        //this.generateDotPANFile();

        if (selectedCuttingMethod.equals("cutterVertical")) {
            createVerticalLine(x, tool, profondeur);
        } else if (selectedCuttingMethod.equals("cutterHorizontal")) {
            createHorizontalLine(y, tool, profondeur);
        } else if (selectedCuttingMethod.equals("restrictedArea")) {
            handleRestrictedAreaCreation(x, y);
        } else if (selectedCuttingMethod.equals("cutterRectangle")) {
            handleSquareCreation(x, y, tool, profondeur);
        } else if (selectedCuttingMethod.equals("cutterBorder")) {
            //handleBorderCreation(x, y, tool, profondeur);
        } else if (selectedCuttingMethod.equals("cutterL")) {
            handleLCreation(x, y, tool, profondeur);
        }

        System.out.println("================================================================================================================================");
        for(String str : GCodeGenerator.convertCutLinesToGCode(this.cncController.pointReferenceManager.appendEveryCutLine(), 1000, 1000))
        {
            System.out.println(str);
        }
        System.out.println("================================================================================================================================");



    }

    public void setCuts(List<Cut> cuts) {
        this.cuts = cuts;
    }

    public void createVerticalLine(double x, Outil tool, double profondeur) {
        if(!isXAxisTouchingRestrictedArea(x))
        {
            System.out.println("Creating vertical line");
            int lineX = (int) ((x));
            //VerticalLine verticalLine = new VerticalLine(lineX, profondeur, tool, cncController.adjustedHeight);
            VerticalLine verticalLine = new VerticalLine(lineX, profondeur, tool);
            //actionHistory.addCut(verticalLine);
            addCut(verticalLine);
            System.out.println("Vertical line created and added to history.");
            //this.actionHistory.updateHistoryAfterClickRelease();
            this.cncController.updateHistoryAfterClickRelease();

        }
    }

    public void createHorizontalLine(double y, Outil tool, double profondeur) {
        if(!isYAxisTouchingRestrictedArea(y))
        {
            System.out.println("Creating horizontal line");
            double lineY = y;
            //HorizontalLine horizontalLine = new HorizontalLine(lineY, profondeur, tool, cncController.adjustedWidth);
            HorizontalLine horizontalLine = new HorizontalLine(lineY, profondeur, tool/*, cncController.adjustedWidth*/);
            this.addCut(horizontalLine);
            System.out.println("Horizontal line created and added to history.");
            this.cncController.updateHistoryAfterClickRelease();
        }
    }

    public void handleRestrictedAreaCreation(double x, double y) {
        if (this.cncController.clickedPointSequence.isEmpty()) {
            this.cncController.clickedPointSequence.add(new Point2D.Double(x, y));
            System.out.println("First point for restricted area recorded.");
        } else {
            this.cncController.clickedPointSequence.add(new Point2D.Double(x, y));
            System.out.println("Second point for restricted area recorded.");

            Point2D.Double firstPoint = this.cncController.clickedPointSequence.get(0);
            Point2D.Double secondPoint = this.cncController.clickedPointSequence.get(1);

            // Calculate width and height based on the two points
            double x1 = Math.min(firstPoint.getX(), secondPoint.getX());
            double y1 = Math.min(firstPoint.getY(), secondPoint.getY());
            double widthFactor = Math.abs(secondPoint.getX() - firstPoint.getX());
            double heightFactor = Math.abs(secondPoint.getY() - firstPoint.getY());

            RestrictedArea restrictedArea = new RestrictedArea(x1, y1, widthFactor, heightFactor);
            if(!this.isCutTouchingOtherCut(restrictedArea))
            {

                this.addCut(restrictedArea);
                this.cncController.clickedPointSequence.clear();
                System.out.println("Restricted area created and added to history.");
                this.cncController.updateHistoryAfterClickRelease();
            }
            else
            {
                this.cncController.clickedPointSequence.removeLast();
            }


        }
    }






    public void handleBorderCreation(double grosseur, Outil tool, double profondeur) {
    //this.getRestrictedAreas();

        Panel panel = this.cncController.panelManager.panel;
        System.out.println("Creating border");
        double height = panel.getHeightFactor() - grosseur*2;
        double width = panel.getWidthFactor() - grosseur*2;
        double x = panel.getX() + grosseur;
        double y = panel.getY() + grosseur;

        //create the border
        Bordure border = new Bordure(x, y, width, height, tool, profondeur);

        for(RestrictedArea restrictedArea : this.getRestrictedAreas())
        {
            if(restrictedArea.isTouching(border))
            {
                return;
            }
        }


        this.addCut(border);


    }


public void handleSquareCreation(double x, double y, Outil tool, double profondeur) {
    System.out.println("Square Cut Creation");
    if (this.cncController.clickedPointSequence.isEmpty()) {
        System.out.println("Square cut | First click");
        InteractiveEntity clickedEntity = this.cncController.isClickingSomething(new Point2D.Double(x, y));
        if (clickedEntity != null) {
            System.out.println("Square cut | First click: clicked something.");
            if (clickedEntity instanceof PointReference) {
                System.out.println("Square cut | First click: clicked a reference point. Adding to sequence.");
                this.cncController.clickedPointSequence.add(new Point2D.Double(x, y));
            }
        }
    }
    else if (this.cncController.clickedPointSequence.size() == 1) {
        System.out.println("Square cut | Second click");
        this.cncController.clickedPointSequence.add(new Point2D.Double(x, y));
        System.out.println("Second click recorded for square.");
    }
    else if (this.cncController.clickedPointSequence.size() == 2) {
        System.out.println("Square cut | Third click");
        this.cncController.clickedPointSequence.add(new Point2D.Double(x, y));
        System.out.println("Third click recorded for square.");

        //if third point is smaller than the second point, swap them. not the points, but rather swap the individual coordinates so that second point will always be smaller
        if(this.cncController.clickedPointSequence.get(2).getX() < this.cncController.clickedPointSequence.get(1).getX())
        {
            double temp = this.cncController.clickedPointSequence.get(2).getX();
            this.cncController.clickedPointSequence.get(2).setLocation(this.cncController.clickedPointSequence.get(1).getX(), this.cncController.clickedPointSequence.get(2).getY());
            this.cncController.clickedPointSequence.get(1).setLocation(temp, this.cncController.clickedPointSequence.get(1).getY());
        }
        if(this.cncController.clickedPointSequence.get(2).getY() < this.cncController.clickedPointSequence.get(1).getY())
        {
            double temp = this.cncController.clickedPointSequence.get(2).getY();
            this.cncController.clickedPointSequence.get(2).setLocation(this.cncController.clickedPointSequence.get(2).getX(), this.cncController.clickedPointSequence.get(1).getY());
            this.cncController.clickedPointSequence.get(1).setLocation(this.cncController.clickedPointSequence.get(1).getX(), temp);
        }
        
                


        Point2D.Double firstClick = this.cncController.clickedPointSequence.get(0);
        Point2D.Double secondClick = this.cncController.clickedPointSequence.get(1);
        Point2D.Double thirdClick = this.cncController.clickedPointSequence.get(2);

        double widthFactor = Math.abs(thirdClick.getX() - secondClick.getX());
        double heightFactor = Math.abs(thirdClick.getY() - secondClick.getY());

        PointReference referencePoint = (PointReference) this.cncController.isClickingSomething(firstClick);
        Square square = new Square(secondClick.getX(), secondClick.getY(), widthFactor, heightFactor, tool, profondeur, referencePoint.clone());
        this.addCut(square);

        this.cncController.clickedPointSequence.clear();
        System.out.println("Square created and added to history.");
        this.cncController.updateHistoryAfterClickRelease();
    }


}



    public void handleLCreation(double x, double y, Outil tool, double profondeur) {
        System.out.println("L Cut Creation");
        // If first click
        if (this.cncController.clickedPointSequence.isEmpty()) {
            System.out.println("L cut | First click");
            InteractiveEntity clickedEntity = this.cncController.isClickingSomething(new Point2D.Double(x, y));
            if (clickedEntity != null) {
                System.out.println("L cut | First click: clicked something.");
                if (clickedEntity instanceof PointReference) {
                    System.out.println("L cut | First click: clicked a reference point. Adding to sequence.");
                    this.cncController.clickedPointSequence.add(new Point2D.Double(x, y));
                } else {
                    System.out.println("First click must be on a reference point.");
                }
            } else {
                System.out.println("First click must be on a reference point.");
            }
        }
        // If second click
        else if (this.cncController.clickedPointSequence.size() == 1) {
            System.out.println("L cut | Second click");
            this.cncController.clickedPointSequence.add(new Point2D.Double(x, y));
            System.out.println("Second click recorded for L cut. Creating L cut.");

            Point2D.Double firstClick = this.cncController.clickedPointSequence.get(0);
            Point2D.Double secondClick = this.cncController.clickedPointSequence.get(1);

            InteractiveEntity clickedEntity = this.cncController.isClickingSomething(firstClick);
            System.out.println("aALLO ALLO ALLO " + clickedEntity);
            if (clickedEntity instanceof PointReference) {
                PointReference referencePoint = (PointReference) clickedEntity;

                CutLine cutLine1 = referencePoint.getCutLineOrigin1();
                CutLine cutLine2 = referencePoint.getCutLineOrigin2();

                List<Double> xCoords = Arrays.asList(
                        cutLine1.getStart().getX(),
                        cutLine1.getEnd().getX(),
                        cutLine2.getStart().getX(),
                        cutLine2.getEnd().getX()
                );
                List<Double> yCoords = Arrays.asList(
                        cutLine1.getStart().getY(),
                        cutLine1.getEnd().getY(),
                        cutLine2.getStart().getY(),
                        cutLine2.getEnd().getY()
                );

                double minX = Collections.min(xCoords);
                double maxX = Collections.max(xCoords);
                double minY = Collections.min(yCoords);
                double maxY = Collections.max(yCoords);

                double secondX = secondClick.getX();
                double secondY = secondClick.getY();

                if (secondX >= minX && secondX <= maxX && secondY >= minY && secondY <= maxY) {
                    //PointReference newPoint = new PointReference(referencePoint.getX(), referencePoint.getY(),
                    L lCut = new L(secondX, secondY, tool, profondeur, referencePoint.clone());
                    System.out.println("HERES THE L CREATED" + lCut);
                    this.addCut(lCut);
                    System.out.println("L cut created and added to history.");
                    this.cncController.updateHistoryAfterClickRelease();
                    this.updateAllAfterAnAction();
                } else {
                    System.out.println("Second click must be within the cutlines of the reference point.");
                }
                this.cncController.clickedPointSequence.clear();
            } else {
                System.out.println("First click must be on a reference point for an L cut.");
                this.cncController.clickedPointSequence.clear();
            }
        }
    }





    //is click inside of a restricted area (use actionHistory.getRestrictedAreas)
    public boolean isClickInsideRestrictedArea(double x, double y) {
        for (RestrictedArea restrictedArea: this.getRestrictedAreas()) {
            if (x >= restrictedArea.getX() && x <= restrictedArea.getX() + restrictedArea.getWidthFactor() && y >= restrictedArea.getY() && y <= restrictedArea.getY() + restrictedArea.getHeightFactor()) {
                return true;
            }
        }
        return false;
    }

    //is Y axis  touching restricted area
    public boolean isYAxisTouchingRestrictedArea(double y) {
        for (RestrictedArea restrictedArea: this.getRestrictedAreas()) {
            if (y >= restrictedArea.getY() && y <= restrictedArea.getY() + restrictedArea.getHeightFactor()) {
                return true;
            }
        }
        return false;
    }

    //is X axis touching restricted area
    public boolean isXAxisTouchingRestrictedArea(double x) {
        for (RestrictedArea restrictedArea: this.getRestrictedAreas()) {
            if (x >= restrictedArea.getX() && x <= restrictedArea.getX() + restrictedArea.getWidthFactor()) {
                return true;
            }
        }
        return false;
    }  
    


    public void handleCutSizeTransformation(Cut selectedEntity, double width, double height)
    {
        if (selectedEntity != null) {
            if (selectedEntity instanceof Cut) {
                if (selectedEntity instanceof RestrictedArea) {
                    RestrictedArea restrictedArea = (RestrictedArea) selectedEntity;
                    this.handleRestrictedAreaSizeTranformation(restrictedArea, width, height);
                }

                if (selectedEntity instanceof Square) {
                    Square square = (Square) selectedEntity;
                    this.handleSquareSizeTranformation(square, width, height);
                }

                if (selectedEntity instanceof L) {
                    L l = (L) selectedEntity;
                    this.handleLSizeTransformation(l, width, height);
                }
            }
            this.cncController.pointReferenceManager.getReferencePoints();
        }
    }

    public void handleLSizeTransformation(L l, double width, double height){
        L futureLClone = l.clone();
        l.setWidthFactor2(width);
        l.setHeightFactor2(height);
        //faut checker si le truc est bon avant de l'appliquer
        if (isCutInsidePanel(futureLClone) && !isCutTouchingRestrictedArea(futureLClone)) {
            l.setWidthFactor2(width);
            l.setHeightFactor2(height);
        }
    }


    public void handleSquareSizeTranformation(Square square,double width, double height)
    {
        Square futureSquareClone = square.clone();
        futureSquareClone.setWidthFactor(width);
        futureSquareClone.setHeightFactor(height);
        //faut checker si le truc est bon avant de l'appliquer
        if (isCutInsidePanel(futureSquareClone) && !isCutTouchingRestrictedArea(futureSquareClone)) {
            square.setWidthFactor(width);
            square.setHeightFactor(height);
        }
    }


    public void handleRestrictedAreaSizeTranformation(RestrictedArea restrictedArea,double width, double height)
    {
        RestrictedArea futureRestrictedAreaClone = restrictedArea.clone();
        futureRestrictedAreaClone.setWidthFactor(width);
        futureRestrictedAreaClone.setHeightFactor(height);
        //faut checker si le truc est bon avant de l'appliquer
        if (isCutInsidePanel(restrictedArea) && !isCutTouchingOtherCut(futureRestrictedAreaClone)) {
            restrictedArea.setWidthFactor(width);
            restrictedArea.setHeightFactor(height);
        }
    }



    //handle entity movement
    public void handleCutMovement(Cut selectedEntity, double x, double y/*, UUID tool, int profondeur*/) {
        System.out.println("MOVING");
        //TODO: Change to a param?
        //InteractiveEntity selectedEntity = actionHistory.getSelectedInteractiveEntity();
        if (selectedEntity != null) {
            if (selectedEntity instanceof Cut) {
                //Cut selectedCut = (Cut) selectedEntity;
                if (selectedEntity instanceof VerticalLine) {
                    this.handleVerticalLineMovement((VerticalLine) selectedEntity, x);
                } else if (selectedEntity instanceof HorizontalLine) {
                    this.handleHorizontalLineMovement((HorizontalLine) selectedEntity, y);
                } else if (selectedEntity instanceof RestrictedArea) {
                    this.handleRestrictedAreaMovement((RestrictedArea) selectedEntity, x, y);
                } else if (selectedEntity instanceof Square) {
                    System.out.println("MOVING SQUARE at " + x + " " + y);
                    this.handleSquareMovement((Square) selectedEntity, x, y);
                } else if (selectedEntity instanceof L) {
                    this.handleLMove((L) selectedEntity, x, y);
                }
                updateCut(selectedEntity);
            }
        }
    }



    public void handleVerticalLineMovement(VerticalLine verticalLine, double x) {
        //VerticalLine verticalLine = (VerticalLine) actionHistory.getSelectedInteractiveEntity();
        VerticalLine futureVerticalLineClone = verticalLine.clone();
        futureVerticalLineClone.setX(x);
        //faut checker si le truc est bon avant de l'appliquer
        if (isCutInsidePanel(futureVerticalLineClone) && !isCutTouchingRestrictedArea(futureVerticalLineClone)) {
            verticalLine.setX(x);
            //this.cncController.updateInteractiveEntity(verticalLine);
            //this.updateCut(verticalLine);
        }
    }



    public void handleHorizontalLineMovement(HorizontalLine horizontalLine, double y) {
        //HorizontalLine horizontalLine = (HorizontalLine) actionHistory.getSelectedInteractiveEntity();
        HorizontalLine futureHorizontalLineClone = horizontalLine.clone();
        futureHorizontalLineClone.setY(y);
        //faut checker si le truc est bon avant de l'appliquer
        if (isCutInsidePanel(futureHorizontalLineClone) && !isCutTouchingRestrictedArea(futureHorizontalLineClone)) {
            horizontalLine.setY(y);
            //this.actionHistory.updateInteractiveEntity(horizontalLine);
            //this.updateCut(horizontalLine);
        }
    }

    public void handleRestrictedAreaMovement(RestrictedArea restrictedArea, double x, double y) {
        //System.out.println("handling restricted area");
        //RestrictedArea restrictedArea = (RestrictedArea) actionHistory.getSelectedInteractiveEntity();
        RestrictedArea futureRestrictedAreaClone = restrictedArea.clone();
        futureRestrictedAreaClone.setX(x);
        futureRestrictedAreaClone.setY(y);
        //faut checker si le truc est bon avant de l'appliquer
        if (isCutInsidePanel(futureRestrictedAreaClone) && !isCutTouchingOtherCut(futureRestrictedAreaClone)) {
            restrictedArea.setX(x);
            restrictedArea.setY(y);
            //this.actionHistory.updateInteractiveEntity(restrictedArea);
            //this.updateCut(restrictedArea);

        }
    }


    public void handleSquareMovement(Square square, double x, double y) {
        //Square square = (Square) actionHistory.getSelectedInteractiveEntity();
        Square futureSquareClone = square.clone();
        futureSquareClone.setX(x);
        futureSquareClone.setY(y);
        //faut checker si le truc est bon avant de l'appliquer
        if (isCutInsidePanel(futureSquareClone) && !isCutTouchingRestrictedArea(futureSquareClone)) {
            square.setX(x);
            square.setY(y);
            //this.updateCut(square);
            //this.actionHistory.updateInteractiveEntity(square);
            //this.updateCut();
        }
    }





    public void handleLMove(L lCut, double x, double y) {
        // Clone the L cut for testing movement
        L futureLCutClone = lCut.clone();
        futureLCutClone.setX(x);
        futureLCutClone.setY(y);

        // Get associated reference point and cutlines
        PointReference referencePoint = lCut.getReferencePoint();
        if (referencePoint == null) {
            System.out.println("L cut has no associated reference point.");
            return;
        }

        CutLine cutLine1 = referencePoint.getCutLineOrigin1();
        CutLine cutLine2 = referencePoint.getCutLineOrigin2();

        if (cutLine1 == null || cutLine2 == null) {
            System.out.println("Reference point is missing associated cutlines.");
            return;
        }

        // Determine the bounds of the cutlines
        List<Double> xCoords = Arrays.asList(
                cutLine1.getStart().getX(),
                cutLine1.getEnd().getX(),
                cutLine2.getStart().getX(),
                cutLine2.getEnd().getX()
        );
        List<Double> yCoords = Arrays.asList(
                cutLine1.getStart().getY(),
                cutLine1.getEnd().getY(),
                cutLine2.getStart().getY(),
                cutLine2.getEnd().getY()
        );

        double minX = Collections.min(xCoords);
        double maxX = Collections.max(xCoords);
        double minY = Collections.min(yCoords);
        double maxY = Collections.max(yCoords);

        // Check if the new position is within bounds
        if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
            // Further check if the new position is valid within panel and not restricted
            if (isCutInsidePanel(futureLCutClone) && !isCutTouchingRestrictedArea(futureLCutClone)) {
                // Apply the movement
                lCut.setX(x);
                lCut.setY(y);
                //this.updateCut(lCut);
                System.out.println("L cut moved successfully.");
            } else {
                System.out.println("Movement is not valid: Out of panel bounds or touching restricted area.");
            }
        } else {
            System.out.println("Movement is out of bounds of the associated cutlines.");
        }
    }


    










    public boolean isCutInsidePanel(Cut cut) {
        Panel panel = this.cncController.panelManager.panel;
        if (cut instanceof VerticalLine) {
            VerticalLine verticalLine = (VerticalLine) cut;
            return verticalLine.getX() >= panel.getX() && verticalLine.getX() <= panel.getX() + panel.getWidthFactor();
        } else if (cut instanceof HorizontalLine) {
            HorizontalLine horizontalLine = (HorizontalLine) cut;
            return horizontalLine.getY() >= panel.getY() && horizontalLine.getY() <= panel.getY() + panel.getHeightFactor();
        } else if (cut instanceof RestrictedArea) {
            RestrictedArea restrictedArea = (RestrictedArea) cut;
            return restrictedArea.getX() >= panel.getX() && restrictedArea.getX() + restrictedArea.getWidthFactor() <= panel.getX() + panel.getWidthFactor() && restrictedArea.getY() >= panel.getY() && restrictedArea.getY() + restrictedArea.getHeightFactor() <= panel.getY() + panel.getHeightFactor();
        } else if (cut instanceof Square) {
            Square square = (Square) cut;
            return square.getX() >= panel.getX() && square.getX() + square.getWidthFactor() <= panel.getX() + panel.getWidthFactor() && square.getY() >= panel.getY() && square.getY() + square.getHeightFactor() <= panel.getY() + panel.getHeightFactor();
        } else if (cut instanceof L) {
            L lCut = (L) cut;
            return lCut.getX() >= panel.getX() && lCut.getX() <= panel.getX() + panel.getWidthFactor() && lCut.getY() >= panel.getY() && lCut.getY() <= panel.getY() + panel.getHeightFactor();
        }
        return false;
    }



    //is touching any other cut
    public boolean isCutTouchingOtherCut(Cut cut) {
        List<Cut> cuts = this.getCuts();
        for (Cut otherCut : cuts) {
            if(cut.id != otherCut.id && cut.isTouching(otherCut))
            {
                System.out.println("Cut " + cut + " is touching " + otherCut);
                return true;
            }
        }
        return false;
    }


    //TODO: if used at all, should make sure to check what type first (make sure its a cut)
    public void deleteSelectedCut() {
        //Cut selectedCut = actionHistory.getSelectedCut();
        InteractiveEntity selectedCut = this.cncController.getSelectedInteractiveEntity();

        if (selectedCut != null) {
            if (selectedCut instanceof PointReference) {
                this.cncController.pointReferenceManager.deletePointReference((PointReference) selectedCut);
            } else if (selectedCut instanceof Cut) {
                this.deleteCut((Cut) selectedCut);
            }
            this.cncController.updateHistoryAfterClickRelease();
        }
    }



    //check if every cut is contained within a restricted area
    public void checkIfCutIsContainedWithinRestrictedArea() {
        List<RestrictedArea> restrictedAreas = getRestrictedAreas();
        for (Cut cut : cuts) {
            for (RestrictedArea restrictedArea : restrictedAreas) {
                if(cut.isTouching(restrictedArea))
                {
                    System.out.println("Cut " + cut + " is touching " + restrictedArea);
                }
            }
        }
    }




    //get every restricted area
    public List<RestrictedArea> getRestrictedAreas() {
        List<RestrictedArea> restrictedAreas = new ArrayList<>();
        for (Cut cut : cuts) {
            if (cut instanceof RestrictedArea) {
                restrictedAreas.add((RestrictedArea) cut);
            }
        }
        return restrictedAreas;
    }
    //check if one cut is touching another a restricted area
    public boolean isCutTouchingRestrictedArea(Cut cut) {
        List<RestrictedArea> restrictedAreas = getRestrictedAreas();
        for (RestrictedArea restrictedArea : restrictedAreas) {
            if(restrictedArea.isTouching(cut))
            {
                System.out.println("Cut " + cut + " is touching " + restrictedArea);
                return true;
            }
        }
        return false;
    }

}



