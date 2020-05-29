import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;
public class Client extends Thread{
	
	private String name;
	public static String username;
	static String ip;
	static int port;
	public Client(String name) {
		this.name = name;
	}
	
	public static void main(String[] args) {
//		Scanner keyboard = new Scanner(System.in);
//		username = keyboard.nextLine();
		try {
			if(args.length!=3)
				throw new InvalidInputException();
			else {
				ip = args[0];
				port = Integer.parseInt(args[1]);
				username = args[2];
				}	
//			String name = username;
//			String portNum = "3000";
//			String hostname = "localhost";
//			int port = Integer.parseInt(portNum);
			Socket sClientChat = new Socket(ip,port);
			Chat2.sClient = sClientChat;
			System.out.println("here1");
			Chat2.writer = new ObjectOutputStream(Chat2.sClient.getOutputStream());
			Chat2.writer.writeObject(username);
			Chat2.writer.flush();
			Chat2.reader = new ObjectInputStream(new BufferedInputStream(Chat2.sClient.getInputStream()));
			System.out.println("herechat2");
			String newMessage = (String)Chat2.reader.readObject();
			if(newMessage.equals("@@@Username has already existed@@@.")) {
				System.out.println("Username has already existed@@@.");
				System.exit(0);
			}
			else if(newMessage.equals("@@@you are approved@@@")) {
				Client chat = new Client("chat");
				chat.start();
				Socket sClient = new Socket(ip,port);
				GridFrame1.sClient = sClient;
				GridFrame1.selfName = username;
				Client whiteboard = new Client("whiteboard");
				whiteboard.start();
			}
			else {
				System.out.println("you are declined");
				System.exit(0);
			}
			
		}
		catch(InvalidInputException exception){
			System.out.println("Invalid Command. Need IP address, port number and username.");
		}
		catch(NumberFormatException exception) {
			System.out.println("Invalid port number.");
		}
		catch (UnknownHostException exception)
		{
			System.out.println("Can't find the host.");
		}
		catch(Exception exception) {
			System.out.println("Failed connecting to server.");
		}
//		catch(Exception exception2) {
//			System.out.println("errors");
//		}
		
//		GridFrame1.selfName = username;
//		Client whiteboard = new Client("whiteboard");
//		whiteboard.start();
//		Client chat = new Client("chat");
//		chat.start();
	}
	
	public void run() {
		if(this.name.equals("chat")) {
			System.out.println("chat");
//			Chat chat = new Chat();
//			chat.setVisible(true);
//			ChatManager.getCM().setWindow(chat);

			Chat2 chat = new Chat2(username);
			chat.keepLis();
			
			
		}
		else if(this.name.equals("whiteboard")) {
//			GridFrame1 white = new GridFrame1();
			GridFrame1.createAndShowGUI();
			GridFrame1.keepLis();

		}		
	}

}
