package Support;

import Components.NavigatePanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * Router class for handling navigation between different views.
 */
public class Router {
    private static Router instance;
    private final NavigatePanel container;
    private final Map<String, NavigatePanel> staticRoutes;
    private final List<Route> dynamicRoutes;
    private String currentRoute;

    /**
     * Private constructor to enforce singleton pattern.
     *
     * @param container The container panel where views will be displayed
     */
    public Router(NavigatePanel container) {
        this.container = container;
        this.staticRoutes = new HashMap<>();
        this.dynamicRoutes = new ArrayList<>();
        this.currentRoute = "";
    }

    /**
     * Get the singleton instance of the Router.
     *
     * @param container The container panel where views will be displayed
     * @return The Router instance
     */
    public static Router getInstance(NavigatePanel container) {
        if (instance == null || instance.container != container) {
            instance = new Router(container);
        }
        return instance;
    }

    /**
     * Register a static route with the Router.
     *
     * @param path The path for the route
     * @param panel The panel to display for the route
     */
    public void register(String path, NavigatePanel panel) {
        staticRoutes.put(path, panel);
    }
    
    /**
     * Register a dynamic route with the Router.
     * 
     * @param pattern The route pattern, may include {paramName} path parameters
     * @param handler The handler to execute when the route is matched
     */
    public void register(String pattern, Route.RouteHandler handler) {
        dynamicRoutes.add(new Route(pattern, handler));
    }

    /**
     * Navigate to a registered route.
     *
     * @param path The path to navigate to
     * @throws IllegalArgumentException If the path is not registered
     */
    public void navigate(String path) {
        // First check static routes
        if (staticRoutes.containsKey(path)) {
            NavigatePanel panel = staticRoutes.get(path);
            displayPanel(panel);
            currentRoute = path;
            return;
        }
        
        // Then check dynamic routes
        for (Route route : dynamicRoutes) {
            if (route.matches(path)) {
                Map<String, String> parameters = route.extractParameters(path);
                NavigatePanel panel = route.execute(parameters);
                displayPanel(panel);
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
        panel.rerender();
        container.removeAll();
        container.add(panel);
        container.revalidate();
        container.repaint();
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