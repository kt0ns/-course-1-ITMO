import math
n = int(input())

a = list(map(int, input().split()))

sum = 1
for i in range(n):
    c = 0
    for j in range(i + 1, n):
        if a[i] > a[j]:
            c += 1
    sum += math.factorial(n - i - 1) * c

print(sum)