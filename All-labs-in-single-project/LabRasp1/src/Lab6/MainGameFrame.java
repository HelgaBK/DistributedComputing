package Lab6;

import javax.swing.*;
import java.awt.*;

public class MainGameFrame extends JFrame {
    private FieldPanel fieldPanel;
    private JButton buttonStart;
    private JButton buttonStop;
    private int numberOfCivilizations;

    public MainGameFrame(int fieldWidth, int fieldHeight, int cellSize, int numberOfCivilizations) {
        super("Game Of Life");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //------------------------------------------------------------------FIELD GRID
        fieldPanel = new FieldPanel(fieldWidth, fieldHeight, cellSize);
        this.add(fieldPanel);
        //----------------------------------------------------------------TOOLBAR
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        this.add(tools, BorderLayout.NORTH);

        buttonStart = new JButton("Start");
        tools.add(buttonStart);

        buttonStop = new JButton("Stop");
        buttonStop.setEnabled(false);
        tools.add(buttonStop);

        this.numberOfCivilizations = numberOfCivilizations;
        if (0 >= numberOfCivilizations || numberOfCivilizations > 5) throw new IllegalArgumentException("number of civilizations is illegal");

        //------------------------------------------------------------------ACTION LISTENERS
        buttonStart.addActionListener(e -> {
            fieldPanel.startLife(numberOfCivilizations);
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);
        });

        buttonStop.addActionListener(e -> {
            fieldPanel.stopLife();
            buttonStop.setEnabled(false);
            buttonStart.setEnabled(true);
        });

        pack();
        setVisible(true);

    }
}
