def C(n, c):
    if c[n] == -1:
        if n == 0:
            c[n] = 1
        else:
            c[n] = C(n - 1, c) * (4 * n - 2) // (n + 1)
    return c[n]


s = input().strip()
n = len(s)
c = [-1] * n

dp = [[0] * (n + 1) for _ in range(n)]
dp[0][0] = 1
for i in range(1, n):
    if i % 2 == 0:
        dp[i][0] = C(i // 2, c)
    for j in range(1, n):
        dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j + 1]

k = 0
bal = 0
balS = 0
opened = []

for i in range(n):
    cur = s[i]
    if cur == '(':
        bal += 1
        balS += 1
        opened.append(1)
    elif cur == ')':
        if balS + 1 <= n - i - 1:
            k += dp[n - i - 1][bal + 1] * (2 ** ((n - i - bal - 2) // 2))
        bal -= 1
        balS -= 1
        opened.pop()
    elif cur == '[':
        if balS + 1 <= n - i - 1:
            k += dp[n - i - 1][bal + 1] * (2 ** ((n - i - bal - 2) // 2))
        if balS > 0 and opened[-1] == 1:
            k += dp[n - i - 1][bal - 1] * (2 ** ((n - i - bal) // 2))
        bal += 1
        opened.append(2)
    elif cur == ']':
        if balS + 1 <= n - i - 1:
            k += dp[n - i - 1][bal + 1] * (2 ** ((n - i - bal - 2) // 2))
        if balS > 0 and opened[-1] == 1:
            k += dp[n - i - 1][bal - 1] * (2 ** ((n - i - bal) // 2))
        if bal - balS + 1 <= n - i - 1:
            k += dp[n - i - 1][bal + 1] * (2 ** ((n - i - bal - 2) // 2))
        bal -= 1
        opened.pop()

print(k)

