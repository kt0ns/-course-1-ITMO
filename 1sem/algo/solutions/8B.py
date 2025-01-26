n, k = map(int, input().split())
dp = [1] * n

s = dp[0]

for i in range(1, n):
    dp[i] = s
    if i >= k:
        s -= dp[i - k]
    s += dp[i]

print(dp[n - 1] % (10**9 + 7))

