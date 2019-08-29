package leetcode.sort.depthFirstSearch;

/**
 * 109. 有序链表转换二叉搜索树
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 *
 * 给定一个单链表，其中的元素按升序排序，将其转换为高度平衡的二叉搜索树。
 * 本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1。
 *
 * 示例:
 * 给定的有序链表： [-10, -3, 0, 5, 9],
 *
 * 一个可能的答案是：[0, -3, 9, -10, null, 5], 它可以表示下面这个高度平衡二叉搜索树：
 *
 *       0
 *      / \
 *    -3   9
 *    /   /
 *  -10  5
 */
public class Solution109 {

	public TreeNode sortedListToBST(ListNode head) {
		if (head == null) {
			return null;
		}
		TreeNode root = null;
		if (head.next == null) {
			return new TreeNode(head.val);
		} else if (head.next.next == null) {
			root = new TreeNode(head.val);
			root.right = new TreeNode(head.next.val);
			return root;
		}
		ListNode pre = head;
		ListNode slow = head;
		ListNode fast = head;
		while (fast.next != null && fast.next.next != null) {
			pre = slow;
			slow = slow.next;
			fast = fast.next.next;
		}
		root = new TreeNode(slow.val);
		root.right = sortedListToBST(slow.next);
		pre.next = null;
		root.left = sortedListToBST(head);
		return root;
	}

	static class ListNode {
		int val;
		ListNode next;
		ListNode(int x) { val = x; }
	}

	static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
}
