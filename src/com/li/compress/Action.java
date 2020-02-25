package com.li.compress;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.li.compress.huffman.HuffmanTreeDecode;
import com.li.compress.huffman.HuffmanTreeEncode;
import com.li.compress.lzw.LZWDecode;
import com.li.compress.lzw.LZWEncode;

public class Action implements ActionListener {
	private void doCompress() throws IOException {
		// �ļ�ѡ��
		JFileChooser Chooser = new JFileChooser();
		Chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		Chooser.setDialogTitle("hello~~��ѡ��Ҫѹ�����ļ�~~~");
		int t = Chooser.showOpenDialog(null);// �����ļ�ѡ���
		if (t == Chooser.APPROVE_OPTION) {// ����������ȷ��
			// �õ��ļ��ľ���·��
			String path = Chooser.getSelectedFile().getAbsolutePath();
			String name = Chooser.getSelectedFile().getName();
			JFileChooser Chooser2 = new JFileChooser();
			String path2 = "";

			path2 = Chooser.getSelectedFile().getParent();
			Chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			Chooser2.setDialogTitle("��ѡ���ļ�ѹ�����ĸ��ļ���");
			int t2 = Chooser2.showOpenDialog(null);// �����ļ�ѡ���
			if (t2 == Chooser.APPROVE_OPTION) {
				path2 = Chooser2.getSelectedFile().getAbsolutePath();

				Compress code = null, zip = null;
				double rate1 = 0, rate2 = 0;
				String newFile = path2 + File.separator + name.split("\\.")[0];
				File f = new File(path);
				if (f.isDirectory()) {
					code = new HuffmanTreeEncode();
					
					rate1 = code.compressDir(f, new File(newFile), "huf");
					JPanel jp = new JPanel();
					JOptionPane.showMessageDialog(jp, "ѹ���ɹ�", "ohh ye", JOptionPane.WARNING_MESSAGE);
					// zip = new LZWEncode();
					// rate2 = zip.compressDir(f, new File(newFile) , "lzw");
				} else {
					try {
						long startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
						code = new HuffmanTreeEncode();
						rate1 = code.compress(path, newFile + ".huf");
						long endTime = System.currentTimeMillis();    //��ȡ����ʱ��
						long hufTime=(endTime - startTime);
						startTime = System.currentTimeMillis();  
						zip = new LZWEncode();
						rate2 = zip.compress(path, newFile + ".lzw");
						endTime = System.currentTimeMillis(); 
						
						String[] obj2 = { "HuffmanTree  ѹ����: " + String.format("%.2f", rate1)+" ѹ��ʱ��: "+ hufTime+"ms",
								"LZW ѹ����:   " + String.format("%.2f", rate2)+ " ѹ��ʱ��: "+ (endTime - startTime)+"ms"};
						String s = (String) JOptionPane.showInputDialog(null, "��ѡ��ѹ���㷨:\n", "ѹ���㷨ѡ��",
								JOptionPane.PLAIN_MESSAGE, null, obj2, obj2[0]);
						chooseDelete(s, code, zip);
						if (s != null) {
							JPanel jp = new JPanel();
							JOptionPane.showMessageDialog(jp, "ѹ���ɹ�", "ohh ye", JOptionPane.WARNING_MESSAGE);
							return;
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						JPanel jp = new JPanel();
						JOptionPane.showMessageDialog(jp, "��Ǹ~ѹ��ʧ��", "oh no", JOptionPane.WARNING_MESSAGE);

						e1.printStackTrace();
						return;
					}
				}
			}

		}
	}

	private void doDecompress() throws IOException {
		// ��ʾ�򿪵Ĵ���
		JFileChooser Chooser = new JFileChooser();
		Chooser.setDialogTitle("��ѡ���ѹ�ļ�");
		Chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		// �������ļ�����(��ʾ��)���ļ�����
		FileNameExtensionFilter filter = new FileNameExtensionFilter("liѹ���ļ�", "huf", "lzw");
		Chooser.setFileFilter(filter);
		int returnVal = Chooser.showOpenDialog(null);
		if (returnVal == Chooser.APPROVE_OPTION) {
			// �õ��ļ��ľ���·��
			String path = Chooser.getSelectedFile().getAbsolutePath();
			String name = Chooser.getSelectedFile().getName();
			File f = new File(path);
			JFileChooser Chooser2 = new JFileChooser();
			String path2 = "";
			path2 = Chooser.getSelectedFile().getParent();

			Chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			Chooser2.setDialogTitle("��ѡ���ļ���ѹ���ĸ��ļ���");
			int t2 = Chooser2.showOpenDialog(null);// �����ļ�ѡ���
			if (t2 == Chooser.APPROVE_OPTION) {
				path2 = Chooser2.getSelectedFile().getAbsolutePath();
			Decompress code = null, zip = null;
			String newFile = path2 + File.separator + name.split("\\.")[0] + "mm";
			if (f.isDirectory()) {
				Decompress.decompressDir(f, new File(newFile));
			} else {
				try {
					if (name.split("\\.")[1].equals("huf")) {
						System.out.println(path + " ");
						code = new HuffmanTreeDecode();
						code.decompress(path, newFile);
					} else if (name.split("\\.")[1].equals("lzw")) {
						zip = new LZWDecode();
						zip.decompress(path, newFile);
					}
				} catch (Exception e1) {
					JPanel jp = new JPanel();
					JOptionPane.showMessageDialog(jp, "��ѹʧ��", "ohh no", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					return;
				}

			}
			JPanel jp = new JPanel();
			JOptionPane.showMessageDialog(jp, "��ѹ�ɹ�", "ohh ye", JOptionPane.WARNING_MESSAGE);
			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if ("ѹ��".equals(command)) {
			try {
				doCompress();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		if ("��ѹ".equals(command)) {

			try {
				doDecompress();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if ("�˳�".equals(command)) {

			System.exit(0);
		}

	}

	void deleteFile(File file) {
		if (file.exists())
			file.delete();
	}

	void chooseDelete(String s, Compress code, Compress zip) {
		if (s == null) {
			deleteFile(code.getOutputFile());
			deleteFile(zip.getOutputFile());
		} else if (s.contains("HuffmanTree")) {
			deleteFile(zip.getOutputFile());
		} else if (s.contains("LZW")) {
			deleteFile(code.getOutputFile());
		}
	}
}