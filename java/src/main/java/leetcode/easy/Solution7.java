package leetcode.easy;

/**
 * 7. Reverse Integer
 * Given a 32-bit signed integer, reverse digits of an integer.
 * Example 1:
 * Input: 123
 * Output: 321
 * <p>
 * Example 2:
 * Input: -123
 * Output: -321
 * <p>
 * Example 3:
 * Input: 120
 * Output: 21
 *
 * Note:
 * Assume we are dealing with an environment which could only store integers
 * within the 32-bit signed integer range: [−2^31,  2^31 − 1]. For the purpose
 * of this problem, assume that your function returns 0 when the reversed integer overflows.
 */
public class Solution7 {

	public static void main(String[] args) {
//		int x = 1534236469;
		int x = 153;
		Solution7 s = new Solution7();
		System.out.println(s.reverse(x));
	}

	public int reverse(int x) {
		String flag = x >= 0 ? "" : "-";
		x = x >= 0 ? x : x * -1;
		int len = (x + "").length();
		long result = 0L;
		for(int i = 0; i < len; i++) {
			long y = (long) (x / Math.pow(10, len - i -1));//拿到第一位
			result += (long) (Math.pow(10, i) * y);
			x -= y * Math.pow(10, len - i - 1);
		}
		if(flag.endsWith("-")){
			result *= -1;
		}
		return (int)result == result ? (int)result : 0;
	}

	//最优解
	public int reverse1(int x) {
		long res = 0;
		while (x != 0) {
			//x % 10 拿到最后一位
			res = res * 10 + x % 10;
			x = x / 10;
		}
		return (int)res == res ? (int)res : 0;
	}
}
