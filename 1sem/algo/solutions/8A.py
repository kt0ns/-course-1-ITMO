n = int(input())
s = input()

a = []

for i in s:
    if i == 'w':
        a.append(-1)
    if i == '.':
        a.append(0)
    if i == '"':
        a.append(1)

dp = [-1] * n
dp[0] = 0

for i in range(1, n):
    if a[i] != -1:
        if dp[i-1] != -1:
            dp[i] = dp[i - 1] + a[i]
        if i - 3 >= 0 and dp[i - 3] != -1:
            if dp[i-3] != -1:
                dp[i] = max(dp[i], dp[i - 3] + a[i])
        if i - 5 >= 0 and dp[i - 5] != -1:
            dp[i] = max(dp[i], dp[i - 5] + a[i])

print(dp[n - 1])
