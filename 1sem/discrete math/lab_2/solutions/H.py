from fractions import Fraction

n = int(input())
letter_counts = list(map(int, input().split()))
string_code = input()
len_string = sum(letter_counts)

p = int(string_code, 2)
q = 2 ** len(string_code)
drob = Fraction(p, q)

letters = sorted("qwertyuiopasdfghjklzxcvbnm")[:n]
proportions = [Fraction(count, len_string) for count in letter_counts]

prop_for_letters = [0]
for p in proportions:
    prop_for_letters.append(prop_for_letters[-1] + p)


def find_string(drob, string, i):
    if i != len_string:
        for j in range(n):
            left = prop_for_letters[j]
            right = prop_for_letters[j + 1]
            if left <= drob < right:
                string.append(letters[j])
                drob = Fraction(drob - left, right - left)
                return find_string(drob, string, i + 1)
    return string


print(''.join(find_string(drob, [], 0)))
