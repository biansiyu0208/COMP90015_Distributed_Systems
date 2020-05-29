import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
	public static ArrayList<Socket> members = new ArrayList<Socket>();
	public static Socket[] member = new Socket[5000000];
	public static ArrayList<Server> serverMem = new ArrayList<Server>();
	public static Server[] serverMember = new Server[100];
	public static Server[] chatServer = new Server[100];
	public static ArrayList<String> usernames = new ArrayList<String>();
	
	public static ArrayList<Clientdata> data;	
	public static ArrayList<Clientdata> dataFromfile = new ArrayList<Clientdata>();
	private Socket socket;
	private String username;
	ObjectOutputStream writer;
	ObjectInputStream reader;
	private int num;
	private int chatNum;
	private String checkIn;
	static int checkSend=0;
	static int checkManager = 0;
	static int checkBoardManager = 0;
	
	public void setUsername(String name) {
		this.username = name;
	}
	public String getUsername() {
		return this.username;
	}
	public void setCheckIn(String checkin) {
		this.checkIn = checkin;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public Socket getSocket() {
		return this.socket;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	public void setChatnum(int chatNum) {
		this.chatNum = chatNum;
	}
	
	
	public void run() {
		try {
//			this.reader = new ObjectInputStream(socket.getInputStream());
//			this.writer = new ObjectOutputStream(socket.getOutputStream());
//			String abc = (String)reader.readObject();
//			System.out.println(checkIn);
			if(checkIn.equals("abc")) {
//				System.out.println("abc is okay to go");
				if(data.size()!=0) {
					for(int i=0;i<data.size();i++) {
						Clientdata existed = data.get(i);
						Server serverNew = this;
						serverNew.writer.writeObject("board");
						serverNew.writer.writeObject(existed);
//						serverNew.writer.writeObject(data);
						serverNew.writer.flush();
					}
				}
				process();
			}
			else {
				int checkLoop = 0;
				while(true) {
					if(checkLoop == 0) {
						boolean checkExist = false;
						for(int k=0;k<usernames.size();k++) {
							if(this.username.contentEquals(usernames.get(k))) {
								this.writer.writeObject("@@@Username has already existed@@@.");
								this.writer.flush();
//								System.out.println("username existed:"+chatNum);
								checkExist = true;
								chatServer[this.chatNum-1] = null;
//								serverMember[this.num-1] = null;
							}
						}
						if(this.chatNum==1 && checkExist == false) {
							this.writer.writeObject("@@@you are approved@@@");
//							System.out.println("@@@you are approved@@@");
							this.writer.flush();
							usernames.add(this.username);
							chatServer[0].writer.writeObject("@@@@@"+this.username);
						}
						
						if(this.chatNum!=1 && checkExist == false) {
							System.out.println("check others herrrrrrrrrrrr");
							Server host = chatServer[0];
							host.writer.writeObject("@@@new users apply to join@@@"+chatNum);
							host.writer.flush();
							
						}
						
						checkLoop++;	
//						System.out.println("check loop is:"+checkLoop);
//						System.out.println("chat num is:"+chatNum);
					}
					
					else {
//						System.out.println("come to phase 2");
						String chatContent = (String)reader.readObject();
						if(chatContent.startsWith("@@@add a new user@@@")) {
//							System.out.println("current chat num is:"+chatNum);
//							System.out.println(chatContent);
							String[] choose = chatContent.split("@@@");
							int index = Integer.valueOf(choose[2]);
//							System.out.println("index is:"+index);
							chatServer[index-1].writer.writeObject("@@@you are approved@@@");
							chatServer[index-1].writer.flush();
//							System.out.println(chatServer[index-1].username);
							usernames.add(chatServer[index-1].username);
							for(int i=0;i<chatServer.length;i++) {
								if(i==(index-1)) {
									for(int j=0;j<index;j++) {
										if(chatServer[j]!=null)
											chatServer[i].writer.writeObject("@@@@@"+chatServer[j].username);
									}
								}
								else if(i!=(index-1) && chatServer[i]!=null) {
									chatServer[i].writer.writeObject("@@@@@"+chatServer[index-1].username);
								}
							}
						}
						else if(chatContent.startsWith("@@@declined adding new one@@@")) {
//							System.out.println("current chat num is:"+chatNum);
//							System.out.println(chatContent);
							String[] choose = chatContent.split("@@@");
							int index = Integer.valueOf(choose[2]);
//							System.out.println("index is:"+index);
//							System.out.println("chat length is"+chatServer.length);
//							System.out.println("board length is"+serverMember.length);
							chatServer[index-1].writer.writeObject("@@@you are declined@@@");
//							System.out.println("here might 1");
							chatServer[index-1].writer.flush();
//							System.out.println("here might 2");
							chatServer[index-1] = null;
//							System.out.println("here might 3");
//							serverMember[index-1] = null;
//							usernames.add(chatServer[index-1].username);
						}
						else {
							System.out.println(chatContent);
							String[] words = chatContent.split("@@");

							if(words[0].equals("username") && (chatNum-1)==0) {
								
								System.out.println("comingcoming");
								for(int j=0;j<usernames.size();j++) {
									for(int k=1;k<words.length;k++) {
										if(usernames.get(j).equals(words[k]))
											usernames.remove(j);
									}							
								}
								String leftNames = "username";
								for(int j=0;j<usernames.size();j++) {
									leftNames += "@@"+usernames.get(j);
								}
								leftNames += "@@"+usernames.size();
								for(int i=0;i<chatServer.length;i++) {							
									if(chatServer[i]!=null) {
										Server serverNew = chatServer[i];
										serverNew.writer.writeObject(leftNames);
										serverNew.writer.flush();
										
									}				
								}
								for(int i=0;i<chatServer.length;i++) {
									if(chatServer[i]!=null) {
										for(int j=1;j<words.length;j++) {
											if(chatServer[i].getUsername().equals(words[j])) {
												chatServer[i].getSocket().close();
												chatServer[i] = null;
											}
										}
										
									}
								}
							}
							else if(words[0].equals("username")==false){
								
								
								for(int i=0;i<chatServer.length;i++) {
									if(chatServer[i]!=null && chatServer[i]!=this) {
										Server serverNew = chatServer[i];
										chatContent += "%%%"+this.username;
										serverNew.writer.writeObject(chatContent);
										serverNew.writer.flush();
									}				
								}
							}
						}
										
					}
					}
					
			}
		}
		catch(Exception e) {
			chatServer[this.chatNum-1] = null;
//			usernames.remove(this.chatNum-1);
//			System.out.println("error in server run:"+chatNum);
			int a = this.chatNum-1;
			
			if(a==0) {
				for(int i=chatServer.length-1;i>=0;i--) {
					if(chatServer[i]!=null) {
						System.out.println("come to clear hererererere.");
						try {
							chatServer[i].socket.close();
//							serverMember[i].socket.close();
							chatServer[i]=null;
							serverMember[i] = null;
						}
						catch(Exception e2) {
							System.out.println(i+" is closed.");
						}
					}
				}
				usernames.clear();
				checkManager = 0;
				checkBoardManager=0;
				System.out.println("checkManager:"+checkManager);
				System.out.println("checkBoardManager:"+checkBoardManager);
			}
			
			System.out.println("error in server run:"+a);
		}
		
	}
	
	public void setTools(ObjectOutputStream writer,ObjectInputStream reader) {
		this.writer = writer;
		this.reader = reader;
	}
	
	public ObjectInputStream getReader() {
		return this.reader;
	}
	
	
	public static void main(String[] args) throws Exception{
		data = new ArrayList<Clientdata>();
		int port = 0;
		try {
			if(args.length!=1)
				throw new InvalidInputException();
			else {
				port = Integer.parseInt(args[0]);
				}
			
			System.out.println("Start running");
			int number = 0;
			int chatNum = 0;
//			new ServerListening().start();
			ServerSocket serverSocket = new ServerSocket(port);
			while(true) {
				Socket socket = serverSocket.accept();			
				Server server = new Server();
				ObjectInputStream reader2 = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream writer2 = new ObjectOutputStream(socket.getOutputStream());
				server.setTools(writer2, reader2);
				server.setSocket(socket);			
				String check2 = (String)server.getReader().readObject();
				server.setCheckIn(check2);
				if(check2.equals("abc")) {
					
//					System.out.println("Number "+number+" whiteboardSocket is running");
//					serverMember[number] = server;
					number++;
//					server.setChatnum(chatNum);	
					if(checkBoardManager==0) {
						
						serverMember[0] = server;
						serverMember[0].setNum(checkBoardManager+1);
						System.out.println("whiteboard Number0 is: "+number);
					}
					else {
						server.setNum(number);
						serverMember[number-1] = server;
						System.out.println("whiteboard Number1 is: "+number);
					}
					checkBoardManager++;
					new Thread(server).start();
				}
				else {
//					usernames.add(check2);
					server.setUsername(check2);
//					System.out.println("Number "+chatNum+" chatSocket is running");
//					chatServer[chatNum] = server;
					chatNum++;
					if(checkManager==0) {
						
						chatServer[0] = server;
						chatServer[0].setChatnum(checkManager+1);
						System.out.println();
						System.out.println(chatServer[chatNum-1]==null);
						System.out.println("chatboard Number0 is: "+chatNum);
					}
					else {
						server.setChatnum(chatNum);	
						chatServer[chatNum-1] = server;
						
						System.out.println("chatboard Number1 is: "+chatNum);
					}
					checkManager++;
							
//					server.setNum(number);
					new Thread(server).start();
				}
				

			}
		}
		catch(InvalidInputException exception){
			System.out.println("Invalid Command. Need a port number");
		}
		catch(NumberFormatException exception) { 
			System.out.println("Invalid port number. Try again.");
			System.exit(0);
		}
		catch(Exception e) {
			System.out.println("Failed to establish the server. Try again.");
			System.exit(0);
		}
		
			
	}
	
	public void process() {
		try {
			String cate = (String) reader.readObject();
			System.out.println(cate);
			while(true) {
				if(cate.equals("board")) {
					Clientdata data0 = (Clientdata)reader.readObject();
					data.add(data0);
					System.out.println("current client number:"+this.num);
					for(int i=0;i<serverMember.length;i++) {
						if(serverMember[i]!=null) {
							Server serverNew = serverMember[i];
							serverNew.writer.writeObject(cate);
							serverNew.writer.writeObject(data0);
//							serverNew.writer.writeObject(data);
							serverNew.writer.flush();
						}				
					}
					cate = (String) reader.readObject();
					System.out.println("cate down here1111 is:"+cate);
					
					if(cate.equals("@@@close the window@@@")||cate.equals("@@@new the window@@@")||cate.equals("@@@save the window@@@")
							||cate.equals("@@@open11 the window@@@")) {
						if(this.num==1) {
							if(cate.equals("@@@save the window@@@")) {
								this.writer.writeObject("@@@save the window@@@");
								this.writer.flush();
							}
							else if(cate.equals("@@@open11 the window@@@")) {
								this.writer.writeObject("@@@open11 the window@@@");
								this.writer.flush();
							}
//							data.clear();
							for(int i=0;i<serverMember.length;i++) {
								if(serverMember[i]!=null) {
									Server serverNew = serverMember[i];
									if(cate.equals("@@@close the window@@@")) {
										data.clear();
										serverNew.writer.writeObject("@@@close the window@@@"+i);
										serverNew.writer.flush();
									}
									else if(cate.equals("@@@new the window@@@")) {
										data.clear();
										serverNew.writer.writeObject("@@@new the window@@@");
										serverNew.writer.flush();
									}
									
									
								}				
							}
							cate = (String) reader.readObject();
							System.out.println("cate down here is:"+cate);
						}
						else {
							cate = (String) reader.readObject();
							System.out.println("cate down here is:"+cate);
						}
					}
					
					else if(cate.equals("@@@client1 transfer data@@@")) {
						Clientdata dataClient1 = (Clientdata)reader.readObject();
						dataFromfile.add(dataClient1);
						cate = (String)reader.readObject();
					}
					else if(cate.equals("@@@clear original data@@@")) {
						System.out.println("check here @@@clear original data@@@");
						for(int i=0;i<serverMember.length;i++) {
							if(serverMember[i]!=null) {
								Server serverNew = serverMember[i];
								serverNew.writer.writeObject("@@@clear original data@@@");
								serverNew.writer.flush();
							}				
						}
						cate = (String)reader.readObject();
					}
					else if(cate.equals("@@@send data@@@")) {
						int index = 0;
						for(int i=0;i<serverMember.length;i++) {
							if(serverMember[i]!=null)
								index++;
						}
						
						for(int j=0;j<dataFromfile.size();j++) {
							Server serverNew = this;
							serverNew.writer.writeObject(cate);
							serverNew.writer.writeObject(dataFromfile.get(j));
							serverNew.writer.flush();
						}
						
						checkSend++;
						if(checkSend==index) {
							dataFromfile.clear();
							checkSend=0;
						}
						cate = (String)reader.readObject();
					}
					
					else if(cate.equals("board")==false) {
						String[] titleName = cate.split("@@");
						String drawingUser = titleName[2];
						System.out.println(drawingUser);
						for(int i=0;i<serverMember.length;i++) {
							if(serverMember[i]!=null) {
								Server serverNew = serverMember[i];
								serverNew.writer.writeObject(drawingUser);
								serverNew.writer.flush();
							}				
						}
						cate = (String)reader.readObject();
						System.out.println("cate down here222 is:"+cate);
					}
//					data0 = (Clientdata)reader.readObject();
//					System.out.println("get data0");
				}
				else if(cate.equals("@@@client1 transfer data@@@")) {
					Clientdata dataClient1 = (Clientdata)reader.readObject();
					dataFromfile.add(dataClient1);
					cate = (String)reader.readObject();
				}
				else if(cate.equals("@@@clear original data@@@")) {
					System.out.println("check here @@@clear original data@@@");
					for(int i=0;i<serverMember.length;i++) {
						if(serverMember[i]!=null ) {
							Server serverNew = serverMember[i];
							serverNew.writer.writeObject("@@@clear original data@@@");
							serverNew.writer.flush();
						}				
					}
					cate = (String)reader.readObject();
				}
				else if(cate.equals("@@@send data@@@")) {
					int index = 0;
					for(int i=0;i<serverMember.length;i++) {
						if(serverMember[i]!=null)
							index++;
					}
					
					for(int j=0;j<dataFromfile.size();j++) {
						Server serverNew = this;
						serverNew.writer.writeObject(cate);
						serverNew.writer.writeObject(dataFromfile.get(j));
						serverNew.writer.flush();
					}
					checkSend++;
					if(checkSend==index) {
						dataFromfile.clear();
						checkSend=0;
					}
						
					cate = (String)reader.readObject();
				}
				else if(cate.equals("@@@close the window@@@")||cate.equals("@@@new the window@@@")||cate.equals("@@@save the window@@@")
						||cate.equals("@@@open11 the window@@@")) {
					if(this.num==1) {
						if(cate.equals("@@@save the window@@@")) {
							this.writer.writeObject("@@@save the window@@@");
							this.writer.flush();
						}
						else if(cate.equals("@@@open11 the window@@@")) {
							this.writer.writeObject("@@@open11 the window@@@");
							this.writer.flush();
						}
//						data.clear();
						for(int i=0;i<serverMember.length;i++) {
							if(serverMember[i]!=null) {
								Server serverNew = serverMember[i];
								if(cate.equals("@@@close the window@@@")) {
									data.clear();
									serverNew.writer.writeObject("@@@close the window@@@"+i);
									serverNew.writer.flush();
								}
								else if(cate.equals("@@@new the window@@@")) {
									data.clear();
									serverNew.writer.writeObject("@@@new the window@@@");
									serverNew.writer.flush();
								}
								
							}				
						}
						cate = (String) reader.readObject();
						System.out.println("cate down here is:"+cate);
					}
					else {
						cate = (String) reader.readObject();
						System.out.println("cate down here is:"+cate);
					}
				}
//				else if(cate.equals("@@@new the window@@@")) {
//					if(this.num==1) {
//						data.clear();
//						for(int i=0;i<serverMember.length;i++) {
//							if(serverMember[i]!=null) {
//								Server serverNew = serverMember[i];
//								serverNew.writer.writeObject("@@@new the window@@@");
//								serverNew.writer.flush();
//							}				
//						}
//						cate = (String) reader.readObject();
//						System.out.println("cate down here is:"+cate);
//					}
//					else {
//						cate = (String) reader.readObject();
//						System.out.println("cate down here is:"+cate);
//					}
//				}
				
				else {
					String[] titleName = cate.split("@@");
					String drawingUser = titleName[2];
					System.out.println(drawingUser);
					for(int i=0;i<serverMember.length;i++) {
						if(serverMember[i]!=null) {
							Server serverNew = serverMember[i];
							serverNew.writer.writeObject(drawingUser);
							serverNew.writer.flush();
						}				
					}
					cate = (String) reader.readObject();
					System.out.println("cate down here is:"+cate);
				}
				
			}
			
		}
		catch(FileNotFoundException e) {
			System.out.println("Cannot find the correspond dictionary throw current path.");
			System.out.println("Try a new path");
			System.exit(0);
		}
		catch(Exception e) {
			System.out.println("Number "+(this.num-1)+" clientSocket is disconnected");
//			serverMem.get(this.num).writer.close();
//			serverMem.remove(this.num);
			serverMember[this.num-1]=null;
		}
	}
	
	
	
}
