import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class SliderDemo extends JFrame {
    private JSlider slider1, slider2, slider3;
    private final int MAX_SUM = 200;

    public SliderDemo() {
        super("Slider Demo");
        setLayout(new FlowLayout());

        slider1 = createSlider(Color.BLUE);
        slider2 = createSlider(Color.GREEN);
        slider3 = createSlider(Color.RED);

        add(slider1);
        add(slider2);
        add(slider3);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JSlider createSlider(Color trackColor) {
        JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPreferredSize(new Dimension(65, 300));

        // Custom UI to paint the slider thumb and track
        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.GRAY);
                g2d.fill(new Ellipse2D.Double(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height));
            }

            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height);

                int y = thumbRect.y + thumbRect.height / 2;
                int trackHeight = trackRect.y + trackRect.height;
                g2d.setColor(trackColor);
                g2d.fillRect(trackRect.x, y, trackRect.width, trackHeight - y);
            }
        });

        // Listener to enforce sum constraint and interactively adjust other sliders
        slider.addChangeListener(e -> adjustSliders(slider));
        return slider;
    }

    private void adjustSliders(JSlider source) {
        JSlider[] sliders = {slider1, slider2, slider3};
        int sum = 0;
        for (JSlider s : sliders) sum += s.getValue();

        if (sum > MAX_SUM) {
            int excess = sum - MAX_SUM;
            for (JSlider s : sliders) {
                if (s != source && s.getValue() > 0 && excess > 0) {
                    int adjustment = Math.min(excess, s.getValue());
                    s.setValue(s.getValue() - adjustment);
                    excess -= adjustment;
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SliderDemo::new);
    }
}
