package com.li.compress.huffman;
import java.util.*;

import com.li.compress.Compress;

import java.io.*;
import java.nio.file.Files;


public class HuffmanTreeEncode extends Compress
{
	/**
	 * 
	 * @BYTE_LENGTH: 一个字节8位
	 */
    final private static int BYTE_LENGTH = 8;

    
    private File outputFile;
    /**
     * 压缩
     * @param input
     * @param output
     * @return  压缩比
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
        //写入整棵树
        sb.append(h);
        Map<Character, String> codes = h.getCodes();
        for(char c : data)
        {
            sb.append(codes.get(c));
        }
        //向前补一个0
        sb = sb.reverse();
        sb.append(0);
        while(sb.length() % BYTE_LENGTH != 0)
        {
            sb.append(0);
        }
        //把它翻转回去
        sb = sb.reverse();
        try
        {
            
           
            byte[] bs=binStringToByte(sb.toString());
            RandomAccessFile out = new RandomAccessFile(output, "rw"); 
            //写入文件类型
            String type=input.split("\\.")[1];
    		out.writeUTF(type);    	
    		out.write(bs);
    		out.close();
       
           //压缩比
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
     * 将二进制字符串转化为byte 数组
     * @param s: 二进制字符串
     * @return: 二进制字符串对应的byte 数组
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
     * 对文件读入二进制，并将其转化为字符串
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
     * 读入文件，返回读入的字符串，无指定编码
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
     * 对字符串计算字符出现的频率
     * @param characters 字符数组
     * @return: 频率的map
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
	 * 用于在Action包中删除压缩后的文件
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	@Override
	public File getOutputFile() {
		
		return this.outputFile;
	}
}