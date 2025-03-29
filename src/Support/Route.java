package Support;

import View.NavigatePanel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Route class for handling dynamic routes with path parameters.
 */
public class Route {
    private static final Pattern PATH_PARAMETER_PATTERN = Pattern.compile("\\{([^{}]+)\\}");
    
    private final String pattern;
    private final Pattern compiledPattern;
    private final String[] parameterNames;
    private final RouteHandler handler;
    
    /**
     * Create a new Route with the given pattern and handler.
     * 
     * @param pattern The route pattern, may include {paramName} path parameters
     * @param handler The handler to execute when the route is matched
     */
    public Route(String pattern, RouteHandler handler) {
        this.pattern = pattern;
        this.handler = handler;
        
        // Extract parameter names from the pattern
        Matcher matcher = PATH_PARAMETER_PATTERN.matcher(pattern);
        Map<String, Integer> parameterIndexes = new HashMap<>();
        int index = 0;
        
        while (matcher.find()) {
            parameterIndexes.put(matcher.group(1), index++);
        }
        
        this.parameterNames = new String[parameterIndexes.size()];
        for (Map.Entry<String, Integer> entry : parameterIndexes.entrySet()) {
            this.parameterNames[entry.getValue()] = entry.getKey();
        }
        
        // Convert pattern to regex for matching
        String regex = pattern;
        regex = regex.replaceAll("\\{[^{}]+\\}", "([^/]+)");
        regex = "^" + regex + "$";
        
        this.compiledPattern = Pattern.compile(regex);
    }
    
    /**
     * Check if the given path matches this route's pattern.
     * 
     * @param path The path to check
     * @return True if the path matches, false otherwise
     */
    public boolean matches(String path) {
        return compiledPattern.matcher(path).matches();
    }
    
    /**
     * Extract parameters from the given path according to this route's pattern.
     * 
     * @param path The path to extract parameters from
     * @return A map of parameter names to values
     */
    public Map<String, String> extractParameters(String path) {
        Map<String, String> parameters = new HashMap<>();
        Matcher matcher = compiledPattern.matcher(path);
        
        if (matcher.matches()) {
            for (int i = 0; i < parameterNames.length; i++) {
                parameters.put(parameterNames[i], matcher.group(i + 1));
            }
        }
        
        return parameters;
    }
    
    /**
     * Get the route pattern.
     * 
     * @return The route pattern
     */
    public String getPattern() {
        return pattern;
    }
    
    /**
     * Execute the route handler with the given parameters.
     * 
     * @param parameters The parameters to pass to the handler
     * @return The panel returned by the handler
     */
    public NavigatePanel execute(Map<String, String> parameters) {
        return handler.handle(parameters);
    }
    
    /**
     * Functional interface for route handlers.
     */
    @FunctionalInterface
    public interface RouteHandler {
        /**
         * Handle a route match.
         * 
         * @param parameters The parameters extracted from the path
         * @return The panel to display
         */
        NavigatePanel handle(Map<String, String> parameters);
    }
} 