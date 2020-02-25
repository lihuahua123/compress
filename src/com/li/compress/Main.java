package com.li.compress;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main {

	javax.swing.JTextArea textarea = null;

	public static void main(String[] args) {
		new Main().setFrame();

	}

	public void setFrame(){
		 JFrame frame=new  JFrame("li压缩软件");
		//加载图片
			ImageIcon icon=new ImageIcon(frame.getClass().getResource("/resource/timg1.jpg"));
			//Image im=new Image(icon);
			//将图片放入label中
			JLabel label=new JLabel(icon);
			
			//设置label的大小
			label.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
			
			
			
			//获取窗口的第二层，将label放入
			frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
				
			//获取frame的顶层容器,并设置为透明
			JPanel j=(JPanel)frame.getContentPane();
			j.setOpaque(false);
	 
			
			//菜单项
			 JPanel jp=new JPanel(); 
		 
			//菜单条目
			JButton Code = new JButton("压缩");
			JButton Decode = new JButton("解压");
			JButton exit = new JButton("退出");
		
			 jp.add(Code);
			 jp.add(Decode);
			 jp.add(exit);
			
			
			
			//添加菜单事件
			Action action = new Action();
			Code.addActionListener(action);
			Decode.addActionListener(action);
			exit.addActionListener(action);
	 
			
	 
			//必须设置为透明的。否则看不到图片
			jp.setOpaque(false);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					int a = JOptionPane.showConfirmDialog(null, "真的要关了我吗？", "温馨提示",
					JOptionPane.YES_NO_OPTION);
					if (a == 0) {
					System.exit(0); //关闭
					}
					}
				});
	 
			frame.add(jp);
			
			frame.setSize(icon.getIconWidth(),icon.getIconHeight());
			frame.setVisible(true);

	
		
		
		
	
		
	}

}
