 package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import domain.Whiteboard;

public class WhiteboardGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private WhiteboardPanel whiteboardPanel;
    private GroupLayout layout;
    private ButtonGroup colorSelector;
    private JRadioButton blackButton, whiteButton;

    public WhiteboardGUI(Whiteboard whiteboard) {
        super(whiteboard.getName());
        this.whiteboardPanel = new WhiteboardPanel(whiteboard);
        
        //Setup Group Layout
        Container cp = this.getContentPane();
        layout = new GroupLayout(cp);
        cp.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        this.colorSelector = new ButtonGroup();
        this.blackButton = new JRadioButton("Draw");
        this.blackButton.setSelected(true);
        this.colorSelector.add(this.blackButton);
        this.whiteButton = new JRadioButton("Erase");
        this.colorSelector.add(this.whiteButton);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(blackButton) 
                        .addComponent(whiteButton))
                .addComponent(whiteboardPanel)        
                );
        
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(blackButton)
                        .addComponent(whiteButton))
                .addComponent(whiteboardPanel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
            );
      
        this.pack();
        
        this.blackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.BLACK);
                whiteboardPanel.setDrawThickness(1);
            }
        });
        
        this.whiteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.WHITE);
                whiteboardPanel.setDrawThickness(30);
            }
        });
    }

}
