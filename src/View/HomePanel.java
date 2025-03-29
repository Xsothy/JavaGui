package View;

import Support.Router;
import Support.SessionManager;

import javax.swing.*;
import java.awt.*;

import static Support.Router.navigate;

public class HomePanel extends NavigatePanel {
    public HomePanel() {
        setPreferredSize(new Dimension(600, 400));
        setLayout(new GridBagLayout());
        setOpaque(true);
    }

    @Override
    public void render() {
        this.removeAll();
        Button btnDashboard = createStyledButton("Dashboard",SessionManager.isLoggedIn() ? Color.green : Color.GRAY);
        btnDashboard.addActionListener(e -> navigate("dashboard"));
        btnDashboard.setEnabled(SessionManager.isLoggedIn());

        Button btnLogin = createStyledButton(
                SessionManager.isLoggedIn() ? "Logout" : "Login",
                SessionManager.isLoggedIn() ? Color.RED : Color.GREEN
        );

        btnLogin.addActionListener(e -> {
            if (SessionManager.isLoggedIn()) {
                SessionManager.logout();
               navigate("/");
            } else {
                navigate("login");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Vertical spacing
        add(btnDashboard, gbc);

        gbc.gridy = 1;
        add(btnLogin, gbc);
    }

    private Button createStyledButton(String text, Color bgColor) {
        Button button = new Button();
        button.setLabel(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // Gradient color from top to bottom
        Color color1 = new Color(70, 130, 180); // Steel Blue
        Color color2 = new Color(25, 25, 112); // Midnight Blue
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}
