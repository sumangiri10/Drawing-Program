package com.sg.drawingprogram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DrawingProgram {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new DrawingFrame();
            frame.setTitle("Drawing Program");
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class Circle {
    private int size;
    private Point center;
    private Color color;

    Circle(int iSize, Point location, Color C) {
        setSize(iSize);
        setLocation(location);
        setColor(C);
    }

    void setSize(int iSize) {
        if (iSize > 1) {
            size = iSize;
        } else {
            size = 1;
        }
    }

    void setLocation(Point Pcenter) {
        center = Pcenter;
    }

    void setColor(Color Ccolor) {
        color = Ccolor;
    }

    int getSize()
    {
        return size;
    }

    Point getCenter()
    {
        return center;
    }

    Color getColor()
    {
        return color;
    }


   public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillOval(getCenter().x, getCenter().y, getSize(), getSize());
    }
}

class DrawingPanel extends JPanel implements MouseMotionListener {
    private int circleDiameter;
    private Color circleColor;
    private ArrayList<Circle> circleArrayList = new ArrayList<>();
    private ArrayList<ArrayList<Circle>> history = new ArrayList<>();
    private int historyIndex = -1;

    DrawingPanel(Color co, int si) {
        setCircleColor(co);
        setCircleDiameter(si);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Iterator<Circle> circleIterator = circleArrayList.iterator();

        Circle drawCircle;

        while (circleIterator.hasNext()) {
            drawCircle = circleIterator.next();
            drawCircle.draw(g);
        }
    }

    void setCircleDiameter(int tempSize) {
        circleDiameter = tempSize;
    }

    void setCircleColor(Color tempColor) {
        circleColor = tempColor;
    }

    int getCircleSize()
    {
        return circleDiameter;
    }

    Color getCircleColor()
    {
        return circleColor;
    }

void clear() {
        circleArrayList.clear();
        repaint();
    }

    void undo() {
        if (historyIndex >= 0) {
            historyIndex--;
            circleArrayList = new ArrayList<>(history.get(historyIndex));
            repaint();
        }
    }

    void redo() {
        if (historyIndex < history.size() - 1) {
            historyIndex++;
            circleArrayList = new ArrayList<>(history.get(historyIndex));
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.isMetaDown()) {
            Circle newCircle = new Circle(getCircleSize(), e.getPoint(), this.getBackground());
            circleArrayList.add(newCircle);
        } else {
            Circle newCircle = new Circle(getCircleSize(), e.getPoint(), this.getCircleColor());
            circleArrayList.add(newCircle);
        }

        history.add(new ArrayList<>(circleArrayList));
        historyIndex = history.size() - 1;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}

class DrawingFrame extends JFrame implements ActionListener {
    final int SMALL = 4;
    final int MEDIUM = 8;
    final int LARGE = 10;

    private DrawingPanel drawPanel = new DrawingPanel(Color.BLACK, SMALL);

    DrawingFrame() {
        Container con = getContentPane();
        BorderLayout layout = new BorderLayout();
        JMenuBar mainMenuBar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        JMenu menu2 = new JMenu("Size");
        JMenu menu3 = new JMenu("Color");
        JMenu menu4 = new JMenu("Help");
        JMenuItem clear = new JMenuItem("Clear");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem small = new JMenuItem("Small");
        JMenuItem medium = new JMenuItem("Medium");
        JMenuItem large = new JMenuItem("Large");
        JMenuItem blackMenu = new JMenuItem("Black");
        JMenuItem greenMenu = new JMenuItem("Green");
        JMenuItem yellowMenu = new JMenuItem("Yellow");
        JMenuItem redMenu = new JMenuItem("Red");
        JMenuItem blueMenu = new JMenuItem("Blue");
        JMenuItem about = new JMenuItem("About");
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");

        con.setLayout(layout);
        drawPanel.setBackground(Color.WHITE);
        drawPanel.setPreferredSize(new Dimension(600, 600));

        setJMenuBar(mainMenuBar);
        mainMenuBar.add(menu1);
        mainMenuBar.add(menu2);
        mainMenuBar.add(menu3);
        mainMenuBar.add(menu4);
        menu1.add(clear);
        menu1.add(exit);
        menu2.add(small);
        menu2.add(medium);
        menu2.add(large);
        menu3.add(blackMenu);
        menu3.add(greenMenu);
        menu3.add(yellowMenu);
        menu3.add(redMenu);
        menu3.add(blueMenu);
        menu4.add(about);

        undoButton.addActionListener(this);
        redoButton.addActionListener(this);

        menu1.setMnemonic('F');
        menu2.setMnemonic('z');
        menu3.setMnemonic('o');
        menu4.setMnemonic('H');
        clear.setMnemonic('C');
        exit.setMnemonic('x');

        exit.addActionListener(this);
        clear.addActionListener(this);
        small.addActionListener(this);
        medium.addActionListener(this);
        large.addActionListener(this);
        blackMenu.addActionListener(this);
        greenMenu.addActionListener(this);
        yellowMenu.addActionListener(this);
        redMenu.addActionListener(this);
        blueMenu.addActionListener(this);
        about.addActionListener(this);
        redMenu.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
        buttonPanel.setPreferredSize(new Dimension(0, 40));

        add(drawPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String arg = e.getActionCommand();
        if (arg.equals("Exit")) {
            System.exit(0);
        }
        if (arg.equals("About")) {
            JOptionPane.showMessageDialog(null, "Drawing Program by Suman Giri");
        }
        if (arg.equals("Clear")) {
            drawPanel.clear();
            drawPanel.repaint();
        }
        if (arg.equals("Red")) {
            drawPanel.setCircleColor(Color.red);
        }
        if (arg.equals("Black")) {
            drawPanel.setCircleColor(Color.black);
        }
        if (arg.equals("Yellow")) {
            drawPanel.setCircleColor(Color.yellow);
        }
        if (arg.equals("Green")) {
            drawPanel.setCircleColor(Color.green);
        }
        if (arg.equals("Blue")) {
            drawPanel.setCircleColor(Color.blue);
        }
        if (arg.equals("Small")) {
            drawPanel.setCircleDiameter(SMALL);
        }
        if (arg.equals("Medium")) {
            drawPanel.setCircleDiameter(MEDIUM);
        }
        if (arg.equals("Large")) {
            drawPanel.setCircleDiameter(LARGE);
        }
        if (arg.equals("Undo")) {
            drawPanel.undo();
        }
        if (arg.equals("Redo")) {
            drawPanel.redo();
        }
    }
}