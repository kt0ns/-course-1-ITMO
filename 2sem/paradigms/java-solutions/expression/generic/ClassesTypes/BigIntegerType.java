package expression.generic.ClassesTypes;

import java.math.BigInteger;

public class BigIntegerType implements TypeForCalculate<BigInteger> {
    public BigIntegerType() {
    }

    @Override
    public BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger subtract(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public BigInteger multiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public BigInteger divide(BigInteger left, BigInteger right) {
        return left.divide(right);
    }

    @Override
    public BigInteger area(BigInteger left, BigInteger right) {
        return left.multiply(right).divide(BigInteger.TWO);
    }

    @Override
    public BigInteger perimeter(BigInteger left, BigInteger right) {
        return left.add(right).multiply(BigInteger.TWO);
    }

    @Override
    public BigInteger getValue(String val) {
        return BigInteger.valueOf(Long.parseLong(val));
    }

    @Override
    public BigInteger negate(BigInteger left, BigInteger right) {
        return BigInteger.ZERO.subtract(left);
    }
}
