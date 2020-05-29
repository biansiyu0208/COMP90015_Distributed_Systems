
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ItemListener implements ActionListener {

//	public GridFrame1 gf;
//
//	public ItemListener() {
//
//	}
//
//	public ItemListener(GridFrame1 gf) {
//		this.gf = gf;
//
//	}
	

	private JFrame frame;
	private JPanel panel;
	private DrawListener1 dl;

	public void actionPerformed(ActionEvent e) {
		// get commander from menu
		String command = e.getActionCommand();
		

		if ("new".equals(command)) {
			try {
				String cate = "@@@new the window@@@";
				GridFrame1.writer.writeObject(cate);
				GridFrame1.writer.flush();
			}
			catch(IOException e1) {
				System.out.println("error write in ");
				System.exit(0);
			}
//			int value = JOptionPane.showConfirmDialog(null, "save your current drawing?", "caution", 0);
//			if (value == 0) {
//				saveFile(GridFrame1.list);
//			}
//			if (value == 1) {
//				if (value == 1) {
//
////					GridFrame1.list.removeAll(GridFrame1.list);
////					GridFrame1.board.repaint();
//				}
//				
//
//			}
		} else if ("open".equals(command)) {
			try {
				String cate = "@@@open11 the window@@@";
				GridFrame1.writer.writeObject(cate);
				GridFrame1.writer.flush();
			}
			catch(IOException e1) {
				System.out.println("error write in ");
				System.exit(0);
			}

//			int value = JOptionPane.showConfirmDialog(null, "save your current drawing?", "caution", 0);
//			if (value == 0) {
//				System.out.println("save");
//				saveFile(GridFrame1.list);
//			}
//			if (value == 1) {
//
//				// clear list
//				
//			}
		} else if ("save".equals(command)) {
			try {
				String cate = "@@@save the window@@@";
				GridFrame1.writer.writeObject(cate);
				GridFrame1.writer.flush();
			}
			catch(IOException e1) {
				System.out.println("error write in ");
				System.exit(0);
			}
//			saveFile(GridFrame1.list);
		} else if("saveAs".equals(command))	
		{
//			String text = JOptionPane.showInputDialog("Please input the filename:");
//			String path = "C:\\Users\\Wayne\\Desktop\\Study\\s2\\Stream\\ASS1"+"\\"+text+".jpg";
			System.out.println("saveas1");
			saveIma(this.frame, this.panel);
		} else if("close".equals(command)) {
			try {
				String cate = "@@@close the window@@@";
				GridFrame1.writer.writeObject(cate);
				GridFrame1.writer.flush();
			}
			catch(IOException e1) {
				System.out.println("error write in ");
				System.exit(0);
			}
			

		}
		
}
	
    public void saveIma(JFrame frame, JPanel panel) {
		//choose the path and name and new the file 
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(null);		
		// gain the file object
		File file =chooser.getSelectedFile();
//		file.getAbsolutePath();
		
		
		if(file==null){
			JOptionPane.showMessageDialog(null, "Didn't choose any file, please try again.");
		}else {			
			try 
			{
		    	BufferedImage myImage = null;
		    	try {
		    		System.out.println("abc");
		    		myImage = new Robot().createScreenCapture(new Rectangle(frame.getX()+8,panel.getY()+60,panel.getWidth(),panel.getHeight()));
		    		ImageIO.write(myImage,"jpg", file);
		    	}
		    	catch(Exception e) {
		    		e.printStackTrace();
		    	}
			} catch (Exception e) {
				String message=e.getMessage();
				System.out.println(message);
			}
		}
    	
    	

    }
//    
	public void setFrame(JFrame frame, JPanel panel, DrawListener1 dl) {
		this.frame = frame;
		this.panel = panel;
		this.dl = dl;
	}
	


	public void saveFile(ArrayList<Clientdata> list) {
		
		

		// choose the path and name and new the file
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(null);
		// gain the file object
		File file = chooser.getSelectedFile();

		if (file == null) {
			JOptionPane.showMessageDialog(null, "Didn't choose any file, please try again.");
		} else {

			try {

				FileOutputStream fs = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fs);
				oos.writeObject(GridFrame1.list);
			    JOptionPane.showMessageDialog(null, "Save successfully");
				oos.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
