import math

n, k, m = map(int, input().split())
num = []
# m -= 1
next = 1
while k > 0:
    if m < math.comb(n - 1, k - 1):
        num.append(next)
        k -= 1
    else:
        m -= math.comb(n - 1, k - 1)
    n -= 1
    next += 1
print(*num)
