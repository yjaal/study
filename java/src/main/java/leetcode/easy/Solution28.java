package leetcode.easy;

import java.util.Objects;

/**
 * 28. 实现strStr()
 * 实现 strStr() 函数。
 * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
 * <p>
 * 示例 1:
 * 输入: haystack = "hello", needle = "ll"
 * 输出: 2
 * <p>
 * 示例 2:
 * 输入: haystack = "aaaaa", needle = "bba"
 * 输出: -1
 * <p>
 * 说明:
 * 当 needle 是空字符串时，我们应当返回什么值呢？这是一个在面试中很好的问题。
 * 对于本题而言，当 needle 是空字符串时我们应当返回 0 。这与C语言的 strstr() 以及 Java的 indexOf() 定义相符。
 */
public class Solution28 {
	public static void main(String[] args) {
		Solution28 s = new Solution28();
		String haystack = "mississippi";
		String needle = "issip";
		int res = s.strStr(haystack, needle);
		System.out.println(res);
	}

	//这种方式时间复杂度较高
	public int strStr(String haystack, String needle) {
		if (Objects.equals(null, haystack) || Objects.equals(null, needle)
				|| haystack.length() < needle.length()) {
			return -1;
		}
		if (Objects.equals("", haystack) || Objects.equals("", needle)) {
			return 0;
		}
		int i = 0;
		while (i < haystack.length()) {
			int j = 0;
			if (!Objects.equals(haystack.charAt(i), needle.charAt(j))) {
				i++;
				continue;
			}
			while (i < haystack.length() && j < needle.length() &&
					Objects.equals(haystack.charAt(i), needle.charAt(j))) {
				i++;
				j++;
			}
			if (j == needle.length()) {
				return i - j;
			}
			//如果上面条件不满足，则从i--处开始找
			i--;
		}
		return -1;
	}

	//其实和上面类似
	int strStr1(String haystack, String needle) {
		if (needle.isEmpty()) return 0;
		int m = haystack.length(), n = needle.length();
		if (m < n) return -1;
		for (int i = 0; i <= m - n; ++i) {
			int j = 0;
			for (j = 0; j < n; ++j) {
				if (!Objects.equals(haystack.charAt(i + j), needle.charAt(j))) break;
			}
			if (j == n) return i;
		}
		return -1;
	}

	//这是最佳答案，可以看看indexOf的实现方式
	public static int strStr2(String haystack, String needle) {
		return haystack.indexOf(needle);
	}
}
