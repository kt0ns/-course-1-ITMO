n, k = map(int, input().split())

dp = [0] * n
dp[0] = 1
s = 0
for i in range(1, n):
    if i - k >= 0:
        s -= dp[i - k - 1]
    s += dp[i - 1]
    dp[i] = s

print(dp[n - 1] % (10 ** 9 + 7))
