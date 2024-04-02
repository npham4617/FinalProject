package LibrarySystem;



import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class AdminMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminMenu frame = new AdminMenu();
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
	public AdminMenu() {
		setResizable(false);
		setTitle("Library System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 611, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		
		JLabel lblWelcome = new JLabel("Hi, Lidaaaaaaaaaa");
		lblWelcome.setBounds(244, 60, 127, 23);
		contentPane.add(lblWelcome);

		JLabel lblTitleWindowLabel = new JLabel("WELCOME TO LIBRARY SYSTEM");
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lblTitleWindowLabel.setBounds(110, 10, 375, 30);
		contentPane.add(lblTitleWindowLabel);

		JButton btnBookButton = new JButton("MANAGE BOOK");
		btnBookButton.setBounds(20, 103, 131, 23);
		contentPane.add(btnBookButton);
		setContentPane(contentPane);
		
		JButton btnBorrowButton = new JButton("BORROW BOOK");
		btnBorrowButton.setBounds(20, 153, 131, 23);
		contentPane.add(btnBorrowButton);
		setContentPane(contentPane);
		
		JButton btnReturnButton = new JButton("RETURN BOOK");
		btnReturnButton.setBounds(20, 203, 131, 23);
		contentPane.add(btnReturnButton);
		setContentPane(contentPane);
		
		JButton btnProfileButton = new JButton("USER PROFILE");
		btnProfileButton.setBounds(20, 253, 131, 23);
		contentPane.add(btnProfileButton);
		setContentPane(contentPane);
		
		JButton btnDiscussButton = new JButton("DISCUSSION");
		btnDiscussButton.setBounds(20, 303, 131, 23);
		contentPane.add(btnDiscussButton);
		setContentPane(contentPane);
		
		 
		JButton btnMeetupButton = new JButton("MEETUP");
		btnMeetupButton.setBounds(20, 353, 131, 23);
		contentPane.add(btnMeetupButton);
		setContentPane(contentPane);
		
		// Set Admin page layout
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(0, 10, 595, 550);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/Library.png")));
		contentPane.add(lblImageLabel);	

	}
}
