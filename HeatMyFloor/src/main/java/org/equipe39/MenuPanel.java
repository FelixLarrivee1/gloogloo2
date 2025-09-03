package org.equipe39;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.equipe39.domain.CNCController;
import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.domain.InteractiveEntity;
import org.equipe39.dto.*;

import javax.swing.*;

import java.io.*;
import java.awt.event.ActionEvent;

public class MenuPanel extends JPanel {


    private JTextField epaisseurOutilTextField;

    private JTextField profondeurTextField;

    private JLabel header;
    private JButton fileButton, exitButton;
    private JPanel gridCheckboxPanel;
    private JCheckBox gridVisibilityCheckbox;
    private JCheckBox gridMagnifierCheckbox;
    private JTextField gridSizeTextField;
    private ButtonGroup cutterGroup;
    private JRadioButton cutterHorizontal, cutterVertical, cutterRectangle, cutterBorder, cutterL, restrictedArea, selectTool;

    private JPanel depthAndThicknessPanel;
    private JPanel outilButtonPanel;
    private JPanel profondeurPanel;
    private JPanel outilPanel;
    public double depth, profondeur;

    public boolean inToolCreationMode = false;
    public OutilDTO selectedOutil;
    private List<OutilDTO> outils;
    private JComboBox<String> thicknessDropdown, outilDropdown;
    private JTextField profondeurTextBox;
    private JButton createOutilButton, deleteOutilButton;

    private JPanel outilCreationPanel;

    private JTextField outilCreationNameField;
    private JButton cancelOutilCreationButton, comfirmOutilCreationButton;

    private JPanel toolPanel;
    private JPanel toolRadioPanel;
    private JButton gridConfirmButton;
    private JTextField hauteurPanneauTextBox, largeurPanneauTextBox, epaisseurPanneauTextBox;
    private JSpinner gridHauteurSpinner, gridLargeurSpinner;

    private MainFrame mainFrame;  // Reference to the MainFrame


    //EDIT SECTION
    private JPanel editPanel;
    private JPanel verticalLineEditPanel;
    private JPanel horizontalLineEditPanel;
    private JPanel rectangleEditPanel;
    private JPanel lEditPanel;
    private JPanel restrictedAreaEditPanel;
    public boolean editMode = false;

    private JTextField xValue;
    private JTextField yValue;
    private JTextField heightValue;
    private JTextField widthValue;
    private InteractiveEntityDTO selectedCut;

    private JComboBox<String> thicknessDropdownEdit, outilDropdownEdit;
    private JButton deleteButton, modifyButton;


    private JButton undoButton, redoButton;
    private JLabel xMousePosition, yMousePosition;


    private JPanel borderCutPanel;
    private JTextField borderDimensionTextField;
    private JButton borderCutCancelButton, borderCutConfirmButton;
    private boolean isBorderCutMode = false;

    public double bordureSize;

    private JButton modifyOutilButton;
    private JPanel outilModificationPanel;
    private JTextField outilModificationNameField;
    private JTextField epaisseurOutilModificationTextField;
    private JButton cancelOutilModificationButton, confirmOutilModificationButton;


    // Constructor that accepts MainFrame
    public MenuPanel(MainFrame mainFrame) {
        this.profondeur = 1;
        this.outils = new ArrayList<>();
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(350, 350));
        setBackground(Color.decode("#15203b"));
        setLayout(new GridLayout(6, 1));

        fileButton = new JButton("File");
        exitButton = new JButton("Exit");

        fileButton.setSize(110, 30);
        exitButton.setSize(110, 30);


        JPanel filePanel = new JPanel(new GridLayout(1, 2));
        filePanel.add(fileButton);
        filePanel.add(exitButton);


        fileButton.addActionListener(e -> {
            JPopupMenu popup = new JPopupMenu();
            JMenuItem saveItem = new JMenuItem("Save");
            JMenuItem loadItem = new JMenuItem("Load");
            JMenuItem saveGCodeItem = new JMenuItem("Save G-Code");


            saveItem.addActionListener(saveAction());
            loadItem.addActionListener(loadAction());
            saveGCodeItem.addActionListener(saveGCodeAction()); // Méthode à créer

            popup.add(saveItem);
            popup.add(loadItem);
            popup.add(saveGCodeItem);
            popup.show(fileButton, 0, fileButton.getHeight());
        });


        exitButton.addActionListener(e -> System.exit(0));

        //cutterGroup = new ButtonGroup();
        //cutterHorizontal = createToolButton("―", "cutterHorizontal");
        //cutterVertical = createToolButton("|", "cutterVertical");
        //cutterRectangle = createToolButton("▬", "cutterRectangle");
        //cutterBorder = createToolButton("▭", "cutterBorder");
        //cutterL = createToolButton("L", "cutterL");
        //restrictedArea = createToolButton("ø", "restrictedArea");
        //selectTool = createToolButton("Select", "select");
        //
        //cutterHorizontal.setFont(new Font("Arial", Font.PLAIN, 15));
        //cutterVertical.setFont(new Font("Arial", Font.PLAIN, 15));
        //cutterRectangle.setFont(new Font("Arial", Font.PLAIN, 15));
        //cutterBorder.setFont(new Font("Arial", Font.PLAIN, 15));
        //cutterL.setFont(new Font("Arial", Font.PLAIN, 15));
        //restrictedArea.setFont(new Font("Arial", Font.PLAIN, 15));
        //selectTool.setFont(new Font("Arial", Font.PLAIN, 15));


        cutterGroup = new ButtonGroup();
        cutterHorizontal = createToolButton("―", "cutterHorizontal");
        cutterVertical = createToolButton("|", "cutterVertical");
        cutterRectangle = createToolButton("▬", "cutterRectangle");
        cutterBorder = createToolButton("▭", "cutterBorder");
        cutterL = createToolButton("L", "cutterL");
        restrictedArea = createToolButton("ø", "restrictedArea");
        selectTool = createToolButton("☝", "select");

        cutterHorizontal.setFont(new Font("", Font.PLAIN, 20));
        cutterVertical.setFont(new Font("", Font.PLAIN, 20));
        cutterRectangle.setFont(new Font("", Font.PLAIN, 20));
        cutterBorder.setFont(new Font("", Font.PLAIN, 20));
        cutterL.setFont(new Font("", Font.PLAIN, 20));
        restrictedArea.setFont(new Font("", Font.PLAIN, 20));
        selectTool.setFont(new Font("", Font.PLAIN, 20));

        cutterGroup.add(cutterHorizontal);
        cutterGroup.add(cutterVertical);
        cutterGroup.add(cutterRectangle);
        cutterGroup.add(cutterBorder);
        cutterGroup.add(cutterL);
        cutterGroup.add(restrictedArea);
        cutterGroup.add(selectTool);


        //toolPanel = new JPanel(new GridLayout(1, 2));
        //toolPanel = new JPanel(new BorderLayout());
        toolPanel = new JPanel(new CardLayout());

        toolRadioPanel = new JPanel(new GridLayout(1, 7));
        toolRadioPanel.add(cutterHorizontal);
        toolRadioPanel.add(cutterVertical);
        toolRadioPanel.add(cutterRectangle);
        toolRadioPanel.add(cutterBorder);
        toolRadioPanel.add(cutterL);
        toolRadioPanel.add(restrictedArea);
        toolRadioPanel.add(selectTool);


        borderCutPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        borderDimensionTextField = new JTextField();
        borderCutCancelButton = new JButton("Cancel");
        borderDimensionTextField.setText("Marge");
        borderCutPanel.add(borderDimensionTextField);
        borderCutPanel.add(borderCutCancelButton);
        borderCutPanel.setVisible(false);
        // Ajouter toolRadioPanel au toolPanel
        toolPanel.add(toolRadioPanel, "radio");
        toolPanel.add(borderCutPanel, "border");

        borderDimensionTextField.addActionListener(e -> {
            try {
                this.bordureSize = ConversionPiedMM.convertirEnMillimetres(borderDimensionTextField.getText());
                // Validation
                if (this.bordureSize <= 0 ||
                        this.bordureSize > this.mainFrame.cncController.getPanelDTO().heightFactor ||
                        this.bordureSize > this.mainFrame.cncController.getPanelDTO().widthFactor) {
                    this.bordureSize = 1;
                    JOptionPane.showMessageDialog(this, "Veuillez rester dans les limites du panneau", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                // Mise à jour de la bordure si nécessaire
                // this.mainFrame.cncController.changeBorderSize(this.bordureSize);
                borderDimensionTextField.setText(ConversionPiedMM.convertirEnMillimetresString(this.bordureSize));
                this.mainFrame.gridPanel.repaint();
            } catch (Exception ex) {
                borderDimensionTextField.setText("Invalid input");
                JOptionPane.showMessageDialog(this, "Entrée invalide pour la dimension", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        cutterBorder.addActionListener(e -> {
            this.isBorderCutMode = true;
            mainFrame.setSelectedCuttingMethod("cutterBorder");

            CardLayout cl = (CardLayout) (toolPanel.getLayout());
            cl.show(toolPanel, "border");
        });
        borderCutCancelButton.addActionListener(e -> {
            this.isBorderCutMode = false;
            cutterRectangle.setSelected(true);
            mainFrame.setSelectedCuttingMethod("cutterRectangle");

            CardLayout cl = (CardLayout) (toolPanel.getLayout());
            cl.show(toolPanel, "radio");
        });


        cutterRectangle.setSelected(true);


        JPanel panneauPanel = new JPanel(new GridLayout(3, 2));
        panneauPanel.setBorder(BorderFactory.createTitledBorder("Panneau"));

        hauteurPanneauTextBox = new JTextField();
        largeurPanneauTextBox = new JTextField();
        epaisseurPanneauTextBox = new JTextField();
        panneauPanel.add(new JLabel("Hauteur"));
        panneauPanel.add(hauteurPanneauTextBox);
        panneauPanel.add(new JLabel("Largeur"));
        panneauPanel.add(largeurPanneauTextBox);
        panneauPanel.add(new JLabel("Epaisseur"));
        panneauPanel.add(epaisseurPanneauTextBox);


        hauteurPanneauTextBox.addActionListener(e -> {
            try {
                this.mainFrame.cncController.panelManager.changePanelSize(((  ConversionPiedMM.convertirEnMillimetres(largeurPanneauTextBox.getText()))), (ConversionPiedMM.convertirEnMillimetres((hauteurPanneauTextBox.getText()))));
                hauteurPanneauTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(this.mainFrame.cncController.getPanelDTO().heightFactor));
                this.mainFrame.gridPanel.repaint();
            } catch (Exception ex) {
                hauteurPanneauTextBox.setText("Invalid input");
            }

        });

        largeurPanneauTextBox.addActionListener(e -> {
            try {
                this.mainFrame.cncController.panelManager.changePanelSize((ConversionPiedMM.convertirEnMillimetres((largeurPanneauTextBox.getText()))), (ConversionPiedMM.convertirEnMillimetres((hauteurPanneauTextBox.getText()))));
                largeurPanneauTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(this.mainFrame.cncController.getPanelDTO().widthFactor));
                this.mainFrame.gridPanel.repaint();
            } catch (Exception ex) {
                largeurPanneauTextBox.setText("Invalid input");
            }

        });

        epaisseurPanneauTextBox.addActionListener(e -> {
            try {
                this.mainFrame.cncController.panelManager.changeEpaisseurPanel(epaisseurPanneauTextBox.getText());
                epaisseurPanneauTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(this.mainFrame.cncController.getPanelDTO().epaisseur));
                this.mainFrame.gridPanel.repaint();
            } catch (Exception ex) {
                epaisseurPanneauTextBox.setText("Invalid input");
            }

        });

        hauteurPanneauTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(this.mainFrame.cncController.getPanelDTO().heightFactor));
        largeurPanneauTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(this.mainFrame.cncController.getPanelDTO().widthFactor));
        epaisseurPanneauTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(this.mainFrame.cncController.getPanelDTO().epaisseur));


        header = new JLabel("                                                                     Outil");
        profondeurTextField = new JTextField("0.5mm");
        outilDropdown = new JComboBox<>();
        //populateDropdowns(outilDropdown);
        depthAndThicknessPanel = new JPanel(new BorderLayout());
        depthAndThicknessPanel.add(header, BorderLayout.NORTH);
        depthAndThicknessPanel.add(profondeurTextField, BorderLayout.WEST);

        //depthAndThicknessPanel.add(outilDropdown, BorderLayout.EAST);
        outilPanel = new JPanel(new GridLayout(2, 1));
        profondeurPanel = new JPanel(new GridLayout(1, 2));
        profondeurTextBox = new JTextField();
        profondeurPanel.add(new JLabel("Profondeur:"));
        profondeurPanel.add(profondeurTextBox);
        profondeurTextBox.setText("0.5mm");
        profondeurTextBox.addActionListener(e -> {
            try {
                if (ConversionPiedMM.convertirEnMillimetres(profondeurTextBox.getText()) > this.mainFrame.cncController.getPanelDTO().epaisseur) {
                    profondeurTextBox.setText("trop profond");
                }
                this.mainFrame.setProfondeur(ConversionPiedMM.convertirEnMillimetres(profondeurTextBox.getText()));
                this.profondeur = ConversionPiedMM.convertirEnMillimetres(profondeurTextBox.getText());
                this.profondeurTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(this.profondeur));
            } catch (Exception ex) {
                profondeurTextBox.setText("Invalid input");
            }
        });
        depthAndThicknessPanel.add(profondeurPanel, BorderLayout.WEST);
        outilButtonPanel = new JPanel(new GridLayout(1, 2));
        createOutilButton = new JButton("Create");
        deleteOutilButton = new JButton("Delete");
        modifyOutilButton = new JButton("Modify");
        outilButtonPanel.add(modifyOutilButton);


        outilPanel.add(outilDropdown, BorderLayout.WEST);
        outilButtonPanel.add(createOutilButton, BorderLayout.NORTH);
        outilButtonPanel.add(deleteOutilButton, BorderLayout.SOUTH);
        outilPanel.add(outilButtonPanel, BorderLayout.EAST);
        depthAndThicknessPanel.add(outilPanel, BorderLayout.EAST);
        createOutilButton.addActionListener(e -> {
            // Activer le mode création d'outil
            this.inToolCreationMode = true;

            // Supprimer le panneau outil actuel
            depthAndThicknessPanel.remove(outilPanel);

            // Ajouter le panneau de création d'outil
            depthAndThicknessPanel.add(outilCreationPanel, BorderLayout.EAST);

            // Réinitialiser les champs si nécessaire
            outilCreationNameField.setText("nom");
            epaisseurOutilTextField.setText("0.5\"");

            // Mettre à jour l'interface utilisateur
            depthAndThicknessPanel.revalidate();
            depthAndThicknessPanel.repaint();
        });
        deleteOutilButton.addActionListener(e -> {
            // Vérifier s'il y a un outil sélectionné
            if (selectedOutil != null) {
                // Supprimer l'outil du contrôleur
                this.mainFrame.cncController.deleteOutil(selectedOutil);

                // Récupérer la liste à jour après suppression
                List<OutilDTO> outilsMisAJour = this.mainFrame.cncController.getOutilsInDTO();

                // Mettre à jour le dropdown
                setOutils(outilsMisAJour);
                this.selectedOutil = null;
                this.mainFrame.cncController.updateCutsAfterOutilDeletion(); //ne fonctionne pas!!
                this.mainFrame.gridPanel.revalidate();
                this.mainFrame.gridPanel.repaint();

                //System.out.println(this.tool);
                //System.out.println(this.tool.id);

            } else {
                JOptionPane.showMessageDialog(this, "Aucun outil sélectionné à supprimer.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        outilCreationPanel = new JPanel(new GridLayout(2, 2));

        outilCreationNameField = new JTextField();
        outilCreationNameField.setText("Nom");
        //outilCreationPanel.add(new JLabel("Name:"));
        outilCreationPanel.add(outilCreationNameField);
        //outilCreationPanel.add(new JLabel("Depth:"));
        epaisseurOutilTextField = new JTextField();
        outilCreationPanel.add(epaisseurOutilTextField);
        cancelOutilCreationButton = new JButton("Cancel");
        outilCreationPanel.add(cancelOutilCreationButton);
        cancelOutilCreationButton.addActionListener(e -> {
            this.inToolCreationMode = false;

            // Supprimer le panneau de création d'outil
            depthAndThicknessPanel.remove(outilCreationPanel);

            // Réajouter le panneau outil original
            depthAndThicknessPanel.add(outilPanel, BorderLayout.EAST);

            // Mettre à jour l'interface utilisateur
            depthAndThicknessPanel.revalidate();
            depthAndThicknessPanel.repaint();
        });
        comfirmOutilCreationButton = new JButton("Confirm");
        outilCreationPanel.add(comfirmOutilCreationButton);
        comfirmOutilCreationButton.addActionListener(e -> {
            if (outilCreationNameField.getText().isEmpty()) {
                // Afficher un message d'erreur si le nom est vide
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nom pour l'outil.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!epaisseurOutilTextField.getText().isEmpty()) {
                if (ConversionPiedMM.convertirEnMillimetres(epaisseurOutilTextField.getText()) > 9999999) {
                    epaisseurOutilTextField.setText("Ne peux pas excéder 5mm de profondeur");
                    return;
                } else if (ConversionPiedMM.convertirEnMillimetres(epaisseurOutilTextField.getText()) <= 0) {
                    epaisseurOutilTextField.setText("La profondeur doit être supérieure à 0");
                    return;
                }
            } else {
                epaisseurOutilTextField.setText("Veuillez définir une profondeur d'outil");
                return;
            }

            OutilDTO outilDTO = new OutilDTO(outilCreationNameField.getText(), ConversionPiedMM.convertirEnMillimetres(epaisseurOutilTextField.getText()));
            this.mainFrame.cncController.addOutil(outilDTO);
            this.setOutils(this.mainFrame.cncController.getOutilsInDTO());

            // Réinitialiser les champs
            outilCreationNameField.setText("");
            epaisseurOutilTextField.setText("");

            // Désactiver le mode création d'outil
            this.inToolCreationMode = false;

            // Réajouter le panneau outil original
            depthAndThicknessPanel.remove(outilCreationPanel);
            depthAndThicknessPanel.add(outilPanel, BorderLayout.EAST);

            // Mettre à jour l'interface utilisateur
            depthAndThicknessPanel.revalidate();
            depthAndThicknessPanel.repaint();
        });
        setOutils(this.mainFrame.cncController.getOutilsInDTO());


        outilModificationPanel = new JPanel(new GridLayout(2, 2));
        outilModificationNameField = new JTextField();
        epaisseurOutilModificationTextField = new JTextField();
        cancelOutilModificationButton = new JButton("Cancel");
        confirmOutilModificationButton = new JButton("Confirm");

        outilModificationPanel.add(outilModificationNameField);
        outilModificationPanel.add(epaisseurOutilModificationTextField);
        outilModificationPanel.add(cancelOutilModificationButton);
        outilModificationPanel.add(confirmOutilModificationButton);

// Listener pour le bouton Modify
        modifyOutilButton.addActionListener(e -> {
            if (selectedOutil != null) {
                // On passe en mode modification
                this.inToolCreationMode = false; // pas le mode création, mais un mode modif
                // Retirer le outilPanel
                depthAndThicknessPanel.remove(outilPanel);

                // Pré-remplir les champs avec les infos de l'outil sélectionné
                outilModificationNameField.setText(selectedOutil.name);
                epaisseurOutilModificationTextField.setText(ConversionPiedMM.convertirEnMillimetresString(selectedOutil.epaisseur));

                // Ajouter le outilModificationPanel
                depthAndThicknessPanel.add(outilModificationPanel, BorderLayout.EAST);
                depthAndThicknessPanel.revalidate();
                depthAndThicknessPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un outil à modifier.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

// Listener pour le bouton Cancel dans le panel de modification
        cancelOutilModificationButton.addActionListener(e -> {
            // Annuler la modification et revenir à l'affichage normal
            depthAndThicknessPanel.remove(outilModificationPanel);
            depthAndThicknessPanel.add(outilPanel, BorderLayout.EAST);
            depthAndThicknessPanel.revalidate();
            depthAndThicknessPanel.repaint();
        });

// Listener pour le bouton Confirm dans le panel de modification
        confirmOutilModificationButton.addActionListener(e -> {
            String newName = outilModificationNameField.getText().trim();
            String newEpaisseurText = epaisseurOutilModificationTextField.getText().trim();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nom pour l'outil.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double newEpaisseur;
            try {
                newEpaisseur = ConversionPiedMM.convertirEnMillimetres(newEpaisseurText);
                if (newEpaisseur <= 0 || newEpaisseur > 99999999) {
                    JOptionPane.showMessageDialog(this, "trop fat", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Entrée invalide pour l'épaisseur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mettre à jour l'outil via le contrôleur
            OutilDTO updatedOutil = new OutilDTO(selectedOutil.id, newName, newEpaisseur);
            // Supposons que votre contrôleur dispose d'une méthode pour mettre à jour un outil.
            // Si non, vous pouvez supprimer l'ancien et ajouter le nouveau en conservant l'ID,
            // ou implémenter une telle méthode dans le contrôleur.
            mainFrame.cncController.updateOutil(selectedOutil, updatedOutil);

            // Rafraîchir la liste d'outils
            setOutils(mainFrame.cncController.getOutilsInDTO());

            // Revenir à l'affichage normal
            depthAndThicknessPanel.remove(outilModificationPanel);
            depthAndThicknessPanel.add(outilPanel, BorderLayout.EAST);
            depthAndThicknessPanel.revalidate();
            depthAndThicknessPanel.repaint();
            this.mainFrame.gridPanel.revalidate();
            this.mainFrame.gridPanel.repaint();
        });


        JPanel coordsAndUndoRedoPanel = new JPanel(new GridLayout(1, 2));
        JPanel coordsPanel = new JPanel(new GridLayout(1, 2));
        xMousePosition = new JLabel("X: 0");
        yMousePosition = new JLabel("Y: 0");
        coordsPanel.add(xMousePosition);
        coordsPanel.add(yMousePosition);

        undoButton = new JButton("←");
        redoButton = new JButton("→");

        JPanel undoRedoPanel = new JPanel(new GridLayout(1, 2));
        undoRedoPanel.add(undoButton);
        undoRedoPanel.add(redoButton);

        coordsAndUndoRedoPanel.add(coordsPanel);
        coordsAndUndoRedoPanel.add(undoRedoPanel);

        gridCheckboxPanel = new JPanel(new GridLayout(2, 1));
        gridVisibilityCheckbox = new JCheckBox("Show Grid");
        gridMagnifierCheckbox = new JCheckBox("Magnify Grid");
        gridVisibilityCheckbox.setSelected(true);
        gridVisibilityCheckbox.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.gridPanel.setGridVisible(gridVisibilityCheckbox.isSelected());
            }
        });
        gridMagnifierCheckbox.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.gridPanel.setMagnifyGrid(gridMagnifierCheckbox.isSelected());
            }
        });
        gridCheckboxPanel.add(gridVisibilityCheckbox);
        gridCheckboxPanel.add(gridMagnifierCheckbox);
        //gridSizeTextField = new JLabel("Grid size: 10");

        //gridHauteurSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999999999, 1));
        gridSizeTextField = new JTextField("100mm");
        gridConfirmButton = new JButton("Confirm");
        //gridLargeurSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999999999, 1));


        JPanel visibilityPanel = new JPanel(new GridLayout(1, 3));

        visibilityPanel.add(gridCheckboxPanel);


        visibilityPanel.add(gridSizeTextField);
        visibilityPanel.add(gridConfirmButton);
        //visibilityPanel.add(gridHauteurSpinner);
        //visibilityPanel.add(gridLargeurSpinner);
        gridConfirmButton.addActionListener(e -> {
            this.mainFrame.gridPanel.setStaticGridDimensions((int) ConversionPiedMM.convertirEnMillimetres(gridSizeTextField.getText()));
            this.mainFrame.gridPanel.repaint();
        });


        outilDropdown.addActionListener(e -> {
            System.out.println("size: " + this.outils + ", " + outilDropdown.getSelectedIndex());

            int selectedIndex = outilDropdown.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < outils.size()) {
                this.selectedOutil = outils.get(selectedIndex);
                System.out.println("Selected Outil: " + this.selectedOutil);
            }
        });


        undoButton.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.cncController.undo();
                this.setOutils(this.mainFrame.cncController.getOutilsInDTO());
                mainFrame.gridPanel.repaint();
            }
        });

        redoButton.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.cncController.redo();
                this.setOutils(this.mainFrame.cncController.getOutilsInDTO());
                mainFrame.gridPanel.repaint();
            }
        });


        add(filePanel);
        add(toolPanel);
        add(visibilityPanel);
        add(panneauPanel);
        add(depthAndThicknessPanel);
        add(coordsAndUndoRedoPanel);

        addToolActionListeners(mainFrame != null ? mainFrame.gridPanel : null);  // Updated for safety


    }

    private ActionListener saveGCodeAction() {
        return evt -> {
            // Créer un panneau avec deux champs de texte pour param1 et param2
            JPanel paramPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField param1Field = new JTextField();
            JTextField param2Field = new JTextField();

            paramPanel.add(new JLabel("Feedrate:"));
            paramPanel.add(param1Field);
            paramPanel.add(new JLabel("Laser Power:"));
            paramPanel.add(param2Field);

            // Afficher la boîte de dialogue pour entrer les paramètres
            int result = JOptionPane.showConfirmDialog(
                    MenuPanel.this,
                    paramPanel,
                    "Entrer les paramètres G-Code",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String param1Text = param1Field.getText().trim();
                String param2Text = param2Field.getText().trim();
                int param1, param2;

                // Validation des entrées
                try {
                    param1 = Integer.parseInt(param1Text);
                    param2 = Integer.parseInt(param2Text);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            MenuPanel.this,
                            "Veuillez entrer des entiers valides pour les paramètres.",
                            "Erreur de saisie",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return; // Arrêter l'exécution si les entrées sont invalides
                }

                // Ouvrir le JFileChooser pour sélectionner l'emplacement de sauvegarde
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Enregistrer le G-Code");
                int userSelection = fileChooser.showSaveDialog(MenuPanel.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    try {
                        // Vérifier et ajouter l'extension .gcode si nécessaire
                        if (!fileToSave.getName().toLowerCase().endsWith(".gcode")) {
                            fileToSave = new File(fileToSave.getAbsolutePath() + ".gcode");
                        }

                        // Appeler la méthode du contrôleur avec les paramètres
                        mainFrame.cncController.saveGCode(fileToSave, param1, param2);
                        JOptionPane.showMessageDialog(MenuPanel.this, "Sauvegarde du G-Code réussie !");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(MenuPanel.this,
                                "Erreur lors de la sauvegarde du G-Code : " + ex.getMessage(),
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
    }

    private ActionListener loadAction() {
        return evt -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Charger un projet");
            int userSelection = fileChooser.showOpenDialog(MenuPanel.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToOpen = fileChooser.getSelectedFile();
                try {
                    // Appel à la méthode load du contrôleur
                    mainFrame.cncController.load(fileToOpen);

                    // Mettre à jour l'interface après le chargement
                    PanelDTO panelDTO = mainFrame.cncController.getPanelDTO();
                    hauteurPanneauTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(panelDTO.heightFactor));
                    largeurPanneauTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(panelDTO.widthFactor));
                    epaisseurPanneauTextBox.setText(ConversionPiedMM.convertirEnMillimetresString(panelDTO.epaisseur));

                    // Mettre à jour la liste d'outils
                    setOutils(mainFrame.cncController.getOutilsInDTO());

                    // Redessiner le panneau graphique
                    mainFrame.gridPanel.revalidate();
                    mainFrame.gridPanel.repaint();

                    JOptionPane.showMessageDialog(MenuPanel.this, "Chargement réussi !");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MenuPanel.this,
                            "Erreur lors du chargement : " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }


    private ActionListener saveAction() {
        return evt -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le projet");
            int userSelection = fileChooser.showSaveDialog(MenuPanel.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    // Appel à la méthode save du contrôleur
                    mainFrame.cncController.save(fileToSave);
                    JOptionPane.showMessageDialog(MenuPanel.this, "Sauvegarde réussie !");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MenuPanel.this,
                            "Erreur lors de la sauvegarde : " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }



    public void setOutils(List<OutilDTO> outils) {
        this.outils = outils;
        outilDropdown.removeAllItems();
        for (OutilDTO outil : outils) {
            outilDropdown.addItem(outil.name);
        }
        if (!outils.isEmpty()) {
            outilDropdown.setSelectedIndex(0);
            this.selectedOutil = outils.get(0);
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private JRadioButton createToolButton(String label, String actionCommand) {
        JRadioButton button = new JRadioButton(label);
        button.setActionCommand(actionCommand);
        return button;
    }

    private void addToolActionListeners(GridPanel gridPanel) {
        cutterHorizontal.addActionListener(e -> mainFrame.setSelectedCuttingMethod("cutterHorizontal"));
        cutterVertical.addActionListener(e -> mainFrame.setSelectedCuttingMethod("cutterVertical"));
        cutterRectangle.addActionListener(e -> mainFrame.setSelectedCuttingMethod("cutterRectangle"));
        //cutterBorder.addActionListener(e -> mainFrame.setSelectedCuttingMethod("cutterBorder"));
        cutterL.addActionListener(e -> mainFrame.setSelectedCuttingMethod("cutterL"));
        restrictedArea.addActionListener(e -> mainFrame.setSelectedCuttingMethod("restrictedArea"));
        selectTool.addActionListener(e -> mainFrame.setSelectedCuttingMethod("select"));


    }

    public void setMouseCoordsPanel(double x, double y) {
        xMousePosition.setText("X: " + x);
        yMousePosition.setText("Y: " + y);
    }


    private void buildEditPanelBasedOnCutType(InteractiveEntityDTO cutDTO) {
        PanelDTO panelDTO = mainFrame.cncController.getPanelDTO();
        //if (selectedCut != null && selectedCut.id == cutDTO.id)//selectedCut.equals(cutDTO)
        if (selectedCut != null && selectedCut.id.equals(cutDTO.id))//selectedCut.equals(cutDTO)
        {
            return;
        }

        // Remove existing edit panel if it exists
        if (editPanel != null) {
            this.remove(editPanel);
            editPanel = null;
        }

        // Set the new selected DTO
        selectedCut = cutDTO;

        // Initialize the edit panel with a dynamic layout
        editPanel = new JPanel();
        editPanel.setBorder(BorderFactory.createTitledBorder("Edit"));
        editPanel.setLayout(new GridLayout(0, 2)); // Flexible rows

        // Populate the edit panel based on the type of cut
        if (cutDTO instanceof VerticalLineDTO) {
            VerticalLineDTO verticalLineDTO = (VerticalLineDTO) cutDTO;

            xValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(verticalLineDTO.x));
            this.profondeurTextField.setText(String.valueOf(verticalLineDTO.profondeur)); // Sans "mm"
            if (verticalLineDTO.tool != null) {
                this.outilDropdown.setSelectedItem(verticalLineDTO.tool.name);
            } else {
                System.out.println("OHAOSHDOISAHDOIHDOIAHSDOIHD");
                //this.outilDropdown.setSelectedItem("Veuillez sélectionner un outil");
                this.outilDropdown.setSelectedItem(null);
            }
            editPanel.add(new JLabel("X:"));
            editPanel.add(xValue);
        } else if (cutDTO instanceof HorizontalLineDTO) {
            HorizontalLineDTO horizontalLineDTO = (HorizontalLineDTO) cutDTO;

            yValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(horizontalLineDTO.y));
            this.profondeurTextField.setText(String.valueOf(horizontalLineDTO.profondeur)); // Sans "mm"
            if (horizontalLineDTO.tool != null) {
                this.outilDropdown.setSelectedItem(horizontalLineDTO.tool.name);
            } else {
                //this.outilDropdown.setSelectedItem("Veuillez sélectionner un outil");
                this.outilDropdown.setSelectedItem(null);
            }

            editPanel.add(new JLabel("Y:"));
            editPanel.add(yValue);
        } else if (cutDTO instanceof SquareDTO) {
            SquareDTO squareDTO = (SquareDTO) cutDTO;

            xValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(squareDTO.x));
            yValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(squareDTO.y));
            heightValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(squareDTO.heightFactor));
            widthValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(squareDTO.widthFactor));
            this.profondeurTextField.setText(String.valueOf(squareDTO.profondeur)); // Sans "mm"
            //this.outilDropdown.setSelectedItem(squareDTO.tool.name); // Utiliser le nom de l'outil

            this.outilDropdown.setSelectedItem(((squareDTO.tool == null) ?  null : squareDTO.tool.name)); // Utiliser le nom de l'outil

            editPanel.add(new JLabel("X:"));
            editPanel.add(xValue);
            editPanel.add(new JLabel("Y:"));
            editPanel.add(yValue);
            editPanel.add(new JLabel("Height:"));
            editPanel.add(heightValue);
            editPanel.add(new JLabel("Width:"));
            editPanel.add(widthValue);
        } else if (cutDTO instanceof LDTO) {
            LDTO lDTO = (LDTO) cutDTO;
            widthValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(Math.abs(lDTO.widthFactor)));
            heightValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(Math.abs(lDTO.heightFactor)));
            xValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(lDTO.x));
            yValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(lDTO.y));
            this.profondeurTextField.setText(String.valueOf(lDTO.profondeur)); // Sans "mm"
            //this.outilDropdown.setSelectedItem(lDTO.tool.name); // Utiliser le nom de l'outil
            this.outilDropdown.setSelectedItem(((lDTO.tool == null) ?  null : lDTO.tool.name)); // Utiliser le nom de l'outil

            editPanel.add(new JLabel("X:"));
            editPanel.add(xValue);
            editPanel.add(new JLabel("Y:"));
            editPanel.add(yValue);
            editPanel.add(new JLabel("Hauteur:"));
            editPanel.add(heightValue);
            editPanel.add(new JLabel("Largeur:"));
            editPanel.add(widthValue);
        } else if (cutDTO instanceof RestrictedAreaDTO) {
            RestrictedAreaDTO restrictedAreaDTO = (RestrictedAreaDTO) cutDTO;

            xValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(restrictedAreaDTO.x));
            yValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(restrictedAreaDTO.y));
            heightValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(restrictedAreaDTO.heightFactor));
            widthValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(restrictedAreaDTO.widthFactor));

            editPanel.add(new JLabel("X:"));
            editPanel.add(xValue);
            editPanel.add(new JLabel("Y:"));
            editPanel.add(yValue);
            editPanel.add(new JLabel("Height:"));
            editPanel.add(heightValue);
            editPanel.add(new JLabel("Width:"));
            editPanel.add(widthValue);
        } else if (cutDTO instanceof BordureDTO) {
            BordureDTO bordureDTO = (BordureDTO) cutDTO;

            widthValue = new JTextField(ConversionPiedMM.convertirEnMillimetresString(bordureSize));
            this.profondeurTextField.setText(String.valueOf(bordureDTO.profondeur));
            //this.outilDropdown.setSelectedItem(bordureDTO.tool.name);
            this.outilDropdown.setSelectedItem(((bordureDTO.tool == null) ?  null : bordureDTO.tool.name)); // Utiliser le nom de l'outil


            editPanel.add(new JLabel("Marge:"));
            editPanel.add(widthValue);
        }
        deleteButton = new JButton("Delete");
        // Add common buttons
        if (!(cutDTO instanceof PointReferenceDTO)) {

            editPanel.add(deleteButton);
            // Restricted area does not have a tool
            //return;
        }

        modifyButton = new JButton("Modify");

        
        editPanel.add(modifyButton);

        // Add event listeners for Delete and Modify buttons
        deleteButton.addActionListener(e -> {
            editMode = false;
            mainFrame.cncController.cutManager.deleteSelectedCut();
            mainFrame.gridPanel.repaint();
            revalidate();
            repaint();
            

        });

        modifyButton.addActionListener(e -> {
            if (cutDTO instanceof VerticalLineDTO) {
                VerticalLineDTO verticalLineDTO = (VerticalLineDTO) cutDTO;
                try {
                    verticalLineDTO.x = ConversionPiedMM.convertirEnMillimetres(xValue.getText());
                } catch (Exception exception) {
                    xValue.setText("Invalid input");
                }
                try {
                    verticalLineDTO.profondeur = ConversionPiedMM.convertirEnMillimetres(profondeurTextField.getText());
                } catch (Exception exception) {
                    profondeurTextField.setText("Invalid input");
                }
                verticalLineDTO.tool = (this.selectedOutil == null) ? null : this.selectedOutil;
            } else if (cutDTO instanceof HorizontalLineDTO) {
                HorizontalLineDTO horizontalLineDTO = (HorizontalLineDTO) cutDTO;
                try {
                    horizontalLineDTO.y = ConversionPiedMM.convertirEnMillimetres(yValue.getText());
                } catch (Exception exception) {
                    yValue.setText("Invalid input");
                }
                try {
                    horizontalLineDTO.profondeur = ConversionPiedMM.convertirEnMillimetres(profondeurTextField.getText());
                } catch (Exception exception) {
                    profondeurTextField.setText("Invalid input");
                }
                horizontalLineDTO.tool = (this.selectedOutil == null) ? null : this.selectedOutil;
            } else if (cutDTO instanceof SquareDTO) {
                SquareDTO squareDTO = (SquareDTO) cutDTO;
                try {
                    squareDTO.x = ConversionPiedMM.convertirEnMillimetres(xValue.getText());
                } catch (Exception exception) {
                    xValue.setText("Invalid input");
                }
                try {
                    squareDTO.y = ConversionPiedMM.convertirEnMillimetres(yValue.getText());
                } catch (Exception exception) {
                    yValue.setText("Invalid input");
                }
                try {
                    squareDTO.heightFactor = ConversionPiedMM.convertirEnMillimetres(heightValue.getText());
                } catch (Exception exception) {
                    heightValue.setText("Invalid input");
                }
                try {
                    squareDTO.widthFactor = ConversionPiedMM.convertirEnMillimetres(widthValue.getText());
                } catch (Exception exception) {
                    widthValue.setText("Invalid input");
                }
                try {
                    squareDTO.profondeur = ConversionPiedMM.convertirEnMillimetres(profondeurTextField.getText());
                } catch (Exception exception) {
                    profondeurTextField.setText("Invalid input");
                }
                squareDTO.tool = (this.selectedOutil == null) ? null : this.selectedOutil;
            } else if (cutDTO instanceof LDTO) {
                LDTO lDTO = (LDTO) cutDTO;
                try {
                    lDTO.x = ConversionPiedMM.convertirEnMillimetres(xValue.getText());
                } catch (Exception exception) {
                    xValue.setText("Invalid input");
                }
                try {
                    lDTO.y = ConversionPiedMM.convertirEnMillimetres(yValue.getText());
                } catch (Exception exception) {
                    yValue.setText("Invalid input");
                }
                try {
                    lDTO.widthFactor = ConversionPiedMM.convertirEnMillimetres(widthValue.getText());
                } catch (Exception exception) {
                    widthValue.setText("Invalid input");
                }
                try {
                    lDTO.heightFactor = ConversionPiedMM.convertirEnMillimetres(heightValue.getText());
                } catch (Exception exception) {
                    heightValue.setText("Invalid input");
                }
                try {
                    lDTO.profondeur = ConversionPiedMM.convertirEnMillimetres(profondeurTextField.getText());
                } catch (Exception exception) {
                    profondeurTextField.setText("Invalid input");
                }
                lDTO.tool = (this.selectedOutil == null) ? null : this.selectedOutil;
            } else if (cutDTO instanceof RestrictedAreaDTO) {
                RestrictedAreaDTO restrictedAreaDTO = (RestrictedAreaDTO) cutDTO;
                try {
                    restrictedAreaDTO.x = ConversionPiedMM.convertirEnMillimetres(xValue.getText());
                } catch (Exception exception) {
                    xValue.setText("Invalid input");
                }
                try {
                    restrictedAreaDTO.y = ConversionPiedMM.convertirEnMillimetres(yValue.getText());
                } catch (Exception exception) {
                    yValue.setText("Invalid input");
                }
                try {
                    restrictedAreaDTO.heightFactor = ConversionPiedMM.convertirEnMillimetres(heightValue.getText());
                } catch (Exception exception) {
                    heightValue.setText("Invalid input");
                }
                try {
                    restrictedAreaDTO.widthFactor = ConversionPiedMM.convertirEnMillimetres(widthValue.getText());
                } catch (Exception exception) {
                    widthValue.setText("Invalid input");
                }
            } else if (cutDTO instanceof BordureDTO) {
                BordureDTO bordureDTO = (BordureDTO) cutDTO;
                try {
                    bordureDTO.widthFactor = panelDTO.widthFactor - ConversionPiedMM.convertirEnMillimetres(widthValue.getText()) * 2;
                } catch (Exception exception) {
                    widthValue.setText("Invalid input");
                }
                try {
                    bordureDTO.heightFactor = panelDTO.heightFactor - ConversionPiedMM.convertirEnMillimetres(widthValue.getText()) * 2;
                } catch (Exception exception) {
                    widthValue.setText("Invalid input");
                }
                try {
                    bordureDTO.x = panelDTO.x + ConversionPiedMM.convertirEnMillimetres(widthValue.getText());
                } catch (Exception exception) {
                    widthValue.setText("Invalid input");
                }
                try {
                    bordureDTO.y = panelDTO.y + ConversionPiedMM.convertirEnMillimetres(widthValue.getText());
                } catch (Exception exception) {
                    widthValue.setText("Invalid input");
                }
                try {
                    bordureDTO.profondeur = ConversionPiedMM.convertirEnMillimetres(profondeurTextField.getText());
                } catch (Exception exception) {
                    profondeurTextField.setText("Invalid input");
                }
                bordureDTO.tool = (this.selectedOutil == null) ? null : this.selectedOutil;
            }



            System.out.println(cutDTO);
            mainFrame.cncController.updateSelectedCutFromDTO((CutDTO) cutDTO);
            mainFrame.gridPanel.repaint();
            revalidate();
            repaint();
        });

        // Add the edit panel below the tool and thickness section
        this.add(editPanel, 5);
        revalidate();
        repaint();
    }


    public void toggleEditArea(InteractiveEntityDTO cutDTO) {
        // Check if the same DTO is already being edited
        //todo: change to check if id match rather than whole object
        if (editMode && selectedCut != null && selectedCut.equals(cutDTO)) {
            System.out.println("nothing selected");

            return;
        }

        //if (selectedCut.id == cutDTO.id)
        //{
        //    System.out.println("its the same cut so no edit panel change");
        //   return;
        //}

        editMode = (cutDTO != null);

        if (editMode) {
            this.setLayout(new GridLayout(7, 1));

            buildEditPanelBasedOnCutType(cutDTO);
            /*if(cutDTO instanceof VerticalLineDTO) {

            }*/
        } else {
            if (editPanel != null) {
                for (Component component : editPanel.getComponents()) {
                    component = null;
                }
                this.remove(editPanel);
                this.setLayout(new GridLayout(6, 1));

                editPanel = null;
            }
            //outil and thickness dropdowns should return to their normal behaviour

            selectedCut = null;
            revalidate();
            repaint();
        }
    }


    // Classe interne pour stocker les données à sauvegarder/charger
    // Vous pouvez adapter ce qu'elle contient en fonction de vos besoins.

}