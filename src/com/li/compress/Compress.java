package com.li.compress;

import java.io.File;
import java.io.IOException;

public abstract class Compress {
	public abstract  double compress(String input, String output) throws IOException;
	public abstract File getOutputFile();
	public double compressDir(File in,File out,String type) throws IOException {
		
		if(in.isFile()) {
			String newFile=out.getAbsolutePath()+File.separator+in.getName().split("\\.")[0]+"."+type;
			System.out.println("Ñ¹ËõÎÄ¼þ£º"+in.getAbsolutePath()+"-->"+newFile);
			File f=new File(newFile);
			if(!f.exists()) {
			f.createNewFile();
			}
			compress(in.getAbsolutePath(),newFile);
		}
		if(in.isDirectory()) {
			File[] fs=in.listFiles();
			System.out.println("Ñ¹ËõÄ¿Â¼£º"+in.getPath()+"-->");
			for(File f:fs) {
				String outFile=out.getAbsolutePath()+File.separator+in.getName();
				System.out.println(outFile);
				File outf=new File(outFile);
				if(!outf.exists()) outf.mkdirs();
				compressDir(f,new File(out.getAbsolutePath()+File.separator+in.getName()),type);
			}
		}
		return 0;
	}
}
