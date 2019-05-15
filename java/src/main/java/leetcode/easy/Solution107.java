package leetcode.easy;


import java.util.*;

/**
 * 好题
 * 107. 二叉树的层次遍历 II
 *
 * 给定一个二叉树，返回其节点值自底向上的层次遍历。 （即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）
 *
 * 例如：
 * 给定二叉树 [3,9,20,null,null,15,7],
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回其自底向上的层次遍历为：
 *
 * [
 *   [15,7],
 *   [9,20],
 *   [3]
 * ]
 */
public class Solution107 {

	public static void main(String[] args) {
		Solution107 s = new Solution107();
		TreeNode root = new TreeNode(3);
		TreeNode left = new TreeNode(9);
		TreeNode right = new TreeNode(20);
		TreeNode rLeft = new TreeNode(15);
		TreeNode rRight = new TreeNode(7);
		right.left = rLeft;
		right.right = rRight;
		root.left = left;
		root.right = right;
		List<List<Integer>> res = s.levelOrderBottom(root);
		for (List list : res) {
			System.out.println(Arrays.toString(list.toArray()));
		}
	}

	//可以借鉴Solution104的迭代法
	public List<List<Integer>> levelOrderBottom(TreeNode root) {
		if (root == null) {
			return new ArrayList<>();
		}
		List<List<Integer>> res = new ArrayList<>();
		Queue<TreeNode> queue = new LinkedList<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			List<Integer> tmpList = new ArrayList<>();
			for(int i = 0; i < size; i++) {
				TreeNode node = queue.poll();
				tmpList.add(node.val);
				TreeNode left = node.left;
				TreeNode right = node.right;
				if (left != null) {
					queue.add(left);
				}
				if (right != null) {
					queue.add(right);
				}
			}
			res.add(tmpList);
		}
		Collections.reverse(res);
		return res;
	}

	/*******别人的答案********/
	public List<List<Integer>> levelOrderBottom2(TreeNode root) {
		List<List<Integer>> lists = new ArrayList<>();
		func(lists, 0, root);
		Collections.reverse(lists);
		return lists;
	}
	private void func(List<List<Integer>> lists, int level, TreeNode root) {
		if (root == null) {
			return;
		}
		//使用level来标识层级
		if (lists.size() == level) {
			List<Integer> list = new ArrayList<>();
			list.add(root.val);
			lists.add(list);
		} else {
			lists.get(level).add(root.val);
		}
		func(lists, level + 1, root.left);
		func(lists, level + 1, root.right);
	}


	//最佳答案，就是上面答案的优化版
	public List<List<Integer>> levelOrderBottom3(TreeNode root) {
		List<List<Integer>> levels = new ArrayList<>();
		helper(levels, root,0);
		return levels;
	}
	private void helper(List<List<Integer>> levels, TreeNode root,int level){
		if(root == null ) return;
		if(level == levels.size())
			levels.add(0,new ArrayList <Integer>());
		if(root.left != null) helper(levels, root.left,level+1);
		if(root.right != null) helper(levels, root.right,level+1);
		//反过来放值
		levels.get(levels.size()-1-level).add(root.val);
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
