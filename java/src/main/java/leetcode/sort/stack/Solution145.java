package leetcode.sort.stack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 145. 二叉树的后序遍历(左右中、右左中)
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 示例:
 * <p>
 * 输入: [1,null,2,3]
 * 1
 * \
 * 2
 * /
 * 3
 * <p>
 * 输出: [3,2,1]
 * 进阶: 递归算法很简单，你可以通过迭代算法完成吗？
 */
public class Solution145 {
	public static void main(String[] args) {
		TreeNode root = new TreeNode(1);
		TreeNode l = new TreeNode(2);
		TreeNode r = new TreeNode(3);
		TreeNode ll = new TreeNode(4);
		TreeNode rr = new TreeNode(5);
		l.left = ll;
		r.right = rr;
		root.left = l;
		root.right = r;
		Solution145 s = new Solution145();
		s.postorderTraversal(root).forEach(System.out::print);
	}

	/**
	 * 迭代法
	 */
	public List<Integer> postorderTraversal(TreeNode root) {
		List<Integer> res = new ArrayList<>();
		if (root == null) {
			return res;
		}
		LinkedList<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			TreeNode cur = queue.getLast();
			if (cur.left == null && cur.right == null) {
				cur = queue.removeLast();
				res.add(cur.val);
			}
			if (cur.right != null) {
				queue.offer(cur.right);
				cur.right = null;
			}
			if (cur.left != null) {
				queue.offer(cur.left);
				cur.left = null;
			}
		}
		return res;
	}

	/**
	 * 递归法
	 */
	List<Integer> ret = new ArrayList<>();

	public List<Integer> postorderTraversal1(TreeNode root) {
		if (root != null) {
			postorderTraversal(root.left);
			postorderTraversal(root.right);
			ret.add(root.val);
		}
		return ret;
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
