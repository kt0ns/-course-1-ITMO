n, M = map(int, input().split())
a = list(map(int, input().split()))

dp = [[0 for _ in range(M + 1)] for _ in range(n + 1)]
dp[0][0] = 1

for i in range(1, n + 1):
    for j in range(M + 1):
        dp[i][j] = dp[i - 1][j]
        if j >= a[i - 1]:
            dp[i][j] = int(dp[i][j] or dp[i - 1][j - a[i - 1]])

for j in range(n + 1):
    if dp[j][M]:
        print(j)
        break
print(0)
