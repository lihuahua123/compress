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
		 JFrame frame=new  JFrame("liѹ�����");
		//����ͼƬ
			ImageIcon icon=new ImageIcon(frame.getClass().getResource("/resource/timg1.jpg"));
			//Image im=new Image(icon);
			//��ͼƬ����label��
			JLabel label=new JLabel(icon);
			
			//����label�Ĵ�С
			label.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
			
			
			
			//��ȡ���ڵĵڶ��㣬��label����
			frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
				
			//��ȡframe�Ķ�������,������Ϊ͸��
			JPanel j=(JPanel)frame.getContentPane();
			j.setOpaque(false);
	 
			
			//�˵���
			 JPanel jp=new JPanel(); 
		 
			//�˵���Ŀ
			JButton Code = new JButton("ѹ��");
			JButton Decode = new JButton("��ѹ");
			JButton exit = new JButton("�˳�");
		
			 jp.add(Code);
			 jp.add(Decode);
			 jp.add(exit);
			
			
			
			//��Ӳ˵��¼�
			Action action = new Action();
			Code.addActionListener(action);
			Decode.addActionListener(action);
			exit.addActionListener(action);
	 
			
	 
			//��������Ϊ͸���ġ����򿴲���ͼƬ
			jp.setOpaque(false);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					int a = JOptionPane.showConfirmDialog(null, "���Ҫ��������", "��ܰ��ʾ",
					JOptionPane.YES_NO_OPTION);
					if (a == 0) {
					System.exit(0); //�ر�
					}
					}
				});
	 
			frame.add(jp);
			
			frame.setSize(icon.getIconWidth(),icon.getIconHeight());
			frame.setVisible(true);

	
		
		
		
	
		
	}

}
