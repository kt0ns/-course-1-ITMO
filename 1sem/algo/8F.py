n = int(input())
a = list(map(int, input().split()))

dp = [1] * n
prev = [-1] * n
for i in range(1, n):
    for j in range(i):
        if a[i] > a[j] and dp[i] < dp[j] + 1:
            dp[i] = dp[j] + 1
            prev[i] = j

last_ind = 0
for i in range(1, n):
    if dp[i] > dp[last_ind]:
        last_ind = i

sequence = []
while last_ind != -1:
    sequence.append(a[last_ind])
    last_ind = prev[last_ind]

sequence.reverse()

print(max(dp))
print(*sequence)
