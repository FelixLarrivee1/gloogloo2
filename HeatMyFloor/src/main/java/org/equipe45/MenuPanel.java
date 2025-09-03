package org.equipe39;


import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.CutDTO;
import org.equipe39.dto.VerticalLineDTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MenuPanel extends JPanel {

    public int depth, thickness;
    private JComboBox<String> thicknessDropdown, depthDropdown;
    private JLabel header;
    private JButton fileButton, exitButton, undoButton, redoButton;
    private JCheckBox gridVisibilityCheckbox;
    private JLabel gridSizeTextField;
    private ButtonGroup cutterGroup;
    private JRadioButton cutterHorizontal, cutterVertical, cutterRectangle, cutterL, restrictedArea;

    private JSpinner hauteurSpinner, largeurSpinner;
    private MainFrame mainFrame;  // Reference to the MainFrame
    private JPanel editPanel;
    public boolean editMode = false;
    private JComboBox<String> thicknessDropdownEdit, depthDropdownEdit;
    //delete and modify buttons
    private JButton deleteButton, modifyButton;


    //text input for the X value
    private JTextField xValue;

    // Existing constructor (empty)
    public MenuPanel() {
        this(null);  // Call the constructor that accepts MainFrame, with null
    }

    // Constructor that accepts MainFrame
    public MenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(250, 200));
        setBackground(Color.LIGHT_GRAY);
        setLayout(new GridLayout(7, 1));

        fileButton = new JButton("File");
        exitButton = new JButton("Exit");



        JPanel filePanel = new JPanel(new GridLayout(1, 2));
        filePanel.add(fileButton);
        filePanel.add(exitButton);

        exitButton.addActionListener(e -> System.exit(0));

        cutterGroup = new ButtonGroup();
        cutterHorizontal = createToolButton("-", "cutterHorizontal");
        cutterVertical = createToolButton("|", "cutterVertical");
        cutterRectangle = createToolButton("▭", "cutterRectangle");
        cutterL = createToolButton("L", "cutterL");
        restrictedArea = createToolButton("⯃", "restrictedArea");

        cutterGroup.add(cutterHorizontal);
        cutterGroup.add(cutterVertical);
        cutterGroup.add(cutterRectangle);
        cutterGroup.add(cutterL);
        cutterGroup.add(restrictedArea);

        JPanel toolPanel = new JPanel(new GridLayout(1, 5));
        toolPanel.add(cutterHorizontal);
        toolPanel.add(cutterVertical);
        toolPanel.add(cutterRectangle);
        toolPanel.add(cutterL);
        toolPanel.add(restrictedArea);

        cutterRectangle.setSelected(true);


        JPanel panneauPanel = new JPanel(new GridLayout(1, 2));
        panneauPanel.setBorder(BorderFactory.createTitledBorder("Panneau"));              

        SpinnerNumberModel hauteurModel = new SpinnerNumberModel(1, 1, 5, 1);
        hauteurSpinner = new JSpinner(hauteurModel);
        JSpinner.DefaultEditor hauteurEditor = (JSpinner.DefaultEditor) hauteurSpinner.getEditor();

        hauteurEditor.getTextField().setEditable(false);
        hauteurSpinner.setPreferredSize(new Dimension(60, 20));

        panneauPanel.add(new JLabel("Hauteur"));
        panneauPanel.add(hauteurSpinner);

        SpinnerNumberModel largeurModel = new SpinnerNumberModel(1, 1, 10, 1);
        largeurSpinner = new JSpinner(largeurModel);
        JSpinner.DefaultEditor largeurEditor = (JSpinner.DefaultEditor) largeurSpinner.getEditor();
        largeurEditor.getTextField().setEditable(false);
        largeurSpinner.setPreferredSize(new Dimension(60, 20));
        panneauPanel.add(new JLabel("Largeur"));
        panneauPanel.add(largeurSpinner);

        header = new JLabel("Thickness and Depth");
        thicknessDropdown = new JComboBox<>();
         depthDropdown = new JComboBox<>();
        populateDropdowns(thicknessDropdown, depthDropdown);
        JPanel depthAndThicknessPanel = new JPanel(new BorderLayout());
        depthAndThicknessPanel.add(header, BorderLayout.NORTH);
        depthAndThicknessPanel.add(thicknessDropdown, BorderLayout.WEST);
        depthAndThicknessPanel.add(depthDropdown, BorderLayout.EAST);

        addDropdownListeners(mainFrame != null ? mainFrame.gridPanel : null);  // Updated for safety

        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");

        JPanel undoRedoPanel = new JPanel(new GridLayout(1, 2));
        undoRedoPanel.add(undoButton);
        undoRedoPanel.add(redoButton);

        gridVisibilityCheckbox = new JCheckBox("Show Grid");
        gridVisibilityCheckbox.setSelected(true);
        gridVisibilityCheckbox.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.gridPanel.setGridVisible(gridVisibilityCheckbox.isSelected());
            }
        });

        gridSizeTextField = new JLabel("Grid size: 10");

        JPanel visibilityPanel = new JPanel(new GridLayout(1, 2));
        visibilityPanel.add(gridVisibilityCheckbox);
        visibilityPanel.add(gridSizeTextField);


        //xValue = new JTextField();
        //editPanel = new JPanel(new GridLayout(2, 2));
        //editPanel.setBorder(new TitledBorder("Edit"));
        //editPanel.add(new JLabel("X:"));
        //editPanel.add(xValue);
        // Add edit components

        xValue = new JTextField();
        editPanel = new JPanel(new GridLayout(2, 2));
        editPanel.setBorder(new TitledBorder("Edit"));

        // Initialize edit dropdowns
        thicknessDropdownEdit = new JComboBox<>();
        depthDropdownEdit = new JComboBox<>();
        populateDropdowns(thicknessDropdownEdit, depthDropdownEdit);
        editPanel.add(new JLabel("X:"));
        editPanel.add(xValue);
        editPanel.add(new JLabel("Thickness:"));
        editPanel.add(thicknessDropdownEdit);
        editPanel.add(new JLabel("Depth:"));
        editPanel.add(depthDropdownEdit);
        //delete and modify buttons
        deleteButton = new JButton("Delete");
        modifyButton = new JButton("Modify");
        editPanel.add(deleteButton);
        editPanel.add(modifyButton);

        //editPanel.setVisible(false);
        

        add(filePanel);
        add(toolPanel);
        add(visibilityPanel);
        add(panneauPanel);
        add(depthAndThicknessPanel);
        add(editPanel);
        add(undoRedoPanel);

        //remove(panneauPanel);
        //remove(undoRedoPanel);
        addToolActionListeners(mainFrame != null ? mainFrame.gridPanel : null);  // Updated for safety

        // Edit dropdown listeners to update CutDTO
        //thicknessDropdownEdit.addActionListener(e -> {
        //    if (editMode) {
        //        mainFrame.cncController.getSelectedCut().setThickness(Integer.parseInt((String) thicknessDropdownEdit.getSelectedItem()));
        //    }
        //});
//
        //depthDropdownEdit.addActionListener(e -> {
        //    if (editMode) {
        //        mainFrame.cncController.getSelectedCut().setDepth(Float.parseFloat((String) depthDropdownEdit.getSelectedItem()));
        //    }
        //});

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
        cutterL.addActionListener(e -> mainFrame.setSelectedCuttingMethod("cutterL"));
        restrictedArea.addActionListener(e -> mainFrame.setSelectedCuttingMethod("restrictedArea"));

        hauteurSpinner.addChangeListener(e -> {
                this.mainFrame.cncController.changePanelSize( (int) largeurSpinner.getValue(), (int) hauteurSpinner.getValue());
            this.mainFrame.gridPanel.repaint();
        });
        //hauteurSpinner.setValue(2);
        largeurSpinner.addChangeListener(e -> {
            this.mainFrame.cncController.changePanelSize((int) largeurSpinner.getValue(), (int) hauteurSpinner.getValue());
            this.mainFrame.gridPanel.repaint();

        });
        //largeurSpinner.setValue(3);

        //todo: change

    }

    private void populateDropdowns(JComboBox<String> thicknessDropdown, JComboBox<String> depthDropdown) {
        thicknessDropdown.addItem("Thickness");
        depthDropdown.addItem("Depth");
        for (int i = 1; i <= 12; i++) {
            thicknessDropdown.addItem(Integer.toString(i));
        }
        for (int i = 1; i <= 6; i++) {
            depthDropdown.addItem(Float.toString(i * 0.1f));
        }
    }

    private void addDropdownListeners(GridPanel gridPanel) {
        thicknessDropdown.addActionListener(e -> {
            this.mainFrame.setThickness(Integer.parseInt((String) thicknessDropdown.getSelectedItem()));
            this.thickness = (Integer.parseInt((String) thicknessDropdown.getSelectedItem()));
        });
        depthDropdown.addActionListener(e -> {
            this.mainFrame.setDepth((int) Float.parseFloat((String) depthDropdown.getSelectedItem()));
            this.depth = ((int) Float.parseFloat((String) depthDropdown.getSelectedItem()));
        });
    }

    public void setGridSize(int size) {
        gridSizeTextField.setText(Integer.toString(size));
    }


    public void toggleEditArea(CutDTO cutDTO) {
        editMode = (cutDTO != null);

        for (Component component : editPanel.getComponents()) {
            component.setVisible(editMode);
        }

        if (editMode && cutDTO instanceof VerticalLineDTO) {
            VerticalLineDTO verticalLineDTO = (VerticalLineDTO) cutDTO;
            xValue.setText(Double.toString(ConversionPiedMM.GridUnitEnMillimetre(verticalLineDTO.x)));
            xValue.addActionListener(e ->  verticalLineDTO.x = (int) ConversionPiedMM.MillimetreEnGridUnit(Double.parseDouble(xValue.getText())));
            System.out.println(verticalLineDTO.x);

            //ConversionPiedMM.GridUnitEnMillimetre()
            //xValue.setText(Integer.toString(verticalLineDTO.x));
            thicknessDropdownEdit.setSelectedItem(Integer.toString(verticalLineDTO.thickness));
            depthDropdownEdit.setSelectedItem(Float.toString(verticalLineDTO.depth));
            thicknessDropdownEdit.addActionListener(e -> verticalLineDTO.thickness = (Integer.parseInt((String) thicknessDropdownEdit.getSelectedItem())));
            depthDropdownEdit.addActionListener(e -> verticalLineDTO.depth = (Float.parseFloat((String) depthDropdownEdit.getSelectedItem())));
                    deleteButton.addActionListener(e -> {
                mainFrame.cncController.deleteSelectedCut();
                mainFrame.gridPanel.repaint();
            });
            modifyButton.addActionListener(e -> {
            mainFrame.cncController.updateSelectedCutFromDTO(verticalLineDTO);
            mainFrame.gridPanel.repaint();
        });
            System.out.println(verticalLineDTO.depth);
        }
    }
}
