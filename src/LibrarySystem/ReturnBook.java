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
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;

public class ReturnBook extends JFrame {

	private static final long serialVersionUID = 1L;

	private static Connection conn = null;
	
	private static JTable tableBookHistory;
	private JTextField SearchField;
	private JTextField ISBNtextField;
	private JTextField TitletextField;
	private JTextField AuthortextField;
	private JTextField GenretextField;
	private JTextField TypetextField;
	private JTextField BorrowDateField;
	private JTextField ExpectedDateField;
	private JPanel contentPane;
	private JLabel lblName;
	
	private JButton btnSearchButton;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReturnBook frame = new ReturnBook(0L); // Pass a default value for demonstration
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
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
		
	public void refreshTable(Long userid) {
		String query;
		PreparedStatement pst;
		try {
			// change status to Borrowed
			query = "update Book set Status=? where ISBN=?";					
			pst = conn.prepareStatement(query);	
			pst.setString(1, "Available");
			pst.setString(2, ISBNtextField.getText());
			pst.execute();
			
			//Refresh the table
			query = "select \r\n"
					+ "    `Transaction`.ISBN, Book.Title, `Transaction`.BorrowDate, \r\n"
					+ "    CASE\r\n"
					+ "        WHEN `Transaction`.ReturnDate IS NULL THEN 'NOT YET'\r\n"
					+ "        ELSE `Transaction`.ReturnDate\r\n"
					+ "    END AS `Return Date`\r\n"
					+ "from `Transaction`\r\n"
					+ "join User on `Transaction`.ID_User = User.ID_User\r\n"
					+ "join Book on `Transaction`.ISBN = Book.ISBN\r\n"
					+ "where `Transaction`.ID_User = ? \r\n"
					+ "order by Book.ISBN asc";
			pst = conn.prepareStatement(query);
			pst.setLong(1, userid);
			ResultSet rs = pst.executeQuery();
			tableBookHistory.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();		
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
		
	public ReturnBook(Long userid) {
		conn = SqliteConnect.connect();
		
		setTitle("Library System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 672, 429);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblReturn = new JLabel("RETURN BOOK");
		lblReturn.setForeground(new Color(255, 255, 255));
		lblReturn.setBounds(246, 10, 210, 22);
		lblReturn.setFont(new Font("Rockwell", Font.PLAIN, 25));
		contentPane.add(lblReturn);
		
		// Show user's name
		String name = null;
		try {
			String query = "select * from User where ID_User = ? ";
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
		lblUserLabel.setBounds(267, 43, 18, 15);
		lblUserLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblUserLabel);
		
		lblName = new JLabel(" ");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setForeground(new Color(255, 255, 255));
		lblName.setText(name);
		lblName.setBounds(288, 43, 93, 14);
		contentPane.add(lblName);
		
		SearchField = new JTextField();
		SearchField.setBounds(15, 92, 173, 23);
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
					String query = "select * from (select *, \r\n"
							+ "    CASE\r\n"
							+ "        WHEN `Transaction`.ReturnDate IS NULL THEN 'NOT YET'\r\n"
							+ "        ELSE `Transaction`.ReturnDate\r\n"
							+ "    END AS `Return Date`\r\n"
							+ "from `Transaction`\r\n"
							+ "join User on `Transaction`.ID_User = User.ID_User\r\n"
							+ "join Book on `Transaction`.ISBN = Book.ISBN\r\n"
							+ "where `Transaction`.ID_User = ? ) AS subquery\r\n"
							+ "where subquery.ISBN = ? ";
					PreparedStatement pst = conn.prepareStatement(query);
					pst.setLong(1, userid);
					pst.setString(1, SearchField.getText());
					ResultSet rs = pst.executeQuery();					
					while (rs.next()) {
						count=count+1;
						ISBNtextField.setText(rs.getString("ISBN"));
						TitletextField.setText(rs.getString("Title"));
						AuthortextField.setText(rs.getString("Author"));
						GenretextField.setText(rs.getString("Genre"));
						TypetextField.setText(rs.getString("Type"));
						BorrowDateField.setText(rs.getString("BorrowDate"));
						ExpectedDateField.setText(rs.getString("ExpectedReturn"));
					}
					if (count < 1) {
						JOptionPane.showMessageDialog(null, "Book is not existed or has never been borrowed by the user.", "SEARCH", JOptionPane.INFORMATION_MESSAGE);				
					}
					rs.close();
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnSearchButton.setBounds(192, 92, 89, 23);
		contentPane.add(btnSearchButton);
	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 127, 317, 186);
		scrollPane.setFont(new Font("Verdana", Font.PLAIN, 11));
		contentPane.add(scrollPane);
		tableBookHistory = new JTable() {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		
		scrollPane.setViewportView(tableBookHistory);
		tableBookHistory.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ISBN", "Title", "Borrow Date", "Return Date"
			}
		));
		
		// Show all books which the user borrow into the table
		try {
				String query = "select \r\n"
					+ "    `Transaction`.ISBN, Book.Title, `Transaction`.BorrowDate, \r\n"
					+ "    CASE\r\n"
					+ "        WHEN `Transaction`.ReturnDate IS NULL THEN 'NOT YET'\r\n"
					+ "        ELSE `Transaction`.ReturnDate\r\n"
					+ "    END AS `Return Date`\r\n"
					+ "from `Transaction`\r\n"
					+ "join User on `Transaction`.ID_User = User.ID_User\r\n"
					+ "join Book on `Transaction`.ISBN = Book.ISBN\r\n"
					+ "where `Transaction`.ID_User = ? \r\n"
					+ "order by Book.ISBN asc";
				PreparedStatement pst = conn.prepareStatement(query);
				pst.setLong(1, userid);
				ResultSet rs = pst.executeQuery();
				tableBookHistory.setModel(DbUtils.resultSetToTableModel(rs));
				pst.close();
			} catch (Exception e) {
					e.printStackTrace();
			}
		
		tableBookHistory.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int row = tableBookHistory.getSelectedRow();
					String id = (tableBookHistory.getModel().getValueAt(row, 0).toString());
					String query = "select * from `Transaction` \r\n"
							+ "join Book on `Transaction`.ISBN = Book.ISBN \r\n"
							+ "where Book.ISBN='"+id+"'";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					
					while (rs.next()) {
						ISBNtextField.setText(rs.getString("ISBN"));
						TitletextField.setText(rs.getString("Title"));
						AuthortextField.setText(rs.getString("Author"));
						GenretextField.setText(rs.getString("Genre"));
						TypetextField.setText(rs.getString("Type"));
						BorrowDateField.setText(rs.getString("BorrowDate"));
						ExpectedDateField.setText(rs.getString("ExpectedReturn"));
					}		
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JLabel lblISBNLabel = new JLabel("ISBN");
		lblISBNLabel.setForeground(new Color(255, 255, 255));
		lblISBNLabel.setBounds(350, 96, 46, 23);
		contentPane.add(lblISBNLabel);
		
		ISBNtextField = new JTextField();
		ISBNtextField.setBounds(394, 96, 77, 23);
		ISBNtextField.setEditable(false);
		contentPane.add(ISBNtextField);
		ISBNtextField.setColumns(10);
		
		JLabel lblTypeLabel = new JLabel("Type");
		lblTypeLabel.setForeground(new Color(255, 255, 255));
		lblTypeLabel.setBounds(497, 96, 40, 23);
		contentPane.add(lblTypeLabel);
		
		TypetextField = new JTextField();
		TypetextField.setEditable(false);
		TypetextField.setBounds(539, 96, 96, 23);
		contentPane.add(TypetextField);
		TypetextField.setColumns(10);
		
		JLabel lblTitleLabel = new JLabel("Title");
		lblTitleLabel.setForeground(new Color(255, 255, 255));
		lblTitleLabel.setBounds(350, 130, 46, 23);
		contentPane.add(lblTitleLabel);
		
		TitletextField = new JTextField();
		TitletextField.setBounds(394, 134, 234, 23);
		TitletextField.setEditable(false);
		contentPane.add(TitletextField);
		TitletextField.setColumns(10);
		
		JLabel lblAuthorLabel = new JLabel("Author");
		lblAuthorLabel.setForeground(new Color(255, 255, 255));
		lblAuthorLabel.setBounds(350, 172, 46, 23);
		contentPane.add(lblAuthorLabel);
		
		AuthortextField = new JTextField();
		AuthortextField.setBounds(394, 172, 173, 23);
		AuthortextField.setEditable(false);
		contentPane.add(AuthortextField);
		AuthortextField.setColumns(10);
		
		JLabel lblGenreLabel = new JLabel("Genre");
		lblGenreLabel.setForeground(new Color(255, 255, 255));
		lblGenreLabel.setBounds(350, 210, 46, 23);
		contentPane.add(lblGenreLabel);
		
		GenretextField = new JTextField();
		GenretextField.setBounds(394, 210, 117, 23);
		GenretextField.setEditable(false);
		contentPane.add(GenretextField);
		GenretextField.setColumns(10);
		
		JLabel BorrowDateLabel = new JLabel("Borrow Date");
		BorrowDateLabel.setForeground(new Color(255, 255, 255));
		BorrowDateLabel.setBounds(350, 248, 77, 23);
		contentPane.add(BorrowDateLabel);
		
		BorrowDateField = new JTextField();
		BorrowDateField.setBounds(437, 248, 173, 23);
		BorrowDateField.setEditable(false);
		contentPane.add(BorrowDateField);
		BorrowDateField.setColumns(10);
		
		JLabel lblExpectedDateLabel = new JLabel("Expected Return Date");
		lblExpectedDateLabel.setForeground(new Color(255, 255, 255));
		lblExpectedDateLabel.setBounds(350, 290, 146, 23);
		contentPane.add(lblExpectedDateLabel);
		
		ExpectedDateField = new JTextField();
		ExpectedDateField.setEditable(false);
		ExpectedDateField.setBounds(492, 290, 135, 23);
		contentPane.add(ExpectedDateField);
		ExpectedDateField.setColumns(10);
			
		JButton btnReturnButton = new JButton("RETURN BOOK");
		btnReturnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				String query;
				PreparedStatement pst;
				try {
					query = "select ReturnDate from `Transaction` where ID_User = ? AND ISBN = ?";
					pst = conn.prepareStatement(query);
					pst.setLong(1, userid);
					pst.setString(2, ISBNtextField.getText());
				    ResultSet rs = pst.executeQuery();
					if(rs.next()) {
						String status = rs.getString("ReturnDate");
						if (status != null) {
							JOptionPane.showMessageDialog(null, "You are not borrowing the book at this time.", "RETURN", JOptionPane.INFORMATION_MESSAGE);				
						} else {
							query = "update \"Transaction\" set ReturnDate = ? where ID_User = ? AND ISBN = ?";
							pst = conn.prepareStatement(query);
							pst.setString(1, dateFormat.format(new Date()));
							pst.setLong(2, userid);
							pst.setString(3, ISBNtextField.getText());
							pst.execute();
							JOptionPane.showMessageDialog(null, "The book has been successfully returned.", "RETURN", JOptionPane.INFORMATION_MESSAGE);	
						}
						
					pst.close();
					refreshTable(userid);
					}
					else {
						JOptionPane.showMessageDialog(null, "The book is not existed or has never been borrowed by the user.", "RETURN", JOptionPane.INFORMATION_MESSAGE);	
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnReturnButton.setBounds(192, 344, 140, 23);
		contentPane.add(btnReturnButton);
		
		JButton btnBackButton = new JButton("MAIN MENU");
		btnBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "select * from User where ID_User= ? ";
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
		btnBackButton.setBounds(361, 344, 140, 23);
		contentPane.add(btnBackButton);

		
		// Set Return background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setForeground(new Color(255, 255, 255));
		lblImageLabel.setBounds(0, 0, 672, 459);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/blue.jpg")));
		contentPane.add(lblImageLabel);



	}
}
