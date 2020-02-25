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
		// 文件选择
		JFileChooser Chooser = new JFileChooser();
		Chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		Chooser.setDialogTitle("hello~~请选择要压缩的文件~~~");
		int t = Chooser.showOpenDialog(null);// 弹出文件选择框
		if (t == Chooser.APPROVE_OPTION) {// 如果点击的是确定
			// 得到文件的绝对路径
			String path = Chooser.getSelectedFile().getAbsolutePath();
			String name = Chooser.getSelectedFile().getName();
			JFileChooser Chooser2 = new JFileChooser();
			String path2 = "";

			path2 = Chooser.getSelectedFile().getParent();
			Chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			Chooser2.setDialogTitle("请选择将文件压缩到哪个文件夹");
			int t2 = Chooser2.showOpenDialog(null);// 弹出文件选择框
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
					JOptionPane.showMessageDialog(jp, "压缩成功", "ohh ye", JOptionPane.WARNING_MESSAGE);
					// zip = new LZWEncode();
					// rate2 = zip.compressDir(f, new File(newFile) , "lzw");
				} else {
					try {
						long startTime = System.currentTimeMillis();    //获取开始时间
						code = new HuffmanTreeEncode();
						rate1 = code.compress(path, newFile + ".huf");
						long endTime = System.currentTimeMillis();    //获取结束时间
						long hufTime=(endTime - startTime);
						startTime = System.currentTimeMillis();  
						zip = new LZWEncode();
						rate2 = zip.compress(path, newFile + ".lzw");
						endTime = System.currentTimeMillis(); 
						
						String[] obj2 = { "HuffmanTree  压缩率: " + String.format("%.2f", rate1)+" 压缩时间: "+ hufTime+"ms",
								"LZW 压缩率:   " + String.format("%.2f", rate2)+ " 压缩时间: "+ (endTime - startTime)+"ms"};
						String s = (String) JOptionPane.showInputDialog(null, "请选择压缩算法:\n", "压缩算法选择",
								JOptionPane.PLAIN_MESSAGE, null, obj2, obj2[0]);
						chooseDelete(s, code, zip);
						if (s != null) {
							JPanel jp = new JPanel();
							JOptionPane.showMessageDialog(jp, "压缩成功", "ohh ye", JOptionPane.WARNING_MESSAGE);
							return;
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						JPanel jp = new JPanel();
						JOptionPane.showMessageDialog(jp, "抱歉~压缩失败", "oh no", JOptionPane.WARNING_MESSAGE);

						e1.printStackTrace();
						return;
					}
				}
			}

		}
	}

	private void doDecompress() throws IOException {
		// 显示打开的窗口
		JFileChooser Chooser = new JFileChooser();
		Chooser.setDialogTitle("请选择解压文件");
		Chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		// 参数：文件描述(显示的)和文件类型
		FileNameExtensionFilter filter = new FileNameExtensionFilter("li压缩文件", "huf", "lzw");
		Chooser.setFileFilter(filter);
		int returnVal = Chooser.showOpenDialog(null);
		if (returnVal == Chooser.APPROVE_OPTION) {
			// 得到文件的绝对路径
			String path = Chooser.getSelectedFile().getAbsolutePath();
			String name = Chooser.getSelectedFile().getName();
			File f = new File(path);
			JFileChooser Chooser2 = new JFileChooser();
			String path2 = "";
			path2 = Chooser.getSelectedFile().getParent();

			Chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			Chooser2.setDialogTitle("请选择将文件解压到哪个文件夹");
			int t2 = Chooser2.showOpenDialog(null);// 弹出文件选择框
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
					JOptionPane.showMessageDialog(jp, "解压失败", "ohh no", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					return;
				}

			}
			JPanel jp = new JPanel();
			JOptionPane.showMessageDialog(jp, "解压成功", "ohh ye", JOptionPane.WARNING_MESSAGE);
			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if ("压缩".equals(command)) {
			try {
				doCompress();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		if ("解压".equals(command)) {

			try {
				doDecompress();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if ("退出".equals(command)) {

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