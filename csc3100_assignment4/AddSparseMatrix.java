import java.io.*;
import java.util.*;

public class AddSparseMatrix {
	public static void main(String[] args) throws Exception {
		FileReader freader = new FileReader(args[0]);
		BufferedReader breader = new BufferedReader(freader);
		FileReader freader2 = new FileReader(args[1]);
		BufferedReader breader2 = new BufferedReader(freader2);
		FileWriter fwriter = new FileWriter(args[2], false);
		BufferedWriter bwriter = new BufferedWriter(fwriter);

		String str1 = breader.readLine();
		String str2 = breader2.readLine();
		bwriter.write(str1);
		bwriter.write("\r\n");

		str1 = breader.readLine();
		str2 = breader2.readLine();
		while (str1 != null && str2 != null) {
			HashMap<Integer, Integer> codeMap = new HashMap<Integer, Integer>();
			String[] arr1 = str1.split(" ");
			String[] arr2 = str2.split(" ");
			bwriter.write(arr1[0] + ' ');
			for (int i = 1; i < arr1.length; i++) {
				int index = arr1[i].indexOf(':');
				if (index == 0)
					break;
				Integer str = Integer.parseInt(arr1[i].split(":")[0]);
				Integer b = Integer.parseInt(arr1[i].split(":")[1]);
				codeMap.put(str, b);
			}

			for (int j = 1; j < arr2.length; j++) {
				int index = arr2[j].indexOf(':');
				if (index == 0)
					break;
				Integer str = Integer.parseInt(arr2[j].split(":")[0]);
				Integer c = Integer.parseInt(arr2[j].split(":")[1]);
				if (codeMap.containsKey(str)) {
					codeMap.compute(str, (key, value) -> value + c);
				} else {
					codeMap.put(str, c);
				}
			}

			Object[] array = codeMap.keySet().toArray();
			Arrays.sort(array);

			if (array.length == 0) {
				bwriter.write(":");
			}else {
			int num = 0;
			for (int p = 0; p < array.length; p++) {
				if (codeMap.get(array[p])==0) {
					continue;
				}
				bwriter.write(String.valueOf(array[p]) + ':' + String.valueOf(codeMap.get(array[p])) + ' ');
				num++;
			}
			if (num==0) {
				bwriter.write(":");
			}
			}
			str1 = breader.readLine();
			str2 = breader2.readLine();
			bwriter.write("\r\n");
		}

		freader.close();
		breader.close();
		freader2.close();
		breader2.close();
		bwriter.flush();
		bwriter.close();
		return;
	}

}