from fractions import Fraction
import string

n = int(input())
s = input()
print(n)

total_length = len(s)

letter_counts = {letter: 0 for letter in string.ascii_lowercase[:n]}

for letter in s:
    if letter in letter_counts:
        letter_counts[letter] += 1

letters = sorted(letter_counts.keys())
proportions = [Fraction(count, total_length) for count in letter_counts.values()]

sorted_counts = [letter_counts[letter] for letter in letters]
print(" ".join(map(str, sorted_counts)))

def recursion_down(l, r, i):
    nmb = letters.index(s[i])
    ratio = proportions[nmb]
    new_l = l + (r - l) * sum(proportions[:nmb])
    new_r = new_l + (r - l) * ratio

    i += 1
    if i == len(s):
        return new_l, new_r
    return recursion_down(new_l, new_r, i)

a1, a2 = recursion_down(Fraction(0), Fraction(1), 0)

def find_P_Q():
    for q in range(1, 2**13):
        mx = 2 ** q
        left, right = 0, mx - 1
        while left <= right:
            mid = (left + right) // 2
            value = Fraction(mid, mx)
            if a1 <= value < a2:
                return q, mid
            elif value < a1:
                left = mid + 1
            else:
                right = mid - 1

q, p = find_P_Q()
p = bin(p)[2:]
p = p.zfill(q)
print(p)
