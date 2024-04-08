package LibrarySystem;

import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static Connection conn = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserMenu frame = new UserMenu(0L); // Pass a default value for demonstration
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
	public UserMenu(long userid) {
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
				
		JLabel lblName = new JLabel("");
		lblName.setBounds(265, 60, 93, 23);
		lblName.setText(name);
		contentPane.add(lblName);
		

		JLabel lblTitleWindowLabel = new JLabel("WELCOME TO LIBRARY SYSTEM");
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lblTitleWindowLabel.setBounds(110, 10, 375, 30);
		contentPane.add(lblTitleWindowLabel);

		JButton btnBorrowButton = new JButton("BORROW BOOK");
		btnBorrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Borrow br = new Borrow(userid);
				br.setVisible(true);
				br.setLocationRelativeTo(null);
				dispose(); 
			}
		});
		btnBorrowButton.setBounds(20, 113, 131, 23);
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
		btnReturnButton.setBounds(20, 163, 131, 23);
		contentPane.add(btnReturnButton);

		JButton btnProfileButton = new JButton("USER PROFILE");
		btnProfileButton.setBounds(20, 213, 131, 23);
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
		btnDiscussButton.setBounds(20, 263, 131, 23);
		contentPane.add(btnDiscussButton);

		
		 
		JButton btnMeetupButton = new JButton("MEETUP");
		btnMeetupButton.setBounds(20, 313, 131, 23);
		contentPane.add(btnMeetupButton);
		setContentPane(contentPane);
		
		// Set User page layout
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(0, 10, 595, 550);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/Library.png")));
		contentPane.add(lblImageLabel);	
		
	}

}
