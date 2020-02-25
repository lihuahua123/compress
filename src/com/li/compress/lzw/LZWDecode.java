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

import com.li.compress.Decompress;

public class LZWDecode extends Decompress{
	//编码表
	public HashMap<String, Integer> table = new HashMap<String, Integer>();
	// public TreeMap<String,Integer> table = new TreeMap<String,Integer>();
	private String[] Array_char;
	//表的长度
	private int count;
	private File newFile;
	public File getNewFile() {
		return newFile;
	}
	/**
	 * 从两个字节中取出12bit 的数据，这个数据就是压缩进来的码字
	 * @param b1
	 * @param b2
	 * @param first 12比特是在2个字节里的左边，还是右边
	 * @return 抽取出来的码字
	 */
	public int getvalue(byte b1, byte b2, boolean first) {
		String temp1 = Integer.toBinaryString(b1);
		String temp2 = Integer.toBinaryString(b2);
		while (temp1.length() < 8) {//不够一个字节的则补零
			temp1 = "0" + temp1;
		}
		if (temp1.length() == 32) {
	//如果temp1长度为32则为一个整型，说明上面的Integer.toBinaryString(b1) 进行了符号扩展
	//只需要取后8个字节
			temp1 = temp1.substring(24, 32);
		}
		while (temp2.length() < 8) {
			temp2 = "0" + temp2;
		}
		if (temp2.length() == 32) {
			temp2 = temp2.substring(24, 32);
		}

		
		if (first) {
			return Integer.parseInt(temp1 + temp2.substring(0, 4), 2);
		} else {
			return Integer.parseInt(temp1.substring(4, 8) + temp2, 2);
		}

	}

	/**
	 * 
	 * 解缩input的文件到output文件
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public void decompress(String input, String output) throws IOException {
	

		Array_char = new String[4096];
		for (int i = 0; i < 256; i++) {
			table.put(Character.toString((char) i), i);
			Array_char[i] = Character.toString((char) i);
		}
		count = 256;

		//对应压缩，读出文件类型
		DataInputStream in = new DataInputStream(new BufferedInputStream(
				new FileInputStream(input)));
		String type=in.readUTF();
		output+="."+type;
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(output)));
        
		int currword=0, priorword;
		byte[] buffer = new byte[3];
		boolean first = true;
		try {

			/**
			 * 对应压缩，三个buffer 存2个码字一个first，另一个！first，取两个buffer 能得到一个码字
			 */
			buffer[0] = in.readByte();
			buffer[1] = in.readByte();
            //解析出数字，通过getValue,priorword 指向前一个字节，currword 指向当前字节
			priorword = getvalue(buffer[0], buffer[1], first);
			first = !first;
			//每个数字转化为string
			out.writeBytes(Array_char[priorword]);

			//下面循环读入三个
			while (true) {

				if (first) {//3个字节的其中前面的码字
					buffer[0] = in.readByte();
					buffer[1] = in.readByte();
					currword = getvalue(buffer[0], buffer[1], first);
				} else {//三个字节后面的码字
					buffer[2] = in.readByte();
					currword = getvalue(buffer[1], buffer[2], first);
				}
				first = !first;
				if (currword >= count) {//如果读到的码不在字典里

					if (count < 4096)//将前缀和前缀第一个字符写入字典里
						Array_char[count] = Array_char[priorword]
								+ Array_char[priorword].charAt(0);
					
					out.writeBytes(Array_char[priorword]
							+ Array_char[priorword].charAt(0));
					count++;
				} else {//如果在字典里
						
					if (count < 4096)
						Array_char[count] = Array_char[priorword]
								+ Array_char[currword].charAt(0);
					count++;
					out.writeBytes(Array_char[currword]);
				}
				priorword = currword;
			}

		} catch (EOFException e) {
			in.close();
			out.close();
		}

	}

}
