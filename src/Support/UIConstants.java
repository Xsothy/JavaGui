package Support;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension; // Keep import if needed elsewhere

/**
 * Constants for UI styling across the application.
 * This class centralizes all UI-related constants to ensure consistency.
 * Updated values for a more modern look (Variable names preserved).
 */
public class UIConstants {

    // --- Modernized Color Palette ---
    // Primary & Accent Colors
    public static final Color PRIMARY_COLOR = new Color(0, 122, 255);       // Standard Blue (iOS-like)
    public static final Color SECONDARY_COLOR = new Color(248, 248, 250); // Very light gray background (Subtle off-white) - Kept similar
    public static final Color ACCENT_COLOR = new Color(88, 86, 214);       // Purple Accent (iOS-like)
    public static final Color SIDEBAR_COLOR = new Color(28, 28, 30);       // Very dark gray/black for sidebar
    public static final Color TEXT_COLOR = new Color(28, 28, 30);        // Near black for primary text
    public static final Color LIGHT_TEXT_COLOR = new Color(60, 60, 67, 153); // Medium gray for secondary text (with alpha)
    public static final Color SUCCESS_COLOR = new Color(52, 199, 89);        // Green (iOS-like)
    public static final Color DANGER_COLOR = new Color(255, 59, 48);         // Red (iOS-like)
    public static final Color WARNING_COLOR = new Color(255, 149, 0);        // Orange (iOS-like)
    public static final Color INFO_COLOR = new Color(0, 122, 255);         // Blue (same as primary for info)
    public static final Color BORDER_COLOR = new Color(220, 220, 224);     // Light gray for subtle borders

    // Card colors and gradients
    public static final Color CARD_BG_COLOR = Color.WHITE;                 // Keep white
    public static final Color CARD_SHADOW_COLOR = new Color(60, 60, 67, 15); // Subtle shadow (alpha)
    public static final Color CARD_HIGHLIGHT_COLOR = new Color(0, 122, 255, 10); // Highlight with updated primary color (alpha)

    // Table specific colors
    public static final Color TABLE_HEADER_BG_COLOR = new Color(248, 248, 250); // Use light background for header (matches SECONDARY_COLOR)
    public static final Color TABLE_HEADER_FG_COLOR = new Color(60, 60, 67, 153); // Use secondary text for header (matches LIGHT_TEXT_COLOR)
    public static final Color TABLE_ROW_BG_COLOR = Color.WHITE;               // White for even rows
    public static final Color TABLE_ALT_ROW_BG_COLOR = new Color(248, 248, 250); // Very light gray/blue for odd rows (matches SECONDARY_COLOR)
    public static final Color TABLE_SELECTION_BG_COLOR = new Color(0, 122, 255, 30); // Light primary with alpha for selection
    public static final Color TABLE_SELECTION_FG_COLOR = new Color(28, 28, 30);   // Use primary text color for selected row (better contrast)

    // Button colors
    public static final Color BUTTON_HOVER_COLOR = new Color(51, 153, 255);     // Lighter blue for hover states
    public static final Color BUTTON_PRESSED_COLOR = new Color(0, 94, 204);      // Darker blue for pressed states

    // --- Fonts - Using Segoe UI, adjusted sizes/weights ---
    private static final String FONT_FAMILY = "Segoe UI"; // Keep font family

    public static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 22);          // Slightly smaller main title
    public static final Font SUBTITLE_FONT = new Font(FONT_FAMILY, Font.PLAIN, 15);        // Regular weight, slightly smaller subtitle
    public static final Font BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);          // Keep button font
    public static final Font SMALL_BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 12);    // Keep small button font
    public static final Font TABLE_HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 12);    // Smaller, bold header font
    public static final Font TABLE_CONTENT_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);   // Slightly larger table content font
    public static final Font CARD_TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);      // Keep card title font
    public static final Font CARD_VALUE_FONT = new Font(FONT_FAMILY, Font.BOLD, 28);      // Slightly smaller card value font
    public static final Font INPUT_LABEL_FONT = new Font(FONT_FAMILY, Font.BOLD, 13);     // Slightly smaller input label
    public static final Font INPUT_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);          // Keep input font size

    // --- Sizes and spacing - Adjusted for better rhythm ---
    public static final int CONTENT_PADDING = 20;          // Slightly reduced standard padding
    public static final int SECTION_SPACING = 16;          // Standardized section spacing
    public static final int BUTTON_PADDING_V = 10;         // Keep vertical button padding
    public static final int BUTTON_PADDING_H = 16;         // Keep horizontal button padding
    public static final int TABLE_ROW_HEIGHT = 48;         // Increased row height for more space
    public static final int TABLE_HEADER_HEIGHT = 40;      // Slightly reduced header height
    public static final int CARD_PADDING = 20;             // Match content padding
    public static final int SMALL_PADDING = 8;             // Keep small padding
    public static final int MEDIUM_PADDING = 16;           // Keep medium padding
    public static final int LARGE_PADDING = 32;            // Keep large padding

    // --- Border radius ---
    public static final int BORDER_RADIUS = 8;             // Keep standard border radius
    public static final int SMALL_BORDER_RADIUS = 4;       // Keep small border radius
    public static final int LARGE_BORDER_RADIUS = 12;      // Keep large border radius

    // --- Existing Table Row Colors (kept variable names) ---
    public static final Color TABLE_EVEN_ROW_COLOR = Color.WHITE; // Matches TABLE_ROW_BG_COLOR
    public static final Color TABLE_ODD_ROW_COLOR = new Color(248, 248, 250); // Matches TABLE_ALT_ROW_BG_COLOR & SECONDARY_COLOR
}
