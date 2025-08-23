package expression.generic;

import expression.generic.ClassesTypes.*;
import expression.generic.utilities.GenericExpression;

import java.math.BigInteger;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(
            String mode, String expression,
            int x1, int x2, int y1, int y2, int z1, int z2)
            throws Exception {
        Object[][][] table = new Object[Math.abs(x2 - x1) + 1][Math.abs(y2 - y1) + 1][Math.abs(z2 - z1) + 1];

        GenericExpression<? extends Number> parsedExpression;
//        System.err.println(expression);
        switch (mode) {
            case "i":
                parsedExpression = new ParserGenerics<Integer>().parse(
                        expression, new IntegerType());
                break;
            case "d":
                parsedExpression = new ParserGenerics<Double>().parse(
                        expression, new DoubleType());
                break;
            case "bi":
                parsedExpression = new ParserGenerics<BigInteger>().parse(
                        expression, new BigIntegerType());
                break;
            default:
                return null;
        }
        for (int x = 0; x < Math.abs(x2 - x1) + 1; x++) {
            for (int y = 0; y < Math.abs(y2 - y1) + 1; y++) {
                for (int z = 0; z < Math.abs(z2- z1) + 1; z++) {
                    Object num;
                    try {
                        num = parsedExpression.evaluate(x1 + x, y1 + y, z1 + z);
                    } catch (ArithmeticException e) {
                        num = null;
                    }
                    table[x][y][z] = num;
                }
            }

        }

        return table;
    }
}
