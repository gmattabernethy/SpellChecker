import spell.ITrie;

public class Trie implements spell.ITrie{
    private Node root;
    private int hash;

    Trie(){
        this.root = new Node(' ');
        this.hash = 0;
    }
    @Override
    public void add(String str){
        str = str.toLowerCase();
        addToHash(str);
        Node currentNode = root;

        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            int loc = c-'a';

            if(currentNode.alphabet[loc] == null) {
                Node n =  new Node(c);
                currentNode.alphabet[loc] = n;
                currentNode = n;
            }
            else  currentNode = currentNode.alphabet[loc];

            if(i == str.length()-1) currentNode.count++;
        }
    }

    private void addToHash(String str){
        for (int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            hash+=c;
        }
    }

    @Override
    public Node find(String str){
        str = str.toLowerCase();
        Node currentNode = root;

        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            int loc = c-'a';

            if(currentNode.alphabet[loc] == null) {
                return null;
            }

            currentNode = currentNode.alphabet[loc];
        }

        if(currentNode.count > 0) return currentNode;
        else return null;
    }

    @Override
    public int getWordCount() {
        return wordCountTraverse(root);
    }

    private int wordCountTraverse(Node node){
        int sum = 0;
        if(node.count > 0) sum = 1;

        for(int i = 0; i < 26; i++){
            if(node.alphabet[i] == null) continue;

            sum += wordCountTraverse(node.alphabet[i]);
        }

        return sum;
    }

    @Override
    public int getNodeCount() {
        return nodeCountTraverse(root);
    }

    private int nodeCountTraverse(Node node){
        int sum = 1;

        for(int i = 0; i < 26; i++){
            if(node.alphabet[i] == null) continue;

            sum += nodeCountTraverse(node.alphabet[i]);
        }

        return sum;
    }

    @Override
    public String toString() {
        return stringTraverse(root, "");
    }

    private String stringTraverse(Node node, String str){
        String word = "";

        if(node.letter != ' ') str += node.letter;

        if(node.count > 0) word = str + "\n";

        for(int i = 0; i < 26; i++){
            if(node.alphabet[i] == null) continue;

            word += stringTraverse(node.alphabet[i], str);
        }

        return word;
    }

    @Override
    public int hashCode(){
        return (hash*2)-15;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Trie)) return false;

        Trie trie = (Trie) o;

        if(trie.getNodeCount() != this.getNodeCount()) return false;

        return equalsTraverse(trie.root,"");
    }

    private boolean equalsTraverse(Node node, String str){
        if(node.letter != ' ') str += node.letter;

        if(node.count > 0){
            Node found = this.find(str);
            if(found == null || found.getValue() != node.getValue()) return false;
        }

        for(int i = 0; i < 26; i++){
            if(node.alphabet[i] == null) continue;
            if(!equalsTraverse(node.alphabet[i], str)) return false;
        }

        return true;

    }

    public static void main(String[] args){
       Trie trie = new Trie();
       //Trie trie2 = new Trie();

       trie.add("Aa");

       System.out.println(trie.find("a"));
       //System.out.println("Nodes: " + trie.getNodeCount());
    }

}
