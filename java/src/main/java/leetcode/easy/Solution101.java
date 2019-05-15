package leetcode.easy;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 101. 对称二叉树
 *
 * 给定一个二叉树，检查它是否是镜像对称的。
 * 例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
 *
 *     1
 *    / \
 *   2   2
 *  / \ / \
 * 3  4 4  3
 * 但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的:
 *
 *     1
 *    / \
 *   2   2
 *    \   \
 *    3    3
 * 说明:
 *
 * 如果你可以运用递归和迭代两种方法解决这个问题，会很加分。
 */
public class Solution101 {

	public static void main(String[] args) {
		Solution101 s = new Solution101();
		TreeNode root = new TreeNode(1);

		TreeNode rLeft = new TreeNode(2);
		TreeNode lLeft = new TreeNode(3);
		TreeNode lRight = new TreeNode(4);
//		rLeft.left = lLeft;
		rLeft.right = lRight;
		root.left = rLeft;

		TreeNode rRight = new TreeNode(2);
		TreeNode rLeft1 = new TreeNode(4);
		TreeNode rRight1 = new TreeNode(3);
//		rRight.left = rLeft1;
		rRight.right = rRight1;
		root.right = rRight;

		System.out.println(s.isSymmetric1(root));
	}

	//递归法
	public boolean isSymmetric(TreeNode root) {
		return root == null || isSymmetric(root.left, root.right);
	}
	private boolean isSymmetric(TreeNode left, TreeNode right) {
		if (left == null || right == null) {
			return left == null && right == null;
		} else return left.val == right.val &&
				isSymmetric(left.left, right.right) && isSymmetric(left.right, right.left);
	}

	//迭代法
	public boolean isSymmetric1(TreeNode root) {
		if (root == null) {
			return true;
		} else if (root.left != null && root.right != null) {
			Queue<TreeNode> qLeft = new LinkedList<>();
			qLeft.add(root.left);
			Queue<TreeNode> qRight = new LinkedList<>();
			qRight.add(root.right);
			return isSymmetric1(qLeft, qRight);
		} else {
			return root.left == null && root.right == null;
		}
	}
	private boolean isSymmetric1(Queue<TreeNode> qLeft, Queue<TreeNode> qRight) {
		while (!qLeft.isEmpty() && !qRight.isEmpty()) {
			TreeNode left = qLeft.remove();
			TreeNode right = qRight.remove();
			if (left != null && right != null && left.val == right.val) {
				qLeft.add(left.left);
				qLeft.add(left.right);
				qRight.add(right.right);
				qRight.add(right.left);
			} else {
				if (!(left == null && right == null)) {
					return false;
				}
			}
		}
		return qLeft.isEmpty() && qRight.isEmpty();
	}

	static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}

}
