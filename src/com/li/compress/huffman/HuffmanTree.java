package com.li.compress.huffman;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;



/**
 * 
 * 
 * @root_ the root of this heap
 * @huffmanEncodings: the "compression" key; each Character maps to a String
 */
public class HuffmanTree
{
	final private static int BYTE_LENGTH = 8;
    private Node root_;
    private Map<Character, String> huffmanEncodings;
    HuffmanTree(){
    	
    }
    /**
     * 将一个二进制字符串解码为哈夫曼树
     * @param s: 一个二进制串
     * @return: 解码后的字符串
     */
    public  String decode(String s)
    {
    	//之前在压缩的时候，为了数据对齐添加了一些零，现在不需要从这些零开始
        s = s.substring(s.indexOf("1"));
        StringBuilder sb = new StringBuilder();
        //在这个构造函数中，构造一个哈夫曼树的树，在s之上进行解码，将解码后的字符串放入sb中
        HuffmanTree h = new HuffmanTree(new Scanner(s), sb);
        return sb.toString();
    }

    /**
     * 构造一个哈夫曼树的树，并且解码
     * @param sc:压缩文件的扫描器，能够一个个比特读入到程序中来
     * @param s: 将解压后的字符串放入s中
     */
    private HuffmanTree(Scanner sc, StringBuilder s)
    {
    	//设置scanner 的定界符，就能一个个比特读入
        sc.useDelimiter("");
        Stack<Node> nodeStack = new Stack<>();
        LinkedList<Node> leafNodes = new LinkedList<>();
        root_ = new Node();
        nodeStack.push(root_);
        boolean pop = false;
        //读入编码
        while(nodeStack.size() > 0)
        {
        	//需要到叶子节点
            while(nodeStack.size() > 0 && nodeStack.peek().getLeft() != null && nodeStack.peek().getRight() != null)
                nodeStack.pop();
            if(nodeStack.size() > 0)
            {
            	
                int check = Integer.valueOf(sc.next());
               
                if(check == 1)
                {
                    Node n = new Node();
                    if(nodeStack.peek().getLeft() == null)
                    {
                        nodeStack.peek().setLeftChild(n);
                    }
                    else
                    {
                        nodeStack.peek().setRightChild(n);
                    }
                    nodeStack.push(n);
                }
                else
                {
                    if(pop)
                    {
                        pop = false;
                        leafNodes.add(nodeStack.pop());
                    }
                    else
                    {
                        pop = true;
                    }
                }
            }
        }
        //读入编码对应的字符
        for(Node n : leafNodes)
        {
            StringBuilder sb = new StringBuilder();
            //之所以是16是因为在此之前压缩的时候是2个字节存的
            for(int i = 0; i < 16; i++)
            {
            	String d=sc.next();
            	
                sb.append(d);
            }
            
            n.setChar((char)(Integer.parseInt(sb.toString(), 2)));
        }
        Node check = root_;
        //根据上面的到的树，对文件内容解码
        while(sc.hasNext())
        {
            int x = Integer.valueOf(sc.next());
            if(x == 1)
                check = check.getLeft();
            else if (x == 0)
                check = check.getRight();
            if(check.isLeaf())
            {
                s.append(check.c_);
                check = root_;
            }
        }
    }


    /**
     * 用于哈夫曼编码中，将传入的词的频率，构造一颗树
     * @param cFreq: an input frequency map
     */
    public HuffmanTree(Map<Character, Integer> cFreq)
    {    	
        PriorityQueue<Node> q = new PriorityQueue<>();
        huffmanEncodings = new HashMap<>();
        LinkedList<Node> charNodes = new LinkedList<>();
        for(Map.Entry<Character, Integer> entry: cFreq.entrySet())
        {
            q.add(new Node(entry.getKey(), entry.getValue()));
        }
        while(q.size() > 1)
        {
            Node a = q.remove();
            if(a.getC() != null)
                charNodes.add(a);
            Node b = q.remove();
            if(b.getC() != null)
                charNodes.add(b);
            Node branch = new Node(a, b);
            q.add(branch);
        }
        root_ = q.remove();
        buildHuffmanCodes(charNodes);
    }
    
   
   

    /**
     * @return: 编码表
     */
    public Map<Character, String> getCodes()
    {
        return huffmanEncodings;
    }

    /**
     * @return 哈夫曼树的根
     */
    public Node getRoot()
    {
        return root_;
    }

    /**
     * 将哈夫曼树转化为编码表
     * @param l:
     */
    public void buildHuffmanCodes(LinkedList<Node> l)
    {
        for(Node n : l)
        {
            Node check = n;
            Character c = n.getC();
            huffmanEncodings.put(c, "");
            while(check.getParent() != null)
            {
                Node s = check.getParent();
                if(s.getLeft() == check)
                    huffmanEncodings.put(c, 1 + huffmanEncodings.get(c));
                else if(s.getRight() == check)
                    huffmanEncodings.put(c, 0 + huffmanEncodings.get(c));
                //System.out.println(c+"-->"+huffmanEncodings.get(c));
                check = s;
            }
        }
    }

   
    
    public String toString()
    {
        StringBuilder scode = new StringBuilder();//存字符的二进制字符串
        StringBuilder string = new StringBuilder();//存编码的二进制字符串
        build(scode, string, root_);
        return scode.toString() + string.toString();
    }

    private void build(StringBuilder sb, StringBuilder sa, Node n)
    {
        if(n == null)
        {
            sb.append(0);
            return;
        }
        if(n.getC() != null)
        {
            String stringByteRepresentation = Integer.toString((int)n.getC(), 2);
            //在这里通过补零存两个字节，因为考虑到中文，stringByteRepresentation.length()/BYTE_LENGTH要等于2
            while(stringByteRepresentation.length()% BYTE_LENGTH!=0
            		||stringByteRepresentation.length()/BYTE_LENGTH<2)
                stringByteRepresentation = 0 + stringByteRepresentation;
            sa.append(stringByteRepresentation);
        }
        if(n.getLeft() != null)
            sb.append(1);
        build(sb, sa, n.getLeft());
        if(n.getRight() != null)
            sb.append(1);
        build(sb, sa, n.getRight());
    }

}