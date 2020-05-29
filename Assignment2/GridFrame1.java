import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class GridFrame1 extends JFrame{

	final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    public static Graphics2D g;
    public static JFrame jFrame;
    static Socket sClient;
	static ObjectOutputStream writer = null;
	static ObjectInputStream reader = null;
//    private Shape[] shapeList = new Shape[1024000];
//    MyList<shapeList>;
	public static ItemListener il;
    public static ArrayList<Clientdata> list=new ArrayList<Clientdata>();
    static DrawListener1 dl = new DrawListener1();
    static String selfName;
    static JPanel board = new JPanel() {
    	public void paint(Graphics g1) {
    		g = (Graphics2D)g1;
    		
    		
    		super.paint(g);
    		for(int i=0;i<list.size();i++) {
    			String shape = list.get(i).getOperation();
    			Color color = list.get(i).getColor();
    			int[] coordinate = list.get(i).getCoordinate();
    			int x1 = coordinate[0];
    			int y1 = coordinate[1];
    			int x2 = coordinate[2];
    			int y2 = coordinate[3];
    			int stroke = list.get(i).getStroke();
    			String text = list.get(i).getText();
    			
    			g.setColor(color);
    			
    			if (shape.equals("Line")) {
    				g.setStroke(new BasicStroke(stroke));
    				g.drawLine(x1, y1, x2, y2);
    			} else if (shape.equals("Oval")) { 
    				g.setStroke(new BasicStroke(stroke));
    				g.drawOval(Math.min(x1,x2), Math.min(y1,y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    			} else if (shape.equals("FillOval")) {
    				g.setColor(color);
    				g.fillOval(Math.min(x1,x2), Math.min(y1,y2), Math.abs(x1 - x2), Math.abs(y1 - y2));			
    			} else if (shape.equals("Rect")) { 
    				g.setStroke(new BasicStroke(stroke));
    				g.drawRect(Math.min(x1,x2), Math.min(y1,y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    			} else if (shape.equals("FillRect")) {
    				g.setColor(color);
    				g.fillRect(Math.min(x1,x2), Math.min(y1,y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    			} else if (shape.equals("Circle")) {
    				   int width = Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2));
    				   int height = width;
    				   g.setStroke(new BasicStroke(stroke));
    				   g.drawOval(Math.min(x1,x2), Math.min(y1,y2), width, height);
    			} else if (shape.equals("Free draw")) {
    				g.setStroke(new BasicStroke(stroke));
    				g.drawLine(x1, y1, x2, y2);
    			} else if (shape.equals("Eraser")) {
    				g.setStroke(new BasicStroke(stroke));
    				g.setColor(Color.white);
    				g.drawLine(x1, y1, x2, y2);
    			} else if (shape.equals("Text")) {
    				g.setFont(new Font("Times", Font.BOLD, stroke));
    				if (text != null) {
    					g.drawString(text, x2, y2);
    				}
//    				Font font = new F
    			}
    		}
    		
    	}
    };

   
    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
	        
	    pane.setLayout(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    
	    if (shouldFill) {
		    //natural height, maximum width
		    gbc.fill = GridBagConstraints.HORIZONTAL;
		}
//		if (shouldWeightX) {
//		    gbc.weightx = 0.5;
//		}
	    
	    String[] shape = {"Line", "Oval", "Circle", "FillOval", "Rect", "FillRect", "Free draw", "Eraser", "Text"};
		for (int i = 0; i < shape.length; i++) {
			JButton button = new JButton(shape[i]);
//			gbc.fill= GridBagConstraints.HORIZONTAL;
			gbc.gridx = i;
			gbc.gridy = 0;
//			gbc.gridwidth = 1;
			gbc.weightx = 0.5;
			button.addActionListener(dl);
			pane.add(button, gbc);
			
		}
		
		String[] set = {"+", "-"};
		for (int j = 0; j < set.length; j++) {
			JButton button = new JButton(set[j]);
			gbc.fill= GridBagConstraints.HORIZONTAL;
			gbc.gridx = j + shape.length;
			gbc.gridy = 0;
//			gbc.gridwidth = 1;
//			gbc.weightx = 0.5;
			button.addActionListener(dl);
//			button.setPreferredSize(new Dimension(70, 30));
			pane.add(button, gbc);
		}
		
		Color[] color = { Color.BLACK, Color.GRAY, Color.WHITE, Color.RED, new Color(199,73,4), new Color(189,3,14), Color.PINK, Color.MAGENTA, new Color(89,3,184),
						  new Color(9,33,164), Color.BLUE, new Color(9,83,94), new Color(89,178,147), Color.GREEN, new Color(189,233,14),new Color(89,73,14), Color.YELLOW, Color.ORANGE};
		for (int k = 0; k < color.length; k++) {
			JButton button = new JButton();
//			gbc.ipadx = 5;
//			gbc.gridwidth = 1;
//			gbc.gridx = k + shape.length + set.length;
//			gbc.gridy = 0;
			if (k < 9) {
				gbc.gridx = k;
				gbc.gridy = 1;
			} else {
				gbc.gridx = k - 9;
				gbc.gridy = 2;
			}
//			gbc.fill= GridBagConstraints.CENTER;
//			gbc.weightx = 1;
			button.addActionListener(dl);
			button.setBackground(color[k]);
			button.setOpaque(true); //foreground
			button.setBorderPainted(false); 
//			button.setPreferredSize(new Dimension(30, 30));
			pane.add(button, gbc);
		}
	    
		board.setBackground(Color.white);
		board.setPreferredSize(new Dimension(500, 500));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = shape.length + set.length + color.length;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weighty = 0.5;
		board.addMouseListener(dl);
		board.addMouseMotionListener(dl);
		pane.add(board, gbc);
			
		
    }
	
	public static void createAndShowGUI() {
		
        //Create and set up the window.
        jFrame = new JFrame("WhiteBoard");
        jFrame.setBackground(Color.GRAY);
//        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        addComponentsToPane(jFrame.getContentPane());
//        addMenu(jFrame);
 
        //Display the window.
        jFrame.pack();
        jFrame.setVisible(true);
//      add menu select        
        JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem item = new JMenuItem("new");
		JMenuItem item1 = new JMenuItem("open");
		JMenuItem item2 = new JMenuItem("save");
		JMenuItem item3 = new JMenuItem("saveAs");
		JMenuItem item4 = new JMenuItem("close");
		
		jFrame.setJMenuBar(bar);
		bar.add(menu);
		menu.add(item);
		menu.add(item1);
		menu.add(item2);
		menu.add(item3);
		menu.add(item4);
		
//		GridFrame1 gf=new GridFrame1();
//		ItemListener il = new ItemListener(gf);
		il = new ItemListener();
		item.addActionListener(il);
		item1.addActionListener(il);
		item2.addActionListener(il);
		item3.addActionListener(il);
		item4.addActionListener(il);
		il.setFrame(jFrame, board,dl);
		
		
        

		try {
//			String portNum = "3000";
//			String hostname = "localhost";
//			int port = Integer.parseInt(portNum);
//			sClient = new Socket(hostname,port);
//			reader = new ObjectInputStream(new BufferedInputStream(sClient.getInputStream()));

			writer = new ObjectOutputStream(sClient.getOutputStream());
			String abc = "abc";
			writer.writeObject(abc);
			writer.flush();
  			reader = new ObjectInputStream(new BufferedInputStream(sClient.getInputStream()));

			dl.setting(sClient, writer, reader);

			jFrame.setVisible(true);	
			dl.setG(board.getGraphics());

		}
		catch(Exception exception2) {
			System.out.println("errors");
		}
        
        dl.setG(board.getGraphics());
        

		
//		dl.setG(g);
//        dl.setList(list);
    }
		
	public static void keepLis() {
		while(true) {
			try {
				String cate = (String) reader.readObject();
				System.out.println("client cate is:"+cate);
				if(cate.equals("board")) {
					GridFrame1.jFrame.setTitle("Whiteboard.");
					Clientdata newOne = (Clientdata)reader.readObject();
					GridFrame1.list.add(newOne);
					GridFrame1.board.repaint();	
				}
				else if(cate.equals("@@@save the window@@@")) {
					il.saveFile(list);
				}
				else if(cate.equals("@@@open11 the window@@@")) {
					
//					GridFrame1.board.addMouseListener(dl);
//					GridFrame1.board.addMouseMotionListener(dl);
//					GridFrame1.board.setBackground(Color.white);
//					System.out.println("open0");
//					GridFrame1.list.removeAll(GridFrame1.list);
//					System.out.println("open1");
//					GridFrame1.board.repaint();
//					System.out.println("open2");
					try {
						JFileChooser chooser = new JFileChooser();
						chooser.showOpenDialog(null);
						File file = chooser.getSelectedFile();
						FileInputStream fis = new FileInputStream(file);
						ObjectInputStream ois = new ObjectInputStream(fis);
						GridFrame1.list = (ArrayList<Clientdata>)ois.readObject();
//						GridFrame1.board.repaint();
						//gain the data
						for (int i = 0; i <GridFrame1.list.size(); i++) 
						{
							Clientdata data=(Clientdata)GridFrame1.list.get(i);
							try {
								cate = "@@@client1 transfer data@@@";
								writer.writeObject(cate);
								writer.writeObject(data);							
								writer.flush();
								System.out.println("already sent data0");
							}
							catch(IOException e1) {
								System.out.println("error write in ");
								System.exit(0);
							}	
						}
						System.out.println("check clear data here000:"+cate);
						cate = "@@@clear original data@@@";
						writer.writeObject(cate);
						System.out.println("check clear data here:"+cate);
						ois.close();
					}catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else if(cate.startsWith("@@@close the window@@@")) {
					GridFrame1.list.removeAll(GridFrame1.list);
					GridFrame1.board.repaint();
					GridFrame1.board.removeMouseListener(dl);
					GridFrame1.board.removeMouseMotionListener(dl);
					GridFrame1.board.setBackground(new Color(238,238,238));
					
					
					
				}
				else if(cate.equals("@@@new the window@@@")||cate.equals("@@@clear original data@@@")) {
					Color backColor = new Color(238,238,238);
					if(GridFrame1.board.getBackground().equals(backColor)) {
						GridFrame1.board.addMouseListener(dl);
						GridFrame1.board.addMouseMotionListener(dl);
						GridFrame1.board.setBackground(Color.white);
					}

					GridFrame1.list.clear();
					GridFrame1.board.repaint();
					if(cate.equals("@@@clear original data@@@")) {
						GridFrame1.list.clear();
						writer.writeObject("@@@send data@@@");
					}
				}
				else if(cate.equals("@@@send data@@@")) {
					Clientdata newOne = (Clientdata)reader.readObject();
					GridFrame1.list.add(newOne);
					GridFrame1.board.repaint();	
				}
				else {
					String drawName = cate;
					GridFrame1.jFrame.setTitle(drawName+" is painting...");
				}
				  						
	  		}
			catch(ClassNotFoundException e2) {
					System.out.println("error3 ");
					System.exit(0);
			}
			catch(IOException e3) {
					System.out.println("error4 ");
					System.exit(0);
			}
		}
	}

//	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
//		createAndShowGUI();
//		if(reader==null) {
//			System.out.println("nlllll");
//		}
//		keepLis();
		
		
		
		
//		while(true) {
//			try {
//				System.out.println("hereListening111");
//				Clientdata newOne = (Clientdata)reader.readObject();
//				System.out.println("");
//				for(int j=0;j<4;j++) {
//					System.out.print(newOne.getCoordinate()[j]);
//					System.out.print(" ");
//				}
//				System.out.println();
//				System.out.println("server "+newOne.getColor());
//				System.out.println("server "+newOne.getOperation());
//				System.out.println("hereListening2222");
//				GridFrame1.list.add(newOne);
//				GridFrame1.board.repaint();
//	  						
//	  		}
//			catch(ClassNotFoundException e2) {
//					System.out.println("error3 ");
//					System.exit(0);
//			}
//			catch(IOException e3) {
//					System.out.println("error4 ");
//					System.exit(0);
//			}
//		}
		
		
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGUI();
//
//                
//            }
//        });
		
//	}
	
	
	
	

}
