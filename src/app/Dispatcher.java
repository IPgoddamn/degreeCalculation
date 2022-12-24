package app;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
public class Dispatcher {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLayout(null);
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

			} catch (Exception exception) {
				System.out.println("Can't change look and feel");
			}
			new MyFrame();
		});
	}
}

class MyFrame extends JFrame {
	private final TextPanel textPanel;
	private final FormPanel formPanel;

	MyFrame() {
		super("Danil Kolotenko SHID-11");
		setSize(600, 400);
		textPanel = new TextPanel();
		formPanel = new FormPanel();
		formPanel.setFormListener(new FormListener() {
			int firstNumber = 0;
			int commonRatio = 0;
			int numberOfTerms = 0;

			public void formEventOccured(FormEvent ev) {
				firstNumber = ev.getFirstNumber();
				commonRatio = ev.getCommonRatio();
				numberOfTerms = ev.getNumberOfTerms();
				textPanel.appendText(calculation());
			}

			private String calculation() {
				StringBuilder result = new StringBuilder("""
						n	x
						_________________
						""");
				for (int n = firstNumber; n < numberOfTerms + 1 + firstNumber; n++) {
					result.append(n).append("	").append((int) Math.round(Math.pow(commonRatio, n))).append("\n");
				}
				return result.toString();
			}
		});
		setLayout(new BorderLayout());
		add(textPanel, BorderLayout.CENTER);
		add(formPanel, BorderLayout.WEST);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}

class TextPanel extends JPanel {
	private static JTextArea textArea;
	public TextPanel() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // постоянно видимий скролл
		add(scrollPane, BorderLayout.CENTER);
		Border border = BorderFactory.createTitledBorder("Result");
		setBorder(border);
	}

	public void appendText(String text) {
		textArea.append(text);
	}

	public static void clear() {
		textArea.setText("");
	}
}

interface FormListener extends EventListener {
	void formEventOccured(FormEvent ev);
}

interface StringListener {
	void sendText(String text);
}
class FormPanel extends JPanel {
	private final JLabel firstNumberLabel;
	private final JLabel commonRatioLabel;
	private final JLabel numberOfTermsLabel;
	private final JTextField firstNumberField;
	private final JTextField commonRatioField;
	private final JTextField numberOfTermsField;
	private final JButton btnCompute;
	private final JButton btnExit;
	private final JButton btnErase;
	private FormListener formListener;
	
	public void setFormListener(FormListener listener) {
		this.formListener = listener;
	}

	FormPanel() {
		Dimension dim = this.getPreferredSize();
		dim.width = 250;
		setPreferredSize(dim);
		Border border = BorderFactory.createTitledBorder("Settings");
		setBorder(border);
		// creating of GUI components
		firstNumberLabel = new JLabel("First Number");
		commonRatioLabel = new JLabel("Common Ratio ");
		numberOfTermsLabel = new JLabel("Number of Terms");
		firstNumberField = new JTextField(10);
		commonRatioField = new JTextField(10);
		numberOfTermsField = new JTextField(10);
		btnCompute = new JButton("Compute");
		btnExit = new JButton("Exit");
		btnErase = new JButton("Erase");
		btnCompute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCompute.setEnabled(false);
				firstNumberField.setEditable(false);
				commonRatioField.setEditable(false);
				numberOfTermsField.setEditable(false);
				String fN = firstNumberField.getText();
				String cR = commonRatioField.getText();
				String nOT = numberOfTermsField.getText();
				try {
					int firstNumber = Integer.parseInt(fN.trim());
					int commonRatio = Integer.parseInt(cR.trim());
					int numberOfTerms = Integer.parseInt(nOT.trim());
					FormEvent ev = new FormEvent(this, firstNumber, commonRatio, numberOfTerms);
					if (formListener != null) {
						formListener.formEventOccured(ev);
					}
					btnErase.setVisible(true);
				} catch (NumberFormatException nfe) {
					SwingUtilities.invokeLater(FrameException::new);
				}
			}
		});
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		btnErase.setVisible(false);
		btnErase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TextPanel.clear();
				btnCompute.setEnabled(true);
				firstNumberField.setEditable(true);
				commonRatioField.setEditable(true);
				numberOfTermsField.setEditable(true);
				btnErase.setVisible(false);
			}
		});
		// creating layout
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		GridBagConstraints gc = new GridBagConstraints();

		// row 0
		gc.weightx = 1;
		gc.weighty = 0.0;

		gc.gridx = 0;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(3, 0, 3, 5);
		add(firstNumberLabel, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		add(firstNumberField, gc);

		// row 1
		gc.weightx = 1;
		gc.weighty = 0.0;

		gc.gridx = 0;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		add(commonRatioLabel, gc);

		gc.gridx = 1;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(commonRatioField, gc);

		// row 2
		gc.weightx = 1;
		gc.weighty = 0.0;

		gc.gridx = 0;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.LINE_END;
		add(numberOfTermsLabel, gc);

		gc.gridx = 1;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.LINE_START;
		add(numberOfTermsField, gc);

		// row 3

		gc.weightx = 1;
		gc.weighty = 1.0;

		gc.gridx = 1;
		gc.gridy = 3;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(btnCompute, gc);

		// row 4
		gc.weightx = 1;
		gc.weighty = 1.0;

		gc.gridx = 1;
		gc.gridy = 4;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(btnErase, gc);

		// row 5

		gc.weightx = 1;
		gc.weighty = 1.0;

		gc.gridx = 1;
		gc.gridy = 6;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(btnExit, gc);
	}
}

class FormEvent extends EventObject {
	private int firstNumber;
	private int commonRatio;
	private int NumberOfTerms;
	public FormEvent(Object source, int firstNumber, int commonRatio, int NumberOfTerms) {
		super(source);
		this.firstNumber = firstNumber;
		this.commonRatio = commonRatio;
		this.NumberOfTerms = NumberOfTerms;
	}

	public int getFirstNumber() {
		return firstNumber;
	}

	private void setFirstNumber(int firstNumber) {
		this.firstNumber = firstNumber;
	}

	public int getCommonRatio() {
		return commonRatio;
	}

	public void setCommonRatio(int commonRatio) {
		this.commonRatio = commonRatio;
	}

	public int getNumberOfTerms() {
		return NumberOfTerms;
	}

	public void setNumberOfTerms(int numberOfTerms) {
		NumberOfTerms = numberOfTerms;
	}

}

class FrameException extends JFrame {
	private final JTextArea textArea;
	private StringListener textListener;

	FrameException() {
		super("Exception");
		// adding layout and components
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		add(textArea, BorderLayout.CENTER);
		textArea.append("Error 404 : Wrong Format \n" + "Please use only positive numbers");
		if (textListener != null) {
			textListener.sendText("Error 404 : Wrong Format \n" + "Please use only numbers");
		}
		textArea.insert(getWarningString(), DISPOSE_ON_CLOSE);
		textArea.setEditable(false);
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
