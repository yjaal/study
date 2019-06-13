package leetcode.sort.greed;

/**
 * 402. 移掉K位数字
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * 给定一个以字符串表示的非负整数 num，移除这个数中的 k 位数字，使得剩下的数字最小。
 * <p>
 * 注意:
 * num 的长度小于 10002 且 ≥ k。
 * num 不会包含任何前导零。
 * <p>
 * 示例 1 :
 * 输入: num = "1432219", k = 3
 * 输出: "1219"
 * 解释: 移除掉三个数字 4, 3, 和 2 形成一个新的最小的数字 1219。
 * <p>
 * 示例 2 :
 * 输入: num = "10200", k = 1
 * 输出: "200"
 * 解释: 移掉首位的 1 剩下的数字为 200. 注意输出不能有任何前导零。
 * <p>
 * 示例 3 :
 * 输入: num = "10", k = 2
 * 输出: "0"
 * 解释: 从原数字移除所有的数字，剩余为空就是0。
 */
public class Solution402 {
	public static void main(String[] args) {
		Solution402 obj = new Solution402();
		String num = "1234567890";
		System.out.println(obj.removeKdigits(num, 9));
	}

	/**
	 * (1)假如第一个数不为0，第二个数为0，那么我们删除第一个数，
	 * 就相当于数量级减少2，这样比删除得到的数其他任何一个方法都小
	 * <p>
	 * (2)另一种情况，我们找到第一次遍历的局部最大值，即遍历num第一个满足
	 * num.charAt(i)>num.charAt(i+1)的值，删除这个点，得到的值最小。这里就是贪心算法，每次删除一个局部最大
	 * 这里未通过全部用例，不知为何？
	 */
	public String removeKdigits(String num, int k) {
		if (null == num || num.length() == 0 || k >= num.length()) {
			return "0";
		}
		if (k <= 0 || num.length() == 1) {
			return num;
		}
		while (k != 0) {
			if (num.charAt(1) == '0') {
				num = num.substring(2);
				k--;
				continue;
			}
			int i = 0;
			int len = num.length();
			for (; i < len - 1; i++) {
				if (num.charAt(i) > num.charAt(i + 1)) {
					num = num.substring(0, i) + num.substring(i + 1);
					k--;
					break;
				}
			}
			//这里表明最后一个元素是最大的，直接移除掉
			if (k != 0 && len == num.length()) {
				num = num.substring(0, len - 1);
				k--;
			}
		}
		return "".equals(num) ? "0" : num;
	}

	/**
	 * 最优解
	 */
	public String removeKdigits1(String num, int k) {
		int digits = num.length() - k;
		char[] stack = new char[num.length()];
		int top = 0;

		for (int i = 0; i < num.length(); i++) {
			char c = num.charAt(i);
			while (top > 0 && stack[top - 1] > c && k > 0) {
				top--;
				k--;
			}
			stack[top++] = c;
		}

		int index = 0;
		while (index < digits && stack[index] == '0') {
			index++;
		}
		return index == digits ? "0" : new String(stack, index, digits - index);
	}
}
