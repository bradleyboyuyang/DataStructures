public static double eval(final String str) {
    return new Object() {
        int pos = -1, ch;
        void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }
        boolean wait(int charToWait) {
            while (ch == ' ') nextChar();
            if (ch == charToWait) {
                nextChar();
                return true;
            }
            return false;
        }
        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
            return x;
        }
        double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if      (wait('+')) x += parseTerm(); // addition
                else if (wait('-')) x -= parseTerm(); // subtraction
                else return x;
            }
        }
        double parseTerm() {
            double x = parseFactor();
            for (;;) {
                if      (wait('*')) x *= parseFactor(); // multiplication
                else if (wait('/')) x /= parseFactor(); // division
                else return x;
            }
        }
        double parseFactor() {
            if (wait('+')) return parseFactor(); // unary plus
            if (wait('-')) return -parseFactor(); // unary minus
            double x;
            int startPos = this.pos;
            if (wait('(')) { // parentheses
                x = parseExpression();
                wait(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(str.substring(startPos, this.pos));
            } else if (ch >= 'a' && ch <= 'z') { // functions
                while (ch >= 'a' && ch <= 'z') nextChar();
                String func = str.substring(startPos, this.pos);
                x = parseFactor();
                if (func.equals("sqrt")) x = Math.sqrt(x);
                else if (func.equals("sin")) x = Math.sin(x);
                else if (func.equals("cos")) x = Math.cos(x);
                else if (func.equals("tan")) x = Math.tan(x);
                else throw new RuntimeException("Unknown function: " + func);
            } else {
                throw new RuntimeException("Unexpected: " + (char)ch);
            }
            if (wait('^')) x = Math.pow(x, parseFactor()); // exponentiation
            return x;
        }
    }.parse();
}

// Grammar:
// expression = term | expression `+` term | expression `-` term
// term = factor | term `*` factor | term `/` factor
// factor = `+` factor | `-` factor | `(` expression `)`
//        | number | functionName factor | factor `^` factor