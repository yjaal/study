package leetcode.easy;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 好题
 * 104. 二叉树的最大深度
 * <p>
 * 给定一个二叉树，找出其最大深度。
 * <p>
 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 * <p>
 * 说明: 叶子节点是指没有子节点的节点。
 * <p>
 * 示例：
 * 给定二叉树 [3,9,20,null,null,15,7]，
 * <p>
 * 3
 * / \
 * 9  20
 * /  \
 * 15   7
 * 返回它的最大深度 3 。
 */
public class Solution104 {

	public static void main(String[] args) {
		Solution104 s = new Solution104();
		TreeNode root = new TreeNode(3);
		TreeNode left = new TreeNode(9);
		TreeNode right = new TreeNode(20);
		TreeNode rLeft = new TreeNode(15);
		TreeNode rRight = new TreeNode(7);
		right.left = rLeft;
		right.right = rRight;
		root.left = left;
		root.right = right;
		int res = s.maxDepth(root);
		System.out.println(res);
	}

	//递归法
	public int maxDepth(TreeNode root) {
		return root == null ? 0 : Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
	}

	//迭代法
	public int maxDepth1(TreeNode root) {
		Queue<TreeNode> queue = new LinkedList<>();
		if (root != null) {
			queue.add(root);
		}
		int depth = 0;
		// 广度优先遍历实现求树的最大深度（高度）
		while (!queue.isEmpty()) {
			// 注意这里的size方法只会计算一次。
			int size = queue.size();
			//这里就是每次会将同一层的node全部放入队列，然后每次又全部弹（出poll）
			for (int i = 0; i < size; i++) {
				TreeNode node = queue.poll();
				if (node.left != null) queue.add(node.left);
				if (node.right != null) queue.add(node.right);
			}
			depth++;
		}
		return depth;
	}

	private static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}
}
