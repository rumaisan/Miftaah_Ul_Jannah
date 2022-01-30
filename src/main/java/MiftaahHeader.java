import javax.swing.*;
import java.awt.*;

public class MiftaahHeader extends JLabel {
    public static final String APP_NAME = "مفتاح الجنة الصلاة";

    public MiftaahHeader() {
        super(APP_NAME, SwingConstants.CENTER);
        setFont(new Font(getFont().getName(), Font.BOLD, 30));
        setBackground(Color.lightGray);
        setOpaque(true);
        setPreferredSize(new Dimension(100, 100));
    }
}
