package LibrarySystem;



import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class AdminMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	public JLabel lblName;

	private static Connection conn = null;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminMenu frame = new AdminMenu(0L); // Pass a default value for demonstration
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);	

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
    
    
    /**
	 * Create the frame.
	 */
	public AdminMenu(Long userid) {
		conn = SqliteConnect.connect();
		setResizable(false);		
		setTitle("Library System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 611, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
				
		JLabel lblWelcome = new JLabel("Hi, ");
		lblWelcome.setBounds(243, 60, 33, 23);
		contentPane.add(lblWelcome);
		
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
				
		lblName = new JLabel("");
		lblName.setBounds(265, 60, 93, 23);
		lblName.setText(name);
		contentPane.add(lblName);
		
		JLabel lblTitleWindowLabel = new JLabel("WELCOME TO LIBRARY SYSTEM");
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lblTitleWindowLabel.setBounds(115, 10, 429, 30);
		contentPane.add(lblTitleWindowLabel);

		JButton btnBookButton = new JButton("MANAGE BOOK");
		btnBookButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Library lb = new Library(userid);
				lb.setVisible(true);
				lb.setLocationRelativeTo(null);
				dispose(); 
			}
		});
		btnBookButton.setBounds(20, 103, 131, 23);
		contentPane.add(btnBookButton);
		setContentPane(contentPane);
		
		JButton btnBorrowButton = new JButton("BORROW BOOK");
		btnBorrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Borrow br = new Borrow(userid);
				br.setVisible(true);
				br.setLocationRelativeTo(null);
				dispose(); 
			}
		});
		btnBorrowButton.setBounds(20, 153, 131, 23);
		contentPane.add(btnBorrowButton);
	
		
		JButton btnReturnButton = new JButton("RETURN BOOK");
		btnReturnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReturnBook rb = new ReturnBook(userid);
				rb.setVisible(true);
				rb.setLocationRelativeTo(null);
				dispose(); 
			}
		});
		
		btnReturnButton.setBounds(20, 203, 131, 23);
		contentPane.add(btnReturnButton);
		
		
		JButton btnProfileButton = new JButton("USER PROFILE");
		btnProfileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Profile pro = new Profile(userid);
				pro.setVisible(true);
				pro.setLocationRelativeTo(null);
			}
		});
		btnProfileButton.setBounds(20, 253, 131, 23);
		contentPane.add(btnProfileButton);
	
		
		JButton btnDiscussButton = new JButton("DISCUSSION");
		btnDiscussButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Discussion dis = new Discussion(userid);
				dis.setVisible(true);
				dis.setLocationRelativeTo(null);
				dispose();
			}
		});
		btnDiscussButton.setBounds(20, 303, 131, 23);
		contentPane.add(btnDiscussButton);
		
		 
		JButton btnMeetupButton = new JButton("GATHERING");
		btnMeetupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Meetup met = new Meetup(userid);
				met.setVisible(true);
				met.setLocationRelativeTo(null);
				dispose();
			}
		});
		btnMeetupButton.setBounds(20, 353, 131, 23);
		contentPane.add(btnMeetupButton);
	
		JButton btnEventButton = new JButton("EVENTS");
		btnEventButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Events event = new Events(userid);
				event.setVisible(true);
				event.setLocationRelativeTo(null);
				dispose();
			}
		});
		btnEventButton.setBounds(20, 403, 131, 23);
		contentPane.add(btnEventButton);
		
		// Set Admin page layout
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel = new JLabel("");
		lblImageLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblImageLabel.setBounds(0, 10, 585, 550);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/Library.png")));
		contentPane.add(lblImageLabel);	
		




	}
}
