package leetcode.sort.stack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
		List<List<Integer>> listList = s.zigzagLevelOrder1(root);
		System.out.println();
	}

	/**
	 * 使用栈实现
	 */
	public List<List<Integer>> zigzagLevelOrder1(TreeNode root) {
		List<List<Integer>> res = new ArrayList<>();
		if (null == root) {
			return res;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		int level = 1;
		while (!queue.isEmpty()) {
			int size = queue.size();
			ArrayList<Integer> list = new ArrayList<>();
			while (size-- > 0) {
				TreeNode cur = queue.poll();
				if (level % 2 == 0) {
					list.add(0, cur.val);
				} else {
					list.add(cur.val);
				}
				if (null != cur.left) {
					queue.offer(cur.left);
				}
				if (null != cur.right) {
					queue.offer(cur.right);
				}
			}
			res.add(list);
			level++;
		}
		return res;
	}

	/**
	 * 递归实现
	 */
	public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
		List<List<Integer>> res = new LinkedList<>();
		if (null == root) {
			return res;
		}
		helper(root, 0, res);
		return res;
	}

	/**
	 * 偶数层从左向右，奇数层从右向左
	 */
	private void helper(TreeNode root, int level, List<List<Integer>> res) {
		if (null == root) {
			return;
		}
		LinkedList<Integer> list;
		if (res.size() <= level) {
			list = new LinkedList<>();
			res.add(list);
		} else {
			list = (LinkedList) res.get(level);
		}
		//偶数层在后面插入，奇数层在前面插入
		if (level % 2 == 0) {
			list.addLast(root.val);
		} else {
			list.addFirst(root.val);
		}
		//注意：下面其实是和上面反着来的，不能调换
		//其实我们可以固定下面的某一种顺序，然后来确定是偶数在后面插，还是在前面插入
		helper(root.left, level + 1, res);
		helper(root.right, level + 1, res);
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