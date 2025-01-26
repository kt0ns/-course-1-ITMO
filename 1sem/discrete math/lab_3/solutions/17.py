n, k = map(int, input().split())

balance = [[0] * (n + 1) for x in range(2 * n + 1)]
balance[0][0] = 1

for i in range(1, 2 * n + 1):
    balance[i][0] = balance[i - 1][1]
    for j in range(1, n):
        balance[i][j] = balance[i - 1][j - 1] + balance[i - 1][j + 1]
    balance[i][n] = balance[i - 1][n - 1]

ans = ""
depth = 0
for i in range(2 * n):
    if depth < n and balance[2 * n - i - 1][depth + 1] > k:
        ans += '('
        depth += 1
    else:
        ans += ')'
        if depth < n:
            k -= balance[2 * n - i - 1][depth + 1]
        depth -= 1

print(''.join(ans))
