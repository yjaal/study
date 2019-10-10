package toutiao;

/**
 * 判断链表是否为回文结构
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 *
 *
 */
public class PalindromeList {

	/**
	 * 反转后再比较
	 */
	public boolean isPalindrome1(Node root) {
		if (null != root) {
			Node cur = revert(root);
			while (root != null) {
				if (root.val != cur.val) {
					return false;
				}
				root = root.next;
				cur = cur.next;
			}
			return true;
		}
		return false;
	}

	private Node revert(Node root) {
		if (null == root || null == root.next) {
			return root;
		}
		Node cur = revert(root.next);
		root.next.next = root;
		root.next = null;
		return cur;
	}

	static class Node{
		int val;
		Node next;

		public Node(){}

		public Node(int val) {
			this.val = val;
		}
	}
}
