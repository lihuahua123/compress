package com.li.compress.lzw;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.li.compress.Compress;
import com.li.compress.Decompress;

public class LZWEncode extends Compress{
	//编码表
	public HashMap<String, Integer> table = new HashMap<String, Integer>();
	//前缀
	//private String[] Array_char;
	private int count;//记录编码表的长度
	//压缩到newFile文件中
	private File newFile;
	/**
	 * 
	 * 用于在Action包中删除压缩后的文件
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	@Override
	public File getOutputFile() {
		
		return this.newFile;
	}
	
	public LZWEncode () {
	}
	/**
	 * 
	 * 压缩input的文件到output文件
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public double compress(String input, String output) throws IOException {

		//初始化前缀表

		//Array_char = new String[4096];
		for (int i = 0; i < 256; i++) {
			//建立表，256个代表单字符，剩下3840给出现的字符串。
			table.put(Character.toString((char) i), i);
			//Array_char[i] = Character.toString((char) i);
		}
		count = 256;

		//读入文件
		DataInputStream read = new DataInputStream(new BufferedInputStream(
				new FileInputStream(input)));
		

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(output)));
		
		//用于记录文件类型，当解压的时候就能回复文件
		out.writeUTF(input.split("\\.")[1]);
		
		byte input_byte;
		String temp = "";
		//将buffer 写入output文件中，buffer是最大前缀的码字
		byte[] buffer = new byte[3];
		boolean first = true;
		
		try {

			//读入第一个字节，这是为了初始化temp前缀
			input_byte = read.readByte();
			int i = new Byte(input_byte).intValue();
			//如果为byte为负数，加256则byte一定为整数
			if (i < 0) {
				i += 256;
			}
			char c = (char) i;
			temp = "" + c;

			
			while (true) {
				input_byte = read.readByte();
				i = new Byte(input_byte).intValue();

				if (i < 0) {
					i += 256;
				}
				c = (char) i;
                  //如果编码表里已经有了这个前缀+c 则扩展前缀temp
				if (table.containsKey(temp + c)) {
					temp = temp + c;
				} else {
					//输入字符串映射成定长（通常为12位）的码字
					String s12 = to12bit(table.get(temp));                   
					if (first) {
						//buffer 第一个字节是码字的第一字节
						buffer[0] = (byte) Integer.parseInt(
								s12.substring(0, 8), 2);
						//buffer 第二字节是码字的第二字节，补零填充到8位
						buffer[1] = (byte) Integer.parseInt(
								s12.substring(8, 12) + "0000", 2);
					} else {
						//为了能充分利用buffer[1]的比特数，之前补零现在填充有效码字。2*12=24正好为3个字节
						//3个字节能存储2个
						buffer[1] += (byte) Integer.parseInt(
								s12.substring(0, 4), 2);
						buffer[2] = (byte) Integer.parseInt(
								s12.substring(4, 12), 2);
						for (int b = 0; b < buffer.length; b++) {
							out.writeByte(buffer[b]);
							buffer[b] = 0;
						}
					}
					first = !first;
					if (count < 4096) {//编码表的长度不能超过4096
						//没有这个前缀，就加入编码表
						table.put(temp + c, count++);
					}
					//将前缀指向当前字符
					temp = "" + c;
				}
			}

		} catch (EOFException e) {//出错说明读到最后一个字节了，或者其他错误，读不了了
			String temp_12 = to12bit(table.get(temp));
			if (first) {//如果是3个比特的开始
				buffer[0] = (byte) Integer.parseInt(temp_12.substring(0, 8), 2);
				buffer[1] = (byte) Integer.parseInt(temp_12.substring(8, 12)
						+ "0000", 2);
				out.writeByte(buffer[0]);
				out.writeByte(buffer[1]);
			} else {//否则这三个比特已经有了数字，再填充进去
				buffer[1] += (byte) Integer
						.parseInt(temp_12.substring(0, 4), 2);
				buffer[2] = (byte) Integer
						.parseInt(temp_12.substring(4, 12), 2);
				for (int b = 0; b < buffer.length; b++) {
					out.writeByte(buffer[b]);
					buffer[b] = 0;
				}
			}
			
			read.close();
			out.close();
			File file=new File(output);
			File outfile=new File(input);
			//计算压缩率
			double rate= ((1.0*file.length())/(outfile.length()));
			
			this.newFile=file;
			return rate;
		}

	}
	/**
	 * 
	 * 将一个8bit的字节，转化为12比特的二进制字符串
	 * @param i
	 * @return String 12bit 的二进制字符串
	 */
	
	public String to12bit(int i) {
		String temp = Integer.toBinaryString(i);
		while (temp.length() < 12) {
			temp = "0" + temp;
		}
		return temp;
	}
	
}
