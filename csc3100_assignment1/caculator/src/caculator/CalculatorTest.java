package caculator;

public class CalculatorTest {
//  a·ûºÅ  a·ûºÅB·ûºÅ  3533 - (96 + 896)
	// 125*6+56*8   750+56*8
	public static void main(String[] args) {
//		String result = "125/3*98/3+69/32*123-89/3";
//		String result = "-1.5/2-1*3/3";
//		String result = "1.2-3.5*5.2-13.7";
//		String result = "(-3+4)/2.5+3.9";
//		String result = "-3+4/ (2.5+3.7)";
//		String result = "(-3+4)/(2.5+3.9)";
//		String result = "(((-3+4/ (2.5+3.7))))";
//		String result = " (((  -   sqrt( cos(+  3*   ( -  tan(- 3.5 ))- 2  / 3* 3.5/ 4) )-       sin(2-  38.9 *  12/(-13 + 5 ) -(2+ cos(3  ) )  ) ))) ";
//		String result = "  14/3*2 ";
		String result = "-(-cos(2)*sqrt(-cos(-  3.5)))";
//		String result = "   -   sqrt( cos(+  3*   ( -  tan(- 3.5 ))- 2  / 3* 3.5/ 4) )";
		System.out.println(Calculator.parse(result));
		System.out.println(Math.round(Calculator.parse(result)));
		System.out.println("-----------------");
//		System.out.println(Math.round(1+2.0*Math.sin(37+(25*3))));
//		System.out.println(Math.round((2+ 3.50)*4*Math.sqrt(Math.sin(1.5))));
//		System.out.println(Math.round(-3+4/ (2.5+3.7)));
//		System.out.println(Math.round((-3+4)/2.5+3.9));
//		System.out.println((((-3+4/ (2.5+3.7)))));
//		System.out.println(Math.round(1.2-3.5f*5.2f-13.2));
		System.out.println(-(-Math.tan(-2)*Math.sqrt(-Math.cos(3.5))));
//		System.out.println(Math.round(2.3f*5f*7f -12*9/8f));
//		System.out.println(Math.round(-Math.sin(3.5-Math.sqrt(4)) + Math.cos(Math.tan(2.5))));
		System.out.println(   -   Math.sqrt( Math.cos(+  3*   (   -  Math.tan(- 3.5 ))- 2f  / 3f*3.5/ 4f) )-       Math.sin(2-  38.9 *  12/(-13 + 5 ) -(2+ Math.cos(3  ) )  ));
	}

}
