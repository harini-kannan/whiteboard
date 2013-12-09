 package client;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import client.networking.ClientSocket;
import client.networking.DrawingDelegate;
import client.networking.DrawingRequestHandler;
import domain.Drawable;
import domain.Whiteboard;

/**
 * A GUI interface that inlcudes a WhiteboardPanel and some tools that
 * change drawing settings. Also includes a list of users logged in.
 *
 */
public class WhiteboardGUI extends JDialog implements DrawingDelegate {
    private static final long serialVersionUID = 1L;
    
    private final WhiteboardPanel whiteboardPanel;
    private final GroupLayout layout;
    private final ButtonGroup colorSelector;
    private final JSlider widthSelector;
    private final JRadioButton redButton, orangeButton, yellowButton, greenButton, blueButton, blackButton, whiteButton;
    private final JList<String> userList;
    
    private ClientSocket clientSocket;

    public WhiteboardGUI(JFrame owner, Whiteboard whiteboard, ClientSocket clientSocket) {
        super(owner, "White Board - " + whiteboard.getName(), true);  // modal
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.whiteboardPanel = new WhiteboardPanel(whiteboard, clientSocket);
        
        DrawingRequestHandler drawingRequestHandler = new DrawingRequestHandler(this);
        this.clientSocket = clientSocket;
        this.clientSocket.switchHandler(drawingRequestHandler);
        
        //Setup Group Layout
        Container cp = this.getContentPane();
        layout = new GroupLayout(cp);
        cp.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String username : whiteboard.getUsers()) {
            listModel.addElement(username);
        }
        
        this.widthSelector = new JSlider(JSlider.VERTICAL,0,100,1);
        this.widthSelector.setMajorTickSpacing(10);
        this.widthSelector.setMinorTickSpacing(5);
        this.widthSelector.setPaintTicks(true);
        this.widthSelector.setPaintLabels(true);
        
        this.colorSelector = new ButtonGroup();
        
        this.blackButton = new JRadioButton();
        this.blackButton.setIcon(new ColorIcon(Color.BLACK));
        this.blackButton.setSelectedIcon(new SelectedColorIcon(Color.BLACK));
        this.blackButton.setSelected(true);
        this.colorSelector.add(this.blackButton);
        
        this.redButton = new JRadioButton();
        this.redButton.setIcon(new ColorIcon(Color.RED));
        this.redButton.setSelectedIcon(new SelectedColorIcon(Color.RED));
        this.redButton.setSelected(true);
        this.colorSelector.add(this.redButton);
        
        this.orangeButton = new JRadioButton();
        this.orangeButton.setIcon(new ColorIcon(Color.ORANGE));
        this.orangeButton.setSelectedIcon(new SelectedColorIcon(Color.ORANGE));
        this.orangeButton.setSelected(true);
        this.colorSelector.add(this.orangeButton);
        
        this.yellowButton = new JRadioButton();
        this.yellowButton.setIcon(new ColorIcon(Color.YELLOW));
        this.yellowButton.setSelectedIcon(new SelectedColorIcon(Color.YELLOW));
        this.yellowButton.setSelected(true);
        this.colorSelector.add(this.yellowButton);
        
        this.greenButton = new JRadioButton();
        this.greenButton.setIcon(new ColorIcon(Color.GREEN));
        this.greenButton.setSelectedIcon(new SelectedColorIcon(Color.GREEN));
        this.greenButton.setSelected(true);
        this.colorSelector.add(this.greenButton);
        
        this.blueButton = new JRadioButton();
        this.blueButton.setIcon(new ColorIcon(Color.BLUE));
        this.blueButton.setSelectedIcon(new SelectedColorIcon(Color.BLUE));
        this.blueButton.setSelected(true);
        this.colorSelector.add(this.blueButton);
        
        this.whiteButton = new JRadioButton("");
        this.whiteButton.setIcon(new ColorIcon(Color.WHITE));
        this.whiteButton.setSelectedIcon(new SelectedColorIcon(Color.WHITE));
        this.colorSelector.add(this.whiteButton);
        this.userList = new JList<String>(listModel);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(blackButton)
                        .addComponent(redButton)
                        .addComponent(orangeButton)
                        .addComponent(yellowButton)
                        .addComponent(greenButton)
                        .addComponent(blueButton)
                        .addComponent(whiteButton)
                        .addComponent(widthSelector)
                        .addComponent(userList))
                .addComponent(whiteboardPanel)        
                );
        
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(blackButton)
                        .addComponent(redButton)
                        .addComponent(orangeButton)
                        .addComponent(yellowButton)
                        .addComponent(greenButton)
                        .addComponent(blueButton)
                        .addComponent(whiteButton)
                        .addComponent(widthSelector)
                        .addComponent(userList))
                .addComponent(whiteboardPanel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
            );
      
        this.pack();
        
        this.blackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.BLACK);
            }
        });
        
        this.redButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.RED);
            }
        });
        
        this.orangeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.ORANGE);
            }
        });
        
        this.yellowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.YELLOW);
            }
        });
        
        this.greenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.GREEN);
            }
        });
        
        this.blueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.BLUE);
            }
        });
        
        this.whiteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.WHITE);
                whiteboardPanel.setDrawThickness(30);
                widthSelector.setValue(30);
            }
        });
        
        this.widthSelector.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    whiteboardPanel.setDrawThickness(source.getValue());
                }
            }
        });
    }
    
    @Override
    public void dispose() {
        this.clientSocket.sendLeave();;
        super.dispose();
    }
    
    @Override
    public void addDrawableToWhiteBoard(Drawable d) {
        this.whiteboardPanel.addDrawableToBoard(d);
    }
    
    @Override
    public void signOnUser(String username) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) this.userList.getModel();
        listModel.addElement(username);
    }
    
    @Override
    public void signOffUser(String username) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) this.userList.getModel();
        listModel.removeElement(username);
    }

}
