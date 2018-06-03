package Homework2;

public class MerkleTree extends SumTree<String>{

	public MerkleTree(String[] leafs, String init) {
		super(String[].class, leafs, init);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String add(String a, String b) {
		// TODO Auto-generated method stub
		return Utils.getSHA256(a+b);
	}
}
