package leetcode.sort.stack;

import java.util.Stack;

/**
 * 71. 简化路径（无太大价值）
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 以 Unix 风格给出一个文件的绝对路径，你需要简化它。或者换句话说，将其转换为规范路径。
 * 在 Unix 风格的文件系统中，一个点（.）表示当前目录本身；此外，两个点 （..） 
 * 表示将目录切换到上一级（指向父目录）；两者都可以是复杂相对路径的组成部分。
 * 更多信息请参阅：Linux / Unix中的绝对路径 vs 相对路径
 * <p>
 * 请注意，返回的规范路径必须始终以斜杠 / 开头，并且两个目录名之间必须只有一个斜杠 /。
 * 最后一个目录名（如果存在）不能以 / 结尾。此外，规范路径必须是表示绝对路径的最短字符串。
 * <p>
 * 示例 1：
 * 输入："/home/"
 * 输出："/home"
 * 解释：注意，最后一个目录名后面没有斜杠。
 * <p>
 * 示例 2：
 * 输入："/../"
 * 输出："/"
 * 解释：从根目录向上一级是不可行的，因为根是你可以到达的最高级。
 * <p>
 * 示例 3：
 * 输入："/home//foo/"
 * 输出："/home/foo"
 * 解释：在规范路径中，多个连续斜杠需要用一个斜杠替换。
 * <p>
 * 示例 4：
 * 输入："/a/./b/../../c/"
 * 输出："/c"
 * <p>
 * 示例 5：
 * 输入："/a/../../b/../c//.//"
 * 输出："/c"
 * <p>
 * 示例 6：
 * 输入："/a//b////c/d//././/.."
 * 输出："/a/b/c"
 */
public class Solution71 {

	public static void main(String[] args) {
		Solution71 s = new Solution71();
		String path = "/a//b////c/d//././/..";
		System.out.println(s.simplifyPath(path));
	}

	/**
	 * 使用栈解决
	 */
	public String simplifyPath(String path) {
		String[] paths = path.split("/");
		Stack<String> stack = new Stack<>();
		for (String path1 : paths) {
			if ("".equals(path1) || ".".equals(path1)) {
				continue;
			}
			if (!stack.isEmpty()) {
				if ("..".equals(path1)) {
					stack.pop();
					continue;
				}
				stack.push(path1);
			} else {
				if ("..".equals(path1)) {
					continue;
				}
				stack.push(path1);
			}
		}
		if (stack.isEmpty()) {
			return "/";
		}
		StringBuilder sb = new StringBuilder("/");
		Stack<String> stack1 = new Stack();
		while (!stack.isEmpty()) {
			stack1.push(stack.pop());
		}
		while (!stack1.isEmpty()) {
			sb.append(stack1.pop());
			sb.append("/");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}
}
