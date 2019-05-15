package leetcode.easy;


/**
 * 好题
 * 110. 平衡二叉树
 *
 * 给定一个二叉树，判断它是否是高度平衡的二叉树。
 * 本题中，一棵高度平衡二叉树定义为：
 * 一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过1。
 *
 * 示例 1:
 * 给定二叉树 [3,9,20,null,null,15,7]
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回 true 。
 *
 * 示例 2:
 * 给定二叉树 [1,2,2,3,3,null,null,4,4]
 *        1
 *       / \
 *      2   2
 *     / \
 *    3   3
 *   / \
 *  4   4
 * 返回 false 。
 */
public class Solution110 {


	//这种方式比较耗时
	public boolean isBalanced(TreeNode root) {
		if (root == null) {
			return true;
		}
		int leftH = root.left == null ? 0 : height(root.left);
		int rightH = root.right == null ? 0 : height(root.right);
		return Math.abs(leftH - rightH) <= 1 && isBalanced(root.left) && isBalanced(root.right);
	}
	private int height(TreeNode node) {
		if (node == null) {
			return 0;
		} else {
			return 1 + Math.max(height(node.left), height(node.right));
		}
	}

	//优化
	public boolean isBalanced1(TreeNode root) {
		return getDepth(root) >= 0;
	}
	private int getDepth(TreeNode root) {
		if (null == root) {
			return 0;
		}
		int leftDepth = getDepth(root.left);
		int rightDepth = getDepth(root.right);
		if (leftDepth >= 0 && rightDepth >= 0 && Math.abs(leftDepth - rightDepth) <= 1) {
			return Math.max(leftDepth, rightDepth) + 1;
		} else {
			return -1;
		}
	}

	//最优解，这个方法可以这样理解，使用一个数组引用来保存高度差，同时
	//使用返回值来判断是否平衡
	public boolean isBalanced2(TreeNode root) {
		if (root == null) {
			return true;
		}
		int[] height = new int[1];
		return isBalancedCore(root, height);
	}
	boolean isBalancedCore(TreeNode node, int[] height){
		if (node == null) {
			height[0] = 0;
			return true;
		}
		int[] left = new int[1];
		boolean leftResult = isBalancedCore(node.left, left);
		if (!leftResult) {
			return false;
		}
		int[] right = new int[1];
		boolean rightResult = isBalancedCore(node.right, right);
		if (!rightResult) {
			return false;
		}
		int diff = Math.abs(left[0] - right[0]);
		if (diff > 1) {
			return false;
		}
		height[0] = left[0] > right[0] ? left[0] + 1: right[0] + 1;
		return true;
	}

	private class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
}
