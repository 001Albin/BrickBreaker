import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame {
    private Point initialClick;

    public GameFrame() {
        setUndecorated(true); // remove OS title bar
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ==== Custom Title Bar ====
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Color.BLACK);
        titleBar.setPreferredSize(new Dimension(getWidth(), 30));

        JLabel title = new JLabel(" Brick Breaker Game ");
        title.setForeground(Color.RED);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        titleBar.add(title, BorderLayout.WEST);

        JButton minButton = new JButton("-");
        minButton.setForeground(Color.BLACK);
        minButton.setBackground(Color.YELLOW);
        minButton.setFocusPainted(false);
        minButton.setBorderPainted(false);
        minButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> System.exit(0));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttons.setOpaque(false);
        buttons.add(minButton);
        buttons.add(closeButton);
        titleBar.add(buttons, BorderLayout.EAST);

        // drag window
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                setLocation(thisX + xMoved, thisY + yMoved);
            }
        });

        // ==== Add TitleBar + Game ====
        add(titleBar, BorderLayout.NORTH);

        Gameplay gameplay = new Gameplay(); // your game panel
        gameplay.setBackground(Color.BLACK);
        gameplay.setFocusable(true);
        add(gameplay, BorderLayout.CENTER);

        // 🔑 Make sure it grabs focus when frame is visible
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                gameplay.requestFocusInWindow();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}
