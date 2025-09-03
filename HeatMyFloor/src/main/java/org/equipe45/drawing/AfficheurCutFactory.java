package org.drawing;


public class AfficheurCutFactory {

    public static AfficheurCut createAfficheurCut(CutDTO cutDTO) {
        if (cutDTO instanceof VerticalLineDTO) {
            return new AfficheurCutVertical((VerticalLineDTO) cutDTO);
        }
        if (cutDTO instanceof HorizontalLineDTO) {
            return new AfficheurCutHorizontal((HorizontalLineDTO) cutDTO);
        }
        //restricted Area
        if (cutDTO instanceof RestrictedAreaDTO) {
            return new AfficheurCutRestrictedArea((RestrictedAreaDTO) cutDTO);
        }
        if (cutDTO instanceof LDTO) {
            return new AfficheurCutL((LDTO) cutDTO);
        }


        // Add other CutDTO types here if needed
        return null;
    }
}