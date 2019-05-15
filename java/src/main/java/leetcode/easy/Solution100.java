package leetcode.easy;

import java.util.Objects;

/**
 * 100. 相同的树
 *
 * 给定两个二叉树，编写一个函数来检验它们是否相同。
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
 *
 * 示例 1:
 * 输入:       1         1
 *           / \       / \
 *          2   3     2   3
 *
 *         [1,2,3],   [1,2,3]
 *
 * 输出: true
 *
 * 示例 2:
 * 输入:      1          1
 *           /           \
 *          2             2
 *
 *         [1,2],     [1,null,2]
 *
 * 输出: false
 *
 * 示例 3:
 * 输入:       1         1
 *           / \       / \
 *          2   1     1   2
 *
 *         [1,2,1],   [1,1,2]
 * 输出: false
 */
public class Solution100 {

	public static void main(String[] args) {
		Solution100 s = new Solution100();
		TreeNode p = new TreeNode(1);
		TreeNode pLeft = new TreeNode(2);
		TreeNode pRight = new TreeNode(3);
		p.left = pLeft;
		p.right = pRight;

		TreeNode q = new TreeNode(1);
		TreeNode qLeft = new TreeNode(2);
		TreeNode qRight = new TreeNode(4);
		q.left = qLeft;
		q.right = qRight;

		System.out.println(s.isSameTree(p, q));
	}

	public boolean isSameTree(TreeNode p, TreeNode q) {
		if (!Objects.equals(null, p) && !Objects.equals(null, q) &&
				p.val == q.val) {
			return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
		} else return Objects.equals(null, p) && Objects.equals(null, q);
	}

	//使用Objects.equals方法似乎没有直接使用==效率高
	public boolean isSameTree1(TreeNode p, TreeNode q) {
		if (p != null && q != null && p.val == q.val) {
			return isSameTree1(p.left, q.left) && isSameTree(p.right, q.right);
		} else return p == null && q == null;
	}

	private static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
}
