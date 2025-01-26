import math

n, k = map(int, input().split())
ar = [0] + list(map(int, input().split()))
l = 0
for i in range(1, k + 1):
    for j in range(ar[i - 1] + 1, ar[i]):
        l += math.comb(n - j, k - i)
print(l)
