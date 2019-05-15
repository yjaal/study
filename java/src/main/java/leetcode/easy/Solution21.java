package leetcode.easy;

import java.util.Objects;

/**
 * 21. Merge Two Sorted Lists
 * Merge two sorted linked lists and return it as a new list.
 * The new list should be made by splicing together the nodes of the first two lists.
 * <p>
 * Example:
 * <p>
 * Input: 1->2->4, 1->3->4
 * Output: 1->1->2->3->4->4
 */
public class Solution21 {
	public static void main(String[] args) {
		ListNode l1 = new ListNode(1);
		l1.next = new ListNode(2);
		l1.next.next = new ListNode(4);

		ListNode l2 = new ListNode(1);
		l2.next = new ListNode(3);
		l2.next.next = new ListNode(4);

		Solution21 s = new Solution21();
		ListNode result = s.mergeTwoLists(l1, l2);
		System.out.println("over");
	}

	public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		ListNode result = new ListNode(-1), cur = result;
		while (!Objects.equals(null, l1) && !Objects.equals(null, l2)) {
			if (l1.val < l2.val) {
				cur.next = l1;
				l1 = l1.next;
			} else {
				cur.next = l2;
				l2 = l2.next;
			}
			cur = cur.next;
		}
		cur.next = Objects.equals(null, l1) ? l2 : l1;
		return result.next;
	}

	//最佳解答
	public ListNode mergeTwoLists1(ListNode l1, ListNode l2) {
		if (l1 == null) {
			return l2;
		}
		if (l2 == null) {
			return l1;
		}
		ListNode h1 = l1;
		ListNode h2 = l2;
		if (h1.val > h2.val) {
			h2.next = mergeTwoLists(h1, h2.next);
		} else {
			h1.next = mergeTwoLists(h1.next, h2);
		}
		return h1.val > h2.val ? h2 : h1;
	}

	private static class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}

}
