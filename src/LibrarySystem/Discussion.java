package LibrarySystem;

import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

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
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
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
	private static JLabel lblmessageLabel;
	private static JButton btnSearch;
	
	private JLabel lblbookname;
	private JButton btnSubmitButton;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Discussion frame = new Discussion(0L); // Pass a default value for demonstration
					frame.setLocationRelativeTo(null);
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
	
	public static void Refreshdata(int feedbackid) {
		String query;
		PreparedStatement pst;
    	StyledDocument doc = textPaneFeedback.getStyledDocument();
		// Append new feedback to the JTextPane
	    MutableAttributeSet BoldStyle = new SimpleAttributeSet();
	    StyleConstants.setForeground(BoldStyle, Color.BLACK);
	    StyleConstants.setBold(BoldStyle, true);
	    try {
		    // Refresh the data
		    query = "select User.Name as 'From', Feedback.Feedback, Feedback.WrittenDate from User join Feedback on User.ID_User = Feedback.ID_User where Feedback.ID = ? ";
		    pst = conn.prepareStatement(query);
		    pst.setLong(1, feedbackid);
		    ResultSet rs = pst.executeQuery();
		    while (rs.next()) {
		        String from = rs.getString("From");
		        String feedback = rs.getString("Feedback");
		        String writtendate = rs.getString("WrittenDate");
		        
		        String defaultText = "\n\t\t          There is no any feedback for this book.";
		        String currentText = doc.getText(0, doc.getLength());
		        
		        if (currentText.isEmpty() || currentText.trim().equals(defaultText.trim())) {
		            // Set the text to null or empty
		            textPaneFeedback.setText(null);
		        }
		        doc.insertString(doc.getLength(),"Written by ", null);
			    doc.insertString(doc.getLength(), from , BoldStyle);
			    doc.insertString(doc.getLength(), " on " + writtendate + " \n", null);
			    doc.insertString(doc.getLength(), feedback + "\n\n", null);
		        pst.close();
				rs.close();
		    }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
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
			query = "select User.Name as 'From', Feedback.Feedback, Feedback.WrittenDate from User join Feedback on User.ID_User = Feedback.ID_User where Feedback.ISBN = ? ";
			pst = conn.prepareStatement(query);
			pst.setLong(1, Long.parseLong(searchtext)); 
			ResultSet rs = pst.executeQuery();
			if (!rs.isBeforeFirst()) {
				doc.insertString(doc.getLength(), "\n\t\t          There is no any feedback for this book.", null);
		    } else {
				while (rs.next()) {
					String from = rs.getString("From");
		            String feedback = rs.getString("Feedback");
		            String writtendate = rs.getString("WrittenDate");
		            // Append data to the JTextPane with the appropriate style            
	                if (rs.getRow() % 2 == 0) {
	                	doc.insertString(doc.getLength(), "Written by ", blueStyle);
	                	doc.insertString(doc.getLength(), from, blueBoldStyle);
	                	doc.insertString(doc.getLength(), " on " + writtendate + "\n", blueStyle);
	                    doc.insertString(doc.getLength(), feedback + "\n\n", blueStyle);
	                } else {
	                	doc.insertString(doc.getLength(), "Written by ", magentaStyle);
	                	doc.insertString(doc.getLength(), from , magentaBoldStyle);
	                	doc.insertString(doc.getLength(), " on " + writtendate + " \n", magentaStyle);
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
		lblTitleWindowLabel.setForeground(new Color(255, 255, 255));
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lblTitleWindowLabel.setBounds(222, 22, 278, 23);
		contentPane.add(lblTitleWindowLabel);
		
		JScrollPane scrollPaneFeedback = new JScrollPane();
		scrollPaneFeedback.setBounds(34, 132, 597, 178);
		contentPane.add(scrollPaneFeedback);
		
		textPaneFeedback = new JTextPane();
		textPaneFeedback.setEditable(false);
		scrollPaneFeedback.setViewportView(textPaneFeedback);
		
		btnSubmitButton = new JButton("SUBMIT");
		btnSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				Random rand = new Random();
				int ran_id = rand.nextInt(100);
				String query;
				PreparedStatement pst;
				
				try {
					query = "insert into Feedback (ID, ID_User, ISBN, Feedback, WrittenDate) values (?, ?, ?, ?, ?)";
					pst = conn.prepareStatement(query);
					pst.setLong(1, ran_id);
					pst.setLong(2, userid);
					pst.setString(3, SearchtextField.getText());
					pst.setString(4, textAreaReply.getText());
					pst.setString(5, dateFormat.format(new Date()));
					pst.execute();
					JOptionPane.showMessageDialog(null, "Thank you for providing feedback for the book.", "FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
					pst.close();	
					Refreshdata(ran_id);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnSubmitButton.setBounds(393, 510, 89, 23);
		contentPane.add(btnSubmitButton);
		
		JButton btnMainButton = new JButton("MAIN MENU");
		btnMainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "select * from User where ID_User=?";
					PreparedStatement pst = conn.prepareStatement(query);
					pst.setLong(1, userid);
					ResultSet rs=pst.executeQuery();
					while(rs.next()) {
						String type = rs.getString("Type");
						if (type.equals("Faculty")) {
								AdminMenu adm = new AdminMenu(userid);
								adm.setVisible(true);
								adm.setLocationRelativeTo(null);
								dispose(); 
								
							} else if (type.equals("Student")) {
								UserMenu userm = new UserMenu(userid);
								userm.setVisible(true);
								userm.setLocationRelativeTo(null);
								dispose(); 
							}
					}
					rs.close();
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnMainButton.setBounds(511, 510, 120, 23);
		contentPane.add(btnMainButton);
		
		SearchtextField = new JTextField();
		SearchtextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }

            private void checkInput() {
                // Get the current text from the searchTextField
                String text = SearchtextField.getText();

                if (text.equals("Enter ISBN here ...")) {
                	lblmessageLabel.setText(""); // Clear the message label
                	btnSearch.setEnabled(false);
                    return; // Exit the method
                }

                // Check if the text is empty
                if (text.isEmpty()) {
                	lblmessageLabel.setText(""); // Clear the message label if input is empty
                	btnSearch.setEnabled(false);
                } else {
                    // Use regular expression to check if text contains only digits
                    if (!Pattern.matches("\\d*", text)) {
                    	lblmessageLabel.setText("Non-numeric input detected!");
                    	btnSearch.setEnabled(false);
                    } else {
                    	lblmessageLabel.setText("");
                    	btnSearch.setEnabled(true);
                    }
                }
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
		
		SearchtextField.setBounds(34, 69, 184, 20);
		contentPane.add(SearchtextField);
		SearchtextField.setColumns(10);
		
		lblmessageLabel = new JLabel();
		lblmessageLabel.setText("");
		lblmessageLabel.setForeground(new Color(255, 0, 0));
		lblmessageLabel.setBounds(346, 69, 251, 23);
		contentPane.add(lblmessageLabel);
		
		btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Get Book feedback
				fetchDataFromDatabase(SearchtextField.getText());
				
				// Get the Book name 
				String query;
				PreparedStatement pst;
				String name = null;
				try {	
					query = "select * from Book where ISBN = ? ";
					pst = conn.prepareStatement(query);
					pst.setLong(1, Long.parseLong(SearchtextField.getText())); 
					ResultSet rs = pst.executeQuery();
					int count = 0;
					while (rs.next()) {
						count = count + 1;
						name = rs.getString("Title");
				    }
					if (count < 1) {
						JOptionPane.showMessageDialog(null, "The book is not existed.", "SEARCH", JOptionPane.INFORMATION_MESSAGE);
						btnSubmitButton.setEnabled(false);
					} else {
						btnSubmitButton.setEnabled(true);
					}
					pst.close();
					rs.close();
					lblbookname.setText(name);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		
		btnSearch.setBounds(233, 68, 89, 23);
		contentPane.add(btnSearch);
		
		JLabel lblWelcome = new JLabel("Welcome to provide your feedback on the book:");
		lblWelcome.setForeground(new Color(255, 255, 255));
		lblWelcome.setFont(new Font("Tahoma", Font.ITALIC, 12));
		lblWelcome.setBounds(34, 102, 287, 23);
		contentPane.add(lblWelcome);
		
		lblbookname = new JLabel("");
		lblbookname.setForeground(new Color(175, 238, 238));
		lblbookname.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblbookname.setBounds(307, 102, 316, 23);
		contentPane.add(lblbookname);
		
		JLabel lblReplyLabel = new JLabel("Please provide some feedback about the book. (Max 500 words)");
		lblReplyLabel.setForeground(new Color(255, 255, 255));
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
		
		// Set Borrow background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setForeground(new Color(255, 255, 255));
		lblImageLabel.setBounds(0, 0, 672, 590);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/blue.jpg")));
		contentPane.add(lblImageLabel);

		
	}
}
