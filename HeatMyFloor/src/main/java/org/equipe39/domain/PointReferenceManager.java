package org.equipe39.domain;


import org.equipe39.domain.Cut.*;
import org.equipe39.domain.CutLine;
import org.equipe39.domain.PointReference;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class PointReferenceManager {
    private List<PointReference> pointReference;// = new ArrayList<>();
    public List<CutLine> cutLines;// = new ArrayList<>();
    private CNCController cncController;
    private  List<PointReference> temporaryClonePointReferenceForMovement = new ArrayList<>();

    public PointReferenceManager(CNCController cncController) {
        this.cncController = cncController;
        this.cutLines = new ArrayList<>();
        this.pointReference = new ArrayList<>();

        //this.cncController.cutManager.
        this.appendEveryCutLine();
        this.appendEveryCutLine();
        //this.cncController.updateAllAfterAnAction();
    }



    /*public void updateInteractiveEntity(InteractiveEntity cut) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).isSelected()) {
                entities.set(i, cut);
            }
        }
        updateAllAfterAnAction();
    }

    public List<InteractiveEntity> getInteractiveEntities()
    {
        return this.entities;
    }

    public void deleteCut(Cut cut) {
        entities.remove(cut);
        updateAllAfterAnAction();
    }*/



    //get point refernce associated with cuts
    /*public void getPointReferenceAssociatedWithCuts() {
        for (Cut cut: this.cncController.cutManager.getCuts()) {
            if (cut instanceof L) {
                L lCut = (L) cut;
                for (PointReference pointReference: pointReference) {
                    if (pointReference.equals(lCut.getReferencePoint())) {
                        lCut.setReferencePoint(pointReference);
                    }
                }
            }
            else if (cut instanceof Square) {
                Square square = (Square) cut;
                for (PointReference pointReference: pointReference) {
                    if (pointReference.equals(square.getReferencePoint())) {
                        square.setReferencePoint(pointReference);
                    }
                }
            }
        }
    }*/

   public void setTemporaryClonePointReferenceForMovement() {
        //this.temporaryClonePointReferenceForMovement = pointReference;
        //has to be cloned because the reference points are being modified
        this.temporaryClonePointReferenceForMovement = new ArrayList<>();
        for(PointReference pointRef : this.pointReference)
        {
            this.temporaryClonePointReferenceForMovement.add(pointRef.clone());
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<PointReference> getPointReferenceAssociatedWithCuts() {
        List<PointReference> pointReferenceAssociatedWithCuts = new ArrayList<>();
        for (Cut cut: this.cncController.cutManager.getCuts()) {
            if (cut instanceof L) {
                L lCut = (L) cut;
                pointReferenceAssociatedWithCuts.add(lCut.getReferencePoint());
            }
            else if (cut instanceof Square) {
                Square square = (Square) cut;
                pointReferenceAssociatedWithCuts.add(square.getReferencePoint());
            }
        }
        return pointReferenceAssociatedWithCuts;
        }


    public List <CutLine> appendEveryCutLine() {
        List <CutLine> cutLinesList = new ArrayList < > ();

        for (Cut cut: this.cncController.cutManager.getCuts()) {
            cutLinesList.addAll(cut.getCutLines());
        }

        // Add the panel's cut lines
        //cutLinesList.addAll(panel.getCutLines());
        cutLinesList.addAll(this.cncController.panelManager.panel.getCutLines());


        //System.out.println("CutLinesList: {");
        /*for (CutLine cutLine: cutLinesList) {
            System.out.println(cutLine);
        }*/
        //System.out.println("}");

        System.out.println("CUT LINE SIZE: " + cutLinesList.size());
        for(int i = 0; i < cutLinesList.size(); i++)
        {
            System.out.println(cutLinesList.get(i));
        }


        return cutLinesList;
    }

    public void setPointReference(List<PointReference> pointReference)
    {
        this.pointReference = pointReference;
    }

    public void deletePointReference(PointReference pointReference){
       this.cncController.cutManager.deleteCut((Cut) pointReference.getCutLineOrigin1().getCut());
       this.cncController.cutManager.deleteCut((Cut) pointReference.getCutLineOrigin2().getCut());
    }


    public void handlePointReferenceMovement(PointReference pointReference, double x, double y) {
        System.out.println("MOVING A POINT");
        //PointReference pointReference = (PointReference) actionHistory.getSelectedInteractiveEntity();
        List<InteractiveEntity> relatedCuts = new ArrayList<>();
        if(pointReference.getCutLineOrigin1().getCut() instanceof Panel)
        {
            relatedCuts.add((Cut) pointReference.getCutLineOrigin1().getCut());
        }
        if(pointReference.getCutLineOrigin2().getCut() instanceof Panel)
        {
            relatedCuts.add((Cut) pointReference.getCutLineOrigin2().getCut());
        }
        relatedCuts.add(pointReference.getCutLineOrigin1().getCut());
        relatedCuts.add(pointReference.getCutLineOrigin2().getCut());

        for(InteractiveEntity cut : relatedCuts)
        {
            this.cncController.cutManager.handleCutMovement((Cut) cut, x, y);
            //this.cncController.updateAllAfterAnAction();
        }
        //pointReference.setX(x);
        //pointReference.setY(y);
        //this.actionHistory.updateInteractiveEntity(pointReference);

        //if (isCutInsidePanel(pointReference)) {
        //    pointReference.setX(x);
        //    pointReference.setY(y);
        //    this.actionHistory.updateInteractiveEntity(pointReference);
        //}



        //pointReference.setX(x);
        //pointReference.setY(y);
        //this.actionHistory.updateInteractiveEntity(pointReference);
    }

    public List<PointReference> getPointReference() {
        return pointReference;
    }

    public void updateAllAfterAnAction() {
        //updateCuts();
        cutLines = appendEveryCutLine();
        getReferencePoints();
    }




    //EXTREMEMENT PLUS EFFICACE QUE L'AUTRE MAIS MARCHE A MOITIER
    //OK IL MARCHE MAINTENANT
    public void getReferencePoints() {
        List<PointReference> updatedPointReferences = new ArrayList<>();
        Set<String> processedPairs = new HashSet<>();

        int n = cutLines.size();
        System.out.println("REGENERATING REF POINTS, CURRENTLY " + this.cutLines.size() + " CUTLINES");
        for (int i = 0; i < n; i++) {
            CutLine cutLine1 = cutLines.get(i);
            for (int j = i + 1; j < n; j++) {
                CutLine cutLine2 = cutLines.get(j);

                if (cutLinesAreSame(cutLine1, cutLine2)) {
                    //System.out.println("Skipping pair: " + cutLine1 + " and " + cutLine2 + " because they are the same");
                    continue;
                }

                String pairKey = generatePairKey(cutLine1, cutLine2);
                if (processedPairs.contains(pairKey)) {
                    System.out.println("Skipping pair: " + pairKey + " because it was already processed");
                    continue;
                }
                processedPairs.add(pairKey);

                // Check for intersection
                Point2D.Double intersection = cutLine1.getIntersection(cutLine2);
                if (intersection != null) {
                    //if point reference already exists, update it by keeping the original id
                    boolean pointRefExists = false;
                    //TODO: rework this so its more effitient
                    for (PointReference pointReference : pointReference) {
                        //if (pointReference.equalsWithCutlines(new PointReference(intersection, cutLine1, cutLine2))) {
                        if (pointReference.equalsWithCutlines(new PointReference(intersection, cutLine1, cutLine2))) {
                            pointReference.setCutLineOrigin1(cutLine1);
                            pointReference.setCutLineOrigin2(cutLine2);
                            pointReference.setPoint(intersection);
                            updatedPointReferences.add(pointReference);

                            pointRefExists = true;
                            break;
                        }
                    }
                    if (!pointRefExists) {
                        //System.out.println("FOR THESE CUTLINES: " + cutLine1 + " " + cutLine2);
                        PointReference pointRef = new PointReference(intersection, cutLine1, cutLine2);
                        updatedPointReferences.add(pointRef);
                    }
                    //PointReference pointRef = new PointReference(intersection, cutLine1, cutLine2);
                    //updatedPointReferences.add(pointRef);
                }
                //System.out.println("no intersections for " + cutLine1 + " and " + cutLine2);
            }
        }

        this.pointReference = updatedPointReferences;
        this.referencePointRelatedEntityUpdate();
    }




    public void handleSquareMovementFromReferencePoint(Square square, PointReference pointReference)
    {
        //make it so that if the reference point is moved, the square moves with it in a way that if the reference point is moved from a complete end to another, the square will move along but the side that will touch the other end will be the other side at the exact moment that the reference point hits the panel wall
        //make sure that if theres no movement, the square stays in place and does not teleport anywhere


        double originalPointReferenceXBeforeDrag = 0;
        double originalPointReferenceYBeforeDrag = 0;
        double originalCutXBeforeDrag = 0;
        double originalCutYBeforeDrag = 0;
        //setTemporaryClonePointReferenceForMovement
        for(Cut pointRef : this.cncController.cutManager.temporaryCloneCutForMovement)
        {
            //if(pointRef.id == square.id)
            if(pointRef.id.equals(square.id))
            {
                Square temp =  (Square) pointRef;
                originalCutXBeforeDrag = temp.getX();
                originalCutYBeforeDrag = temp.getY();
            }
        }


        for(PointReference pointRef : this.temporaryClonePointReferenceForMovement)
        {
            if(pointRef.equals(pointReference))
            {
                originalPointReferenceXBeforeDrag = pointRef.getX() ;//+ originalCutXBeforeDrag;
                originalPointReferenceYBeforeDrag = pointRef.getY();
            }
        }

        double newValueX = (pointReference.getX() + (originalCutXBeforeDrag - originalPointReferenceXBeforeDrag));// * arrivalPercentage;
        double newValueY = (pointReference.getY() + (originalCutYBeforeDrag - originalPointReferenceYBeforeDrag));

        this.cncController.cutManager.handleSquareMovement(square, newValueX, newValueY);
    }


    /*
    public void handleLMovementFromReferencePoint(L lCut, PointReference pointReference)
    {
        System.out.println("");
        //lCut.updateFactors();

        double originalCutWidthBeforeDrag = 0;
        double originalCutHeightBeforeDrag = 0;
        //setTemporaryClonePointReferenceForMovement
        for(Cut pointRef : this.cncController.cutManager.temporaryCloneCutForMovement)
        {
            if(pointRef.id == lCut.id)
            {
                L temp =  (L) pointRef;
                originalCutWidthBeforeDrag = temp.getWidthFactor();
                originalCutHeightBeforeDrag = temp.getHeightFactor();
            }
        }

        System.out.println(originalCutHeightBeforeDrag + " " + originalCutHeightBeforeDrag);
        if(pointReference.getCutLineOrigin1().getCut() instanceof Square && pointReference.getCutLineOrigin2().getCut() instanceof Square )
        {
//
            System.out.println("IN A A SQUARE");
            Square square = (Square)  lCut.getReferencePoint().getCutLineOrigin2().getCut();
            //double newX = square.getX() - originalCutWidthBeforeDrag;
            //double newY = square.getY() - originalCutHeightBeforeDrag;
            //this.cncController.cutManager.handleLMove(lCut, pointReference.getX() + lCut.getWidthFactor(), pointReference.getY() + lCut.getHeightFactor());
        }
        if( pointReference.getCutLineOrigin1().getCut() instanceof L && pointReference.getCutLineOrigin2().getCut() instanceof L)
        {
            System.out.println("IN A A L");
            //lCut.updateFactors();
        }
        else
        {
            System.out.println("IN something else");
            double panelWidth = this.cncController.panelManager.panel.getWidthFactor();
            double panelHeight = this.cncController.panelManager.panel.getWidthFactor();
            double panelPercentageX = pointReference.getX() / panelWidth;
            double panelPercentageY = pointReference.getY() / (panelHeight / 2);
            double changeX = (lCut.getX() + pointReference.getX()) * panelPercentageX;
            double changeY = (lCut.getY() + pointReference.getY()) * panelPercentageY;

            //double panelWidth = this.cncController.panelManager.panel.getWidthFactor();
            //double panelHeight = this.cncController.panelManager.panel.getWidthFactor();
            //double panelPercentageX = pointReference.getX() / panelWidth;
            //double panelPercentageY = pointReference.getY() / panelHeight;
            ////double changeX = (lCut.getX()) + panelPercentageX;
            ////double changeY = (lCut.getY()) + panelPercentageY;
            //double changeX = ((pointReference.getX() * panelPercentageX) + (lCut.getX() - lCut.getReferencePoint().getX()));


            this.cncController.cutManager.handleLMove(lCut, changeX, changeY);
            //movement of the line * completion percentage
            //lCut.updateFactors();
        }

        //if inside squre, stay static.


        //else move mirrored
    }

     */

    public void handleLMovementFromReferencePoint(L lCut, PointReference pointReference) {
        double originalCutWidthBeforeDrag = 0;
        double originalCutHeightBeforeDrag = 0;

        // Get the original dimensions of the L from the temporary clone
        for (Cut pointRef : this.cncController.cutManager.temporaryCloneCutForMovement) {
            //if (pointRef.id == lCut.id) {
            if (pointRef.id.equals(lCut.id)) {
                L temp = (L) pointRef;
                originalCutWidthBeforeDrag = temp.getWidthFactor();
                originalCutHeightBeforeDrag = temp.getHeightFactor();
                break;
            }
        }

        // Retrieve panel dimensions
        double panelWidth = this.cncController.panelManager.panel.getWidthFactor();
        double panelHeight = this.cncController.panelManager.panel.getWidthFactor();

        double refX = pointReference.getX();
        double refY = pointReference.getY();

        // Midpoints to determine "opposite" corners or ends
        double midpointX = panelWidth / 2.0;
        double midpointY = panelHeight / 2.0;

        InteractiveEntity parentCut1 = pointReference.getCutLineOrigin1().getCut();
        InteractiveEntity parentCut2 = pointReference.getCutLineOrigin2().getCut();

        boolean parentIsSquarePair = (parentCut1 instanceof Square && parentCut2 instanceof Square);
        boolean parentIsLPair = (parentCut1 instanceof L && parentCut2 instanceof L);

        if (parentIsSquarePair) {
            // Example logic:
            // If originally top-left quadrant and now bottom-right quadrant (or vice versa),
            // we consider it "opposite corner" and flip the L.

            // Determine old quadrant (from original reference point)
            // For simplicity, assume the original reference point was near (0,0)
            // and L was in the top-left corner initially. You should store the original
            // reference if needed.
            boolean wasTopLeft = (lCut.getX() < midpointX && lCut.getY() < midpointY);

            // Determine new quadrant
            boolean nowBottomRight = (refX > midpointX && refY > midpointY);

            // If we've moved diagonally across, flip the L
            if (wasTopLeft && nowBottomRight) {
                lCut.setWidthFactor(-originalCutWidthBeforeDrag);
                lCut.setHeightFactor(-originalCutHeightBeforeDrag);
            } else {
                // No flip needed, just keep original factors
                lCut.setWidthFactor(originalCutWidthBeforeDrag);
                lCut.setHeightFactor(originalCutHeightBeforeDrag);
            }

            double newX = refX + lCut.getWidthFactor();
            double newY = refY + lCut.getHeightFactor();

            Square square = ((Square) pointReference.getCutLineOrigin1().getCut());
            if(square.getWidthFactor() < Math.abs(lCut.getWidthFactor()))
            {
                System.out.println("HIT");
                lCut.setWidthFactor2(square.getWidthFactor());
            }
            if(square.getHeightFactor() < Math.abs(lCut.getHeightFactor()))
            {
                System.out.println("HIT");
                lCut.setHeightFactor2(square.getHeightFactor());
            }

            this.cncController.cutManager.handleLMove(lCut, newX, newY);


        } else if (parentIsLPair) {
            // If the L is inside another L, we shrink/resize according to the parent's cutlines.
            // We need the parent's original and new dimensions. Let's assume we find them similarly
            // from the temporary clones. We'll just show the logic here:

            double parent1OriginalWidth = 0, parent1OriginalHeight = 0;
            double parent2OriginalWidth = 0, parent2OriginalHeight = 0;

            double parent1NewWidth = ((L)parentCut1).getWidthFactor();
            double parent1NewHeight = ((L)parentCut1).getHeightFactor();
            double parent2NewWidth = ((L)parentCut2).getWidthFactor();
            double parent2NewHeight = ((L)parentCut2).getHeightFactor();

            // Retrieve parent's original dimensions from the temporary clones as well
            for (Cut c : this.cncController.cutManager.temporaryCloneCutForMovement) {
                //if (c.id == parentCut1.id && c instanceof L) {
                if (c.id.equals(parentCut1.id) && c instanceof L) {
                    L tempParent1 = (L) c;
                    parent1OriginalWidth = tempParent1.getWidthFactor();
                    parent1OriginalHeight = tempParent1.getHeightFactor();
                }
                //if (c.id == parentCut2.id && c instanceof L) {
                if (c.id.equals(parentCut2.id) && c instanceof L) {
                    L tempParent2 = (L) c;
                    parent2OriginalWidth = tempParent2.getWidthFactor();
                    parent2OriginalHeight = tempParent2.getHeightFactor();
                }
            }

            // Compute scaling factors based on parent's dimension changes
            // This is an arbitrary approach: average the scale factors of the two parent L lines
            double scaleX = ((parent1NewWidth / (parent1OriginalWidth == 0 ? 1 : parent1OriginalWidth))
                    + (parent2NewWidth / (parent2OriginalWidth == 0 ? 1 : parent2OriginalWidth))) / 2.0;
            double scaleY = ((parent1NewHeight / (parent1OriginalHeight == 0 ? 1 : parent1OriginalHeight))
                    + (parent2NewHeight / (parent2OriginalHeight == 0 ? 1 : parent2OriginalHeight))) / 2.0;

            // Apply scale to child's L
            lCut.setWidthFactor(originalCutWidthBeforeDrag * scaleX);
            lCut.setHeightFactor(originalCutHeightBeforeDrag * scaleY);

            // Position the child L at the reference point plus scaled factors
            double newX = refX + lCut.getWidthFactor();
            double newY = refY + lCut.getHeightFactor();
            this.cncController.cutManager.handleLMove(lCut, newX, newY);

        } else {
            lCut.setWidthFactor(originalCutWidthBeforeDrag);
            lCut.setHeightFactor(originalCutHeightBeforeDrag);

            double newX = refX + lCut.getWidthFactor();
            double newY = refY + lCut.getHeightFactor();

            this.cncController.cutManager.handleLMove(lCut, newX, newY);
        }
    }



    public void referencePointRelatedEntityUpdate() {
        //check for deletion
        ArrayList<Cut> toDelete = new ArrayList<>();
        for(Cut cut : this.cncController.cutManager.getCuts())
        {
            //if the cut is an L or a square, check if the reference points are still present in the point reference list
            if(cut instanceof L)
            {
                L lCut = (L) cut;
                toDelete.add(lCut);
                for(PointReference pointReference : this.pointReference)
                {
                    if(pointReference.equals(lCut.getReferencePoint()))
                    {
                        //TODO: handle movement
                        toDelete.remove(lCut);
                        System.out.println("WE CAN KEEP " + lCut);
                        if(pointReference.getX() != lCut.getReferencePoint().getX() || pointReference.getY() != lCut.getReferencePoint().getY())
                        {
                            this.handleLMovementFromReferencePoint(lCut, pointReference);
                        }
                        lCut.setReferencePoint(pointReference.clone());
                        break;
                    }

                }

            }
            else if(cut instanceof Square)
            {    
                Square square = (Square) cut;
                toDelete.add(square);
                for(PointReference pointReference : this.pointReference)
                {
                    if(pointReference.equals(square.getReferencePoint()))
                    {
                        toDelete.remove(square);
                        if(pointReference.getX() != square.getReferencePoint().getX() || pointReference.getY() != square.getReferencePoint().getY())
                        {
                            this.handleSquareMovementFromReferencePoint(square, pointReference);
                        }
                        square.setReferencePoint(pointReference.clone());
                        //this.cncController.cutManager.handleCutMovement();
                        break;
                    }
                }

            }
        }
        for(Cut cut : toDelete)
        {
            //this.entities.remove(cut);

            this.cncController.cutManager.deleteCut(cut);
        }
        if(toDelete.isEmpty())
        {
            //updateHistoryAfterClickRelease();
            return;
        }
        this.cncController.updateAllAfterAnAction();
    }




    // Helper method to determine if two CutLines are the same
    private boolean cutLinesAreSame(CutLine cl1, CutLine cl2) {
        return cl1.getId() == cl2.getId() && Objects.equals(cl1.getCut(), cl2.getCut());
    }

    private String generatePairKey(CutLine cl1, CutLine cl2) {
        int hash1 = cl1.hashCode();
        int hash2 = cl2.hashCode();
        if (hash1 < hash2) {
            return hash1 + "_" + hash2;
        } else {
            return hash2 + "_" + hash1;
        }
    }

    public void addPointReference(PointReference pointReference) {
        this.pointReference.add(pointReference);
    }



}