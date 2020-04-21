package Lab1.a;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm extends JFrame {

    private JButton increasePriorityButton;
    private JButton increasePriorityButton1;
    private JButton decreasePriorityButton;
    private JButton decreasePriorityButton1;
    private JSlider jSlider;
    private JPanel jPanel;

    public MainForm() {

        super("Result");
        setContentPane(jPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(1000, 500));
        setVisible(true);
        setResizable(true);
        pack();

        final ConcurrentThreads concThreads = new ConcurrentThreads(jSlider);
        concThreads.startTesting();

        increasePriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                concThreads.incDecPriority();
            }
        });


        decreasePriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                concThreads.decDecPriority();
            }
        });

        increasePriorityButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                concThreads.incIncPriority();
            }
        });

        decreasePriorityButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                concThreads.decIncPriority();
            }
        });
    }

}
