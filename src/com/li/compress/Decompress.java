package com.li.compress;

import java.io.File;
import java.io.IOException;

import com.li.compress.huffman.HuffmanTreeDecode;
import com.li.compress.lzw.LZWDecode;

public abstract class Decompress {
	public abstract  void decompress(String input, String output) throws IOException;
    public final static double decompressDir(File in,File out) throws IOException {
		
		if(in.isFile()) {
			String newFile=out.getAbsolutePath()+File.separator+in.getName().split("\\.")[0];
			System.out.println("解压文件："+in.getAbsolutePath()+"-->"+newFile);
			File f=new File(newFile);
			String name=in.getName();
			Decompress code,zip=null;
			if (name.split("\\.")[1].equals("huf")) {
				code = new HuffmanTreeDecode();
				code.decompress(in.getAbsolutePath(), newFile);
			} else if (name.split("\\.")[1].equals("lzw")) {
				zip = new LZWDecode();
				zip.decompress(in.getAbsolutePath(), newFile);
			}
		}
		if(in.isDirectory()) {
			File[] fs=in.listFiles();
			for(File f:fs) {
				String outFile=out.getAbsolutePath()+File.separator+in.getName();
				System.out.println("解压目录："+f.getPath()+"-->"+outFile);
				File outf=new File(outFile);
				if(!outf.exists()) outf.mkdirs();
				decompressDir(f,new File(out.getAbsolutePath()+File.separator+in.getName()));
			}
		}
		return 0;
	}
}
