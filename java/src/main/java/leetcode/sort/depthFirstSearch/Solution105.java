package leetcode.sort.depthFirstSearch;

/**
 * 105. 从前序与中序遍历序列构造二叉树
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 *
 * 根据一棵树的前序遍历与中序遍历构造二叉树。
 *
 * 注意:
 * 你可以假设树中没有重复的元素。
 *
 * 例如，给出
 *
 * 前序遍历 preorder = [3,9,20,15,7]
 * 中序遍历 inorder = [9,3,15,20,7]
 * 返回如下的二叉树：
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 */
public class Solution105 {
	public static void main(String[] args) {
		int[] preorder = new int[]{3,9,20,15,7};
		int[] inorder = new int[]{9,3,15,20,7};
		TreeNode root = new Solution105().buildTree(preorder, inorder);
		System.out.println();
	}

	public TreeNode buildTree(int[] preorder, int[] inorder) {
		if (null == preorder || null == inorder || preorder.length == 0 ||
				inorder.length == 0 || preorder.length != inorder.length) {
			return null;
		}
		return helper(preorder, inorder, 0, 0, inorder.length);
	}

	private TreeNode helper(int[] pres, int[] ins, int preLeft, int inLeft,
							int len) {
		if (len == 0) {
			return null;
		}
		int rootVal = pres[preLeft];
		TreeNode root = new TreeNode(rootVal);
		for (int i = 0; i < len; i++) {
			if (rootVal == ins[inLeft + i]) {
				root.left = helper(pres, ins, preLeft + 1, inLeft, i);
				root.right = helper(pres, ins, preLeft + i + 1, inLeft + i + 1,
						len - i - 1);
				return root;
			}
		}
		return null;
	}

	static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
}
