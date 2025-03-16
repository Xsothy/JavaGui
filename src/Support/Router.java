package Support;

import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Router class for handling navigation between different views.
 */
public class Router {
    private static Router instance;
    private final JPanel container;
    private final Map<String, JPanel> routes;
    private String currentRoute;
    
    /**
     * Private constructor to enforce singleton pattern.
     * 
     * @param container The container panel where views will be displayed
     */
    private Router(JPanel container) {
        this.container = container;
        this.routes = new HashMap<>();
        this.currentRoute = "";
    }
    
    /**
     * Get the singleton instance of the Router.
     * 
     * @param container The container panel where views will be displayed
     * @return The Router instance
     */
    public static Router getInstance(JPanel container) {
        if (instance == null || instance.container != container) {
            instance = new Router(container);
        }
        return instance;
    }
    
    /**
     * Register a route with the Router.
     * 
     * @param path The path for the route
     * @param panel The panel to display for the route
     */
    public void register(String path, JPanel panel) {
        routes.put(path, panel);
    }
    
    /**
     * Navigate to a registered route.
     * 
     * @param path The path to navigate to
     * @throws IllegalArgumentException If the path is not registered
     */
    public void navigate(String path) {
        if (!routes.containsKey(path)) {
            throw new IllegalArgumentException("Route not found: " + path);
        }
        
        JPanel panel = routes.get(path);
        container.removeAll();
        container.add(panel);
        container.revalidate();
        container.repaint();
        currentRoute = path;
    }
    
    /**
     * Get the current route.
     * 
     * @return The current route
     */
    public String getCurrentRoute() {
        return currentRoute;
    }
    
    /**
     * Check if a route is registered.
     * 
     * @param path The path to check
     * @return True if the route is registered, false otherwise
     */
    public boolean hasRoute(String path) {
        return routes.containsKey(path);
    }
    
    /**
     * Get a registered panel by its path.
     * 
     * @param path The path of the panel
     * @return The panel, or null if not found
     */
    public JPanel getPanel(String path) {
        return routes.get(path);
    }
} 