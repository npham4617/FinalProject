package LibrarySystem;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;
import java.sql.*;

import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;



public class Library extends JFrame {

	private static Connection conn = null;
	
	private static final long serialVersionUID = 1L;
	private static JTable table;	
	private JScrollPane scrollPane;
	private JTextField textTitle;
	private JTextField textISBN;
	private JTextField textAuthor;
	private JTextField textGenre;
	private JTextField textStatus;
	private JTextField textSearchField;
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Library frame = new Library();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					
					String query = "select ISBN, Title, Author, Genre, Status from Book";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					pst.close();							
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
	
	public void refreshTable() {
		try {
			String query = "select ISBN, Title, Author, Genre, Status from Book";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();							
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Library() {
		setResizable(false);
		setTitle("Library System");
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		conn = SqliteConnect.connect();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 611, 619);
		
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 160));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Verdana", Font.PLAIN, 11));
		scrollPane.setBounds(27, 359, 545, 199);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Verdana", Font.PLAIN, 10));
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ISBN", "Title", "Author", "Genre", "Status"
			}
		));
		
		JRadioButton rdbtnAllButton = new JRadioButton("All Books");
		rdbtnAllButton.setOpaque(false);
		rdbtnAllButton.setForeground(new Color(255, 255, 255));
		rdbtnAllButton.setSelected(true);
		rdbtnAllButton.setBounds(107, 324, 93, 23);
		contentPane.add(rdbtnAllButton);
		
		JRadioButton rdbtnFictionButton = new JRadioButton("Fiction Books");
		rdbtnFictionButton.setForeground(new Color(255, 255, 255));
		rdbtnFictionButton.setOpaque(false);
		rdbtnFictionButton.setBounds(227, 324, 106, 23);
		contentPane.add(rdbtnFictionButton);
		
		JRadioButton rdbtnNonFictionButton = new JRadioButton("Non-fiction Books");
		rdbtnNonFictionButton.setForeground(new Color(255, 255, 255));
		rdbtnNonFictionButton.setOpaque(false);
		rdbtnNonFictionButton.setBounds(368, 324, 127, 23);
		contentPane.add(rdbtnNonFictionButton);
		
		JLabel lblisbn = new JLabel("ISBN");
		lblisbn.setForeground(new Color(255, 255, 255));
		lblisbn.setBounds(37, 168, 36, 14);
		contentPane.add(lblisbn);
		
		textISBN = new JTextField();
		textISBN.setFont(new Font("Verdana", Font.PLAIN, 11));
		textISBN.setBounds(83, 163, 112, 23);
		contentPane.add(textISBN);
		textISBN.setColumns(10);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setForeground(new Color(255, 255, 255));
		lblTitle.setBounds(37, 208, 38, 14);
		contentPane.add(lblTitle);
		
		textTitle = new JTextField();
		textTitle.setFont(new Font("Verdana", Font.PLAIN, 11));
		textTitle.setBounds(83, 202, 328, 25);
		contentPane.add(textTitle);
		textTitle.setColumns(10);

		JLabel lblNewLabel = new JLabel("Author");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(37, 245, 46, 14);
		contentPane.add(lblNewLabel);
		
		textAuthor = new JTextField();
		textAuthor.setFont(new Font("Verdana", Font.PLAIN, 11));
		textAuthor.setBounds(83, 241, 112, 23);
		contentPane.add(textAuthor);
		textAuthor.setColumns(10);
		
		JLabel lblGenre = new JLabel("Genre");
		lblGenre.setForeground(new Color(255, 255, 255));
		lblGenre.setBounds(224, 245, 38, 14);
		contentPane.add(lblGenre);
		
		textGenre = new JTextField();
		textGenre.setFont(new Font("Verdana", Font.PLAIN, 11));
		textGenre.setBounds(268, 240, 100, 24);
		contentPane.add(textGenre);
		textGenre.setColumns(10);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setForeground(new Color(255, 255, 255));
		lblStatus.setBounds(405, 245, 38, 14);
		contentPane.add(lblStatus);
		
		textStatus = new JTextField();
		textStatus.setFont(new Font("Verdana", Font.PLAIN, 11));
		textStatus.setBounds(449, 240, 100, 24);
		contentPane.add(textStatus);
		textStatus.setColumns(10);
		
		JButton btnAdd = new JButton("ADD");
		btnAdd.setBackground(new Color(255, 255, 255));
		btnAdd.setBounds(44, 285, 80, 23);
		contentPane.add(btnAdd);
		
		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.setBackground(new Color(255, 255, 255));
		btnUpdate.setBounds(163, 285, 80, 23);
		contentPane.add(btnUpdate);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.setBackground(new Color(255, 255, 255));
		btnDelete.setBounds(278, 285, 80, 23);
		contentPane.add(btnDelete);
		
		JButton btnNewButton = new JButton("BACK TO MAIN MENU");
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setBounds(395, 285, 154, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblTypeLabel = new JLabel("Type");
		lblTypeLabel.setForeground(new Color(255, 255, 255));
		lblTypeLabel.setBounds(320, 168, 46, 14);
		contentPane.add(lblTypeLabel);
		
		JLabel lblTitleWindowLabel = new JLabel("MANAGE BOOK");
		lblTitleWindowLabel.setForeground(new Color(255, 255, 255));
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lblTitleWindowLabel.setBounds(204, 11, 207, 49);
		contentPane.add(lblTitleWindowLabel);
		
		JLabel lblWelcome = new JLabel("Hi, Lidaaaaaaaaaa");
		lblWelcome.setForeground(new Color(255, 255, 255));
		lblWelcome.setBounds(244, 65, 127, 23);
		contentPane.add(lblWelcome);

		JComboBox<String> comboBoxType = new JComboBox<String>();
		comboBoxType.setBackground(new Color(255, 255, 255));
		comboBoxType.setFont(new Font("Verdana", Font.PLAIN, 11));
		comboBoxType.setModel(new DefaultComboBoxModel<String>(new String[] {"Fiction Book", "NonFiction Book"}));
		comboBoxType.setBounds(366, 163, 160, 22);
		contentPane.add(comboBoxType);
		
		textSearchField = new JTextField();
		textSearchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(textSearchField.getText().equals("Enter ISBN here ...")) {
					textSearchField.setText(null);
					textSearchField.requestFocus();
					removePlaceholder(textSearchField);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(textSearchField.getText().length()==0) {
					addPlaceholder(textSearchField);
					textSearchField.setText("Enter ISBN here ...");
				}
			}
		});
		textSearchField.setBackground(Color.WHITE);
		textSearchField.setBounds(163, 120, 138, 20);
		contentPane.add(textSearchField);
		textSearchField.setColumns(10);
		
		JButton btnSearchButton = new JButton("SEARCH");
		btnSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count=0;
				try {
					String query = "select * from Book where ISBN='"+textSearchField.getText()+"'";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();					
					while (rs.next()) {
						count=count+1;
						textISBN.setText(rs.getString("ISBN"));
						textTitle.setText(rs.getString("Title"));
						textAuthor.setText(rs.getString("Author"));
						textGenre.setText(rs.getString("Genre"));
						textStatus.setText(rs.getString("Status"));
						for (int i = 0; i < comboBoxType.getItemCount(); i ++ ) {
							if(comboBoxType.getItemAt(i).toString().equalsIgnoreCase(rs.getString("Type"))){
								comboBoxType.setSelectedIndex(i);
							}
						}
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
		btnSearchButton.setBounds(320, 119, 89, 23);
		contentPane.add(btnSearchButton);
		
		// Set Library background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(0, 0, 611, 619);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/blue.jpg")));
		contentPane.add(lblImageLabel);		

		// All Book check-box is selected
		rdbtnAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnAllButton.isSelected()) {
					rdbtnFictionButton.setSelected(false);
					rdbtnNonFictionButton.setSelected(false);
					try {
						String query = "select ISBN, Title, Author, Genre, Status from Book";
						PreparedStatement pst = conn.prepareStatement(query);
						ResultSet rs = pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));
										
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		// Fiction Book check-box is selected
		rdbtnFictionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnAllButton.setSelected(false);
				rdbtnNonFictionButton.setSelected(false);
				try {
					String query = "select ISBN, Title, Author, Genre, Status from Book where Type = \"Fiction Book\"";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
									
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// NonFiction Book check-box is selected
		rdbtnNonFictionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnAllButton.setSelected(false);
				rdbtnFictionButton.setSelected(false);
				try {
					String query = "select ISBN, Title, Author, Genre, Status from Book where Type = \"NonFiction Book\"";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
									
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// Get value from JTable to set into JTextField
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int row = table.getSelectedRow();
					String id = (table.getModel().getValueAt(row, 0).toString());
					String query = "select * from Book where ISBN='"+id+"'";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					
					while (rs.next()) {
						textISBN.setText(rs.getString("ISBN"));
						textTitle.setText(rs.getString("Title"));
						textAuthor.setText(rs.getString("Author"));
						textGenre.setText(rs.getString("Genre"));
						textStatus.setText(rs.getString("Status"));
						for (int i = 0; i < comboBoxType.getItemCount(); i ++ ) {
							if(comboBoxType.getItemAt(i).toString().equalsIgnoreCase(rs.getString("Type"))){
								comboBoxType.setSelectedIndex(i);
							}
						}
					}			
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		// Add new book
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query;
				PreparedStatement pst;
				try {
					query = "select * from Book where ISBN='"+textISBN.getText()+"'";
					pst = conn.prepareStatement(query);
					ResultSet rs=pst.executeQuery();
					int count =0;
					while(rs.next()) {
						count=count+1;
					}
					if(count >= 1)
					{
						JOptionPane.showMessageDialog(null, "Book is existed. Try Again.", "LOGIN", JOptionPane.INFORMATION_MESSAGE);
						// Set text field is clear
						textISBN.setText("");
						textTitle.setText("");
						textAuthor.setText("");
						textGenre.setText("");
						textStatus.setText("");
					}
					else {			
					query = "insert into \"Book\" (ISBN, Title, Author, Genre, Status, Type) values (?, ?, ?, ?, ?, ?)";
					pst = conn.prepareStatement(query);
					pst.setString(1, textISBN.getText());
					pst.setString(2, textTitle.getText());
					pst.setString(3, textAuthor.getText());
					pst.setString(4, textGenre.getText());
					pst.setString(5, "Available");
					String selectedItem = comboBoxType.getSelectedItem().toString();
					pst.setString(6, selectedItem);
					pst.execute();
					refreshTable();
					JOptionPane.showMessageDialog(null, "Book is saved successfully.", "ADD NEW BOOK", JOptionPane.INFORMATION_MESSAGE);
					}
					rs.close();
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		// Update a book
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query;
				PreparedStatement pst;
				try {
					query = "select * from Book where ISBN='"+textISBN.getText()+"'";
					pst = conn.prepareStatement(query);
					ResultSet rs=pst.executeQuery();
					int count =0;
					while(rs.next()) {
						count=count+1;
					}
					if(count < 1) {
						JOptionPane.showMessageDialog(null, "Book is not existed. Try Again.", "LOGIN", JOptionPane.INFORMATION_MESSAGE);
						// Set text field is clear
						textISBN.setText("");
						textTitle.setText("");
						textAuthor.setText("");
						textGenre.setText("");
						textStatus.setText("");
					}
					if(count == 1) {
						query = "update \"Book\" set Title='"+textTitle.getText()+"', Author='"+textAuthor.getText()+"', Genre='"+textGenre.getText()+"', Status='"+textStatus.getText()+"', Type='"+comboBoxType.getSelectedItem().toString()+"' where ISBN='"+textISBN.getText()+"' ";
						pst = conn.prepareStatement(query);
						pst.execute();
						refreshTable();
						JOptionPane.showMessageDialog(null, "Book is updated successfully.", "UPDATE BOOK", JOptionPane.INFORMATION_MESSAGE);
						pst.close();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		// Delete a book
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "delete from Book where ISBN='"+textISBN.getText()+"'";
					PreparedStatement pst = conn.prepareStatement(query);
					pst.execute();
					refreshTable();
					JOptionPane.showMessageDialog(null, "Book is deleted successfully.", "DELETE BOOK", JOptionPane.INFORMATION_MESSAGE);
					pst.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});
	}
}
