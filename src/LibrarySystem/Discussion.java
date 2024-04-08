package LibrarySystem;

import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JButton;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;


public class Discussion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static Connection conn = null;
	
	private static JTextPane textPaneFeedback;
	private static JTextField SearchtextField;
	private static JLabel lblWordCount;
	private static JTextArea textAreaReply;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Discussion frame = new Discussion(0L); // Pass a default value for demonstration
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// create a method to add placeholder style
	public void addPlaceholder (JTextField text) {
		Font font = text.getFont();
		font = font.deriveFont(Font.ITALIC);
		text.setFont(font);
		text.setForeground(Color.gray);
	}
			
	// create a method to remove placeholder style
	public void removePlaceholder (JTextField text) {
		Font font = text.getFont();
		font = font.deriveFont(Font.PLAIN);
		text.setFont(font);
		text.setForeground(Color.black);
	}	
	
	public static void fetchDataFromDatabase(String searchtext) {
		textPaneFeedback.setText(null);
        StyledDocument doc = textPaneFeedback.getStyledDocument();
        
        // Define styles (colors) for each row
        MutableAttributeSet blueBoldStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(blueBoldStyle, Color.BLUE);
        StyleConstants.setBold(blueBoldStyle, true);
        
        MutableAttributeSet magentaBoldStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(magentaBoldStyle, Color.MAGENTA);
        StyleConstants.setBold(magentaBoldStyle, true);
        
        MutableAttributeSet blueStyle = doc.addStyle("blue", null);
        StyleConstants.setForeground(blueStyle, Color.BLUE);
        
        MutableAttributeSet magentaStyle = doc.addStyle("magenta", null);
        StyleConstants.setForeground(magentaStyle, Color.MAGENTA);

		String query;
		PreparedStatement pst;

		try {	
			query = "select User.Name as 'From', Feedback.Feedback from User join Feedback on User.ID_User = Feedback.ID_User where Feedback.ISBN = ? ";
			pst = conn.prepareStatement(query);
			pst.setLong(1, Long.parseLong(searchtext)); 
			ResultSet rs = pst.executeQuery();
			if (!rs.isBeforeFirst()) {
		        doc.insertString(doc.getLength(), "\n\t\t          There is no any feedback for this book.", null);
		    } else {
				while (rs.next()) {
					String from = rs.getString("From");
		            String feedback = rs.getString("Feedback");
		            // Append data to the JTextPane with the appropriate style            
	                if (rs.getRow() % 2 == 0) {
	                	doc.insertString(doc.getLength(), "From: ", blueStyle);
	                	doc.insertString(doc.getLength(), from + "\n", blueBoldStyle);
	                    doc.insertString(doc.getLength(), feedback + "\n\n", blueStyle);
	                } else {
	                	doc.insertString(doc.getLength(), "From: ", magentaStyle);
	                	doc.insertString(doc.getLength(), from + "\n", magentaBoldStyle);
	                    doc.insertString(doc.getLength(), feedback + "\n\n", magentaStyle);
	                }
				}
				pst.close();
				rs.close();
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }

	/**
	 * Create the frame.
	 */
	public Discussion(Long userid) {
		conn = SqliteConnect.connect();
		setResizable(false);
		setTitle("Library System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 672, 590);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitleWindowLabel = new JLabel("DISCUSSION FORUM");
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lblTitleWindowLabel.setBounds(222, 22, 278, 23);
		contentPane.add(lblTitleWindowLabel);
		
		JScrollPane scrollPaneFeedback = new JScrollPane();
		scrollPaneFeedback.setBounds(34, 132, 597, 178);
		contentPane.add(scrollPaneFeedback);
		
		textPaneFeedback = new JTextPane();
		textPaneFeedback.setEditable(false);
		scrollPaneFeedback.setViewportView(textPaneFeedback);
		
		JButton btnSubmitButton = new JButton("SUBMIT");
		btnSubmitButton.setBounds(393, 510, 89, 23);
		contentPane.add(btnSubmitButton);
		
		JButton btnMainButton = new JButton("MAIN MENU");
		btnMainButton.setBounds(511, 510, 120, 23);
		contentPane.add(btnMainButton);
		
		SearchtextField = new JTextField();
		SearchtextField.setBounds(34, 69, 184, 20);
		contentPane.add(SearchtextField);
		SearchtextField.setColumns(10);
		
		JButton btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fetchDataFromDatabase(SearchtextField.getText());
			}
		});
		SearchtextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(SearchtextField.getText().equals("Enter ISBN here ...")) {
					SearchtextField.setText(null);
					SearchtextField.requestFocus();
					removePlaceholder(SearchtextField);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(SearchtextField.getText().length()==0) {
					addPlaceholder(SearchtextField);
					SearchtextField.setText("Enter ISBN here ...");
				}
			}
		});
		btnSearch.setBounds(233, 68, 89, 23);
		contentPane.add(btnSearch);
		
		JLabel lblWelcome = new JLabel("Welcome to provide your feedback on the book");
		lblWelcome.setFont(new Font("Tahoma", Font.ITALIC, 12));
		lblWelcome.setBounds(34, 102, 287, 23);
		contentPane.add(lblWelcome);

		JLabel lblbookname = new JLabel("");
		lblbookname.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblbookname.setBounds(322, 102, 301, 23);
		contentPane.add(lblbookname);
		
		JLabel lblReplyLabel = new JLabel("Please provide some feedback about the book. (Max 500 words)");
		lblReplyLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
		lblReplyLabel.setBounds(34, 321, 385, 23);
		contentPane.add(lblReplyLabel);
		
		
		JScrollPane scrollPaneReply = new JScrollPane();
		scrollPaneReply.setBounds(34, 346, 597, 149);
		
		contentPane.add(scrollPaneReply);
		
		textAreaReply = new JTextArea();
		textAreaReply.setLineWrap(true);

		textAreaReply.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateWordCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateWordCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateWordCount();
            }

            private void updateWordCount() {
                // Get the text from the JTextArea
                String text = textAreaReply.getText();
                
                // Split the text into words
                String[] words = text.trim().split("\\s+");
                
                // Update the word count label
                lblWordCount.setText("Words: " + words.length);
                
                // Disable editing if the word count exceeds 500
                textAreaReply.setEditable(words.length <= 500);
            }
        });
		scrollPaneReply.setViewportView(textAreaReply);
		
		lblWordCount = new JLabel("Word: 0");
		lblWordCount.setForeground(new Color(255, 0, 0));
		lblWordCount.setBounds(523, 321, 100, 23);
		contentPane.add(lblWordCount);
		
	}
}
