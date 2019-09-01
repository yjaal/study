package leetcode.sort.depthFirstSearch;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 114. 二叉树展开为链表
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 *
 * 给定一个二叉树，原地将它展开为链表。
 * 例如，给定二叉树
 *
 *     1
 *    / \
 *   2   5
 *  / \   \
 * 3   4   6
 * 将其展开为：
 *
 * 1
 *  \
 *   2
 *    \
 *     3
 *      \
 *       4
 *        \
 *         5
 *          \
 *           6
 */
public class Solution114 {

	public void flatten(TreeNode root) {
		if (root != null) {
			helper(root);
		}
	}

	private TreeNode helper(TreeNode root) {
		if (root == null) {
			return null;
		}
		if (root.left == null) {
			root.right = helper(root.right);
			return root;
		}
		if (root.right == null) {
			root.right = helper(root.left);
			root.left = null;
			return root;
		}
		TreeNode left = helper(root.left);
		TreeNode right = helper(root.right);
		root.right = left;
		root.left = null;
		while (true) {
			if (left.right != null) {
				left = left.right;
			} else {
				left.right = right;
				break;
			}
			new AtomicInteger();
		}

		return root;
	}


	static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
}
