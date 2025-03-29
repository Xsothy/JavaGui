package Support;

import View.NavigatePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Router class for handling navigation between different views.
 */
public class Router {
    private static Router instance;
    private final JFrame container;
    private static final Map<String, NavigatePanel> staticRoutes = new HashMap<>();
    private static final List<Route> dynamicRoutes = new ArrayList<>();
    private static String currentRoute = "";

    /**
     * Private constructor to enforce singleton pattern.
     *
     * @param container The container panel where views will be displayed
     */
    public Router(JFrame container) {
        this.container = container;
    }

    public static Router getInstance() {
        return instance;
    }

    public static void initialize(JFrame container) {
        if (instance == null || instance.container != container) {
            instance = new Router(container);
        }
    }

    public static void register(String path, NavigatePanel panel) {
        staticRoutes.put(path, panel);
    }
    
    /**
     * Register a dynamic route with the Router.
     * 
     * @param pattern The route pattern, may include {paramName} path parameters
     * @param handler The handler to execute when the route is matched
     */
    public static void register(String pattern, Route.RouteHandler handler) {
        dynamicRoutes.add(new Route(pattern, handler));
    }

    /**
     * Navigate to a registered route.
     *
     * @param path The path to navigate to
     * @throws IllegalArgumentException If the path is not registered
     */
    public static void navigate(String path) {
        if (staticRoutes.containsKey(path)) {
            NavigatePanel panel = staticRoutes.get(path);
            getInstance().displayPanel(panel);
            currentRoute = path;
            return;
        }
        
        // Then check dynamic routes
        for (Route route : dynamicRoutes) {
            if (route.matches(path)) {
                Map<String, String> parameters = route.extractParameters(path);
                NavigatePanel panel = route.execute(parameters);
                getInstance().displayPanel(panel);
                currentRoute = path;
                return;
            }
        }
        throw new IllegalArgumentException("Route not found: " + path);
    }

    /**
     * Display a panel in the container.
     * 
     * @param panel The panel to display
     */
    private void displayPanel(NavigatePanel panel) {
        container.setVisible(false);
        panel.render();
        container.setContentPane(panel);
        container.pack();
        container.setLocationRelativeTo(null);
        container.setVisible(true);
    }

    public static JFrame getContainer() {
        return instance.container;
    }

    /**
     * Get the current route.
     *
     * @return The current route
     */
    public static String getCurrentRoute() {
        return currentRoute;
    }

    /**
     * Check if a route is registered.
     *
     * @param path The path to check
     * @return True if the route is registered, false otherwise
     */
    public boolean hasRoute(String path) {
        if (staticRoutes.containsKey(path)) {
            return true;
        }
        
        for (Route route : dynamicRoutes) {
            if (route.matches(path)) {
                return true;
            }
        }
        
        return false;
    }
} 