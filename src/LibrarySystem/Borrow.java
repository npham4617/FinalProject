package LibrarySystem;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import com.toedter.calendar.JDateChooser;


public class Borrow extends JFrame {

	private static Connection conn = null;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextField SearchField;
	private JTextField ISBNtextField;
	private JTextField TitletextField;
	private JTextField AuthortextField;
	private JTextField GenretextField;
	private JTextField StatustextField;
	private JTextField TypetextField;
	
	private static JTable tableBookList;
	private static JTextPane textPaneFeedback;
	private static JButton btnSearchButton;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Borrow frame = new Borrow(0L);  // Pass a default value for demonstration
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// show feedback
	public static void fetchDataFromDatabase(String searchtext) {
		textPaneFeedback.setText(null);
		
        StyledDocument doc = textPaneFeedback.getStyledDocument();
        
        // Define styles (colors) for each row
        MutableAttributeSet BoldStyle = new SimpleAttributeSet();
        StyleConstants.setBold(BoldStyle, true);
        
		String query;
		PreparedStatement pst;

		try {	
			query = "select User.Name as 'From', Feedback.Feedback from User join Feedback on User.ID_User = Feedback.ID_User where Feedback.ISBN = ? ";
			pst = conn.prepareStatement(query);
			pst.setLong(1, Long.parseLong(searchtext)); 
			ResultSet rs = pst.executeQuery();
			if (!rs.isBeforeFirst()) {
		        doc.insertString(doc.getLength(), "\n         There is no any feedback for this book.", null);
		    } else {
				while (rs.next()) {
					String from = rs.getString("From");
		            String feedback = rs.getString("Feedback");
		            // Append data to the JTextPane with the appropriate style            
	                doc.insertString(doc.getLength(), "Written by ", null);
	                doc.insertString(doc.getLength(), from + ":\n", BoldStyle);
	                doc.insertString(doc.getLength(), feedback + "\n\n", null);
				}
				pst.close();
				rs.close();
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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

	public void refreshTable() {
		String query;
		PreparedStatement pst;
		try {
			// change status to Borrowed
			query = "update Book set Status=? where ISBN=?";					
			pst = conn.prepareStatement(query);	
			pst.setString(1, "Borrowed");
			pst.setString(2, ISBNtextField.getText());
			pst.execute();
			//Refresh the table
			query = "select ISBN, Title, Author, Genre from Book where Status='Available'";
			pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			tableBookList.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the frame.
	 */
	public Borrow(Long userid) {
		conn = SqliteConnect.connect();
		setResizable(false);
		setTitle("Library System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 672, 518);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitleWindowLabel = new JLabel("BORROW BOOK");
		lblTitleWindowLabel.setForeground(new Color(255, 255, 255));
		lblTitleWindowLabel.setBounds(251, 11, 205, 29);
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		contentPane.add(lblTitleWindowLabel);
		
		// Show user's name
		String name = null;
		try {
			String query = "select * from User where ID_User = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setLong(1, userid);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				name = rs.getString("Name");
			}	
			pst.close();							
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JLabel lblUserLabel = new JLabel("Hi, ");
		lblUserLabel.setForeground(new Color(255, 255, 255));
		lblUserLabel.setBounds(287, 51, 30, 14);
		lblUserLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblUserLabel);
		
		JLabel lblName = new JLabel("");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setForeground(new Color(255, 255, 255));
		lblName.setText(name);
		lblName.setBounds(311, 51, 93, 14);
		contentPane.add(lblName);
		
		JLabel lblISBNLabel = new JLabel("ISBN");
		lblISBNLabel.setForeground(new Color(255, 255, 255));
		lblISBNLabel.setBounds(346, 96, 46, 14);
		contentPane.add(lblISBNLabel);
		
		ISBNtextField = new JTextField();
		ISBNtextField.setBounds(390, 93, 77, 20);
		ISBNtextField.setEditable(false);
		contentPane.add(ISBNtextField);
		ISBNtextField.setColumns(10);
		
		JLabel lblTypeLabel = new JLabel("Type");
		lblTypeLabel.setForeground(new Color(255, 255, 255));
		lblTypeLabel.setBounds(493, 96, 40, 14);
		contentPane.add(lblTypeLabel);
		
		TypetextField = new JTextField();
		TypetextField.setEditable(false);
		TypetextField.setBounds(535, 93, 96, 20);
		contentPane.add(TypetextField);
		TypetextField.setColumns(10);
		
		JLabel lblTitleLabel = new JLabel("Title");
		lblTitleLabel.setForeground(new Color(255, 255, 255));
		lblTitleLabel.setBounds(346, 128, 46, 14);
		contentPane.add(lblTitleLabel);
		
		TitletextField = new JTextField();
		TitletextField.setBounds(390, 125, 234, 20);
		TitletextField.setEditable(false);
		contentPane.add(TitletextField);
		TitletextField.setColumns(10);
		
		JLabel lblAuthorLabel = new JLabel("Author");
		lblAuthorLabel.setForeground(new Color(255, 255, 255));
		lblAuthorLabel.setBounds(346, 163, 46, 14);
		contentPane.add(lblAuthorLabel);
		
		AuthortextField = new JTextField();
		AuthortextField.setBounds(390, 160, 173, 20);
		AuthortextField.setEditable(false);
		contentPane.add(AuthortextField);
		AuthortextField.setColumns(10);
		
		JLabel lblGenreLabel = new JLabel("Genre");
		lblGenreLabel.setForeground(new Color(255, 255, 255));
		lblGenreLabel.setBounds(346, 199, 46, 14);
		contentPane.add(lblGenreLabel);
		
		GenretextField = new JTextField();
		GenretextField.setBounds(390, 196, 81, 20);
		GenretextField.setEditable(false);
		contentPane.add(GenretextField);
		GenretextField.setColumns(10);
		
		JLabel lblFeedbackLabel = new JLabel("Feedback");
		lblFeedbackLabel.setForeground(new Color(255, 255, 255));
		lblFeedbackLabel.setBounds(346, 237, 77, 14);
		contentPane.add(lblFeedbackLabel);
		
		SearchField = new JTextField();
		SearchField.setBounds(9, 96, 173, 23);
		SearchField.getDocument().addDocumentListener(new DocumentListener() {
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
                String text = SearchField.getText();

                if (text.equals("Enter ISBN here ...")) {
                	btnSearchButton.setEnabled(false);
                    return; // Exit the method
                }

                // Check if the text is empty
                if (text.isEmpty()) {
                	btnSearchButton.setEnabled(false);
                } else {
                    // Use regular expression to check if text contains only digits
                    if (!Pattern.matches("\\d*", text)) {
                    	btnSearchButton.setEnabled(false);
                    } else {
                    	btnSearchButton.setEnabled(true);
                    }
                }
            }
        });
		
		SearchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(SearchField.getText().equals("Enter ISBN here ...")) {
					SearchField.setText(null);
					SearchField.requestFocus();
					removePlaceholder(SearchField);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(SearchField.getText().length()==0) {
					addPlaceholder(SearchField);
					SearchField.setText("Enter ISBN here ...");
				}
			}
		});
		contentPane.add(SearchField);
		SearchField.setColumns(10);
		
		btnSearchButton = new JButton("Search");
		btnSearchButton.setBounds(192, 96, 89, 23);
		btnSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count=0;
				try {
					String query = "select * from Book where ISBN='"+SearchField.getText()+"'";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();					
					while (rs.next()) {
						count=count+1;
						ISBNtextField.setText(rs.getString("ISBN"));
						TitletextField.setText(rs.getString("Title"));
						AuthortextField.setText(rs.getString("Author"));
						GenretextField.setText(rs.getString("Genre"));
						StatustextField.setText(rs.getString("Status"));
						TypetextField.setText(rs.getString("Type"));
						
						fetchDataFromDatabase(ISBNtextField.getText());
					}
					if (count < 1) {
						JOptionPane.showMessageDialog(null, "Book is not existed", "SEARCH", JOptionPane.INFORMATION_MESSAGE);				
					}
					rs.close();
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		contentPane.add(btnSearchButton);
	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 140, 317, 231);
		scrollPane.setFont(new Font("Verdana", Font.PLAIN, 11));
		contentPane.add(scrollPane);
		
		tableBookList = new JTable() {
				@Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    };
		};
		
		scrollPane.setViewportView(tableBookList);
		tableBookList.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ISBN", "Title", "Author", "Genre"
			}
			
			
		));
		
			
		// Show all available books into the table
		try {
			String query = "select ISBN, Title, Author, Genre from Book where Status='Available'";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			tableBookList.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tableBookList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int row = tableBookList.getSelectedRow();
					String id = (tableBookList.getModel().getValueAt(row, 0).toString());
					String query = "select * from Book where ISBN='"+id+"'";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					
					while (rs.next()) {
						ISBNtextField.setText(rs.getString("ISBN"));
						TitletextField.setText(rs.getString("Title"));
						AuthortextField.setText(rs.getString("Author"));
						GenretextField.setText(rs.getString("Genre"));
						StatustextField.setText(rs.getString("Status"));
						TypetextField.setText(rs.getString("Type"));
						
						fetchDataFromDatabase(ISBNtextField.getText());
					}		
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JLabel lblStatusLabel = new JLabel("Status");
		lblStatusLabel.setForeground(new Color(255, 255, 255));
		lblStatusLabel.setBounds(499, 199, 60, 14);
		contentPane.add(lblStatusLabel);
		
		StatustextField = new JTextField();
		StatustextField.setBounds(550, 196, 81, 20);
		StatustextField.setEditable(false);
		contentPane.add(StatustextField);
		StatustextField.setColumns(10);
				
		JScrollPane scrollPaneFeedback = new JScrollPane();
		scrollPaneFeedback.setBounds(346, 260, 290, 110);
		contentPane.add(scrollPaneFeedback);
		
		textPaneFeedback = new JTextPane();

		textPaneFeedback.setEditable(false);
		scrollPaneFeedback.setViewportView(textPaneFeedback);
		
		JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("MM-dd-yyyy");
		dateChooser.setBounds(161, 396, 231, 20);
		contentPane.add(dateChooser);
		
		JLabel lblReturnLabel = new JLabel("Expected Return Date");
		lblReturnLabel.setForeground(new Color(255, 255, 255));
		lblReturnLabel.setBounds(22, 396, 129, 20);
		contentPane.add(lblReturnLabel);
		
		JButton btnBorrowButton = new JButton("BORROW");
		btnBorrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				Random rand = new Random();
				int ran_id = rand.nextInt(100);
				String query;
				PreparedStatement pst;
				try {
					query = "select Status from Book where ISBN=?";
					pst = conn.prepareStatement(query);
					pst.setString(1, ISBNtextField.getText());
				    ResultSet rs = pst.executeQuery();
					if(rs.next()) {
						String status = rs.getString("Status");
						if (status.equals("Borrowed")) {
							JOptionPane.showMessageDialog(null, "The book has been borrowed by someone.", "BORROW", JOptionPane.INFORMATION_MESSAGE);				
						} else if (status.equals("Available")) {
							query = "insert into \"Transaction\" (ID, ID_User, ISBN, BorrowDate, ExpectedReturn) values (?, ?, ?, ?, ?)";
							pst = conn.prepareStatement(query);
							pst.setLong(1, ran_id);
							pst.setLong(2, userid);
							pst.setString(3, ISBNtextField.getText());
							pst.setString(4, dateFormat.format(new Date()));
							pst.setString(5, dateFormat.format(dateChooser.getDate()));		
							pst.execute();
							JOptionPane.showMessageDialog(null, "The book has been successfully borrowed.", "BORROW", JOptionPane.INFORMATION_MESSAGE);	
						}
						
					pst.close();
					refreshTable();
					}
					else {
						JOptionPane.showMessageDialog(null, "The book is not existed.", "BORROW", JOptionPane.INFORMATION_MESSAGE);	
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnBorrowButton.setBounds(358, 439, 89, 23);
		contentPane.add(btnBorrowButton);
		
		JButton btnReturnButton = new JButton("BACK TO MAIN MENU");
		btnReturnButton.addActionListener(new ActionListener() {
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
		btnReturnButton.setBounds(473, 439, 173, 23);
		contentPane.add(btnReturnButton);
		
		// Set Borrow background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setForeground(new Color(255, 255, 255));
		lblImageLabel.setBounds(0, 0, 672, 518);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/blue.jpg")));
		contentPane.add(lblImageLabel);

	}
}
