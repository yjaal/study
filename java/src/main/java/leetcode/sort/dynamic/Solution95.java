package leetcode.sort.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * 95. 不同的二叉搜索树 II
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个整数 n，生成所有由 1 ... n 为节点所组成的二叉搜索树。
 * <p>
 * 示例:
 * 输入: 3
 * 输出:
 * [
 * [1,null,3,2],
 * [3,2,null,1],
 * [3,1,null,null,2],
 * [2,1,3],
 * [1,null,2,null,3]
 * ]
 * 解释:
 * 以上的输出对应以下 5 种不同结构的二叉搜索树：
 * <p>
 * 1         3     3      2      1
 * \       /     /      / \      \
 * 3     2     1      1   3      2
 * /     /       \                 \
 * 2     1         2                 3
 */
public class Solution95 {

	/**
	 * 二叉搜索树BST：
	 * 从微观上来讲，BST的每个节点都大于其左节点，且小于其右节点。
	 * 从宏观上来将，BST的每个节点都大于其左子树的每个节点，且小于其右子树的每个节点。
	 * 一棵BST的中序遍历是有序的。这个性质我们称为BST的单调性。
	 *
	 * 本题用动态规划
	 * 分治法和动态规划
	 * 相同点：都是将原问题分而治之,分解成若干个规模较小
	 * 区别：
	 * 分治法常常利用递归求解，分解后的子问题看成相互独立的
	 * 动态规划通常利用迭代自底向上求解，或者具有记忆能力对桂法自顶向下，其分解的子问题理解成相互有联系的
	 * 思路：其实很好理解，i节点左子树由1~i - 1节点组成，右子树由i + 1 ~ n 节点组成
	 * 1. 选出根结点后应该先分别求解该根的左右子树集合，也就是根的左子树有若干种，它们组成左子树集合，根的右子树有若干种，它们组成右子树集合。
	 * 2. 然后将左右子树相互配对，每一个左子树都与所有右子树匹配，每一个右子树都与所有的左子树匹配。然后将两个子树插在根结点上。
	 * 3. 最后，把根结点放入list中。
	 */
	public List<TreeNode> generateTrees(int n) {
		if (n < 1) {
			return new ArrayList<TreeNode>();
		}
		return generate(1, n);
	}

	private List<TreeNode> generate(int start, int end) {
		List<TreeNode> list = new ArrayList<TreeNode>();
		// 加null至关重要，start>end表示左大于右了，而这种情况
		//下是没有i能存在于此区间的，而start、i、end可以是一个数
		//于是这里没有等于
		if (start > end) {
			list.add(null);
			return list;
		}
		if (start == end) {
			list.add(new TreeNode(start));
			return list;
		}
		// 确定根节点
		for (int i = start; i <= end; i++) {
			// 确定左右子树
			List<TreeNode> left = generate(start, i - 1);
			List<TreeNode> right = generate(i + 1, end);
			for (TreeNode leftT : left) {
				for (TreeNode rightT : right) {
					TreeNode root = new TreeNode(i);
					root.left = leftT;
					root.right = rightT;
					list.add(root);
				}
			}
		}
		return list;
	}

	private class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}
}
