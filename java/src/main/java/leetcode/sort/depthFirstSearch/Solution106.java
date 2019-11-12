package leetcode.sort.depthFirstSearch;

/**
 * 106. 从中序与后序遍历序列构造二叉树
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 根据一棵树的中序遍历与后序遍历构造二叉树。
 * <p>
 * 注意:
 * 你可以假设树中没有重复的元素。
 * <p>
 * 例如，给出
 * 中序遍历 inorder = [9,3,15,20,7]
 * 后序遍历 postorder = [9,15,7,20,3]
 * 返回如下的二叉树：
 * <p>
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 */
public class Solution106 {

	/**
	 * 这是一种暴力求解的方法
	 */
	public TreeNode buildTree(int[] inorder, int[] postorder) {
		//未被使用则为false
		boolean[] flags = new boolean[postorder.length];
		return buildTree(inorder, postorder, 0, inorder.length - 1, flags);
	}

	private TreeNode buildTree(int[] inorder, int[] postorder, int inStart, int inEnd,
							   boolean[] flags) {
		if (inStart > inEnd) {
			return null;
		}
		int inIdx = 0;
		TreeNode root = null;
		for (int j = postorder.length - 1; j >= 0; j--) {
			if (flags[j]) {
				continue;
			}
			for (int i = inStart; i <= inEnd; i++) {
				if (inorder[i] == postorder[j]) {
					inIdx = i;
					flags[j] = true;
					root = new TreeNode(inorder[i]);
					break;
				}
			}
			if (flags[j]) {
				break;
			}
		}
		if (null != root) {
			root.left = buildTree(inorder, postorder, inStart, inIdx - 1, flags);
			root.right = buildTree(inorder, postorder, inIdx + 1, inEnd, flags);
		}
		return root;
	}

	/**
	 * 最优解
	 */
	public TreeNode buildTree1(int[] inorder, int[] postorder) {
		return buildTree1(postorder, inorder, postorder.length - 1,
				inorder.length - 1, inorder.length);
	}

	private TreeNode buildTree1(int[] postorder, int[] inorder, int postright,
						  int inright, int length) {
		if (length == 0) {
			return null;
		}

		int rootVal = postorder[postright];
		TreeNode root = new TreeNode(rootVal);
		if (length == 1) {
			return root;
		}
		//对于inorder来说，从后往前遍历，直到找到root节点，所经过的节点全部为
		//右子树，剩下的为左子树
		for (int i = 0; i <= length - 1; i++) {
			if (inorder[inright - i] == rootVal) {
				root.left = buildTree1(postorder, inorder, postright - i - 1,
						inright - i - 1, length - i - 1);
				root.right = buildTree1(postorder, inorder, postright - 1, inright, i);
				return root;
			}
		}
		return null;
	}

	static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}
}
