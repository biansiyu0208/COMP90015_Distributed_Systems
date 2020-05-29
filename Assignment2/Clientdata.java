import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Clientdata extends JFrame implements Serializable{
	private String operation;
	private Color color;
//	private ArrayList<Integer[]> getDragged;
//	private Integer[] array;
	private int[] coordinate;
	private int stroke;
	private String text = "";
	
	public Clientdata(String operation, Color color, int[] coordinate, int stroke, String text) {
		this.operation = operation;
		this.color = color;
//		this.array = array;
		this.coordinate = coordinate;
		this.stroke = stroke;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getOperation() {
		return operation;
	}

	public Color getColor() {
		return color;
	}
	
//	public Integer[] getArray() {
//		return this.array;
//	}
//	
//	
//	public void check() {
//		for(int i=0;i<array.length;i++) {
//			System.out.print(array[i]);
//			System.out.print(" ");
//		}
//	}

//	public ArrayList<Integer[]> getGetDragged() {
//		return getDragged;
//	}
//	
//	public void checkList() {
//		for(int j=0;j<getDragged.size();j++) {
//			System.out.print(j);
//			System.out.print(":");
//			for(int k=0;k<2;k++) {
//				System.out.print(getDragged.get(j)[k]);
//				System.out.print(" ");
//			}
//		}
//	}

	public int[] getCoordinate() {
		return coordinate;
	}

	public int getStroke() {
		return stroke;
	}

	public void setStroke(int stroke) {
		this.stroke = stroke;
	}
	
	
	
	
}
