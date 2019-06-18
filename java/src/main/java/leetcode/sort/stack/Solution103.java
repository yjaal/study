package leetcode.sort.stack;

import java.util.LinkedList;
import java.util.List;

/**
 * 103. 二叉树的锯齿形层次遍历
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个二叉树，返回其节点值的锯齿形层次遍历。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。
 * <p>
 * 例如：
 * 给定二叉树 [3,9,20,null,null,15,7],
 * <p>
 * 3
 * / \
 * 9  20
 * /  \
 * 15   7
 * 返回锯齿形层次遍历如下：
 * <p>
 * [
 * [3],
 * [20,9],
 * [15,7]
 * ]
 */
public class Solution103 {

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
		Solution103 s = new Solution103();
		List<List<Integer>> listList = s.zigzagLevelOrder(root);
		System.out.println();
	}

	List<List<Integer>> res = new LinkedList<>();

	public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
		if (null == root) {
			return res;
		}
		helper(root, 0, true);
		return res;
	}

	private void helper(TreeNode root, int count, boolean flag) {
		if (null == root) {
			return;
		}
		LinkedList<Integer> list;
		if (res.size() <= count) {
			list = new LinkedList<>();
			res.add(list);
		} else {
			list = (LinkedList) res.get(count);
		}
		if (flag) {
			list.addFirst(root.val);
		} else {
			list.add(root.val);
		}
		count++;
		flag = !flag;
		//flag:true表示从左向右
		if (flag) {
			helper(root.left, count, flag);
			helper(root.right, count, flag);
		} else {
			helper(root.right, count, flag);
			helper(root.left, count, flag);
		}
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