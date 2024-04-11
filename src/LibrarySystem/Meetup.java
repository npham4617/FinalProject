package LibrarySystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import javax.swing.table.TableCellRenderer;

import net.proteanit.sql.DbUtils;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;

public class Meetup extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private static Connection conn = null;
	public JLabel lblName;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Meetup frame = new Meetup(0L); // Pass a default value for demonstration
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
	public Meetup(Long userid) {
		conn = SqliteConnect.connect();


		setResizable(false);		
		setTitle("Library System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 737, 464);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitleWindowLabel = new JLabel("GATHERING");
		lblTitleWindowLabel.setForeground(new Color(255, 255, 255));
		lblTitleWindowLabel.setFont(new Font("Rockwell", Font.PLAIN, 25));
		lblTitleWindowLabel.setBounds(279, 11, 179, 30);
		contentPane.add(lblTitleWindowLabel);
		
		JScrollPane userscrollPane = new JScrollPane();
		userscrollPane.setBounds(21, 117, 356, 167);
		contentPane.add(userscrollPane);
		
		JPanel userpanel = new JPanel();
		userscrollPane.setViewportView(userpanel);
		userpanel.setLayout(new BoxLayout(userpanel, BoxLayout.Y_AXIS));
		
		// Show all available books into the table
		try {
			String query = "select * from User";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
                String name = rs.getString("name");

                // Create a panel for each user
                JPanel userRowPanel = new JPanel(new GridLayout(1, 3)); // Use GridLayout with 1 row and 2 columns
                userRowPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding
                userRowPanel.setBackground(new Color(255, 255, 102));
        		
                JLabel nameLabel = new JLabel(name);
                
                // Create buttons panel for the "CHAT" and "PROFILE" actions
                JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
                buttonsPanel.setBackground(new Color(255, 255, 102));
                JButton userChatButton = new JButton();
                JButton userProfileButton = new JButton();
                JButton userFollowButton = new JButton();
                
                int buttonWidth = 30; // Set the desired width
                int buttonHeight = 30; // Set the desired height
                userChatButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                userProfileButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                userFollowButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                
                ImageIcon chatIcon = new ImageIcon(getClass().getResource("/Image/chat.png"));
                ImageIcon profileIcon = new ImageIcon(getClass().getResource("/Image/profile.png"));
                ImageIcon followIcon = new ImageIcon(getClass().getResource("/Image/follow.png"));
                
                userChatButton.setIcon(chatIcon);
                userProfileButton.setIcon(profileIcon);
                userFollowButton.setIcon(followIcon);
                
                buttonsPanel.add(userChatButton);
                buttonsPanel.add(userProfileButton);
                buttonsPanel.add(userFollowButton);
                
                userRowPanel.add(nameLabel);
                userRowPanel.add(buttonsPanel);
                
                // Add labels to the panel
                userpanel.add(userRowPanel);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JScrollPane profilescrollPane = new JScrollPane();
		profilescrollPane.setBounds(403, 117, 288, 167);
		contentPane.add(profilescrollPane);
		
		JTextArea profiletextArea = new JTextArea();
		profilescrollPane.setViewportView(profiletextArea);
		
		JLabel lblWelcome = new JLabel("Hi, ");
		lblWelcome.setForeground(new Color(255, 255, 255));
		lblWelcome.setBounds(301, 52, 28, 23);
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
		lblName.setForeground(new Color(255, 255, 255));
		lblName.setBounds(325, 52, 144, 23);
		lblName.setText(name);
		contentPane.add(lblName);
		
		JButton btnshareProfile = new JButton("SHARE PROFILE");
		btnshareProfile.setBounds(267, 349, 125, 23);
		contentPane.add(btnshareProfile);
		
		JButton btnMainmenu = new JButton("MAIN MENU");
		btnMainmenu.setBounds(577, 349, 125, 23);
		contentPane.add(btnMainmenu);
		
		JButton btngroup = new JButton("CREATE GROUP");
		btngroup.setBounds(420, 349, 125, 23);
		contentPane.add(btngroup);
		
		// Set Meetup background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setForeground(new Color(255, 255, 255));
		lblImageLabel.setBounds(0, 0, 721, 448);
		lblImageLabel.setIcon(new ImageIcon(Library.class.getResource("/Image/blue.jpg")));
		contentPane.add(lblImageLabel);
		
		
	}
}



