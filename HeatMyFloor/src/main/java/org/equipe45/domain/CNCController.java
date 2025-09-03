package org.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CNCController {

    public void addCut(int x, int y, String selectedCuttingMethod, int depth, int thickness) {
        System.out.println("test");
    }

    private List < Outil > outils;
    private List < Point > clickedPointSequence;
    public Point startPoint;
    public Point endPoint;
    //actionHistory
    public History actionHistory; // = new History();
    public boolean isDragging = false;
    public Panel panel;
    public List < PointReference > pointReference = new ArrayList < > ();
    public List <InteractiveEntity> interactiveEntities = new ArrayList < > ();

    public CNCController() {
        System.out.println("test");
        this.actionHistory = new History();
        this.clickedPointSequence = new ArrayList < > ();
        this.outils = new ArrayList < > ();
        this.panel = new Panel(0, 0, ConversionPiedMM.convertirPiedEnMillimetres(6), ConversionPiedMM.convertirPiedEnMillimetres(3) /*,30*/ );
        this.actionHistory.setPanel(this.panel);
        //this.outils.add(new Outil(UUID.randomUUID(), "outil1", 1));
    }

    public void changePanelSize(int width, int height) {
        // Ensure dimensions are valid
        if (width > 0 && height > 0) {
            // Convert to millimeters
            double adjustedWidth = ConversionPiedMM.convertirPiedEnMillimetres(width);
            double adjustedHeight = ConversionPiedMM.convertirPiedEnMillimetres(height);

            // Update panel size
            this.panel.changeSize(adjustedWidth, adjustedHeight);

            // Adjust cuts that are out of bounds
            for (Cut cut : actionHistory.getCuts()) {
                if (cut instanceof VerticalLine) {
                    VerticalLine verticalLine = (VerticalLine) cut;
                    // If the line's X-coordinate is outside the panel, move it back inside
                    if (verticalLine.getX() < panel.getX()) {
                        verticalLine.setX((int) panel.getX());
                    } else if (verticalLine.getX() > panel.getX() + adjustedWidth) {
                        verticalLine.setX((int) (panel.getX() + adjustedWidth));
                    }
                } else if (cut instanceof HorizontalLine) {
                    HorizontalLine horizontalLine = (HorizontalLine) cut;
                    // If the line's Y-coordinate is outside the panel, move it back inside
                    if (horizontalLine.getY() < panel.getY()) {
                        horizontalLine.setY((int) panel.getY());
                    } else if (horizontalLine.getY() > panel.getY() + adjustedHeight) {
                        horizontalLine.setY((int) (panel.getY() + adjustedHeight));
                    }
                } else if (cut instanceof RestrictedArea) {
                    RestrictedArea restrictedArea = (RestrictedArea) cut;
                    // Adjust the position and size of the restricted area to fit within bounds
                    if (restrictedArea.getX() < panel.getX()) {
                        restrictedArea.setX((int) panel.getX());
                    }
                    if (restrictedArea.getY() < panel.getY()) {
                        restrictedArea.setY((int) panel.getY());
                    }
                    if (restrictedArea.getX() + restrictedArea.getWidthFactor() > panel.getX() + adjustedWidth) {
                        restrictedArea.setWidthFactor(adjustedWidth - (restrictedArea.getX() - panel.getX()));
                    }
                    if (restrictedArea.getY() + restrictedArea.getHeightFactor() > panel.getY() + adjustedHeight) {
                        restrictedArea.setHeightFactor(adjustedHeight - (restrictedArea.getY() - panel.getY()));
                    }
                } else if (cut instanceof Square) {
                    Square square = (Square) cut;
                    // Adjust the position and size of the square to fit within bounds
                    if (square.getX() < panel.getX()) {
                        square.setX((int) panel.getX());
                    }
                    if (square.getY() < panel.getY()) {
                        square.setY((int) panel.getY());
                    }
                    if (square.getX() + square.getWidthFactor() > panel.getX() + adjustedWidth) {
                        square.setWidthFactor(adjustedWidth - (square.getX() - panel.getX()));
                    }
                    if (square.getY() + square.getHeightFactor() > panel.getY() + adjustedHeight) {
                        square.setHeightFactor(adjustedHeight - (square.getY() - panel.getY()));
                    }
                } else if (cut instanceof L) {
                    L lCut = (L) cut;
                    // Adjust the L cut's position to fit within bounds
                    if (lCut.getX() < panel.getX()) {
                        lCut.setX((int) panel.getX());
                    }
                    if (lCut.getY() < panel.getY()) {
                        lCut.setY((int) panel.getY());
                    }
                    if (lCut.getX() > panel.getX() + adjustedWidth) {
                        lCut.setX((int) (panel.getX() + adjustedWidth));
                    }
                    if (lCut.getY() > panel.getY() + adjustedHeight) {
                        lCut.setY((int) (panel.getY() + adjustedHeight));
                    }
                }
            }

            System.out.println("Panel size updated and cuts adjusted to fit within bounds.");
        }
    }


    //getOutilDTOById
    public OutilDTO getOutilDTOById(UUID id) {
        for (Outil outil: outils) {
            if (outil.getId() == id) {
                return new OutilDTO(outil.getId(), outil.getName(), outil.getProfondeur());
            }
        }
        return null;
    }

/*
    public void controllerClick(int x, int y, String selectedCuttingMethod, UUID tool, int thickness, double gridSize) {

        System.out.println("clicked");
        Point clickedPoint = new Point(x, y);
        //System.out.println(clickedPoint);
        //Cut selectedCut = actionHistory.getSelectedCut();

        //Cut selectedCut = actionHistory.getSelectedInteractiveEntity();
        InteractiveEntity selectedCut = actionHistory.getSelectedInteractiveEntity();

        if (selectedCut == null) {
            //Cut clickedCut = actionHistory.isClickingSomething(clickedPoint);
            InteractiveEntity clickedCut = actionHistory.isClickingSomething(clickedPoint);
            if (clickedCut != null) {
                System.out.println("nothing selected, something clicked");
                clickedCut.toggleSelection();
                //gridPanel.repaint();
            } else {
                System.out.println("nothing selected, nothing clicked");
                if (isClickInsidePanel(x, y)) {


                    if (selectedCuttingMethod.equals("cutterVertical") || selectedCuttingMethod.equals("cutterHorizontal")) {
                        if (selectedCuttingMethod.equals("cutterVertical")) {

                            System.out.println("cutterVertical");
                            int lineX = (int)((x / gridSize) * gridSize);

                            VerticalLine verticalLine = new VerticalLine(lineX, Color.BLUE, thickness, tool);
                            //actionHistory.addCut(verticalLine);
                            actionHistory.addCut(verticalLine);

                        }
                        if (selectedCuttingMethod.equals("cutterHorizontal")) {
                            System.out.println("cutterHorizontal");
                            int lineY = (int)((y / gridSize) * gridSize);

                            HorizontalLine horizontalLine = new HorizontalLine(lineY, Color.BLUE, thickness, tool);
                            actionHistory.addCut(horizontalLine);
                        }
                    } else {
                        if (clickedPointSequence.isEmpty()) {
                            clickedPointSequence.add(new Point(x, y));
                        } else {

                            clickedPointSequence.add(new Point(x, y));

                            //if its restricted area
                            if (selectedCuttingMethod.equals("restrictedArea")) {
                                System.out.println("restrictedArea" + "oijawsdoijasodijasodijasoidjasoidjasoijd");
                                int x1 = (int)((clickedPointSequence.get(0).x));
                                int y1 = (int)((clickedPointSequence.get(0).y));
                                int x2 = (int)((clickedPointSequence.get(1).x));
                                int y2 = (int)((clickedPointSequence.get(1).y));
                                double widthFactor = (double)(x2 - x1);
                                double heightFactor = (double)(y2 - y1);

                                RestrictedArea restrictedArea = new RestrictedArea(x1, y1, widthFactor, heightFactor, Color.RED);
                                actionHistory.addCut(restrictedArea);
                            }
                            clickedPointSequence.clear();


                        }
                    }
                }

            }

        } else {
            //Cut clickedCut = actionHistory.isClickingSomething(clickedPoint);
            InteractiveEntity clickedCut = actionHistory.isClickingSomething(clickedPoint);

            if (clickedCut != null) {
                if (clickedCut.id == selectedCut.id) {
                    System.out.println("something selected, same thing clicked");
                } else {
                    System.out.println("something selected, another thing clicked");
                    selectedCut.toggleSelection();
                    clickedCut.toggleSelection();
                }
            } else {
                System.out.println("something selected, nothing clicked");
                selectedCut.toggleSelection();
            }
        }
    }*/



    public void controllerClick(int x, int y, String selectedCuttingMethod, UUID tool, int thickness) {
        System.out.println("clicked");
        Point clickedPoint = new Point(x, y);
        InteractiveEntity selectedCut = actionHistory.getSelectedInteractiveEntity();
        InteractiveEntity clickedEntity = actionHistory.isClickingSomething(clickedPoint);

        if (selectedCut == null) {
            if (clickedEntity != null) {
                System.out.println("Entity clicked: toggling selection");
                clickedEntity.toggleSelection();
            } else {
                System.out.println("New cut creation started.");
                handleCutCreation(x, y, selectedCuttingMethod, tool, thickness);
            }
        } else {
            if (clickedEntity != null) {
                if (clickedEntity.id.equals(selectedCut.id)) {
                    System.out.println("Selected entity re-clicked.");
                } else {
                    System.out.println("Switched selection to a different entity.");
                    selectedCut.toggleSelection();
                    clickedEntity.toggleSelection();
                }
            } else {
                System.out.println("Deselected entity.");
                selectedCut.toggleSelection();
            }
        }
    }

    private void handleCutCreation(int x, int y, String selectedCuttingMethod, UUID tool, int thickness) {
        if (!isClickInsidePanel(x, y)) {
            System.out.println("Click outside panel, ignoring.");
            return;
        }

        if (selectedCuttingMethod.equals("cutterVertical")) {
            createVerticalLine(x, tool, thickness);
        } else if (selectedCuttingMethod.equals("cutterHorizontal")) {
            createHorizontalLine(y, tool, thickness);
        } else if (selectedCuttingMethod.equals("restrictedArea")) {
            handleRestrictedAreaCreation(x, y);
        } else if (selectedCuttingMethod.equals("square")) {
            handleSquareCreation(x, y, tool, thickness);
        } else if (selectedCuttingMethod.equals("L")) {
            handleLCreation(x, y, tool, thickness);
        }
    }

    private void createVerticalLine(int x, UUID tool, int thickness) {
        System.out.println("Creating vertical line");
        int lineX = (int) ((x));
        VerticalLine verticalLine = new VerticalLine(lineX, Color.BLUE, thickness, tool);
        actionHistory.addCut(verticalLine);
        System.out.println("Vertical line created and added to history.");
    }

    private void createHorizontalLine(int y, UUID tool, int thickness) {
        System.out.println("Creating horizontal line");
        int lineY = (int) (y);
        HorizontalLine horizontalLine = new HorizontalLine(lineY, Color.BLUE, thickness, tool);
        actionHistory.addCut(horizontalLine);
        System.out.println("Horizontal line created and added to history.");
    }

    private void handleRestrictedAreaCreation(int x, int y) {
        if (clickedPointSequence.isEmpty()) {
            clickedPointSequence.add(new Point(x, y));
            System.out.println("First point for restricted area recorded.");
        } else {
            clickedPointSequence.add(new Point(x, y));
            System.out.println("Second point for restricted area recorded.");

            Point firstPoint = clickedPointSequence.get(0);
            Point secondPoint = clickedPointSequence.get(1);

            // Calculate width and height based on the two points
            int x1 = Math.min(firstPoint.x, secondPoint.x);
            int y1 = Math.min(firstPoint.y, secondPoint.y);
            double widthFactor = Math.abs(secondPoint.x - firstPoint.x);
            double heightFactor = Math.abs(secondPoint.y - firstPoint.y);

            RestrictedArea restrictedArea = new RestrictedArea(x1, y1, widthFactor, heightFactor, Color.RED);
            actionHistory.addCut(restrictedArea);

            clickedPointSequence.clear();
            System.out.println("Restricted area created and added to history.");
        }
    }


    private void handleSquareCreation(int x, int y, UUID tool, int thickness) {
        if (clickedPointSequence.size() == 1) {
            // Second click anywhere within bounds
            clickedPointSequence.add(new Point(x, y));
            System.out.println("Second click recorded for square.");
        } else if (clickedPointSequence.size() == 2) {
            // Third click defines width and height
            clickedPointSequence.add(new Point(x, y));
            System.out.println("Third click recorded for square.");

            Point firstClick = clickedPointSequence.get(0);
            Point secondClick = clickedPointSequence.get(1);
            Point thirdClick = clickedPointSequence.get(2);

            // Calculate dimensions
            double widthFactor = Math.abs(thirdClick.x - secondClick.x);
            double heightFactor = Math.abs(thirdClick.y - secondClick.y);

            // Create the Square
            PointReference referencePoint = (PointReference) actionHistory.isClickingSomething(firstClick);
            Square square = new Square(secondClick.x, secondClick.y, widthFactor, heightFactor, Color.GREEN, tool, thickness, referencePoint);
            actionHistory.addCut(square);

            clickedPointSequence.clear();
            System.out.println("Square created and added to history.");
        }
    }

    private void handleLCreation(int x, int y, UUID tool, int thickness) {
        if (clickedPointSequence.size() == 1) {
            // Second click anywhere within bounds
            clickedPointSequence.add(new Point(x, y));

            Point firstClick = clickedPointSequence.get(0);
            Point secondClick = clickedPointSequence.get(1);

            // Create the L cut
            PointReference referencePoint = (PointReference) actionHistory.isClickingSomething(firstClick);
            if (referencePoint != null) {
                L lCut = new L(secondClick.x, secondClick.y, Color.BLUE, tool, thickness, referencePoint);
                actionHistory.addCut(lCut);

                clickedPointSequence.clear();
                System.out.println("L cut created and added to history.");
            } else {
                System.out.println("First click must be on a reference point for an L cut.");
                clickedPointSequence.clear();
            }
        }
    }


    //TODO: REWORK
    public boolean isClickInsidePanel(int x, int y) {

        if (x >= panel.getX() && x <= panel.getX() + panel.getWidthFactor() && y >= panel.getY() && y <= panel.getY() + panel.getHeightFactor()) {
            return true;
        }

        /*System.out.println(x + "," + y);
        System.out.println(panel.getWidthFactor());
        if (x >= panel.getX() && x <= panel.getX() + (panel.getWidthFactor() * 30) && y >= panel.getY() && y <= panel.getY() + (panel.getHeightFactor() * 30))
        {
            return true;
        }
        System.out.println("not clicked inside");
        return false;*/
        return false;
    }

    public PanelDTO getPanelDTO() {
        return new PanelDTO(this.panel.getX(), this.panel.getY(), this.panel.getWidthFactor(), this.panel.getHeightFactor() /*, this.panel.getInitialGridSize()*/ );
    }


    public void controllerDrag(int x, int y, String selectedCuttingMethod, int depth, int thickness) {
        System.out.println("test");


        //Cut selectedCut = actionHistory.getSelectedCut();<
        InteractiveEntity selectedCut = actionHistory.getSelectedInteractiveEntity();
        if (selectedCut != null) {
            if (selectedCut instanceof VerticalLine) {
                if (this.isClickInsidePanel(x, y)) {
                    VerticalLine verticalLine = (VerticalLine) selectedCut;
                    verticalLine.move(x);
                    //this.actionHistory.updateCut(verticalLine);
                    this.actionHistory.updateInteractiveEntity(verticalLine);
                }
                if (selectedCut instanceof HorizontalLine) {
                    if (this.isClickInsidePanel(x, y)) {
                        HorizontalLine horizontalLine = (HorizontalLine) selectedCut;
                        horizontalLine.move(y);
                        //this.actionHistory.updateCut(horizontalLine);
                        this.actionHistory.updateInteractiveEntity(horizontalLine);
                    }
                }

            }
        } else {


        }


    }

    public void updateSelectedCutFromDTO(CutDTO cutDTO) {
        //Cut selectedCut = actionHistory.getSelectedCut();
        InteractiveEntity selectedCut = actionHistory.getSelectedInteractiveEntity();
        if (selectedCut instanceof VerticalLine && cutDTO instanceof VerticalLineDTO) {
            VerticalLine verticalLine = (VerticalLine) selectedCut;
            //TODO: fix
            //verticalLine.setDepth(((VerticalLineDTO) cutDTO).depth);
            verticalLine.setThickness(((VerticalLineDTO) cutDTO).thickness);
            verticalLine.setX(((VerticalLineDTO) cutDTO).x);
            //System.out.println(((VerticalLineDTO) cutDTO).x);
        }
        if (selectedCut instanceof HorizontalLine && cutDTO instanceof HorizontalLineDTO) {
            HorizontalLine horizontalLine = (HorizontalLine) selectedCut;

            horizontalLine.setThickness(((HorizontalLineDTO) cutDTO).thickness);
            horizontalLine.setY(((HorizontalLineDTO) cutDTO).y);
        }
        if (selectedCut instanceof RestrictedArea && cutDTO instanceof RestrictedAreaDTO) {
            RestrictedArea restrictedArea = (RestrictedArea) selectedCut;
            restrictedArea.setColor(((RestrictedAreaDTO) cutDTO).color);
            restrictedArea.setHeightFactor(((RestrictedAreaDTO) cutDTO).heightFactor);
            restrictedArea.setWidthFactor(((RestrictedAreaDTO) cutDTO).widthFactor);
            restrictedArea.setX(((RestrictedAreaDTO) cutDTO).x);
            restrictedArea.setY(((RestrictedAreaDTO) cutDTO).y);
        }
        if (selectedCut instanceof Square && cutDTO instanceof SquareDTO) {
            Square square = (Square) selectedCut;
            square.setColor(((SquareDTO) cutDTO).color);
            square.setHeightFactor(((SquareDTO) cutDTO).heightFactor);
            square.setWidthFactor(((SquareDTO) cutDTO).widthFactor);
            square.setX(((SquareDTO) cutDTO).x);
            square.setY(((SquareDTO) cutDTO).y);
        }


    }


    public void deleteSelectedCut() {
        //Cut selectedCut = actionHistory.getSelectedCut();
        InteractiveEntity selectedCut = actionHistory.getSelectedInteractiveEntity();

        if (selectedCut != null) {
            actionHistory.deleteCut((Cut) selectedCut);
        }
    }


    public void controllerPress(int x, int y, String selectedCuttingMethod, int depth, int thickness) {
        InteractiveEntity selectedCut = actionHistory.getSelectedInteractiveEntity();

        //Cut selectedCut = actionHistory.getSelectedCut();
        if (selectedCut != null) {
            if (selectedCut != null) {
                if (selectedCut instanceof VerticalLine) {
                    VerticalLine verticalLine = (VerticalLine) selectedCut;
                }
            }
        }

    }


    public List <InteractiveEntityDTO> getHistoryInDTO() {
        List <InteractiveEntityDTO> cutDTOList = new ArrayList < > ();
        for (InteractiveEntity cut: actionHistory.getInteractiveEntities()) {
            cutDTOList.add( cut.toDTO());
            //if (cut instanceof VerticalLine) {
            //   VerticalLine verticalLine = (VerticalLine) cut;
            //   cutDTOList.add(verticalLine.toDTO());
            //System.out.println(verticalLine);
            //}
        }
        return cutDTOList;
    }
    /*public List < CutDTO > getHistoryInDTO() {
        List < CutDTO > cutDTOList = new ArrayList < > ();
        for (InteractiveEntity cut: actionHistory.getInteractiveEntities()) {
            cutDTOList.add((CutDTO) cut.toDTO());
            //if (cut instanceof VerticalLine) {
             //   VerticalLine verticalLine = (VerticalLine) cut;
             //   cutDTOList.add(verticalLine.toDTO());
                //System.out.println(verticalLine);
            //}
        }
        return cutDTOList;
    }*/

    public InteractiveEntityDTO getSelectedCut() {
        //Cut selectedCut = actionHistory.getSelectedCut();
        InteractiveEntity selectedCut = actionHistory.getSelectedInteractiveEntity();

        if (selectedCut != null) {
            return selectedCut.toDTO();
        }


        /*if (selectedCut != null) {
            if (selectedCut instanceof VerticalLine) {
                VerticalLine verticalLine = (VerticalLine) selectedCut;
                return verticalLine.toDTO();
            }
        }*/
        return null;
    }


    //ADD OUTILDTO TO OUTILS
    public void addOutil(OutilDTO outilDTO) {
        outils.add(outilDTO.toOutil());

        System.out.println(outils);
    }

    //GET OUTILS IN DTO
    public List < OutilDTO > getOutilsInDTO() {
        List < OutilDTO > outilDTOList = new ArrayList < > ();
        for (Outil outil: outils) {
            System.out.println("testtttttttttttttttttt");
            outilDTOList.add(new OutilDTO(outil.getId(), outil.getName(), outil.getProfondeur()));
        }
        return outilDTOList;
    }



    public void controllerRelease(int x, int y, String selectedCuttingMethod, int depth, int thickness) {

        //Cut selectedCut = actionHistory.getSelectedCut();
        InteractiveEntity selectedCut = actionHistory.getSelectedInteractiveEntity();
        if (selectedCut != null) {
            if (selectedCut != null) {
                if (selectedCut instanceof VerticalLine) {
                    VerticalLine verticalLine = (VerticalLine) selectedCut;
                    isDragging = false;

                }
            }
        }

    }



/*
//update all interactive entities
    public void updateInteractiveEntitiesList() {
        //for every cut and every point reference but make sure that it is not already in the list

        //for (Cut cut: actionHistory.getCuts()) {
        for (InteractiveEntity cut: actionHistory.getInteractiveEntities()) {
            if (!interactiveEntities.contains(cut)) {
                interactiveEntities.add(cut);
            }
        }

        for (PointReference pointReference: pointReference) {
            if (!interactiveEntities.contains(pointReference)) {
                interactiveEntities.add(pointReference);
            }
        }
    }

*/





    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //reference points

    /*
    public class CutLines
    {
        private Point start;
        private Point end;
        private Cut cut;

        public CutLines(Point start, Point end, Cut cut)
        {
            this.start = start;
            this.end = end;
            this.cut = cut;
        }
    }


        private Point point;
        private UUID id;
        private CutLines cutLineOrigin1;
        private CutLines cutLineOrigin2;
        //private List<Cut> childCuts = new ArrayList<>();
        private static final int HITBOX_SIZE = 10;
        public PointReference(Point point, CutLines cutLineOrigin1, CutLines cutLineOrigin2) {
            this.id = UUID.randomUUID();
            this.cutLineOrigin1 = cutLineOrigin1;
            this.cutLineOrigin2 = cutLineOrigin2;
            this.point = point;
        }

        public boolean isHit(Point point) {
            return this.point.getX() - HITBOX_SIZE <= point.getX() && point.getX() <= this.point.getX() + HITBOX_SIZE
                    && this.point.getY() - HITBOX_SIZE <= point.getY() && point.getY() <= this.point.getY() + HITBOX_SIZE;
        }





    //for instance in the cut classes, theres this method
        @Override
        public List<CutLines> getCutLines() {
        }


    ////////////////////////////////////////////////////////////////////////////



     */

    //get all reference points
    //a reference points is a point that is created whenever the lines of two cuts




/*


    public List <CutLine> appendEveryCutLine() {
        List <CutLine> cutLinesList = new ArrayList < > ();
        for (Cut cut: actionHistory.getCuts()) {
            cutLinesList.addAll(cut.getCutLines());
        }
        return cutLinesList;
    }






    //first method should get every cuts, before using the

    public List < PointReference > getReferencePoints() {

        //Dont forget to check if one is already in the list, or if one has a reference to the same two cuts, or if one has a reference to the same two cuts but in reverse order or if an already existing point has a reference to a CutLine from a cut that seems to have been deleted

        List < PointReference > pointReferenceList = this.pointReference;

        for (Cut cut: actionHistory.getCuts()) {}




        return pointReferenceList;
    }


//get point reference by id
    public PointReference getPointReferenceById(UUID id) {
        for (PointReference pointReference: pointReference) {
            if (pointReference.getId() == id) {
                return pointReference;
            }
        }
        return null;
    }


//get point reference dto by id
    public PointReferenceDTO getPointReferenceDTOById(UUID id) {
        for (PointReference pointReference: pointReference) {
            if (pointReference.getId() == id) {
                return pointReference.toDTO();
            }
        }
        return null;
    }


    //get point reference list in DTO
    public List <PointReferenceDTO> getPointReferenceListInDTO() {
        List <PointReferenceDTO> pointReferenceDTOList = new ArrayList < > ();
        for (PointReference pointReference: pointReference) {
            pointReferenceDTOList.add(pointReference.toDTO());
        }
        return pointReferenceDTOList;
    }



    public boolean areCutLinesIntersecting(CutLine cutLines1, CutLine cutLines2) {
        //if the two lines are vertical
        if (cutLines1.getStart().getX() == cutLines1.getEnd().getX() && cutLines2.getStart().getX() == cutLines2.getEnd().getX()) {
            return false;
        }
        //if the two lines are horizontal
        if (cutLines1.getStart().getY() == cutLines1.getEnd().getY() && cutLines2.getStart().getY() == cutLines2.getEnd().getY()) {
            return false;
        }

        //if one line is vertical and the other is horizontal
        if (cutLines1.getStart().getX() == cutLines1.getEnd().getX() && cutLines2.getStart().getY() == cutLines2.getEnd().getY()) {
            return cutLines1.getStart().getX() >= cutLines2.getStart().getX() && cutLines1.getStart().getX() <= cutLines2.getEnd().getX() && cutLines2.getStart().getY() >= cutLines1.getStart().getY() && cutLines2.getStart().getY() <= cutLines1.getEnd().getY();
        }
        if (cutLines1.getStart().getY() == cutLines1.getEnd().getY() && cutLines2.getStart().getX() == cutLines2.getEnd().getX()) {
            return cutLines1.getStart().getY() >= cutLines2.getStart().getY() && cutLines1.getStart().getY() <= cutLines2.getEnd().getY() && cutLines2.getStart().getX() >= cutLines1.getStart().getX() && cutLines2.getStart().getX() <= cutLines1.getEnd().getX();
        }

        return false;

    }
}

*/
}



