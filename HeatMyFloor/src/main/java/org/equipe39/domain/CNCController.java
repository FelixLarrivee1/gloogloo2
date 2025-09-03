package org.equipe39.domain;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.equipe39.domain.Cut.*;
import org.equipe39.dto.*;

import javax.sound.sampled.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;

public class CNCController {

    private List < Outil > outils;
    public List < Point2D.Double > clickedPointSequence;
    //public Point startPoint;
    //public Point endPoint;
    //actionHistory
    //public History actionHistory; // = new History();
    public boolean isDragging = false;
    //public Panel panel;
    private String selectedCuttingMethod = "cutterRectangle";
    public Point2D.Double startPositionAfterDrag;
    private int currentHistoryIndex = 0;
    private List<InteractiveEntity> entities = new ArrayList<>();
    private List<List<InteractiveEntity>> entityHistory = new ArrayList<>();
    public CutManager cutManager; //= new CutController();
    public PointReferenceManager pointReferenceManager;
    public PanelManager panelManager;



    //public List < PointReference > pointReference = new ArrayList < > ();
    //public List <InteractiveEntity> interactiveEntities = new ArrayList < > ();
    public double adjustedWidth = 2438.4;
    public double adjustedHeight = 1219.2;

    public CNCController() {
        //this.actionHistory = new History();
        //this.actionHistory.setPanel(this.panel);
        //this.actionHistory.updateAllAfterAnAction();
        //this.outils.add(new Outil(UUID.randomUUID(), "outil1", 1));



        //this.panel = new Panel(0, 0, ConversionPiedMM.convertirPiedEnMillimetres(6), ConversionPiedMM.convertirPiedEnMillimetres(3), 0.5);
        this.panelManager = new PanelManager(this);
        this.outils = new ArrayList < > ();
        Outil defaultOutil = new Outil("Scie", 12.7);
        outils.add(defaultOutil);
        this.clickedPointSequence = new ArrayList < > ();
        this.entities = new ArrayList<>();
        this.cutManager = new CutManager(this);
        this.pointReferenceManager = new PointReferenceManager(this);

        this.entityHistory = new ArrayList<>();
        List<InteractiveEntity> firstIndex = new ArrayList<>();
        firstIndex.add(panelManager.panel);
        this.entityHistory.add(firstIndex);

        this.pointReferenceManager.appendEveryCutLine();
        this.pointReferenceManager.getReferencePoints();
    }

    /*public void changePanelSize(String width, String height) {
        adjustedWidth = ConversionPiedMM.convertirEnMillimetres(width);
        adjustedHeight = ConversionPiedMM.convertirEnMillimetres(height);
        System.out.println("im here" + (adjustedWidth > 0 && adjustedHeight > 0));
        //if (width > 0 && height > 0) {
        if (adjustedWidth > 0 && adjustedHeight > 0 && ConversionPiedMM.MillimetreEnPied(adjustedWidth) <= 10 && ConversionPiedMM.MillimetreEnPied(adjustedHeight) <= 5) {
            //double adjustedWidth = ConversionPiedMM.convertirPiedEnMillimetres(width);
            //double adjustedHeight = ConversionPiedMM.convertirPiedEnMillimetres(height);
            System.out.println("im here");
                    //truncate the decimals to 3
            //adjustedWidth = Math.round(adjustedWidth * 1000.0) / 1000.0;
            //adjustedHeight = Math.round(adjustedHeight * 1000.0) / 1000.0;
            this.panel.changeSize(adjustedWidth, adjustedHeight);


            System.out.println("Panel size updated and cuts adjusted to fit within bounds.");
        }
    }*/




    //getOutilDTOById
    public OutilDTO getOutilDTOById(UUID id) {
        for (Outil outil: outils) {
            if (outil.getId() == id) {
                return new OutilDTO(outil.getId(), outil.getName(), outil.getEpaisseur());
            }
        }
        return null;
    }





    //change epaisseur panel
    /*public void changeEpaisseurPanel(String epaisseur) {
        double adjustedEppaiseur =  ConversionPiedMM.convertirEnMillimetres(epaisseur);
        this.panelManager.setEpaisseur(adjustedEppaiseur);
        //update the outil list to set their max epaisseur to the new panel epaisseur if it exceeds it
        for (Outil outil: outils) {
            if(outil.getEpaisseur() > adjustedEppaiseur)
            {
                outil.setEpaisseur(adjustedEppaiseur);
            }
        }
        //todo: reajust every single cut that exceed it
    }*/


public void setSelectedCuttingMethod(String selectedCuttingMethod)
{
    if(selectedCuttingMethod != this.selectedCuttingMethod)
    {
        if(this.selectedCuttingMethod == "select")
        {
            InteractiveEntity potentialSelection = this.getSelectedInteractiveEntity();
            if(potentialSelection != null )
            {
                System.out.println("untoggled from switching tool");
                potentialSelection.toggleSelection();
            }
        }
        clickedPointSequence.clear();
    }
    this.selectedCuttingMethod = selectedCuttingMethod;
}


/*
    public void controllerClick(int x, int y, String selectedCuttingMethod, UUID tool, int profondeur, double gridSize) {

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

                            VerticalLine verticalLine = new VerticalLine(lineX, Color.BLUE, profondeur, tool);
                            //actionHistory.addCut(verticalLine);
                            actionHistory.addCut(verticalLine);

                        }
                        if (selectedCuttingMethod.equals("cutterHorizontal")) {
                            System.out.println("cutterHorizontal");
                            int lineY = (int)((y / gridSize) * gridSize);

                            HorizontalLine horizontalLine = new HorizontalLine(lineY, Color.BLUE, profondeur, tool);
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



   /*public void controllerClick(int x, int y, String selectedCuttingMethod, UUID tool, int profondeur) {
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
            handleCutCreation(x, y, selectedCuttingMethod, tool, profondeur);
            this.actionHistory.updateHistoryAfterClickRelease();
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
}*/
   public void controllerClick(double x, double y, String selectedCuttingMethod, Outil tool, double profondeur) {
       System.out.println("clicked");

       //System.out.println("RECTANGLE FINDER" + RectangleFinder.findRectanglesFromCutlines(this.actionHistory.appendEveryCutLine()));


       this.setSelectedCuttingMethod(selectedCuttingMethod);
       Point2D.Double clickedPoint = new Point2D.Double(x, y);
       if(selectedCuttingMethod == "select")
       {
            InteractiveEntity selectedCut = this.getSelectedInteractiveEntity();
            InteractiveEntity clickedEntity = this.isClickingSomething(clickedPoint);

            if (selectedCut == null) {
                if (clickedEntity != null) {
                    System.out.println("Entity clicked: toggling selection");
                    clickedEntity.toggleSelection();
                } else {
                    System.out.println("New cut creation started.");
                    //handleCutCreation(x, y, selectedCuttingMethod, tool, profondeur);
                    //this.actionHistory.updateHistoryAfterClickRelease();
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
       else {
           this.cutManager.handleCutCreation(x, y, selectedCuttingMethod, tool, profondeur);
          //this.uh();
       }
   }






    public void uh()
    {

        new Thread(() -> {
            try {
                String audioFilePath = "C:\\Users\\Rajeej\\Desktop\\LAVAL\\Gloo-gloo\\equipe39\\src\\main\\java\\org\\equipe39\\BruceDropEmOff goes off on fat women.wav";

                // Get the audio file
                File audioFile = new File(audioFilePath);

                // Create an AudioInputStream from the file
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                // Get a sound clip resource
                Clip clip = AudioSystem.getClip();

                // Open the clip with the audio input stream
                clip.open(audioStream);

                // Start playing the audio clip
                clip.start();

                // Optional: Wait until the audio is done playing
                Thread.sleep(clip.getMicrosecondLength() / 1000); // milliseconds

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                // Handle exceptions
                e.printStackTrace();
            }
        }).start(); // Start the thread to play audio

    }

    public Point2D.Double getCickedPointSequenceById(int id) {
        try {
            return clickedPointSequence.get(id);
        } catch (Exception e) {
            return null;
        }
    }



    //TODO: CHANGE TO RECENTRALIZE ARCHITECTURE
    public void controllerDrag(double x, double y/*, String selectedCuttingMethod, UUID tool, int profondeur*/) {
        InteractiveEntity selectedCut = this.getSelectedInteractiveEntity();
        //if(selectedCut instanceof VerticalLine || selectedCut instanceof HorizontalLine || selectedCut instanceof PointReference)
        //{
        //    this.handleEntityTransformation(selectedCut, x,y, 1, 1);
        //}
        //else
        //{
        //    if()
        //    this.handleEntityTransformation(selectedCut, x,y, 1, 1);
//
        //}
        this.handleEntityTransformation(selectedCut, x,y);

    }





    //handle entity movement
    public void handleEntityTransformation(InteractiveEntity selectedEntity, double x, double y) {
        //TODO: Change to a param?
        //InteractiveEntity selectedEntity = actionHistory.getSelectedInteractiveEntity();
        if (selectedEntity != null) {
            if (selectedEntity instanceof Cut) {
                this.cutManager.handleCutMovement((Cut) selectedEntity, x, y);
                //handle entity size tranformation
            }
            else if(selectedEntity instanceof PointReference) {
                this.pointReferenceManager.handlePointReferenceMovement((PointReference) selectedEntity, x, y);
            }
        }
    }





    

    public void updateSelectedCutFromDTO(CutDTO cutDTO) {
        //Cut selectedCut = actionHistory.getSelectedCut();
        InteractiveEntity selectedCut = this.getSelectedInteractiveEntity();
        if (selectedCut instanceof VerticalLine && cutDTO instanceof VerticalLineDTO) {
            VerticalLine verticalLine = (VerticalLine) selectedCut;
            verticalLine.setProfondeur(((VerticalLineDTO) cutDTO).profondeur);
            this.cutManager.handleCutMovement(verticalLine,((VerticalLineDTO) cutDTO).x, ((VerticalLineDTO) cutDTO).x);
            verticalLine.setTool(((VerticalLineDTO) cutDTO).tool.toOutil());
            this.cutManager.updateCut(verticalLine);

        }
        if (selectedCut instanceof HorizontalLine && cutDTO instanceof HorizontalLineDTO) {
            HorizontalLine horizontalLine = (HorizontalLine) selectedCut;
            this.cutManager.handleCutMovement(horizontalLine,((HorizontalLineDTO) cutDTO).y, ((HorizontalLineDTO) cutDTO).y);
            horizontalLine.setProfondeur(((HorizontalLineDTO) cutDTO).profondeur);
            horizontalLine.setTool(((HorizontalLineDTO) cutDTO).tool.toOutil());

            this.cutManager.updateCut(horizontalLine);

        }
        if (selectedCut instanceof RestrictedArea && cutDTO instanceof RestrictedAreaDTO) {
            //RestrictedArea restrictedArea = (RestrictedArea) selectedCut;
            //restrictedArea.setHeightFactor(((RestrictedAreaDTO) cutDTO).heightFactor);
            //restrictedArea.setWidthFactor(((RestrictedAreaDTO) cutDTO).widthFactor);
            //restrictedArea.setX(((RestrictedAreaDTO) cutDTO).x);
            //restrictedArea.setY(((RestrictedAreaDTO) cutDTO).y);
            //this.cutManager.updateCut(restrictedArea);
            RestrictedArea restrictedArea = (RestrictedArea) selectedCut;
            this.cutManager.handleCutMovement(restrictedArea, ((RestrictedAreaDTO) cutDTO).x, ((RestrictedAreaDTO) cutDTO).y);
            this.cutManager.handleCutSizeTransformation(restrictedArea, ((RestrictedAreaDTO) cutDTO).widthFactor, ((RestrictedAreaDTO) cutDTO).heightFactor);
            this.cutManager.updateCut(restrictedArea);

        }
        if (selectedCut instanceof Square && cutDTO instanceof SquareDTO) {
            Square square = (Square) selectedCut;

            this.cutManager.handleCutMovement(square, ((SquareDTO) cutDTO).x, ((SquareDTO) cutDTO).y);
            this.cutManager.handleCutSizeTransformation(square, ((SquareDTO) cutDTO).widthFactor, ((SquareDTO) cutDTO).heightFactor);
            square.setTool(((SquareDTO) cutDTO).tool.toOutil());
            this.cutManager.updateCut(square);
            this.pointReferenceManager.getReferencePoints();

        }
        if (selectedCut instanceof Bordure && cutDTO instanceof BordureDTO) {
            //Bordure bordure = (Bordure) selectedCut;
            //bordure.setHeightFactor(((BordureDTO) cutDTO).heightFactor);
            //bordure.setWidthFactor(((BordureDTO) cutDTO).widthFactor);
            //bordure.setX(((BordureDTO) cutDTO).x);
            //bordure.setY(((BordureDTO) cutDTO).y);
            //bordure.setTool(((BordureDTO) cutDTO).tool.toOutil());
            //this.cutManager.updateCut(bordure);
            Bordure bordure = (Bordure) selectedCut;

            this.cutManager.handleCutMovement(bordure, ((BordureDTO) cutDTO).x, ((BordureDTO) cutDTO).y);
            this.cutManager.handleCutSizeTransformation(bordure, ((BordureDTO) cutDTO).widthFactor, ((BordureDTO) cutDTO).heightFactor);
            bordure.setTool(((BordureDTO) cutDTO).tool.toOutil());
            this.cutManager.updateCut(bordure);


        }
        if (selectedCut instanceof L && cutDTO instanceof LDTO) {
            L l = (L) selectedCut;


            this.cutManager.handleCutMovement(l, ((LDTO) cutDTO).x, ((LDTO) cutDTO).y);
            this.cutManager.handleCutSizeTransformation(l, ((LDTO) cutDTO).widthFactor, ((LDTO) cutDTO).heightFactor);
            l.setTool(((LDTO) cutDTO).tool.toOutil());
            this.cutManager.updateCut(l);

        }
    }


    public void pointReferenceChildrenMovementHandler(double staticXStartingPoint, double staticYStartingPoint, double x, double y) {

    }




/*
    public boolean isCutInsidePanel(Cut cut) {
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
*/

    /*public boolean isCutTouchingRestrictedArea(Cut cut) {
        List<RestrictedArea> restrictedAreas = this.actionHistory.getRestrictedAreas();
        for (RestrictedArea restrictedArea : restrictedAreas) {
            if(restrictedArea.isTouching(cut))
            {
                //System.out.println("Cut " + cut + " is touching " + restrictedArea);
                return true;
            }
        }
        return false;
    }

    //is touching any other cut
    public boolean isCutTouchingOtherCut(Cut cut) {
        List<Cut> cuts = this.cutManager.getCuts();
        for (Cut otherCut : cuts) {
            if(cut.id != otherCut.id && cut.isTouching(otherCut))
            {
                System.out.println("Cut " + cut + " is touching " + otherCut);
                return true;
            }
        }
        return false;
    }*/


    public void deleteSelectedEntity() {
        //Cut selectedCut = actionHistory.getSelectedCut();
        InteractiveEntity selectedCut = this.getSelectedInteractiveEntity();

        /*
        if (selectedCut != null) {
            actionHistory.deleteCut((Cut) selectedCut);
        }*/
        if (selectedCut != null) {
            if(selectedCut instanceof Cut)
            {
                cutManager.deleteCut((Cut) selectedCut);
            }
        }
        
    }


    //TODO: uh?
    public void controllerPress(double x, double y, String selectedCuttingMethod, Outil tool, double profondeur) {
        if(this.startPositionAfterDrag == null)
        {
            this.cutManager.setTemporaryClonePointReferenceForMovement();
            this.pointReferenceManager.setTemporaryClonePointReferenceForMovement();
            this.startPositionAfterDrag = new Point2D.Double(x,y);
        }

        InteractiveEntity selectedCut = this.getSelectedInteractiveEntity();

        //Cut selectedCut = actionHistory.getSelectedCut();
        if (selectedCut != null) {
            if (selectedCut != null) {
                if (selectedCut instanceof VerticalLine) {
                    VerticalLine verticalLine = (VerticalLine) selectedCut;
                }
                if (selectedCut instanceof HorizontalLine) {
                    HorizontalLine horizontalLine = (HorizontalLine) selectedCut;
                }
            }
        }

    }


    public List <InteractiveEntityDTO> getHistoryInDTO() {
        List <InteractiveEntityDTO> cutDTOList = new ArrayList < > ();
        for (InteractiveEntity cut: this.getAllInteractiveEntities()) {
            cutDTOList.add( cut.toDTO());
        }
        return cutDTOList;
    }

    public List<InteractiveEntityDTO> getPointsInDTO()
    {
        List <InteractiveEntityDTO> cutDTOList = new ArrayList < > ();
        
        //for (InteractiveEntity cut: this.actionHistory.getPointReference()) {
        for (InteractiveEntity cut: this.pointReferenceManager.getPointReference()) {
            cutDTOList.add(cut.toDTO());
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
        InteractiveEntity selectedCut = this.getSelectedInteractiveEntity();

        if (selectedCut != null) {
            return selectedCut.toDTO();
        }
        return null;
    }


    //ADD OUTILDTO TO OUTILS
    public void addOutil(OutilDTO outilDTO) {
        //outils.add(outilDTO.toOutil());
        Outil newOutil = new Outil(outilDTO.name, outilDTO.epaisseur);
        outils.add(newOutil);
        System.out.println(outils);
    }

    //GET OUTILS IN DTO
    public List < OutilDTO > getOutilsInDTO() {
        List < OutilDTO > outilDTOList = new ArrayList < > ();
        for (Outil outil: outils) {
            outilDTOList.add(new OutilDTO(outil.getId(), outil.getName(), outil.getEpaisseur()));
        }
        return outilDTOList;
    }
    





    public void controllerRelease(double x, double y, String selectedCuttingMethod, Outil outil, double profondeur) {
        if(this.startPositionAfterDrag != null)
        {
            this.startPositionAfterDrag = null; //= new Point2D.Double(x,y);
        }
        //todo: uh?
        InteractiveEntity selectedCut = this.getSelectedInteractiveEntity();


        //if(this.entityHistory.get(this.entityHistory.size()-1) != this.entities)
        //{
        System.out.println("entities CHANGED UPON CLICK RELEASE");
        //for(InteractiveEntity entities  : this.entityHistory.get(this.entityHistory.size()-1))
        List<Cut> cutsForLastIndex = new ArrayList<>();
        for(int i = 0; i < this.entityHistory.get(this.entityHistory.size()-1).size(); i++)
        {
            InteractiveEntity lastIndexEntities = this.entityHistory.get(this.entityHistory.size()-1).get(i);
            if(lastIndexEntities instanceof Cut)
            {
                cutsForLastIndex.add(((Cut)lastIndexEntities));
            }
        }
        //for(int i = 1; i < this.entityHistory.get(this.entityHistory.size()-1).size(); i++)
        //{
        for(int i = 0; i < cutsForLastIndex.size(); i++)
        {
            InteractiveEntity lastIndexEntities = cutsForLastIndex.get(i);
            if(lastIndexEntities instanceof Cut)
            {
                //this.entityHistory.get(this.entityHistory.size()-1)
                //if(entities.equals(this.entityHistory.get(i-1)))
                if(lastIndexEntities.equals(this.cutManager.getCuts().get(i)))
                {
                    System.out.println("entities are the same" + entities + " " + this.entities.get(i));
                }
                else
                {
                    System.out.println("entities are different" + entities + " " + this.entities.get(i));
                    this.updateHistoryAfterClickRelease();
                    break;
                }
            }
        }
        //}


        //something selected upon release
        //if (selectedCut != null) {
        //    isDragging = false;
        //    if (selectedCut != null) {
        //        this.updateHistoryAfterClickRelease();
        //    }
        //}






        //something selected upon release
        //if (selectedCut != null) {
        //    isDragging = false;
        //    if (selectedCut != null) {
        //        this.updateHistoryAfterClickRelease();
        //    }
        //}
        //else
        //{
        //    this.updateHistoryAfterClickRelease();
        //}

    }
    

    public void generateDotPANFile()  {

        //ObjectMapper objectMapper = new ObjectMapper();
//
        //String jsonString = null;
        //try {
        //    jsonString = objectMapper.writeValueAsString(this.getAllInteractiveEntities());
        //} catch (JsonProcessingException e) {
        //    throw new RuntimeException(e);
        //}
        //System.out.println(jsonString); // Output: {"name":"John","age":30}
    }












    public void updateAllAfterAnAction()
    {
        this.cutManager.updateAllAfterAnAction();
        this.pointReferenceManager.updateAllAfterAnAction();

        this.getAllInteractiveEntities();
    }

//separate the cuts and the point references from the entities into two lists
    public void separateEntities() {
        List < Cut > cuts = new ArrayList < > ();
        List < PointReference > pointReferences = new ArrayList < > ();
        for (InteractiveEntity entity: this.entities) {
            if (entity instanceof Cut) {
                cuts.add((Cut) entity);
            } else if (entity instanceof PointReference) {
                pointReferences.add((PointReference) entity);
            }
        }
        this.cutManager.setCuts(cuts);
        this.pointReferenceManager.setPointReference(pointReferences);
    }



    public void updateHistoryAfterClickRelease() {
        this.getAllInteractiveEntities();
        if (this.entityHistory == null) {
            this.entityHistory = new ArrayList<>();
            this.currentHistoryIndex = -1;
        }

        if (this.currentHistoryIndex < this.entityHistory.size() - 1) {
            this.entityHistory = new ArrayList<>(this.entityHistory.subList(0, this.currentHistoryIndex + 1));
            //this.actionHistory.add(0, new ArrayList<>());
        }

        List<InteractiveEntity> entitiesCopy = new ArrayList<>();
        System.out.println("Entities before updating history: " + this.entities);
        //TODO: SEE IF THAT WORKS
        List<PointReference> pointReferencesThatMatter = this.pointReferenceManager.getPointReferenceAssociatedWithCuts();
        List<Cut> cuts = this.cutManager.getCuts();	
        entitiesCopy.add(panelManager.panel);
        for (PointReference pointReference: pointReferencesThatMatter) {
            entitiesCopy.add(pointReference.clone());
        }
        for (Cut cut: cuts) {
            entitiesCopy.add(cut.clone());
        }
        //for (InteractiveEntity entity : this.entities) {
        //    entitiesCopy.add(entity.clone());
        //}

        this.entityHistory.add(entitiesCopy);

        this.currentHistoryIndex = this.entityHistory.size() - 1;

        System.out.println("History (currently at index " + this.currentHistoryIndex + "): {");
        int counter = 0;
        for (List<InteractiveEntity> action : this.entityHistory) {
            System.out.println(counter + ": " + action);
            counter++;
        }



        System.out.println("}");
        System.out.println("Updated history list, current index: " + this.currentHistoryIndex + " out of " + this.entityHistory.size());
    }




    //todo: add all cuts + reference points that actually matter

    public void undo() {
        if (this.currentHistoryIndex - 1 >= 0) {
            this.currentHistoryIndex -= 1;

            List<InteractiveEntity> entitiesCopy = new ArrayList<>();
            for (InteractiveEntity entity : this.entityHistory.get(this.currentHistoryIndex)) {
                if(!(entity instanceof Panel))
                {
                    entitiesCopy.add(entity.clone());
                }
                else
                {
                    this.panelManager.panel = (Panel) entity.clone();
                }
            }
            this.entities = entitiesCopy;
            this.separateEntities();


            for(InteractiveEntity entity: this.entities)
            {
                if(entity instanceof Cut)
                {
                    Cut cut = (Cut) entity;

                    if(cut instanceof VerticalLine)
                    {
                        VerticalLine verticalLine = (VerticalLine) cut;
                        Outil outil = verticalLine.getTool();
                        if(outil != null)
                        {
                            boolean outilExists = false;
                            for(Outil outilInList: this.outils)
                            {
                                if(outilInList.getId().equals(outil.getId()))
                                {
                                    outilExists = true;
                                }
                            }
                            if(!outilExists)
                            {
                                System.out.println("outil had been deleted");
                                this.outils.add(outil);
                            }
                        }
                    }
                    if(cut instanceof HorizontalLine)
                    {
                        HorizontalLine horizontalLine = (HorizontalLine) cut;
                        Outil outil = horizontalLine.getTool();
                        if(outil != null)
                        {
                            boolean outilExists = false;
                            for(Outil outilInList: this.outils)
                            {
                                if(outilInList.getId().equals(outil.getId()))
                                {
                                    outilExists = true;
                                }
                            }
                            if(!outilExists)
                            {
                                this.outils.add(outil);
                            }
                        }
                    }
                    if(cut instanceof Square)
                    {
                        Square square = (Square) cut;
                        Outil outil = square.getTool();
                        if(outil != null)
                        {
                            boolean outilExists = false;
                            for(Outil outilInList: this.outils)
                            {
                                if(outilInList.getId().equals(outil.getId()))
                                {
                                    outilExists = true;
                                }
                            }
                            if(!outilExists)
                            {
                                this.outils.add(outil);
                            }
                        }
                    }
                    if(cut instanceof Bordure)
                    {
                        Bordure bordure = (Bordure) cut;
                        Outil outil = bordure.getTool();
                        if(outil != null)
                        {
                            boolean outilExists = false;
                            for(Outil outilInList: this.outils)
                            {
                                if(outilInList.getId().equals(outil.getId()))
                                {
                                    outilExists = true;
                                }
                            }
                            if(!outilExists)
                            {
                                this.outils.add(outil);
                            }
                        }
                    }
                    if(cut instanceof L)
                    {
                        L l = (L) cut;
                        Outil outil = l.getTool();
                        if(outil != null)
                        {
                            boolean outilExists = false;
                            for(Outil outilInList: this.outils)
                            {
                                if(outilInList.getId().equals(outil.getId()))
                                {
                                    outilExists = true;
                                }
                            }
                            if(!outilExists)
                            {
                                this.outils.add(outil);
                            }
                        }
                    }
                }

            }


            System.out.println("Undoing " + this.currentHistoryIndex);
            this.updateAllAfterAnAction();
        }
    }





    //todo: add all cuts + reference points that actually matter
    public void redo() {
        if (this.currentHistoryIndex < this.entityHistory.size() - 1) {
            //collect all the tools that are used before we redo
            List<Outil> outilInUseBeforeRedo = new ArrayList<>();
            for(InteractiveEntity entity: this.entities) {
                if (entity instanceof Cut) {
                    Cut cut = (Cut) entity;

                    if (cut instanceof VerticalLine) {
                        VerticalLine verticalLine = (VerticalLine) cut;
                        Outil outil = verticalLine.getTool();
                        if (outil != null) {
                            if (!outilInUseBeforeRedo.contains(outil)) {
                                outilInUseBeforeRedo.add(outil);
                            }
                        }
                    }
                    if (cut instanceof HorizontalLine) {
                        HorizontalLine horizontalLine = (HorizontalLine) cut;
                        Outil outil = horizontalLine.getTool();
                        if (outil != null) {
                            if (!outilInUseBeforeRedo.contains(outil)) {
                                outilInUseBeforeRedo.add(outil);
                            }
                        }
                    }
                    if (cut instanceof Square) {
                        Square square = (Square) cut;
                        Outil outil = square.getTool();
                        if (outil != null) {
                            if (!outilInUseBeforeRedo.contains(outil)) {
                                outilInUseBeforeRedo.add(outil);
                            }
                        }
                    }
                    if (cut instanceof Bordure) {
                        Bordure bordure = (Bordure) cut;
                        Outil outil = bordure.getTool();
                        if (outil != null) {
                            if (!outilInUseBeforeRedo.contains(outil)) {
                                outilInUseBeforeRedo.add(outil);
                            }
                        }
                    }
                    if (cut instanceof L) {
                        L l = (L) cut;
                        Outil outil = l.getTool();
                        if (outil != null) {
                            if (!outilInUseBeforeRedo.contains(outil)) {
                                outilInUseBeforeRedo.add(outil);
                            }
                        }
                    }
                }
            }

            this.currentHistoryIndex += 1;

            // Clone entities from history
            List<InteractiveEntity> entitiesCopy = new ArrayList<>();
            for (InteractiveEntity entity : this.entityHistory.get(this.currentHistoryIndex)) {
                if(!(entity instanceof Panel))
                {
                    entitiesCopy.add(entity.clone());
                }
                else
                {
                    this.panelManager.panel = (Panel) entity.clone();
                }

            }
            this.entities = entitiesCopy;
            this.separateEntities();
            System.out.println("Redoing " + this.currentHistoryIndex);
            this.updateAllAfterAnAction();
        
            List<Outil> postRedoOutilCheck = new ArrayList<>();
            for(Outil outil: this.outils)
            {
                boolean outilExists = false;
                for(InteractiveEntity entity: this.entities)
                {
                    if(entity instanceof Cut)
                    {
                        Cut cut = (Cut) entity;
                        if(cut instanceof VerticalLine)
                        {
                            VerticalLine verticalLine = (VerticalLine) cut;
                            //if tool isnt null and the tool is the same as the outil
                            if(verticalLine.getTool() != null)
                            {
                                //if not in outils to remove
                                if(!postRedoOutilCheck.contains(outil))
                                {
                                    postRedoOutilCheck.add(outil);
                                }
                            }

                        }
                        if(cut instanceof HorizontalLine)
                        {
                            HorizontalLine horizontalLine = (HorizontalLine) cut;
                            if(horizontalLine.getTool() != null)
                            {
                                if(!postRedoOutilCheck.contains(outil))
                                {
                                    postRedoOutilCheck.add(outil);
                                }
                            }
                        }   
                        if(cut instanceof Square)
                        {
                            Square square = (Square) cut;
                            if(square.getTool() != null)
                            {
                                if(!postRedoOutilCheck.contains(outil))
                                {
                                    postRedoOutilCheck.add(outil);
                                }
                            }
                        }
                        if(cut instanceof Bordure)
                        {
                            Bordure bordure = (Bordure) cut;

                            if(bordure.getTool() != null)
                            {
                                if(!postRedoOutilCheck.contains(outil))
                                {
                                    postRedoOutilCheck.add(outil);
                                }
                            }
                        }
                        if(cut instanceof L)
                        {
                            L l = (L) cut;
                            
                            if(l.getTool() != null)
                            {
                                if(!postRedoOutilCheck.contains(outil))
                                {
                                    postRedoOutilCheck.add(outil);
                                }
                            }
                        }
                    }
                }
            }
            //if the OUTILS IN USE BEFORE REDO is not the same as the OUTILS IN USE AFTER REDO
            for(Outil outil : outilInUseBeforeRedo)
            {
                if(!postRedoOutilCheck.contains(outil))
                {
                    this.outils.remove(outil);
                }
            }
            //if(!outilInUseBeforeRedo.equals(postRedoOutilCheck))
            //{
            //    //remove all the outils that are not in use
            //    for(Outil outil: this.outils)
            //    {
            //        if(!postRedoOutilCheck.contains(outil))
            //        {
            //            this.outils.remove(outil);
            //        }
            //    }
            //}
        }
    }

    public InteractiveEntity isClickingSomething(Point2D.Double point) {
        double x = point.getX();
        double y = point.getY();
        getAllInteractiveEntities();


        List<PointReference> pointList = this.pointReferenceManager.getPointReference(); // Populate with your points
        List<List<PointReference>> rectangles = RectangleFinder.findAllRectangles(pointList);

// Now rectangles contain all quadruples forming rectangles, sorted by area.
        for (List<PointReference> rect : rectangles) {
            System.out.println("Rectangle points: " + rect + " Area: " + RectangleFinder.rectangleArea(rect));
        }
        //getAllInteractiveEntities
        for (InteractiveEntity entity : this.entities) {
            if (entity.isClicked(x, y)) {
                System.out.println("CLICKED: " + entity);
                return entity;
            }
        }



        return null;
    }

    public InteractiveEntity getSelectedInteractiveEntity() {
        //check to see if its a reference point first

        /*for (PointReference pointReference: pointReference) {
            if (pointReference.isSelected()) {
                return pointReference;
            }
        }
        for (InteractiveEntity entity: entities) {
            System.out.println(entity);
            if (entity.isSelected()) {
                return entity;
            }
        }*/
        for (InteractiveEntity entity: entities) {
            //System.out.println(entity);
            if (entity.isSelected()) {
                return entity;
            }
        }
        return null;
    }

    /*public void updateInteractiveEntity(InteractiveEntity cut) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).isSelected()) {
                entities.set(i, cut);
            }
        }
        updateAllAfterAnAction();
    }*/


    public List<InteractiveEntity> getAllInteractiveEntities() {
        //this.entities = new ArrayList<>();
        //for (InteractiveEntity entity : this.actionHistory.getInteractiveEntities()) {
        //    this.entities.add(entity.clone());
        //}

        List<Cut> cuts = this.cutManager.getCuts();
        List<PointReference> pointReferences = this.pointReferenceManager.getPointReference();
        this.entities = new ArrayList<>();
        //point de ref en premier
        this.entities.addAll(pointReferences);
        this.entities.addAll(cuts);
        //System.out.println("SIZE " + pointReferences.size() + " entities in question " + entities);

        return this.entities;
    }


    public PanelDTO getPanelDTO()
    {
        return panelManager.panel.getPanelDTO();
    }

    /**
     * Recrée la liste d'outils à partir d'une liste de OutilDTO.
     * On vide d’abord la liste actuelle, puis on ajoute chaque outil recréé.
     */
    //todo: uh
    public void setOutils(List<OutilDTO> outilsDTO) {
        this.outils.clear();
        for (OutilDTO dto : outilsDTO) {
            //TODO: il manquait l'id
            Outil newOutil = new Outil(dto.id, dto.name, dto.epaisseur);
            this.outils.add(newOutil);
        }
    }

    public List<Outil> getOutils()
    {
        return this.outils;
    }



    //outil delete
    public void deleteOutil(OutilDTO outilDTO) {

        outils.removeAll(new ArrayList<>(Arrays.asList(outilDTO.toOutil())));
        updateAllCutsWithOutil(outilDTO.toOutil(), null);
        this.updateHistoryAfterClickRelease();

    }

    public void updateOutil(OutilDTO oldOutil, OutilDTO newOutil) {
        System.out.println("LAJSDOISAJHDIAJSDOIJDOIASJDOISAJD");
        Outil newOutilNotDTO = oldOutil.toOutil();
        Outil oldOutilNotDTO =  newOutil.toOutil();
        for (Outil outil : this.outils) {
            if (outil.getId() == (oldOutil.id)) {
                outil.setName(newOutil.name);
                outil.setEpaisseur(newOutil.epaisseur);
            }
        }
        updateAllCutsWithOutil(oldOutilNotDTO, newOutilNotDTO);
        this.updateHistoryAfterClickRelease();

    }

    public void updateAllCutsWithOutil(Outil oldOutil, Outil newOutil)
    {
        for (Cut cut : this.cutManager.getCuts()) {
            if (cut instanceof VerticalLine) {
                VerticalLine verticalLine = (VerticalLine) cut;
                if(verticalLine.getTool() != null)
                {
                    if(verticalLine.getTool().getId() == oldOutil.getId()) {
                        verticalLine.setTool(newOutil);
                    }
                }
            } else if (cut instanceof HorizontalLine) {
                if (cut instanceof HorizontalLine) {
                    HorizontalLine horizontalLine = (HorizontalLine) cut;
                    if(horizontalLine.getTool() != null)
                    {
                        if(horizontalLine.getTool().getId() == oldOutil.getId()) {
                            horizontalLine.setTool(newOutil);
                        }
                    }
                }
            } else if (cut instanceof Square) {
                if (cut instanceof Square) {
                    Square square = (Square) cut;
                    if(square.getTool() != null)
                    {
                        if(square.getTool().getId() == oldOutil.getId()) {
                            square.setTool(newOutil);
                        }
                    }
                }
            } else if (cut instanceof L) {
                if (cut instanceof L) {
                    L l = (L) cut;
                    if(l.getTool() != null)
                    {
                            if(l.getTool().getId() == oldOutil.getId()) {
                            l.setTool(newOutil);
                        }
                    }
                }
            } else if (cut instanceof Bordure) {
                if (cut instanceof Bordure) {
                    Bordure bordure = (Bordure) cut;
                    if(bordure.getTool() != null)
                    {
                        if(bordure.getTool().getId() == oldOutil.getId()) {
                            bordure.setTool(newOutil);
                        }
                    }
                }
            }
            this.cutManager.updateCut(cut);
        }
    }


    public void save(File file) throws IOException {
        // Récupérer l'état actuel du panneau
        PanelDTO panelDTO = this.getPanelDTO();

        // Récupérer les outils
        List<OutilDTO> outils = this.getOutilsInDTO();

        // Récupérer les coupes
        List<Cut> cuts = this.cutManager.getCuts();
        List<InteractiveEntityDTO> cutDTOs = new ArrayList<>();
        for (Cut cut : cuts) {
            cutDTOs.add(cut.toDTO());
        }

        // Récupérer les points de référence associés aux coupes
        List<PointReference> associatedPoints = this.pointReferenceManager.getPointReferenceAssociatedWithCuts();
        //List<PointReference> associatedPoints = this.pointReferenceManager.getPointReference();
        List<InteractiveEntityDTO> pointReferenceDTOs = new ArrayList<>();
        for (PointReference pr : associatedPoints) {
            System.out.println(pr.toDTO());
            pointReferenceDTOs.add(pr.toDTO());
        }

        // Combiner coupes et points de référence dans une seule liste
        List<InteractiveEntityDTO> entities = new ArrayList<>();
        entities.addAll(pointReferenceDTOs);
        entities.addAll(cutDTOs);
        System.out.println("Saving " + entities);
        // Créer l'objet de sauvegarde
        SaveData saveData = new SaveData(panelDTO, outils, entities);


        //ObjectMapper mapper = new ObjectMapper();
        //mapper.writerWithDefaultPrettyPrinter().writeValue(file, saveData);
//
        //System.out.println("Données sauvegardées avec succès dans " + file.getAbsolutePath());
    }

    /*
    public void load(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Grâce aux annotations sur vos DTO, Jackson sait maintenant quelle classe concrète instancier.
        SaveData loadedData = mapper.readValue(file, SaveData.class);

        // Nettoyer l'état actuel
        this.outils.clear();
        this.cutManager.getCuts().clear();
        this.pointReferenceManager.getPointReference().clear();

        this.panelManager.panel = loadedData.panelDTO.toPanel();
        this.pointReferenceManager.cutLines.addAll(this.panelManager.panel.getCutLines());

        // Ré-injecter les données du panneau
        //panelManager.changePanelSize(
        //        ConversionPiedMM.convertirEnMillimetresString(loadedData.panelDTO.widthFactor),
        //        ConversionPiedMM.convertirEnMillimetresString(loadedData.panelDTO.heightFactor)
        //);
        //panelManager.changeEpaisseurPanel(
        //        ConversionPiedMM.convertirEnMillimetresString(loadedData.panelDTO.epaisseur)
        //);

        // Mettre à jour la liste d'outils
        setOutils(loadedData.outils);

        // Séparer les entités par type pour un chargement ordonné
        List<InteractiveEntityDTO> lineDTOs = new ArrayList<>();
        List<InteractiveEntityDTO> pointRefDTOs = new ArrayList<>();
        List<InteractiveEntityDTO> complexDTOs = new ArrayList<>();

        for (InteractiveEntityDTO dto : loadedData.entities) {
            if (dto instanceof VerticalLineDTO || dto instanceof HorizontalLineDTO) {
                lineDTOs.add(dto);
            } else if (dto instanceof PointReferenceDTO) {
                pointRefDTOs.add(dto);
            } else if (dto instanceof SquareDTO || dto instanceof LDTO) {
                complexDTOs.add(dto);
            } else {
                lineDTOs.add(dto);
            }
        }
        for (InteractiveEntityDTO lineDTO : lineDTOs) {
            Cut line = ((CutDTO) lineDTO).toCut();
            this.cutManager.addCutAtLoad(line);
            System.out.println("[DEBUG] Ligne ajoutée: " + line);
            this.pointReferenceManager.cutLines.addAll(line.getCutLines());
            this.entities.add(line);

        }
        //todo remove
        this.pointReferenceManager.appendEveryCutLine();
        //this.pointReferenceManager.getReferencePoints();

        // Charger d'abord les points de référence
        //for (InteractiveEntityDTO refDTO : pointRefDTOs) {
        //    PointReference pr = ((PointReferenceDTO) refDTO).toPointReference();
        //    this.pointReferenceManager.addPointReference(pr);
        //    System.out.println("[DEBUG] PointReference ajouté: " + pr);
        //}
//
        //// Charger enfin les entités plus complexes (L, Square, etc.)
        //for (InteractiveEntityDTO complexDTO : complexDTOs) {
        //    Cut complexCut = ((CutDTO) complexDTO).toCut();
        //    this.cutManager.addCutAtLoad(complexCut);
        //    System.out.println("[DEBUG] Entité complexe ajoutée: " + complexCut);
        //    this.pointReferenceManager.cutLines.addAll(complexCut.getCutLines());
        //}
        //System.out.println("NEW CUTLINES WHATEVER " + this.pointReferenceManager.cutLines);
        //System.out.println("NEW CUTLINES WHATEVER " + this.pointReferenceManager.appendEveryCutLine());


        ///////////////////////////////////////////////////////////////////////////////////////////

        //this.getAllInteractiveEntities();
        //this.pointReferenceManager.getReferencePoints();
//
        //System.out.println(this.cutManager.getCuts().get(0).getCutLines());
        //this.entities.addAll(this.cutManager.getCuts());
        //this.pointReferenceManager.cutLines = this.pointReferenceManager.appendEveryCutLine();
        //System.out.println("NEW CUTLINES LOADED " + this.pointReferenceManager.cutLines);



        //System.out.println(c);
        //System.out.println("these are the entites: " + entities);
        //this.getAllInteractiveEntities();
        //this.pointReferenceManager.updateAllAfterAnAction();

        //this.updateAllAfterAnAction();
    }*/

    /*public void load(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Grâce aux annotations sur vos DTO, Jackson sait maintenant quelle classe concrète instancier.
        SaveData loadedData = mapper.readValue(file, SaveData.class);

        // Nettoyer l'état actuel
        this.outils.clear();
        this.cutManager.getCuts().clear();
        this.pointReferenceManager.getPointReference().clear();

        this.panelManager.panel = loadedData.panelDTO.toPanel();

        // Mettre à jour la liste d'outils
        setOutils(loadedData.outils);

        // Séparer les entités par type pour un chargement ordonné
        List<InteractiveEntityDTO> lineDTOs = new ArrayList<>();
        List<InteractiveEntityDTO> pointRefDTOs = new ArrayList<>();
        List<InteractiveEntityDTO> complexDTOs = new ArrayList<>();

        for (InteractiveEntityDTO dto : loadedData.entities) {
            if (dto instanceof VerticalLineDTO || dto instanceof HorizontalLineDTO) {
                lineDTOs.add(dto);
            } else if (dto instanceof PointReferenceDTO) {
                pointRefDTOs.add(dto);
            } else if (dto instanceof SquareDTO || dto instanceof LDTO) {
                complexDTOs.add(dto);
            } else {
                lineDTOs.add(dto);
            }
        }

        // Charger d'abord les points de référence
        for (InteractiveEntityDTO refDTO : pointRefDTOs) {
            PointReference pr = ((PointReferenceDTO) refDTO).toPointReference();
            this.pointReferenceManager.addPointReference(pr);
            System.out.println("[DEBUG] PointReference ajouté: " + pr);
        }

        // Charger ensuite les lignes
        for (InteractiveEntityDTO lineDTO : lineDTOs) {
            Cut line = ((CutDTO) lineDTO).toCut();
            this.cutManager.addCutAtLoad(line);
            System.out.println("[DEBUG] Ligne ajoutée: " + line);
        }
//
        // Charger enfin les entités plus complexes (L, Square, etc.)
        for (InteractiveEntityDTO complexDTO : complexDTOs) {
            Cut complexCut = ((CutDTO) complexDTO).toCut();
            this.cutManager.addCutAtLoad(complexCut);
            System.out.println("[DEBUG] Entité complexe ajoutée: " + complexCut);
        }


        this.pointReferenceManager.cutLines.addAll(this.pointReferenceManager.appendEveryCutLine());
        this.getAllInteractiveEntities();

        for(int i = 0; i < this.entities.size(); i++)
        {
            System.out.println("CIUT " + this.entities.get(i));
        }
    

        System.out.println(this.pointReferenceManager.cutLines);



        //this.pointReferenceManager.getReferencePoints();


        //this.pointReferenceManager.updateAllAfterAnAction();

        //this.updateAllAfterAnAction();
    }*/
    /*

    public void load(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SaveData loadedData = mapper.readValue(file, SaveData.class);

        this.outils.clear();
        this.cutManager.getCuts().clear();
        this.pointReferenceManager.getPointReference().clear();

        this.panelManager.panel = loadedData.panelDTO.toPanel();
        this.pointReferenceManager.cutLines.addAll(this.panelManager.panel.getCutLines());
        this.pointReferenceManager.getReferencePoints();



        setOutils(loadedData.outils);

        // Séparer les entités par type pour un chargement ordonné
        List<InteractiveEntityDTO> lineDTOs = new ArrayList<>();
        List<InteractiveEntityDTO> pointRefDTOs = new ArrayList<>();
        List<InteractiveEntityDTO> complexDTOs = new ArrayList<>();

        for (InteractiveEntityDTO dto : loadedData.entities) {
            if (dto instanceof VerticalLineDTO || dto instanceof HorizontalLineDTO) {
                lineDTOs.add(dto);
            } else if (dto instanceof PointReferenceDTO) {
                pointRefDTOs.add(dto);
            } else if (dto instanceof SquareDTO || dto instanceof LDTO) {
                complexDTOs.add(dto);
            } else {
                lineDTOs.add(dto);
            }
        }
        for (InteractiveEntityDTO lineDTO : lineDTOs) {
            Cut line = ((CutDTO) lineDTO).toCut();
            this.cutManager.addCutAtLoad(line);
            System.out.println("[DEBUG] Ligne ajoutée: " + line);
            //this.entities.add(line);

        }
        //todo remove
        //this.getAllInteractiveEntities();
        //this.pointReferenceManager.getReferencePoints();
        //updateAllAfterAnAction();

        // Charger d'abord les points de référence
        //for (InteractiveEntityDTO refDTO : pointRefDTOs) {
        //    PointReference pr = ((PointReferenceDTO) refDTO).toPointReference();
        //    this.pointReferenceManager.addPointReference(pr);
        //    System.out.println("[DEBUG] PointReference ajouté: " + pr);
        //}
//
        //// Charger enfin les entités plus complexes (L, Square, etc.)
        //for (InteractiveEntityDTO complexDTO : complexDTOs) {
        //    Cut complexCut = ((CutDTO) complexDTO).toCut();
        //    this.cutManager.addCutAtLoad(complexCut);
        //    System.out.println("[DEBUG] Entité complexe ajoutée: " + complexCut);
        //    this.pointReferenceManager.cutLines.addAll(complexCut.getCutLines());
        //}
        //System.out.println("NEW CUTLINES WHATEVER " + this.pointReferenceManager.cutLines);
        //System.out.println("NEW CUTLINES WHATEVER " + this.pointReferenceManager.appendEveryCutLine());


        ///////////////////////////////////////////////////////////////////////////////////////////

        //this.getAllInteractiveEntities();
        //this.pointReferenceManager.getReferencePoints();
//
        //System.out.println(this.cutManager.getCuts().get(0).getCutLines());
        //this.entities.addAll(this.cutManager.getCuts());
        //this.pointReferenceManager.cutLines = this.pointReferenceManager.appendEveryCutLine();
        //System.out.println("NEW CUTLINES LOADED " + this.pointReferenceManager.cutLines);



        //System.out.println(c);
        //System.out.println("these are the entites: " + entities);
        //this.getAllInteractiveEntities();
        //this.pointReferenceManager.updateAllAfterAnAction();

        //this.updateAllAfterAnAction();
    }


*/


    public void load(File file) throws IOException {
        //System.out.println("LOAD CALLED");
        //ObjectMapper mapper = new ObjectMapper();
        //// Grâce aux annotations sur vos DTO, Jackson sait maintenant quelle classe concrète instancier.
        //SaveData loadedData = mapper.readValue(file, SaveData.class);

        //// Nettoyer l'état actuel

        //this.outils.clear();
        //this.cutManager.getCuts().clear();
        //this.pointReferenceManager.appendEveryCutLine();
        //this.pointReferenceManager.getPointReference().clear();
        //this.pointReferenceManager.getReferencePoints();

        ////this.pointReferenceManager.getReferencePoints();

        //this.panelManager.panel = loadedData.panelDTO.toPanel();
        ////this.pointReferenceManager.cutLines.addAll(panelManager.panel.getCutLines());

        //// Mettre à jour la liste d'outils
        //setOutils(loadedData.outils);

        //List<InteractiveEntityDTO> remainingDTOs = new ArrayList<>();

        //for(InteractiveEntityDTO dto : loadedData.entities) {
        //    if (!(dto instanceof PanelDTO)) {
        //        remainingDTOs.add(dto);
        //        if(dto instanceof PointReferenceDTO)
        //        {
        //            pointReferenceManager.addPointReference(((PointReferenceDTO) dto).toPointReference());
        //        }
        //        else if(dto instanceof CutDTO)
        //        {
        //            System.out.println("[DEBUG] new cut ajouté: " + dto);
        //            Cut unDTOed = ((CutDTO) dto).toCut();
        //            cutManager.addCutAtLoad(unDTOed);
        //            this.pointReferenceManager.cutLines.addAll(unDTOed.getCutLines());
        //        }
        //    }
        //}
        //System.out.println("Currently in cutlines: " + this.pointReferenceManager.cutLines);
        //this.pointReferenceManager.getReferencePoints();
    }



    public void saveGCode(File file, int laserPower, int feedRate) throws IOException {
        // Récupérer les lignes de coupe depuis votre gestionnaire de coupes
        List<CutLine> cutLines = this.pointReferenceManager.appendEveryCutLine();
        List<CutLine> cutLinesCopyWithHardSetLimits = new ArrayList<>();
        for(CutLine cutLine : cutLines)
        {
            CutLine clonedInstance = cutLine.clone();
            if(cutLine.getCut() instanceof HorizontalLine)
            {
                clonedInstance.setEnd(new Point2D.Double(this.panelManager.panel.getWidthFactor(), ((HorizontalLine) cutLine.getCut()).getY()));
            }
            else if (cutLine.getCut() instanceof VerticalLine)
            {
                clonedInstance.setEnd(new Point2D.Double( ((VerticalLine) cutLine.getCut()).getX(), this.panelManager.panel.getHeightFactor()));
            }
            if(!(cutLine.getCut() instanceof Panel))
            {
                cutLinesCopyWithHardSetLimits.add(clonedInstance);
            }
        }

        // Convertir les lignes de coupe en instructions G-Code
        List<String> gcodeInstructions = GCodeGenerator.convertCutLinesToGCode(cutLinesCopyWithHardSetLimits, laserPower, feedRate);

        // Sauvegarder le G-Code dans le fichier spécifié
        GCodeGenerator.saveGCodeToFile(gcodeInstructions, file);
    }
    public static class SaveData {
        public PanelDTO panelDTO;
        public List<OutilDTO> outils;
        public List<InteractiveEntityDTO> entities;

        public SaveData(PanelDTO panelDTO, List<OutilDTO> outils, List<InteractiveEntityDTO> entities) {
            this.panelDTO = panelDTO;
            this.outils = outils;
            this.entities = entities;
        }

        // Constructeur par défaut requis pour Jackson
        public SaveData() {
        }
    }
    public void updateCutsAfterOutilDeletion() {
        List<Cut> cuts = this.cutManager.getCuts();
        List<InteractiveEntityDTO> cutDTOs = new ArrayList<>();
        for (Cut cut : cuts) {
            this.cutManager.updateCut(cut);

        }

    }





}



