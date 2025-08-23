package expression.generic.ClassesTypes;

public class IntegerType implements TypeForCalculate<Integer> {

    public IntegerType() {
    }

    @Override
    public Integer add(Integer left, Integer right) throws ArithmeticException {
        if (checkAdd(left, right)) {
            throw new ArithmeticException("Overflow expression for type");
        }
        return left + right;
    }

    @Override
    public Integer subtract(Integer left, Integer right) throws ArithmeticException {
        if (checkSubtract(left, right)) {
            throw new ArithmeticException("Overflow expression for type");
        }
        return left - right;
    }

    @Override
    public Integer multiply(Integer left, Integer right) throws ArithmeticException {
        if (checkMultiply(left, right)) {
            throw new ArithmeticException("Overflow expression for type");
        }
        return left * right;
    }

    @Override
    public Integer divide(Integer left, Integer right) throws ArithmeticException {
        if (checkDivide(left, right)) {
            throw new ArithmeticException("Overflow expression for type");
        }

        return left / right;
    }

    @Override
    public Integer area(Integer left, Integer right) throws ArithmeticException {
        if (checkArea(left, right)) {
            throw new ArithmeticException("Overflow expression for type");
        }

        return (right % 2 == 0) ? left * (right / 2) : left * (right / 2) + left / 2;
    }

    @Override
    public Integer perimeter(Integer left, Integer right) throws ArithmeticException {
        if (checkPerimeter(left, right)) {
            throw new ArithmeticException("Overflow expression for type");
        }

        return (left + right) * 2;
    }

    @Override
    public Integer getValue(String val) {
        return Integer.valueOf(val);
    }

    @Override
    public Integer negate(Integer left, Integer right) {
        if (checkNegate(left)) {
            throw new ArithmeticException("Overflow expression for type");
        }

        return -left;
    }

    private boolean checkAdd(Integer left, Integer right) {
        return (left < 0 && right < Integer.MIN_VALUE - left
                || left > 0 && right > Integer.MAX_VALUE - left);
    }

    private boolean checkSubtract(Integer left, Integer right) {
        return (left >= 0 && right <= Integer.MIN_VALUE + left) ||
                (left < 0 && right > Integer.MAX_VALUE + left + 1);
    }

    private boolean checkMultiply(Integer left, Integer right) {
        if (left != 0) {
            int lg = left == -1 ? Integer.MAX_VALUE : Integer.MIN_VALUE / left;
            return left > 0
                    && (lg > right || right > Integer.MAX_VALUE / left)
                    || left < 0
                    && (Integer.MAX_VALUE / left > right || right > lg);
        }
        return false;
    }

    private boolean checkDivide(Integer left, Integer right) {
        return right == 0;
    }

    private boolean checkArea(Integer left, Integer right) {
        return left < 0 || right < 0
                || (right % 2 == 0 && checkMultiply(left, right / 2)
                || (right % 2 != 0 && (checkMultiply(left, right / 2)
                || checkAdd(left * (right / 2), left / 2))));
    }

    private boolean checkPerimeter(Integer left, Integer right) {
        return left < 0 || right < 0
                || left > (Integer.MAX_VALUE / 2 - right);
    }

    private boolean checkNegate(Integer val) {
        return val == Integer.MIN_VALUE;
    }
}