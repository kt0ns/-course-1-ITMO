def nand(a, b):

    return f"({a}|{b})"


def generate_carry_bit_formula(N):
    if N == 1:
        return nand(nand("A0", "B0"), nand("A0", "B0"))

    carry = generate_carry_bit_formula(N - 1)

    Ai = f"A{N - 1}"
    Bi = f"B{N - 1}"

    x_n12 = nand(Ai, Bi)
    Ai = nand(Ai, Ai)
    Bi = nand(Bi, Bi)
    x_n11 = nand(Ai, Bi)
    x_n1 = nand(carry, x_n11)
    next = nand(x_n1, x_n12)

    return next

N = int(input())

carry_formula = generate_carry_bit_formula(N)

print(carry_formula)

# ( ( ( x_n1 | x_n11 ) | x_n12 )