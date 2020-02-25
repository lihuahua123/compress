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
     * ��һ���������ַ�������Ϊ��������
     * @param s: һ�������ƴ�
     * @return: �������ַ���
     */
    public  String decode(String s)
    {
    	//֮ǰ��ѹ����ʱ��Ϊ�����ݶ��������һЩ�㣬���ڲ���Ҫ����Щ�㿪ʼ
        s = s.substring(s.indexOf("1"));
        StringBuilder sb = new StringBuilder();
        //��������캯���У�����һ������������������s֮�Ͻ��н��룬���������ַ�������sb��
        HuffmanTree h = new HuffmanTree(new Scanner(s), sb);
        return sb.toString();
    }

    /**
     * ����һ�������������������ҽ���
     * @param sc:ѹ���ļ���ɨ�������ܹ�һ�������ض��뵽��������
     * @param s: ����ѹ����ַ�������s��
     */
    private HuffmanTree(Scanner sc, StringBuilder s)
    {
    	//����scanner �Ķ����������һ�������ض���
        sc.useDelimiter("");
        Stack<Node> nodeStack = new Stack<>();
        LinkedList<Node> leafNodes = new LinkedList<>();
        root_ = new Node();
        nodeStack.push(root_);
        boolean pop = false;
        //�������
        while(nodeStack.size() > 0)
        {
        	//��Ҫ��Ҷ�ӽڵ�
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
        //��������Ӧ���ַ�
        for(Node n : leafNodes)
        {
            StringBuilder sb = new StringBuilder();
            //֮������16����Ϊ�ڴ�֮ǰѹ����ʱ����2���ֽڴ��
            for(int i = 0; i < 16; i++)
            {
            	String d=sc.next();
            	
                sb.append(d);
            }
            
            n.setChar((char)(Integer.parseInt(sb.toString(), 2)));
        }
        Node check = root_;
        //��������ĵ����������ļ����ݽ���
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
     * ���ڹ����������У�������Ĵʵ�Ƶ�ʣ�����һ����
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
     * @return: �����
     */
    public Map<Character, String> getCodes()
    {
        return huffmanEncodings;
    }

    /**
     * @return ���������ĸ�
     */
    public Node getRoot()
    {
        return root_;
    }

    /**
     * ����������ת��Ϊ�����
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
        StringBuilder scode = new StringBuilder();//���ַ��Ķ������ַ���
        StringBuilder string = new StringBuilder();//�����Ķ������ַ���
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
            //������ͨ������������ֽڣ���Ϊ���ǵ����ģ�stringByteRepresentation.length()/BYTE_LENGTHҪ����2
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