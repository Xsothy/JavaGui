package Support;

import java.awt.Color;
import java.awt.Font;

/**
 * Constants for UI styling across the application.
 * This class centralizes all UI-related constants to ensure consistency.
 */
public class UIConstants {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(59, 130, 246);  // Blue
    public static final Color SECONDARY_COLOR = new Color(249, 250, 251);  // Light gray
    public static final Color ACCENT_COLOR = new Color(79, 70, 229);  // Indigo
    public static final Color SIDEBAR_COLOR = new Color(31, 41, 55);  // Dark blue/gray
    public static final Color TEXT_COLOR = new Color(17, 24, 39);  // Dark gray
    public static final Color LIGHT_TEXT_COLOR = new Color(156, 163, 175);  // Medium gray
    public static final Color SUCCESS_COLOR = new Color(16, 185, 129);  // Green
    public static final Color DANGER_COLOR = new Color(239, 68, 68);  // Red
    public static final Color WARNING_COLOR = new Color(245, 158, 11);  // Amber
    public static final Color INFO_COLOR = new Color(14, 165, 233);  // Sky blue
    public static final Color BORDER_COLOR = new Color(229, 231, 235);  // Light gray for borders
    
    // Table specific colors
    public static final Color TABLE_HEADER_BG_COLOR = new Color(59, 130, 246);  // Same as PRIMARY_COLOR
    public static final Color TABLE_HEADER_FG_COLOR = Color.WHITE;  // White text for header
    public static final Color TABLE_ROW_BG_COLOR = Color.WHITE;  // White for even rows
    public static final Color TABLE_ALT_ROW_BG_COLOR = new Color(249, 250, 251);  // Light gray for odd rows
    public static final Color TABLE_SELECTION_BG_COLOR = new Color(235, 245, 255);  // Light blue when selected
    public static final Color TABLE_SELECTION_FG_COLOR = TEXT_COLOR;  // Keep text color when selected
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font SMALL_BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TABLE_CONTENT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    // Sizes and spacing
    public static final int CONTENT_PADDING = 25;
    public static final int SECTION_SPACING = 20;
    public static final int BUTTON_PADDING_V = 8;
    public static final int BUTTON_PADDING_H = 15;
    public static final int TABLE_ROW_HEIGHT = 40;
    public static final int TABLE_HEADER_HEIGHT = 40;
} 