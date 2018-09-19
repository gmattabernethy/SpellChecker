import spell.ITrie;

public class Node implements ITrie.INode{
    public Node[] alphabet;
    public int count;
    public char letter;

    public Node(char letter){
        alphabet = new Node[26];
        count = 0;
        this.letter = letter;
    }

    public int getValue() {
        return count;
    }

}
