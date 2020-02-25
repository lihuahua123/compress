package com.li.compress.huffman;
import java.util.*;

import com.li.compress.Compress;

import java.io.*;
import java.nio.file.Files;


public class HuffmanTreeEncode extends Compress
{
	/**
	 * 
	 * @BYTE_LENGTH: һ���ֽ�8λ
	 */
    final private static int BYTE_LENGTH = 8;

    
    private File outputFile;
    /**
     * ѹ��
     * @param input
     * @param output
     * @return  ѹ����
     * @throws IOException 
     */
    public  double compress(String input, String output) throws IOException
    {
        
        File read = new File(input);
        File write = new File(output);
        outputFile=write;
        double rate=0;
       
        char[] data = read(read).toCharArray();
        HuffmanTree h = new HuffmanTree(getFreqMap(data));
        StringBuilder sb = new StringBuilder();
        //д��������
        sb.append(h);
        Map<Character, String> codes = h.getCodes();
        for(char c : data)
        {
            sb.append(codes.get(c));
        }
        //��ǰ��һ��0
        sb = sb.reverse();
        sb.append(0);
        while(sb.length() % BYTE_LENGTH != 0)
        {
            sb.append(0);
        }
        //������ת��ȥ
        sb = sb.reverse();
        try
        {
            
           
            byte[] bs=binStringToByte(sb.toString());
            RandomAccessFile out = new RandomAccessFile(output, "rw"); 
            //д���ļ�����
            String type=input.split("\\.")[1];
    		out.writeUTF(type);    	
    		out.write(bs);
    		out.close();
       
           //ѹ����
            rate=(double)bs.length/read.length();
            out.close();
           
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
       
        return rate;
        
    }

    /**
     * ���������ַ���ת��Ϊbyte ����
     * @param s: �������ַ���
     * @return: �������ַ�����Ӧ��byte ����
     */
    private static byte[] binStringToByte(String s){
        byte[] bytes = new byte[s.length()/BYTE_LENGTH];
        for(int i = 0; i < bytes.length; i++)
        {
            Integer byteVal = Integer.parseInt(s.substring(i * BYTE_LENGTH, i * BYTE_LENGTH + BYTE_LENGTH), 2);
            bytes[i] = byteVal.byteValue();
        }
        return bytes;
    }

    /**
     * ���ļ���������ƣ�������ת��Ϊ�ַ���
     * @param f: the File to be read
     * @return: a binary String
     */
    public static String readBinString(File f)
    {
        byte[] bytes = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            bytes = Files.readAllBytes(f.toPath());
        }
        catch (IOException e) {
            System.out.println(e);
        }
        for(byte b : bytes)
        {
        	 sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
               return sb.toString();
    }

    /**
     * �����ļ������ض�����ַ�������ָ������
     * @param file: the file to be read
     * @return: A string representation of the contents of <f>
     */
    String read(File file) throws IOException {
		InputStream in = new FileInputStream(file);

		StringBuffer sbf = new StringBuffer();

		int len = 0;
		byte[] car = new byte[1024];
		while ((len = in.read(car)) != -1) { 
			String ss = new String(car, 0, len); 
			sbf.append(ss);
		}
		in.close();
		return sbf.toString();
	}
    

    /**
     * ���ַ��������ַ����ֵ�Ƶ��
     * @param characters �ַ�����
     * @return: Ƶ�ʵ�map
     */
    private static Map<Character, Integer> getFreqMap(char[] characters)
    {
        Map<Character, Integer> m = new HashMap<>();
        for(char c : characters)
        {
            if(!m.containsKey(c))
                m.put(c, 1);
            else
                m.put(c, m.get(c) + 1);
        }
        
        return m;
    }

    /**
	 * 
	 * ������Action����ɾ��ѹ������ļ�
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	@Override
	public File getOutputFile() {
		
		return this.outputFile;
	}
}