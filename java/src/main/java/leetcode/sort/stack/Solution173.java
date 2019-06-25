package leetcode.sort.stack;

import java.util.Stack;

/**
 * 173. 二叉搜索树迭代器
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 实现一个二叉搜索树迭代器。你将使用二叉搜索树的根节点初始化迭代器。
 * 调用 next() 将返回二叉搜索树中的下一个最小的数。
 * <p>
 * 示例：[7, 3, 15, null, null, 9, 20]
 * BSTIterator iterator = new BSTIterator(root);
 * iterator.next();    // 返回 3
 * iterator.next();    // 返回 7
 * iterator.hasNext(); // 返回 true
 * iterator.next();    // 返回 9
 * iterator.hasNext(); // 返回 true
 * iterator.next();    // 返回 15
 * iterator.hasNext(); // 返回 true
 * iterator.next();    // 返回 20
 * iterator.hasNext(); // 返回 false
 *  
 * <p>
 * 提示：
 * next() 和 hasNext() 操作的时间复杂度是 O(1)，并使用 O(h) 内存，其中 h 是树的高度。
 * 你可以假设 next() 调用总是有效的，也就是说，当调用 next() 时，BST 中至少存在一个下一个最小的数。
 */
public class Solution173 {

	public static void main(String[] args) {

		TreeNode root = new TreeNode(7);
		TreeNode l = new TreeNode(3);
		TreeNode r = new TreeNode(15);
		TreeNode rl = new TreeNode(9);
		TreeNode rr = new TreeNode(20);
		r.left = rl;
		r.right = rr;
		root.left = l;
		root.right = r;
		Solution173 s = new Solution173(root);
		while (s.hasNext()) {
			System.out.println(s.next());
		}
	}

	Stack<Integer> stack = new Stack<>();

	public Solution173(TreeNode root) {
		if (null == root) {
			return;
		}
		Stack<TreeNode> stackTmp = new Stack<>();
		while (null != root || !stackTmp.isEmpty()) {
			if (null != root) {
				stackTmp.push(root);
				root = root.right;
			} else {
				root = stackTmp.pop();
				stack.push(root.val);
				root = root.left;
			}
		}
	}

	/**
	 * @return the next smallest number
	 */
	public int next() {
		return stack.pop();
	}

	/**
	 * @return whether we have a next smallest number
	 */
	public boolean hasNext() {
		return stack.size() != 0;
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
