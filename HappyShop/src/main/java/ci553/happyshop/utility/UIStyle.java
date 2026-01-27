package ci553.happyshop.utility;

/**
 * UIStyle is a centralized Java final class that holds all JavaFX UI-related style and size constants
 * used across all client views in the system.
 *
 * These values are grouped here rather than being hardcoded throughout the codebase:
 * - improves maintainability, ensures style consistency,
 * - avoids hardcoded values scattered across the codebase.
 *
 * Example usages:
 * - UIStyle.HistoryWinHeight for setting the height of the order history window
 * - UIStyle.labelStyle for applying consistent styling to labels
 *
 * Design rationale:
 * - Declared as a final class: prevents inheritance and misuse
 * - Private constructor: prevents instantiation (this is a static-only utility class)
 * - Holds only static constants: ensures minimal memory usage and clean syntax
 *
 *  Why a Record is NOT appropriate:
 * - Records are intended for immutable instance data (e.g., DTOs), not static constants
 * - This class has no record components — everything is static
 * - We're using this as a utility container, not a data model
 *
 *  Reminder:
 *  Just because a class has no behavior and only data does NOT mean it should be a record.
 *  If all members are static constants, use a final utility class like this one.
 */

public final class UIStyle {

    //private constructor prevents instantiation
    private UIStyle() {
        throw new UnsupportedOperationException("UIStyle is a utility class");
    }

    // ========== WINDOW SIZES (KEEP THESE) ==========
    public static final int customerWinWidth = 610;
    public static final int customerWinHeight = 300;
    public static final int removeProNotifierWinWidth = customerWinWidth / 2 + 160;
    public static final int removeProNotifierWinHeight = 230;

    public static final int pickerWinWidth = 310;
    public static final int pickerWinHeight = 300;

    public static final int trackerWinWidth = 210;
    public static final int trackerWinHeight = 300;

    public static final int warehouseWinWidth = 630;
    public static final int warehouseWinHeight = 300;
    public static final int AlertSimWinWidth = 300;
    public static final int AlertSimWinHeight = 170;
    public static final int HistoryWinWidth = 300;
    public static final int HistoryWinHeight = 140;

    public static final int EmergencyExitWinWidth = 200;
    public static final int EmergencyExitWinHeight = 300;

    // ========== ALL STYLES SET TO EMPTY - CSS TAKES OVER ==========
    public static final String labelTitleStyle = "";
    public static final String labelStyle = "";
    public static final String labelLowStockStyle = "";
    public static final String comboBoxStyle = "";
    public static final String buttonStyle = "";

    public static final String rootStyle = "";
    public static final String rootStyleBlue = "";
    public static final String rootStyleGray = "";
    public static final String rootStyleWarehouse = "";
    public static final String rootStyleYellow = "";
    public static final String rootVipCustomerStyle = "";

    public static final String spinnerArrowStyle = "";
    public static final String textFiledStyle = "";
    public static final String smallTextFiledStyle = "";
    public static final String tinyTextFiledStyle = "";

    public static final String labelMulLineStyle = "";
    public static final String labelPriceStyle = "";
    public static final String listViewStyle = "";

    public static final String manageStockChildStyle = "";
    public static final String manageStockChildStyle1 = "";

    public static final String greenFillBtnStyle = "";
    public static final String redFillBtnStyle = "";
    public static final String searchBtnStyle = "";
    public static final String grayFillBtnStyle = "";
    public static final String blueFillBtnStyle = "";
    public static final String alertBtnStyle = "";

    public static final String alertTitleLabelStyle = "";
    public static final String alertContentTextAreaStyle = "";
    public static final String alertContentUserActionStyle = "";
    public static final String tooltipStyle = "";
}