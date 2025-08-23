package expression.generic.ClassesTypes;

public class DoubleType implements TypeForCalculate<Double> {
    public DoubleType() {
    }

    @Override
    public Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double multiply(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double divide(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double area(Double left, Double right) {
        return left * (right / 2);
    }

    @Override
    public Double perimeter(Double left, Double right) {
        return (left + right) * 2;
    }

    @Override
    public Double getValue(String val) {
        return Double.parseDouble(val);
    }

    @Override
    public Double negate(Double left, Double right) {
        return -left;
    }
}
