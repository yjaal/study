package toutiao;

/**
 * 删除有序链表中重复的节点
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class DeleteRepeatNode {

	public void deleteDuplicate(Node root) {
		if (null == root || null == root.next) {
			return;
		}
		Node pre = root;
		Node after = pre.next;
		while (after != null) {
			if (pre.val == after.val) {
				pre.next = after.next;
				after = pre.next;
			} else {
				pre = pre.next;
				after = pre.next;
			}
		}
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
