# n = int(input())
#
# a = []
#
# for _ in range(n):
#     a.append(list(map(int, input().split())))
# s = 0
# while n > 0:
#     if a[-n][0] < a[-n][1] and a[-n][0] < a[-n][2] or n == 1:
#         s += a[-n][0]
#         n -= 1
#     elif a[-n][1] < a[-n][0] and a[-n][1] < a[-n][2] and n > 1:
#         s += a[-n][1]
#         n -= 2
#     else:
#         s += a[-n][2]
#         n -= 3
#
# print(s)

n = int(input())
a = []

for _ in range(n):
    a.append(list(map(int, input().split())))


dp = [10**9] * (n + 1)
dp[0] = 0

for i in range(n):
    dp[i + 1] = min(dp[i + 1], dp[i] + a[i][0])
    if i + 2 <= n:
        dp[i + 2] = min(dp[i + 2], dp[i] + a[i][1])
    if i + 3 <= n:
        dp[i + 3] = min(dp[i + 3], dp[i] + a[i][2])


print(dp[n])
