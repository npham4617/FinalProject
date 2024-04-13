package LibrarySystem;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextArea DisplaytextArea;
	public JTextArea SendtextArea;
	public JLabel username;
	public JButton btnSendButton;
	public Window otherWindow;
	static String name1;
	static String name2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setVisible(true);	
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setOtherWindow(Window otherWindow) {
        this.otherWindow = otherWindow;
    }
	
	private void sendMessage() {
        String message = SendtextArea.getText().trim();
        if (!message.isEmpty()) {
        	DisplaytextArea.append(username.getText() + ": " + message + "\n");
        	if (otherWindow != null) {
        		otherWindow.DisplaytextArea.append(username.getText() + ": " + message + "\n");
        	}
        	SendtextArea.setText("");
        }
    }

	/**
	 * Create the frame.
	 */
	public Window() {
		setTitle("Chat Window");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 455, 454);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		DisplaytextArea = new JTextArea();
		DisplaytextArea.setBounds(10, 45, 419, 277);
		contentPane.add(DisplaytextArea);
		
		SendtextArea = new JTextArea();
		SendtextArea.setBounds(10, 333, 311, 51);
		contentPane.add(SendtextArea);
		
		btnSendButton = new JButton("SEND");
		btnSendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		btnSendButton.setBounds(340, 346, 89, 38);
		contentPane.add(btnSendButton);
		
		JLabel lblNewLabel = new JLabel("Chat window for ");
		lblNewLabel.setBounds(10, 11, 96, 23);
		contentPane.add(lblNewLabel);
		
		username = new JLabel("");
		username.setBounds(109, 11, 125, 23);
		contentPane.add(username);
		
		JButton btnClearButton = new JButton("CLEAR");
		btnClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisplaytextArea.setText("");
			}
		});
		btnClearButton.setBounds(340, 11, 89, 23);
		contentPane.add(btnClearButton);
	}

}
