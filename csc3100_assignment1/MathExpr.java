import java.util.*;

public class MathExpr {
	public static double parse(String p) {
		try {
			// check for possible spaces
			p = p.trim(); 
			p = p.replaceAll(" +", " ");
			for (int j=1; j < p.length()-1; j++) {
				if (' '==p.charAt(j)) {
					if ((Character.isDigit(p.charAt(j-1))||'.'==p.charAt(j-1)) && (Character.isDigit(p.charAt(j+1))||'.'==p.charAt(j+1))) {
						return Double.NaN;		
					}
				}
			}
			p = p.replaceAll(" ", "");
			return count(p);
		}
		catch (Exception e) {
			return Double.NaN; // catch an invalid expression
		}
	}

	public static double count(String p) {
		Double one = null;
		String symbol = null;
		int symbolIndex = 0;
		Double two = null;
		String symbol2 = null;	
		Stack <Integer> stk = new Stack<>();
		int num = 0;
		byte[] bytes = p.getBytes();	
		
		for (int i=0; i < bytes.length; i++) {
			if ('('==(char)bytes[i]) {
				num++;
				stk.push(i);
			}
			if (')'==(char)bytes[i]) {
				int left = stk.pop();
				num = num-1;
				if (left != 0 && 's' == (char)bytes[left-1]) 
					return count(new StringBuilder(p).replace(left-3, i+1, String.valueOf(math3(count(p.substring(left+1, i)),"cos"))).toString());
				
				if (left != 0 && 't' == (char)bytes[left-1]) 
					return count(new StringBuilder(p).replace(left-4, i+1, String.valueOf(math3(count(p.substring(left+1, i)),"sqrt"))).toString());
				
				if (left != 0 && 'n' == (char)bytes[left-1]) {
					if ('i' == (char)bytes[left-2]) {
						return count(new StringBuilder(p).replace(left-3, i+1, String.valueOf(math3(count(p.substring(left+1, i)),"sin"))).toString());	
					}else {
						return count(new StringBuilder(p).replace(left-3, i+1, String.valueOf(math3(count(p.substring(left+1, i)),"tan"))).toString());
					}
				}
				return count(new StringBuilder(p).replace(left, i+1, String.valueOf(count(p.substring(left+1, i)))).toString());
			}
		}
		
		if (num != 0)
			return Double.NaN;
		
		if ('+'==(char)bytes[0]) {
			bytes = p.substring(1).getBytes();
		}
		if ('-'==(char)bytes[0] && '-'==(char)bytes[1]) {
			bytes = p.substring(2).getBytes();
		}
		
		for(int x =0;x < bytes.length ;x++) {
			if (x==0 && ('+'==(char)bytes[x]||'-'==(char)bytes[x])) {
				continue;
			}	
			if('+'==(char)bytes[x] || '-'==(char)bytes[x]
					|| '*' == (char)bytes[x] || '/' == (char)bytes[x])
			{
				if (x != 0 && ('-'==(char)bytes[x]|| '+'==(char)bytes[x]) && ('*'==(char)bytes[x-1] 
						|| '/'==(char)bytes[x-1] || '+'==(char)bytes[x-1]
								|| '-'==(char)bytes[x-1]))
					continue;
				if(one == null) {
					one = Double.parseDouble(new String(bytes,0,x));
					symbol = String.valueOf((char)bytes[x]);
					symbolIndex = x ;
				}else if(two == null) {
					two = Double.parseDouble(new String(bytes,symbolIndex+1,x-symbolIndex-1));
					symbol2 = String.valueOf((char)bytes[x]);
					symbolIndex=x;
				}else {
					if("*".equals(symbol) || "/".equals(symbol)) {
						one = math(one,symbol,two);
						symbol = symbol2;
						two = Double.parseDouble(new String(bytes,symbolIndex+1,x-symbolIndex-1));
					}else if("*".equals(symbol2) || "/".equals(symbol2)) {
						Double three = Double.parseDouble(new String(bytes,symbolIndex+1,x-symbolIndex-1));
						two = math(two,symbol2,three);
					}else {
						one = math(one,symbol,two);
						symbol = symbol2;
						two = Double.parseDouble(new String(bytes,symbolIndex+1,x-symbolIndex-1));
					}
					symbol2=String.valueOf((char)bytes[x]);
					symbolIndex = x ;
				}
			}
		}
		if(symbol==null) {
			if (p.contains("+")){
				p = p.replaceAll("\\+", "");
			}
			if ('-'==p.charAt(0) && '-'==p.charAt(1)) {
				return Double.parseDouble(p.substring(2));
			}
			return Double.parseDouble(p);
		}
		
		if(two == null) {
			return math(one,symbol,Double.parseDouble(new String(bytes,symbolIndex+1,bytes.length-symbolIndex-1)));
		}
		return math2(one,symbol,two,symbol2,Double.parseDouble(new String(bytes,symbolIndex+1,bytes.length-symbolIndex-1)));
	}

	private static double math(double a, String symbol, double b) {
		if ("+".equals(symbol)) {
			return a + b;
		} else if ("-".equals(symbol)) {
			return a - b;
		} else if ("*".equals(symbol)) {
			return a * b;
		} else {
			if (b == 0) {
				return Double.NaN;
			} else
				return a / b;
		}
	}

	private static double math2(double a, String sy1, double b, String sy2, double c) {
		if ("+".equals(sy1)) {
			return a + math(b, sy2, c);
		} else if ("-".equals(sy1)) {
			if ("+".equals(sy2)) {
				return a - b + c;
			} else if ("-".equals(sy2)) {
				return a - b - c;
			} else {
				return a - math(b, sy2, c);
			}
		} else {
			return math(math(a, sy1, b), sy2, c);
		}
	}

	private static double math3(double a, String symbol) {
		if ("sqrt".equals(symbol)) {
			return Math.sqrt(a);
		} else if ("sin".equals(symbol)) {
			return Math.sin(a);
		} else if ("cos".equals(symbol)) {
			return Math.cos(a);
		} else {
			return Math.tan(a);
		}
	}
}