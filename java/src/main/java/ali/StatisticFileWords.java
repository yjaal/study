package ali;

import java.io.*;
import java.util.*;

/**
 * 统计给定目录下所有txt文档内的单词按出现频次倒序输出，包括出现次数
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class StatisticFileWords {

	private Map<String, Integer> map = new HashMap<>();
	private Comparator<Map.Entry<String, Integer>> comparator = (o1, o2) -> o2.getValue() - o1.getValue();
	private List<File> files = new ArrayList<>();

	public List<Map.Entry<String, Integer>> solution(String fileName) {
		getAllFiles(fileName);
		files.forEach(file -> {
			try {
				solution(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		List<Map.Entry<String, Integer>> res = new ArrayList<>(map.entrySet());
		res.sort(comparator);
		return res;
	}

	private void solution(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] strs = line.split(" ");
			for (String str : strs) {
				map.merge(str, 1, (a, b) -> a + b);
			}
		}
	}

	private void getAllFiles(String fileName) {
		File file = new File(fileName);
		if (!file.isDirectory() && file.getName().endsWith(".txt")) {
			files.add(file);
		}
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			Arrays.asList(fs).forEach(f -> getAllFiles(f.getAbsolutePath()));
		}
	}
}
