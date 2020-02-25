package com.li.compress.huffman;



public class Node implements Comparable<Node>
{
    Character c_;
    private int freq_;
    private Node parent, left, right;

    public Node(char c, int freq)
    {
        c_ = c;
        freq_ = freq;
        left = null;
        right = null;
        parent = null;
    }

    public Node(Node a, Node b)
    {
        freq_ = a.freq_ + b.freq_;
        setLeftChild(a);
        setRightChild(b);
        c_ = null;
        a.setParent(this);
        b.setParent(this);
    }

    public Node(char c, Node p)
    {
        c_ = c;
        parent = p;
    }

    public Node(char c)
    {
        c_ = c;
    }

    public Node()
    {

    }

    public void setChar(Character c)
    {
        c_ = c;
    }

    public String toString()
    {
        return (this.getC() == null ? "" : this.getC() + ",") + this.freq_;
    }

    private void setParent(Node n)
    {
        parent = n;
    }

    void setRightChild(Node n)
    {
        right = n;
    }

    void setLeftChild(Node n)
    {
        left = n;
    }

    public Node getLeft()
    {
        return left;
    }

    public Node getRight()
    {
        return right;
    }

    public Node getParent()
    {
        return parent;
    }

    public int compareTo(Node n)
    {
        return Integer.compare(this.freq_, n.freq_);
    }

    public boolean isLeaf()
    {
        return getLeft() == null && getRight() == null;
    }

    public Character getC()
    {
        return c_;
    }

}
