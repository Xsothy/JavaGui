package Support;

import javax.swing.*;
import java.util.Map;

public interface RouterInterface 
{
    /**
     * Navigates to the specified route.
     *
     * @param route The route to navigate to
     */
    void navigateTo(String route);

    /**
     * Navigates to the specified route with a parameter.
     *
     * @param route The route to navigate to
     * @param id The parameter (usually an ID)
     */
    void navigateTo(String route, String id);

    /**
     * Navigates to the specified route with parameters.
     *
     * @param route The route to navigate to
     * @param params The parameters
     */
    void navigateTo(String route, Map<String, String> params);

    JPanel getContainer();
}
