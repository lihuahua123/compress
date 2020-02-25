package com.li.compress.huffman;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

import com.li.compress.Decompress;




public class HuffmanTreeDecode extends Decompress{

    final private static int BYTE_LENGTH = 8;
    private int readed;
    /**
     * ��Ҫ�Ľ�ѹ����
     * @param input:��Ҫѹ�����ļ�����
     * @param output:ѹ���ļ���Ŀ���ļ�
     * @throws IOException 
     */
    public  void decompress(String input, String output) throws IOException
    {
        
        File read = new File(input);
       
        //�����ļ�����
        RandomAccessFile in = new RandomAccessFile(input, "r");  
		String type=in.readUTF();
		output+="."+type;
		this.readed=(int) in.getFilePointer();
		in.close();
		String s = readBinString(read);
		File write = new File(output);
        try
        {
        	String en=new HuffmanTree().decode(s);
            OutputStream fileout=new FileOutputStream(write);
            fileout.write(en.getBytes());
            fileout.flush();
            fileout.close();
        }
        catch(IOException e)
        {
           e.printStackTrace();
        }
    }

    /**
     * ��һ���ļ��ж��������
     * @param f: ��Ҫ������ļ�
     * @return: һ�������ƴ�
     */
    public  String readBinString(File f)
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
        for(int i=this.readed;i<bytes.length;i++)
        {
        	byte b =bytes[i];
        	 sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
               return sb.toString();
    }

    

  

}
