n = int(input())
data = sorted(list(map(int, input().split())))

l, cnt = 0, 0

for r in range(n):
    while data[r] - data[l] > 5:
        l += 1
    cnt = max(cnt, r - l + 1)
print(cnt)
