package leetcode.sort.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 94. 二叉树的中序遍历(左中右、右中左)
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个二叉树，返回它的中序 遍历。
 * <p>
 * 示例:
 * 输入: [1,null,2,3]
 * 1
 * \
 * 2
 * /
 * 3
 * 输出: [1,3,2]
 * <p>
 * 进阶: 递归算法很简单，你可以通过迭代算法完成吗？
 */
public class Solution94 {

	/**
	 * 迭代法
	 */
	public List<Integer> inorderTraversal(TreeNode root) {
		if (null == root) {
			return new ArrayList<>();
		}
		List<Integer> res = new ArrayList<>();
		Stack<TreeNode> stack = new Stack<>();
		while (root != null || !stack.empty()) {
			//入
			if (root != null) {
				stack.push(root);
				root = root.left;
			} else {
				//出
				root = stack.pop();
				res.add(root.val);
				root = root.right;
			}
		}
		return res;
	}

	/**
	 * 递归法
	 */
	List<Integer> res1 = new ArrayList<>();

	public List<Integer> inorderTraversal1(TreeNode root) {
		if (null == root) {
			return res1;
		}
		inorderTraversal(root.left);
		res1.add(root.val);
		inorderTraversal(root.right);
		return res1;
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
