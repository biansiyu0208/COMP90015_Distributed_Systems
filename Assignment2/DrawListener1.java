import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawListener1 implements ActionListener, MouseListener, MouseMotionListener {
	
	private JFrame frame = null;
	private JPanel panel = null;
	private String shape = "Line";
	private Color color = Color.BLACK;
	private Graphics2D g;
	private int x1, y1, x2, y2;
	private int stroke = 1;
	private String text = "";
//	private int[] coordinate = new int[4]; 
	
//	public Shape[] arrayShape;
//	private int index = 0; // the number of shapes
	
	
//	private ArrayList<Redraw> arrayShapes = new ArrayList<Redraw>();
//	public ArrayList<Clientdata> list;
	private Socket sClient;
	private ObjectOutputStream writer = null;
	private ObjectInputStream reader = null;
	private Clientdata incoming;
	
	public Graphics2D getGraphic() {
		return g;
	}
	
	
//	public ArrayList<Clientdata> getList() {
//		return list;
//	}
//
//
//
//	public void setList(ArrayList<Clientdata> list) {
//		this.list = list;
//	}



	public void setting(Socket sClient,ObjectOutputStream writer,ObjectInputStream reader) {
		this.sClient = sClient;
		this.writer = writer;
		this.reader = reader;
	}
	
	
	
	public void setG(Graphics g) {
		this.g = (Graphics2D)g;
	}
	
//	public void setShape(ArrayList<Redraw> arrayShapes) {
//		this.arrayShapes = arrayShapes;
//	}
//	
//	public ArrayList<Redraw> getShape() {
//		return arrayShapes;
//	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("+")) {
			stroke += 5;
		} else if (e.getActionCommand().equals("-")) {
			if (stroke < 1) {
				stroke = 1;
			} else {
				stroke -= 5;
			}
		} else if (!e.getActionCommand().equals("")) {
			JButton button = (JButton) e.getSource();
			shape = button.getActionCommand();
//			System.out.println("shape = " + shape);
		} else {
			JButton button = (JButton) e.getSource();
			color = button.getBackground();
//			System.out.println("color = " + color);
		}
		
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
//		GridFrame1.jFrame.setTitle(GridFrame1.selfName+" is painting...");
		x1 = e.getX();
		y1 = e.getY();
//		JComponent df = null;
//		g=(Graphics2D) df.getGraphics();
		g.setColor(color);
		try {
			String cate = "drawing@@drawing@@"+GridFrame1.selfName;
			writer.writeObject(cate);
			writer.flush();
		}
		catch(IOException e1) {
			System.out.println("error write in ");
			System.exit(0);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		GridFrame1.jFrame.setTitle("whiteboard");
		x2 = e.getX();
		y2 = e.getY();
		
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
		} 
		else if (shape.equals("Circle")) {
			   int width = Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2));
			   int height = width;
			   g.setStroke(new BasicStroke(stroke));
			   g.drawOval(Math.min(x1,x2), Math.min(y1,y2), width, height);
		} else if (shape.equals("Text")) {
//			DrawArea drawarea = new DrawArea(this);
			System.out.println("here we are texting.");
			this.text = JOptionPane.showInputDialog("Please input the text:");
//			drawarea.createNewitem();//
//			drawarea.repaint(); 
			g.setFont(new Font("Times", Font.BOLD, stroke));
			if (text != null) {
				g.drawString(text, x2, y2);
			}
//			Font font = new F
		} 
		
		System.out.println("released");
		int[] coordinate = getCoordinate();
		Clientdata client = new Clientdata(this.shape,this.color,coordinate, this.stroke, this.text);
		GridFrame1.list.add(client);
		System.out.print("check sending:");
		
		try {
			String cate = "board";
			writer.writeObject(cate);
			writer.writeObject(client);
			
			writer.flush();
			System.out.println("already sent data0");
		}
		catch(IOException e1) {
			System.out.println("error write in ");
			System.exit(0);
		}
	}
	
	
	public void setFrame(JFrame frame, JPanel panel) {
		this.frame = frame;
		this.panel = panel;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	

	@Override
	public void mouseDragged(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
		g.setColor(color);
		if (shape.equals("Free draw")) {
			g.setStroke(new BasicStroke(stroke));
			g.drawLine(x1, y1, x2, y2);
		} else if (shape.equals("Eraser")) {
			g.setStroke(new BasicStroke(stroke));
			g.setColor(Color.white);
			g.drawLine(x1, y1, x2, y2);
		}

		if(shape.equals("Free draw")||shape.equals("Eraser")) {
			int[] coordinate = getCoordinate();
			Clientdata client = new Clientdata(this.shape,this.color,coordinate, this.stroke, this.text);
			GridFrame1.list.add(client);
			x1 = x2;
			y1 = y2;
			try {
				String cate = "board";
				writer.writeObject(cate);
				writer.writeObject(client);
				writer.flush();
			}
			catch(IOException e1) {
				System.out.println("error write in ");
				System.exit(0);
			}
		}
		

	}
	
	public int[] getCoordinate() {
		int[] coordinate = {x1,y1,x2,y2};
		return coordinate;
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
