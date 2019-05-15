package leetcode.easy;

import java.util.Objects;

/**
 * 83. 删除排序链表中的重复元素
 * 给定一个排序链表，删除所有重复的元素，使得每个元素只出现一次。
 * <p>
 * 示例 1:
 * 输入: 1->1->2
 * 输出: 1->2
 * <p>
 * 示例 2:
 * 输入: 1->1->2->3->3
 * 输出: 1->2->3
 */
public class Solution83 {

	public static void main(String[] args) {
		Solution83 s = new Solution83();
		ListNode head = new ListNode(1);
		ListNode next1 = new ListNode(1);
		ListNode next2 = new ListNode(2);
		ListNode next3 = new ListNode(2);
		ListNode next4 = new ListNode(3);
		ListNode next5 = new ListNode(3);
		next4.next = next5;
		next3.next = next4;
		next2.next = next3;
		next1.next = next2;
		head.next = next1;
		head = s.deleteDuplicates(head);
		while (!Objects.equals(null, head)) {
			System.out.print(head.val + ",");
			head = head.next;
		}
	}

	public ListNode deleteDuplicates(ListNode head) {
		if (Objects.equals(null, head) || Objects.equals(null, head.next)) {
			return head;
		}
		ListNode root = head;
		ListNode headNext = head.next;
		while (!Objects.equals(null, headNext.next)) {
			if (head.val == headNext.val) {
				head.next = headNext.next;
				headNext = head.next;
			} else {
				head = head.next;
				headNext = headNext.next;
			}
		}
		if (head.val == headNext.val) {
			head.next = null;
		}
		return root;
	}

	//最佳解答
	public ListNode deleteDuplicates1(ListNode head) {
		ListNode current = head;
		while (current != null && current.next != null) {
			if (current.next.val == current.val) {
				current.next = current.next.next;
			} else {
				current = current.next;
			}
		}
		return head;
	}

	static class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}
}
