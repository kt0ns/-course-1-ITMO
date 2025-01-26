n, m = map(int, input().split())

a = []

for _ in range(n):
    a.append(list(map(int, input().split())))

for i in range(1, m):
    a[0][i] += a[0][i - 1]
for i in range(1, n):
    a[i][0] += a[i - 1][0]

for i in range(1, n):
    for j in range(1, m):
        if a[i - 1][j] > a[i][j - 1]:
            a[i][j] = a[i - 1][j] + a[i][j]
        else:
            a[i][j] = a[i][j - 1] + a[i][j]

i = n - 1
j = m - 1
answ = []

for _ in range(n + m - 2):
    if i > 0 and (j == 0 or a[i - 1][j] > a[i][j - 1]):
        answ.append("D")
        i -= 1
    else:
        answ.append("R")
        if j > 0:
            j -= 1

print(a[n - 1][m - 1])
print(''.join(answ[::-1]))
