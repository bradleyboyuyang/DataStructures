import java.io.*;
import java.util.*;

public class Puzzle8 {
	public class Node {
		private Board board;
		private Node previous;
		private int g;
		private int h;

		public Node(Board b, Node prev, int m) {
			board = new Board(b.getBlock());
			previous = prev;
			g = m;
			h = h();
		}

		public Board getBoard() {
			return board;
		}

		public Node getPrevious() {
			return previous;
		}

		public int f() {
			return g + h;
		}

		public int g() {
			return g;
		}

		public int h() {
//			return Heuristic1();
			return Heuristic2();
		}
		
		// first choice of heuristic: number of misplaced tiles
		private int Heuristic1() {
			int misplace = 0;
			for (int i = 0; i < board.getSize(); i++)
				for (int j = 0; j < board.getSize(); j++) {
					if ((3 * i + j + 1 != 9) && (board.getBlock()[i][j] != 3 * i + j + 1))
						misplace++;
				}
			return misplace;
		}

		// second choice: total Manhattan distance
		private int Heuristic2() {
			int dist = 0;

			for (int i = 0; i < board.getSize(); i++)
				for (int j = 0; j < board.getSize(); j++) {
					int X = (board.getBlock()[i][j]);
					if (X != 0) {
						int targetX = (X-1)/3;
						int targetY = (X-1)%3;
						dist += Math.abs(i-targetX)+Math.abs(j-targetY);
					}
				}
			return dist;
		}

		public boolean checkGoal() {
			for (int i = 0; i < board.getSize(); i++)
				for (int j = 0; j < board.getSize(); j++) {
					if ((3 * i + j + 1 != 9) && (board.getBlock()[i][j] != 3 * i + j + 1))
						return false;
				}
			return true;
		}

		public Stack<Board> neighbors() {
			Stack<Board> stk_board = new Stack<Board>();
			int[][] neighbor;
			int zeroRow = board.getZeroRow();
			int zeroCol = board.getZeroCol();
			if (zeroRow - 1 >= 0) {
				neighbor = board.copy();
				neighbor[zeroRow][zeroCol] = neighbor[zeroRow - 1][zeroCol];
				neighbor[zeroRow - 1][zeroCol] = 0;
				stk_board.push(new Board(neighbor));
			}
			if (zeroRow + 1 < board.getSize()) {
				neighbor = board.copy();
				neighbor[zeroRow][zeroCol] = neighbor[zeroRow + 1][zeroCol];
				neighbor[zeroRow + 1][zeroCol] = 0;
				stk_board.push(new Board(neighbor));
			}
			if (zeroCol - 1 >= 0) {
				neighbor = board.copy();
				neighbor[zeroRow][zeroCol] = neighbor[zeroRow][zeroCol - 1];
				neighbor[zeroRow][zeroCol - 1] = 0;
				stk_board.push(new Board(neighbor));
			}
			if (zeroCol + 1 < board.getSize()) {
				neighbor = board.copy();
				neighbor[zeroRow][zeroCol] = neighbor[zeroRow][zeroCol + 1];
				neighbor[zeroRow][zeroCol + 1] = 0;
				stk_board.push(new Board(neighbor));
			}
			return stk_board;
		}
	}

	public static Node bestNode(List<Node> N0) {
		int indice = 0;
		int hmin = N0.get(indice).f();
		for (int i = 1; i < N0.size(); i++) {
			if (hmin >= N0.get(i).f()) {
				hmin = N0.get(i).f();
				indice = i;
			}
		}
		return N0.get(indice);
	}
	
	public class Board {
		private int block[][];
		private int size;
		private int zeroRow;
		private int zeroCol;

		public Board(int b[][]) {
			size = b.length;
			block = new int[size][size];
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++) {
					block[i][j] = b[i][j];
					if (b[i][j] == 0) {
						zeroRow = i;
						zeroCol = j;
					}
				}
		}

		public int[][] getBlock() {
			return block;
		}

		public int get(int i, int j) {
			return block[i][j];
		}

		public int getZeroRow() {
			return zeroRow;
		}

		public int getZeroCol() {
			return zeroCol;
		}

		public int getSize() {
			return size;
		}

		public int[][] copy() {
			Board cp = new Board(this.block);
			return cp.block;
		}

		public boolean equals(Board y) {
			if (y.size == size) {
				for (int i = 0; i < size; i++)
					for (int j = 0; j < size; j++) {
						if (block[i][j] != y.block[i][j])
							return false;
					}
				return true;
			}
			return false;
		}

		public int isExist(List<Node> l) {
			for (int i = 0; i < l.size(); i++) {
				Board b = l.get(i).getBoard();
				if (this.equals(b))
					return i;
			}
			return -1;
		}

		public String toString() {
			String res = "";
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					res += block[i][j];
					if (j != size - 1)
						res += ' ';
				}
				if (i < size - 1)
					res += "\r\n";
			}
			return res;
		}
	}

	public class Solver {
		private List<Node> List1;
		private List<Node> List2;

		public Solver(Board initial) {
			List1 = new ArrayList<Node>();
			List2 = new ArrayList<Node>();
			Node currentNode = new Node(initial, null, 0);
			List1.add(currentNode);
			while (!currentNode.checkGoal() && !List1.isEmpty()) {
				currentNode = bestNode(List1);
				List1.remove(currentNode);
				List2.add(currentNode);
				for (Board b : currentNode.neighbors())
					if (b.isExist(List2) == -1) {
						if (b.isExist(List1) != -1) {
							List1.add(b.isExist(List1), new Node(b, currentNode, currentNode.g() + 1));
						} else {
							List1.add(new Node(b, currentNode, currentNode.g() + 1));
						}
					}
			}
		}

		public List<Board> finalPath() {
			List<Board> res = new ArrayList<Board>();
//			for (int i = 0; i < List2.size(); i++)
//				res.add(List2.get(i).getBoard());
			Stack<Board> stk = new Stack<Board>();
			Node cur = List2.get(List2.size() - 1);
			while (cur != null) {
				stk.push(cur.getBoard());
				cur = cur.getPrevious();
			}
			while (!stk.isEmpty()) {
				res.add(stk.pop());
			}
			return res;
		}
	}

	public static void main(String[] args) throws Exception {
		FileReader freader = new FileReader(args[0]);
		BufferedReader breader = new BufferedReader(freader);
		String str = breader.readLine();
		int[][] block = new int[3][3];
		for (int i = 0; i < 3; i++) {
			String[] temp = str.split(" ");
			for (int j = 0; j < temp.length; j++) {
				block[i][j] = Integer.parseInt(temp[j]);
			}
			str = breader.readLine();
		}
		freader.close();
		breader.close();
		Board initial = new Puzzle8().new Board(block);
		Solver sol = new Puzzle8().new Solver(initial);
		List res = sol.finalPath();
		FileWriter fwriter = new FileWriter(args[1], false);
		BufferedWriter bwriter = new BufferedWriter(fwriter);
		if (res != null)
			for (int i = 0; i < res.size(); i++) {
				bwriter.write(res.get(i).toString());
				if (i < res.size() - 1) {
					bwriter.write("\r\n");
					bwriter.write("\r\n");
				}
			}
		bwriter.flush();
		bwriter.close();
	}
}
