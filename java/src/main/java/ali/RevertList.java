package ali;

/**
 * 反转单向链表
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class RevertList {

	public static void main(String[] args) {
		Node node1 = new Node(1);
		Node node2 = new Node(2);
		Node node3 = new Node(3);
		Node node4 = new Node(4);
		Node node5 = new Node(5);
		node4.next = node5;
		node3.next = node4;
		node2.next = node3;
		node1.next = node2;
		RevertList revertList = new RevertList();
		Node res = revertList.revertList(node1);
		while (res != null) {
			System.out.print(res.val + ",");
			res = res.next;
		}
	}

	/**
	 * 此方法遍历了两次，不好
	 */
	public Node revertList(Node root) {
		if (null == root || root.next == null) {
			return root;
		}
		Node pre = root;
		Node tail;
		int count = 0;
		while (root.next != null) {
			root = root.next;
			count++;
		}
		tail = root;
		while (count-- > 0) {
			Node tmp = tail.next;
			tail.next = pre;
			pre = pre.next;
			tail.next.next = tmp;
		}
		return tail;
	}

	/**
	 * 递归方式
	 */
	public Node revertList1(Node root) {
		if (root == null || root.next == null) {
			return root;
		}
		Node tHead = revertList1(root.next);
		root.next.next = root;
		root.next = null;
		return tHead;
	}

	/**
	 * 遍历方式
	 */
	public Node revertList2(Node root) {
		if (root == null) {
			return root;
		}
		Node pre = root;
		Node cur = root.next;
		Node tmp;
		while (cur != null) {
			tmp = cur.next;
			cur.next = pre;

			//节点往后移动
			pre = cur;
			cur = tmp;
		}
		root.next = null;
		return root;
	}

	static class Node {
		int val;
		Node next;

		public Node() {
		}

		public Node(int val) {
			this.val = val;
		}
	}
}
