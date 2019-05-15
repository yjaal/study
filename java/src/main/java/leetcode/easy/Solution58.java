package leetcode.easy;

import java.util.Objects;

/**
 * 58. 最后一个单词的长度
 * <p>
 * 给定一个仅包含大小写字母和空格 ' ' 的字符串，返回其最后一个单词的长度。
 * <p>
 * 如果不存在最后一个单词，请返回 0 。
 * <p>
 * 说明：一个单词是指由字母组成，但不包含任何空格的字符串。
 * <p>
 * 示例:
 * <p>
 * 输入: "Hello World"
 * 输出: 5
 */

public class Solution58 {
	public static void main(String[] args) {
		Solution58 s = new Solution58();
		int res = s.lengthOfLastWord("Hello");
		System.out.println(res);
	}

	public int lengthOfLastWord(String s) {
		char[] chars = s.toCharArray();
		int len = s.length();
		int idx = 0;
		int count = 0;
		for (int i = len - 1; i >= 0; i--) {
			if (!Objects.equals(' ', chars[i])) {
				idx = i;
				while (true) {
					if (Objects.equals(' ', chars[idx--])) {
						return count;
					} else {
						count++;
					}
					if (idx < 0) {
						return count;
					}
				}
			}
		}
		return count;
	}

	//最佳解答,和上面相差不大
	public int lengthOfLastWord1(String s) {
		int res = 0;
		int count = 0;
		int len = s.length();
		if (len == 0) {
			return 0;
		}
		for (int i = len - 1; i >= 0; i--) {
			if (s.charAt(i) == ' ') {
				count++;
			} else {
				break;
			}
		}
		len = len - count;
		for (int i = len - 1; i >= 0; i--) {
			if (s.charAt(i) != ' ') {
				res++;
			} else {
				return res;
			}
		}
		return res;
	}
}
