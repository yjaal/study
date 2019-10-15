package leetcode.medium;

import java.util.ArrayList;
import java.util.List;

/**
 * Leetcode 545. Boundary of Binary Tree
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 *
 * Given a binary tree, return the values of its boundary in anti-clockwise 
 * direction starting from root. Boundary includes left boundary, leaves, and
 * right boundary in order without duplicate nodes.
 * Left boundary is defined as the path from root to the left-most node. 
 * Right boundary is defined as the path from root to the right-most node.
 * If the root doesn't have left subtree or right subtree, then the root itself
 * is left boundary or right boundary. Note this definition only applies to
 * the input binary tree, and not applies to any subtrees.
 * The left-most node is defined as a leaf node you could reach when you always
 * firstly travel to the left subtree if exists. If not, travel to the right subtree.
 * Repeat until you reach a leaf node.
 * The right-most node is also defined by the same way with left and right exchanged.
 *
 * Example 1
 * Input:
 * 1
 * \
 * 2
 * / \
 * 3   4
 *
 * Ouput:
 * [1, 3, 4, 2]
 * Explanation:
 * The root doesn't have left subtree, so the root itself is left boundary.
 * The leaves are node 3 and 4.
 * The right boundary are node 1,2,4. Note the anti-clockwise direction means you should output reversed right boundary.
 * So order them in anti-clockwise without duplicates and we have [1,3,4,2].
 *
 * Example 2
 * Input:
 * ____1_____
 * /          \
 * 2            3
 * / \          /
 * 4   5        6
 * / \      / \
 * 7   8    9  10
 *
 * Ouput:
 * [1,2,4,7,8,9,10,6,3]
 * Explanation:
 * The left boundary are node 1,2,4. (4 is the left-most node according to definition)
 * The leaves are node 4,7,8,9,10.
 * The right boundary are node 1,3,6,10. (10 is the right-most node).
 * So order them in anti-clockwise without duplicate nodes we have [1,2,4,7,8,9,10,6,3].
 */
public class Solution545 {

	private List<Integer> res = new ArrayList<>();

	public static void main(String[] args) {
		TreeNode node10 = new TreeNode(10, null, null);
		TreeNode node9 = new TreeNode(9, null, null);
		TreeNode node8 = new TreeNode(8, null, null);
		TreeNode node7 = new TreeNode(7, null, null);
		TreeNode node6 = new TreeNode(6, node9, node10);
		TreeNode node5 = new TreeNode(5, node7, node8);
		TreeNode node4 = new TreeNode(4, null, null);
		TreeNode node3 = new TreeNode(3, node6, null);
		TreeNode node2 = new TreeNode(2, node4, node5);
		TreeNode node1 = new TreeNode(1, node2, node3);

		List<Integer> result = new Solution545().boundaryOfBinaryTree(node1);
		System.out.println();
	}

	public List<Integer> boundaryOfBinaryTree(TreeNode root) {
		if (null == root) {
			return res;
		}
		res.add(root.val);
		help(root.left, true, false);
		help(root.right, false, true);
		return res;
	}

	private void help(TreeNode root, boolean lBound, boolean rBound) {
		if (null == root) {
			return;
		}
		if (lBound) {
			res.add(root.val);
		}
		if (!lBound && !rBound && root.left == null && null == root.right) {
			res.add(root.val);
		}
		//父节点不是左（右）边界，那左（右）子节点肯定不是左（右边界）
		help(root.left, lBound, rBound && root.right == null);
		help(root.right, lBound && root.left == null, rBound);
		if (rBound) {
			res.add(root.val);
		}
	}


	static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		public TreeNode() {
		}

		public TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}
}
