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
	//�����
	public HashMap<String, Integer> table = new HashMap<String, Integer>();
	// public TreeMap<String,Integer> table = new TreeMap<String,Integer>();
	private String[] Array_char;
	//��ĳ���
	private int count;
	private File newFile;
	public File getNewFile() {
		return newFile;
	}
	/**
	 * �������ֽ���ȡ��12bit �����ݣ�������ݾ���ѹ������������
	 * @param b1
	 * @param b2
	 * @param first 12��������2���ֽ������ߣ������ұ�
	 * @return ��ȡ����������
	 */
	public int getvalue(byte b1, byte b2, boolean first) {
		String temp1 = Integer.toBinaryString(b1);
		String temp2 = Integer.toBinaryString(b2);
		while (temp1.length() < 8) {//����һ���ֽڵ�����
			temp1 = "0" + temp1;
		}
		if (temp1.length() == 32) {
	//���temp1����Ϊ32��Ϊһ�����ͣ�˵�������Integer.toBinaryString(b1) �����˷�����չ
	//ֻ��Ҫȡ��8���ֽ�
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
	 * ����input���ļ���output�ļ�
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

		//��Ӧѹ���������ļ�����
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
			 * ��Ӧѹ��������buffer ��2������һ��first����һ����first��ȡ����buffer �ܵõ�һ������
			 */
			buffer[0] = in.readByte();
			buffer[1] = in.readByte();
            //���������֣�ͨ��getValue,priorword ָ��ǰһ���ֽڣ�currword ָ��ǰ�ֽ�
			priorword = getvalue(buffer[0], buffer[1], first);
			first = !first;
			//ÿ������ת��Ϊstring
			out.writeBytes(Array_char[priorword]);

			//����ѭ����������
			while (true) {

				if (first) {//3���ֽڵ�����ǰ�������
					buffer[0] = in.readByte();
					buffer[1] = in.readByte();
					currword = getvalue(buffer[0], buffer[1], first);
				} else {//�����ֽں��������
					buffer[2] = in.readByte();
					currword = getvalue(buffer[1], buffer[2], first);
				}
				first = !first;
				if (currword >= count) {//����������벻���ֵ���

					if (count < 4096)//��ǰ׺��ǰ׺��һ���ַ�д���ֵ���
						Array_char[count] = Array_char[priorword]
								+ Array_char[priorword].charAt(0);
					
					out.writeBytes(Array_char[priorword]
							+ Array_char[priorword].charAt(0));
					count++;
				} else {//������ֵ���
						
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
