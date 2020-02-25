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
	//�����
	public HashMap<String, Integer> table = new HashMap<String, Integer>();
	//ǰ׺
	//private String[] Array_char;
	private int count;//��¼�����ĳ���
	//ѹ����newFile�ļ���
	private File newFile;
	/**
	 * 
	 * ������Action����ɾ��ѹ������ļ�
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
	 * ѹ��input���ļ���output�ļ�
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public double compress(String input, String output) throws IOException {

		//��ʼ��ǰ׺��

		//Array_char = new String[4096];
		for (int i = 0; i < 256; i++) {
			//������256�������ַ���ʣ��3840�����ֵ��ַ�����
			table.put(Character.toString((char) i), i);
			//Array_char[i] = Character.toString((char) i);
		}
		count = 256;

		//�����ļ�
		DataInputStream read = new DataInputStream(new BufferedInputStream(
				new FileInputStream(input)));
		

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(output)));
		
		//���ڼ�¼�ļ����ͣ�����ѹ��ʱ����ܻظ��ļ�
		out.writeUTF(input.split("\\.")[1]);
		
		byte input_byte;
		String temp = "";
		//��buffer д��output�ļ��У�buffer�����ǰ׺������
		byte[] buffer = new byte[3];
		boolean first = true;
		
		try {

			//�����һ���ֽڣ�����Ϊ�˳�ʼ��tempǰ׺
			input_byte = read.readByte();
			int i = new Byte(input_byte).intValue();
			//���ΪbyteΪ��������256��byteһ��Ϊ����
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
                  //�����������Ѿ��������ǰ׺+c ����չǰ׺temp
				if (table.containsKey(temp + c)) {
					temp = temp + c;
				} else {
					//�����ַ���ӳ��ɶ�����ͨ��Ϊ12λ��������
					String s12 = to12bit(table.get(temp));                   
					if (first) {
						//buffer ��һ���ֽ������ֵĵ�һ�ֽ�
						buffer[0] = (byte) Integer.parseInt(
								s12.substring(0, 8), 2);
						//buffer �ڶ��ֽ������ֵĵڶ��ֽڣ�������䵽8λ
						buffer[1] = (byte) Integer.parseInt(
								s12.substring(8, 12) + "0000", 2);
					} else {
						//Ϊ���ܳ������buffer[1]�ı�������֮ǰ�������������Ч���֡�2*12=24����Ϊ3���ֽ�
						//3���ֽ��ܴ洢2��
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
					if (count < 4096) {//�����ĳ��Ȳ��ܳ���4096
						//û�����ǰ׺���ͼ�������
						table.put(temp + c, count++);
					}
					//��ǰ׺ָ��ǰ�ַ�
					temp = "" + c;
				}
			}

		} catch (EOFException e) {//����˵���������һ���ֽ��ˣ������������󣬶�������
			String temp_12 = to12bit(table.get(temp));
			if (first) {//�����3�����صĿ�ʼ
				buffer[0] = (byte) Integer.parseInt(temp_12.substring(0, 8), 2);
				buffer[1] = (byte) Integer.parseInt(temp_12.substring(8, 12)
						+ "0000", 2);
				out.writeByte(buffer[0]);
				out.writeByte(buffer[1]);
			} else {//���������������Ѿ��������֣�������ȥ
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
			//����ѹ����
			double rate= ((1.0*file.length())/(outfile.length()));
			
			this.newFile=file;
			return rate;
		}

	}
	/**
	 * 
	 * ��һ��8bit���ֽڣ�ת��Ϊ12���صĶ������ַ���
	 * @param i
	 * @return String 12bit �Ķ������ַ���
	 */
	
	public String to12bit(int i) {
		String temp = Integer.toBinaryString(i);
		while (temp.length() < 12) {
			temp = "0" + temp;
		}
		return temp;
	}
	
}
