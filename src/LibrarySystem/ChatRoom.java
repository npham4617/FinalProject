package LibrarySystem;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class ChatRoom extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField user1textField;
	public JTextField user2textField;
	private static Connection conn = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoom frame = new ChatRoom(0L, 0L);
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
	public ChatRoom(Long user1, Long user2) {
		conn = SqliteConnect.connect();
		setTitle("Chat Room");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 479, 379);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLabeluser1 = new JLabel("1st username");
		lblLabeluser1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblLabeluser1.setForeground(new Color(255, 255, 255));
		lblLabeluser1.setBounds(189, 81, 93, 23);
		contentPane.add(lblLabeluser1);
		
		// Show user's name
		String username1 = null;
		try {
			String query = "select * from User where ID_User = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setLong(1, user1);
			ResultSet rs = pst.executeQuery();
							
			while(rs.next()) {
				username1 = rs.getString("Name");
			}	
			pst.close();							
		} catch (Exception e) {
				e.printStackTrace();
		}
		
		user1textField = new JTextField();
		user1textField.setEditable(false);
		user1textField.setBounds(123, 117, 203, 23);
		user1textField.setText(username1);
		contentPane.add(user1textField);
		user1textField.setColumns(10);
		
		JLabel lblLabeluser2 = new JLabel("2nd username");
		lblLabeluser2.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblLabeluser2.setForeground(new Color(255, 255, 255));
		lblLabeluser2.setBounds(189, 176, 93, 23);
		contentPane.add(lblLabeluser2);
		
		// Show user's name
		String username2 = null;
		try {
			String query = "select * from User where ID_User = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setLong(1, user2);
			ResultSet rs = pst.executeQuery();
							
			while(rs.next()) {
				username2 = rs.getString("Name");
			}	
			pst.close();							
		} catch (Exception e) {
				e.printStackTrace();
		}		
		
		user2textField = new JTextField();
		user2textField.setEditable(false);
		user2textField.setBounds(123, 210, 203, 23);
		contentPane.add(user2textField);
		user2textField.setText(username2);
		user2textField.setColumns(10);
		
		JButton btnjoinButton = new JButton("JOIN CHAT");
		btnjoinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window win1 = new Window();
				win1.username.setText(user1textField.getText());
				win1.setUndecorated(true);
				win1.setLocationRelativeTo(null);
				
				Window win2 = new Window();
				win2.username.setText(user2textField.getText());		
				win2.setUndecorated(true);
				win2.setLocation(win1.getX() + win1.getWidth() + 20, win1.getY());
				
				win1.setOtherWindow(win2);
		        win2.setOtherWindow(win1);
		        
				win1.setVisible(true);
				win2.setVisible(true);
				dispose();
			}
		});
		btnjoinButton.setBounds(171, 277, 107, 23);
		contentPane.add(btnjoinButton);
		
		JLabel lbltitleLabel = new JLabel("CHAT ROOM");
		lbltitleLabel.setForeground(new Color(255, 255, 255));
		lbltitleLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lbltitleLabel.setBounds(144, 25, 167, 23);
		contentPane.add(lbltitleLabel);
		
		// Set ChatRoom background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(0, 0, 479, 379);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/floral-ornaments.jpg")));
		contentPane.add(lblImageLabel);
	}
}
