import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

 
public class Chat2 extends JFrame{
	JButton sendBt;
	JTextField inputField;
	JTextArea chatContent;
	JPanel userIDPanel;
	JCheckBox userBox;
	JButton removeButton;
	
//	ArrayList<String> usernames = new ArrayList<String>();
	ArrayList<JCheckBox> userlist = new ArrayList<JCheckBox>();
	private int index4=0;
	private String message;
    static Socket sClient;
	static ObjectOutputStream writer = null;
	static ObjectInputStream reader = null;
	private String name;
	
	public String getMessage() {
		return this.message;
	}
	
	public void addUsers(String s) {		
//		for(int i = 0; i < usernames.size(); i++) {
//		    userlist.add(new JCheckBox());
//		    userlist.get(i).setText(usernames.get(i));
//		    userIDPanel.add(userlist.get(i));
//		}
		userlist.add(new JCheckBox());
	    userlist.get(index4).setText(s);
	    userIDPanel.add(userlist.get(index4));
	    index4++;
	}
		
	public void setTools() {
		try {
			String portNum = "3000";
			String hostname = "localhost";
			int port = Integer.parseInt(portNum);
			sClient = new Socket(hostname,port);
			System.out.println("here1");
			writer = new ObjectOutputStream(sClient.getOutputStream());
			String abc = this.name;
			writer.writeObject(abc);
			writer.flush();
  			reader = new ObjectInputStream(new BufferedInputStream(sClient.getInputStream()));
			System.out.println("herechat2");
		}
		catch(Exception exception2) {
			System.out.println("errors");
		}
	}
	
	public void keepLis() {
		while(true) {
			try {
				System.out.println("hereListening111");
				String newMessage = (String)reader.readObject();
//				if(newMessage.equals("@@@Username has already existed@@@.")) {
//					System.out.println("Username has already existed@@@.");
//					System.exit(0);
//				}
				System.out.println("new message herrrrrrrrr:"+newMessage);
				if(newMessage.startsWith("@@@new users apply to join@@@")) {
					String[] choose = newMessage.split("@@@");
					
					int value = JOptionPane.showConfirmDialog(null,"A new user applys to join", "caution",0);
					if(value==0) {
						String abc = "@@@add a new user@@@"+choose[2];
						System.out.println(abc);
						writer.writeObject(abc);
						writer.flush();
					}
					else if(value==1) {
						String abc = "@@@declined adding new one@@@"+choose[2];
						System.out.println(abc);
						writer.writeObject(abc);
						writer.flush();
					}
				}
				else if(newMessage.startsWith("@@@@@")) {
					String[] processedWord = newMessage.split("@@@@@");
					System.out.println(processedWord[1]);
					userIDPanel.remove(removeButton);
					addUsers(processedWord[1]);
					userIDPanel.add(removeButton);
					userIDPanel.updateUI();
					userIDPanel.repaint();

				}
				else {
					String[] processedWord = newMessage.split("@@");
					
					for(int i=0;i<processedWord.length;i++) {
						System.out.println(processedWord[i]);
					}
					if(processedWord[0].equals("username")) {
						index4 = Integer.valueOf(processedWord[processedWord.length-1]);
//						for(int i=0;i<users.size();i++) {
//							userIDPanel.remove(users.get(i));
//						}
						userIDPanel.removeAll();
//						userIDPanel.remove(removeButton);
						userlist.clear();
						for(int j=1;j<processedWord.length-1;j++) {
							JCheckBox jcb = new JCheckBox();
							jcb.setText(processedWord[j]);
							userlist.add(jcb);
							userIDPanel.add(userlist.get(j-1));
						}
						userIDPanel.add(removeButton);
						userIDPanel.updateUI();
						userIDPanel.repaint();
					}
					else {
						String[] ccc = newMessage.split("%%%");
						chatContent.append(ccc[1]+": "+ccc[0]+"\n");
						System.out.println("hereListening2222");
					}
				}
				
				
	  						
	  		}
			catch(ClassNotFoundException e2) {
					System.out.println("error3 ");
					System.exit(0);
			}
			catch(IOException e3) {
					System.out.println("error4 ");
					JOptionPane.showMessageDialog(null,"You have been removed from the room.");
					System.exit(0);
			}
		}
	}
	
	
	public Chat2(String name){
		this.name = name;
//		setTools();
		this.setLayout(new BorderLayout());
		chatContent = new JTextArea(12, 34);
		
		JScrollPane showPanel = new JScrollPane(chatContent);
		chatContent.setEditable(false);		
		JPanel inputPanel = new JPanel();
		inputField = new JTextField(20);

		sendBt = new JButton("send");
		
		
		Label label = new Label("Message");
		inputPanel.add(label);
		inputPanel.add(inputField);
		inputPanel.add(sendBt);
		
		
		userIDPanel = new JPanel();
		userIDPanel.setBorder(new TitledBorder("User List"));
		userIDPanel.setLayout(new BoxLayout(userIDPanel, BoxLayout.Y_AXIS));
//		addUsers();
		

		removeButton = new JButton("Remove");
		userIDPanel.add(removeButton);
		
		
		this.add(showPanel,BorderLayout.CENTER);
		this.add(inputPanel,BorderLayout.SOUTH);
		this.add(userIDPanel,BorderLayout.EAST);
		this.setTitle("Chat Window");
		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		AddActionListener();
		
	}
	
	
	private void AddActionListener() {
		
		sendBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// override actionPerformed
				message = inputField.getText();
//				SendMessage("MESSAGE@"+ToTargetName+"@"+NickNameText.getText()+"@"+message); 
				if(message!=null && !message.trim().equals("")){
					chatContent.append("I:"+message+"\n");
				}
				else{
					chatContent.append("Chat message cannot be empty" + "\n");
				}
				inputField.setText("");
				String abc = message;
				try {
					writer.writeObject(abc);
					writer.flush();
				}
				catch(Exception e2) {
					
				}
			}
		});
		
		
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String abc = "username";
				for(int k=0;k<userlist.size();k++) {
					if (userlist.get(k).isSelected()) {
						abc += "@@"+userlist.get(k).getText();

						
//						userIDPanel.remove(users.get(k));
					}
				}

				try {
					System.out.println(abc);
					writer.writeObject(abc);
					writer.flush();
				}
				catch(Exception e2) {
					
				}
				
//				userIDPanel.updateUI();
//				userIDPanel.repaint();
			}
		});
		
		

	}
	
	
	
	public class Tokenizer{
		String Tokens[];
		int TokenIndex = 0;

		public Tokenizer(String Message, String Delimiter) {
			Tokens = Message.split(Delimiter);
		}

		public String nextToken() {
			TokenIndex++;
			return Tokens[TokenIndex-1];
		}
	}

	
	

}
