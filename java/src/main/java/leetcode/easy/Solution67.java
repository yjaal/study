package leetcode.easy;

/**
 * 67. 二进制求和
 * <p>
 * 给定两个二进制字符串，返回他们的和（用二进制表示）。
 * <p>
 * 输入为非空字符串且只包含数字 1 和 0。
 * <p>
 * 示例 1:
 * <p>
 * 输入: a = "11", b = "1"
 * 输出: "100"
 * 示例 2:
 * <p>
 * 输入: a = "1010", b = "1011"
 * 输出: "10101"
 */
public class Solution67 {
	public static void main(String[] args) {
		Solution67 s = new Solution67();
		String a = "1010";
		String b = "1011";
		String res = s.addBinary(a, b);
		System.out.println(res);
	}

	public String addBinary(String a, String b) {
		int aLen = a.length();
		int bLen = b.length();
		int len = aLen > bLen ? aLen : bLen;
		if (aLen > bLen) {
			int count = aLen - bLen;
			while (count != 0) {
				b = "0" + b;
				count--;
			}
		} else {
			int count = bLen - aLen;
			while (count != 0) {
				a = "0" + a;
				count--;
			}
		}
		int tmp = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = len - 1; i >= 0; i--) {
			tmp += Integer.valueOf(a.charAt(i) + "") + Integer.valueOf(b.charAt(i) + "");
			if (tmp >= 2) {
				sb.append(tmp % 2);
				tmp /= 2;
			} else {
				sb.append(tmp);
				tmp = 0;
			}
		}
		if (tmp == 1) {
			sb.append(tmp);
		}
		return sb.reverse().toString();
	}

	//最佳解答
	public String addBinary1(String a, String b) {
		StringBuilder sb = new StringBuilder();
		int flag = 0;
		int i = a.length() - 1;
		int j = b.length() - 1;
		while (i >= 0 || j >= 0 || flag == 1) {
			//这里包含了对长度不一致的处理
			int ia = i >= 0 ? a.charAt(i) == '1' ? 1 : 0 : 0;
			int ib = j >= 0 ? b.charAt(j) == '1' ? 1 : 0 : 0;
			int sum = ia + ib + flag;
			switch (sum) {
				case 0:
					sb.append(0);
					break;
				case 1:
					sb.append(1);
					flag = 0;
					break;
				case 2:
					sb.append(0);
					flag = 1;
					break;
				case 3:
					sb.append(1);
					flag = 1;
					break;
			}
			i--;
			j--;
		}
		return sb.reverse().toString();
	}
}
