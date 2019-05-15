package leetcode.easy;

/**
 * 9.Palindrome Number 回文数字
 * Determine whether an integer is a palindrome. An integer is a palindrome when it reads the same backward as forward.
 * <p>
 * Example 1:
 * Input: 121
 * Output: true
 * <p>
 * Example 2:
 * Input: -121
 * Output: false
 * Explanation: From left to right, it reads -121. From right to left, it becomes 121-. Therefore it is not a palindrome.
 * <p>
 * Example 3:
 * Input: 10
 * Output: false
 * Explanation: Reads 01 from right to left. Therefore it is not a palindrome.
 * <p>
 * Follow up:
 * Coud you solve it without converting the integer to a string?
 */
public class Solution9 {
	//还可以转换为字符串进行验证
	public boolean isPalindrome(int x) {
		if (x < 0) {
			return false;
		} else {
			long res = 0;
			int y = x;
			while (y != 0) {
				//y % 10 拿到最后一位
				res = res * 10 + y % 10;
				y = y / 10;
			}
			return (int) res == res && res == x;
		}
	}

	//最佳解答：相比上面的解法（将数字完全反转），其实可以只反转一半就可以进行比较
	public boolean isPalindrome1(int x) {
		//负数或者10的倍数直接返回false
		if (x < 0 || (x != 0 && x % 10 == 0)) return false;
		int rev = 0;
		while (x > rev) {
			rev = rev * 10 + x % 10;
			x = x / 10;
		}
		//the length can be even or odd
		//两种情况：
		//123321或者12321
		return (x == rev || x == rev / 10);
	}
}
