package LibrarySystem;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class Account extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Connection conn = null;


	private JPanel contentPane;
	private JPanel panelLogin;
	private JPanel panelRegister;
	private JLayeredPane layeredPane;
	private JTextField LoginEmailField;
	private JPasswordField LoginPasswordField;
	private JTextField RegisterEmailField;
	private JPasswordField RegisterPasswordField;
	private JTextField RegisterNameField;

		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Account frame = new Account();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void Switch_screen(JPanel p) {
		layeredPane.removeAll();
		layeredPane.add(p);
		layeredPane.repaint();
		layeredPane.revalidate();
	}
	
	/**
	 * Create the frame.
	 */
	public Account() {
		conn = SqliteConnect.connect();
		setTitle("Library System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 611, 573);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 595, 534);
		contentPane.add(layeredPane);
		layeredPane.setLayout(null);
		
		// Login screen
		panelLogin = new JPanel();
		panelLogin.setBackground(new Color(0, 0, 160));
		panelLogin.setBounds(0, 0, 595, 534);
		layeredPane.add(panelLogin);
		panelLogin.setLayout(null);
						
		JPanel panel = new JPanel();
		panel.setBounds(229, 0, 366, 534);
		panelLogin.add(panel);
		panel.setLayout(null);
		
		JLabel EmailLabel = new JLabel("Email");
		EmailLabel.setBounds(24, 181, 46, 14);
		panel.add(EmailLabel);
		
		LoginEmailField = new JTextField();
		LoginEmailField.setBounds(115, 178, 204, 20);
		panel.add(LoginEmailField);
		LoginEmailField.setColumns(10);
		
		JLabel PasswordLabel = new JLabel("Password");
		PasswordLabel.setBounds(24, 218, 66, 14);
		panel.add(PasswordLabel);
		
		LoginPasswordField = new JPasswordField();
		LoginPasswordField.setBounds(115, 215, 204, 20);
		panel.add(LoginPasswordField);
		
		JButton LoginButton = new JButton("LOGIN");
		LoginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "select * from User where Email=? and Password=?";
					PreparedStatement pst = conn.prepareStatement(query);
					pst.setString(1, LoginEmailField.getText());
					pst.setString(2, LoginPasswordField.getText());
					ResultSet rs=pst.executeQuery();
					int count =0;
					String type = null; 
					long ID_User = 0;
					while(rs.next()) {
						type = rs.getString("Type");
						ID_User = rs.getLong("ID_User");
						count=count+1;
					}
					if(count ==1)
					{
						JOptionPane.showMessageDialog(null, "      Login successfully.", "LOGIN", JOptionPane.INFORMATION_MESSAGE);	
						if (type.equals("Faculty")) {
								AdminMenu adm = new AdminMenu(ID_User);
								adm.setVisible(true);
								adm.setLocationRelativeTo(null);
								dispose(); 
								
							} else if (type.equals("Student")) {
								UserMenu userm = new UserMenu(ID_User);
								userm.setVisible(true);
								userm.setLocationRelativeTo(null);
								dispose(); 
							}
					}
					else if (count < 1) {
						JOptionPane.showMessageDialog(null, "Email or Password is not correct. Try Again.", "LOGIN", JOptionPane.INFORMATION_MESSAGE);				
					}
					rs.close();
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		LoginButton.setBounds(237, 254, 89, 23);
		panel.add(LoginButton);
		
		JLabel lblNewLabel_2 = new JLabel("If you don't have an account");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(10, 411, 170, 14);
		panel.add(lblNewLabel_2);
		
		JButton btnCreateAccount = new JButton("Create a new account");
		btnCreateAccount.setBounds(181, 407, 164, 23);
		panel.add(btnCreateAccount);
		
		JLabel lblWelcome = new JLabel("Welcome Back!");
		lblWelcome.setFont(new Font("Verdana", Font.PLAIN, 25));
		lblWelcome.setForeground(new Color(255, 255, 255));
		lblWelcome.setBounds(10, 89, 199, 53);
		panelLogin.add(lblWelcome);
		
		JLabel lblsubtext = new JLabel("Please login to your account");
		lblsubtext.setFont(new Font("Rockwell", Font.PLAIN, 12));
		lblsubtext.setForeground(new Color(255, 255, 255));
		lblsubtext.setBounds(60, 141, 160, 26);
		panelLogin.add(lblsubtext);
		
		// Set Login background
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(0, 0, 396, 534);
		lblNewLabel.setIcon(new ImageIcon(Account.class.getResource("/Image/blue.jpg")));
		panelLogin.add(lblNewLabel);		
		
		// Register screen
		panelRegister = new JPanel();
		panelRegister.setBounds(0, 0, 595, 534);
		layeredPane.add(panelRegister);
		panelRegister.setLayout(null);
		panelRegister.setVisible(false);
		
		JPanel panel1 = new JPanel();
		panel1.setBounds(229, 0, 366, 534);
		panelRegister.add(panel1);
		panel1.setLayout(null);
		
		JLabel lblNameLabel = new JLabel("Name");
		lblNameLabel.setBounds(24, 160, 46, 14);
		lblNameLabel.setForeground(new Color(255, 255, 255));
		panel1.add(lblNameLabel);
		
		RegisterNameField = new JTextField();
		RegisterNameField.setBounds(114, 157, 204, 20);
		panel1.add(RegisterNameField);
		RegisterNameField.setColumns(10);
		
		JLabel lblEmailLabel = new JLabel("Email");
		lblEmailLabel.setBounds(24, 201, 46, 14);
		lblEmailLabel.setForeground(new Color(255, 255, 255));
		panel1.add(lblEmailLabel);
		
		RegisterEmailField = new JTextField();
		RegisterEmailField.setBounds(115, 198, 203, 20);
		panel1.add(RegisterEmailField);
		RegisterEmailField.setColumns(10);
		
		JLabel PasswordLabel1 = new JLabel("Password");
		PasswordLabel1.setBounds(24, 238, 66, 14);
		PasswordLabel1.setForeground(new Color(255, 255, 255));
		panel1.add(PasswordLabel1);
		
		RegisterPasswordField = new JPasswordField();
		RegisterPasswordField.setBounds(115, 235, 203, 20);
		panel1.add(RegisterPasswordField);
		
		JButton RegisterButton = new JButton("REGISTER");
		RegisterButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				Random rand = new Random();
				int ran_id = rand.nextInt(100);
				String query;
				PreparedStatement pst;
				try {
					query = "select * from User where Email='"+RegisterEmailField.getText()+"'";
					pst = conn.prepareStatement(query);
					ResultSet rs=pst.executeQuery();
					int count =0;
					while(rs.next()) {
						count=count+1;
					}
					if(count >= 1)
					{
						JOptionPane.showMessageDialog(null, "Account is existed. Try Again.", "LOGIN", JOptionPane.INFORMATION_MESSAGE);
						// Set text field is clear
						RegisterNameField.setText("");
						RegisterEmailField.setText("");
						RegisterPasswordField.setText("");
					}
					else {				
						query = "insert into User (ID_User, Name, Email, Password, Type) values (?, ?, ?, ?, ?)";
						pst = conn.prepareStatement(query);
						pst.setLong(1, ran_id);
						pst.setString(2, RegisterNameField.getText());
						pst.setString(3, RegisterEmailField.getText());
						pst.setString(4, RegisterPasswordField.getText());			
						pst.setString(5, "Student");
						pst.execute();
						JOptionPane.showMessageDialog(null, "New account is created successfully.", "LOGIN", JOptionPane.INFORMATION_MESSAGE);		
						UserMenu userm = new UserMenu(ran_id);
						userm.setVisible(true);
						userm.setLocationRelativeTo(null);
						dispose(); 
					}
					rs.close();
					pst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		RegisterButton.setBounds(237, 274, 92, 23);
		panel1.add(RegisterButton);
		
		JLabel lblNewLabel_3 = new JLabel("If you already have an account");
		lblNewLabel_3.setBounds(15, 431, 175, 14);
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel1.add(lblNewLabel_3);
		
		JButton RegisterLoginButton = new JButton("Login");
		RegisterLoginButton.setBounds(202, 427, 140, 23);
		panel1.add(RegisterLoginButton);
		
		JLabel lblsignup = new JLabel("Good To See You!");
		lblsignup.setFont(new Font("Verdana", Font.PLAIN, 23));
		lblsignup.setBounds(10, 89, 222, 53);
		panelRegister.add(lblsignup);
		
		JLabel lblRsubtext = new JLabel("Please sign up an account");
		lblRsubtext.setFont(new Font("Rockwell", Font.PLAIN, 12));
		lblRsubtext.setBounds(60, 141, 160, 26);
		panelRegister.add(lblRsubtext);
		
		// Set Register background
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(0, 0, 396, 534);
		lblImageLabel.setIcon(new ImageIcon(Account.class.getResource("/Image/blue.jpg")));
		panel1.add(lblImageLabel);
	
		// Switch between the layers
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Switch_screen(panelRegister);
				panelRegister.setVisible(true);
			}
		});
		
		RegisterLoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Switch_screen(panelLogin);
			}
		});
	}

}
