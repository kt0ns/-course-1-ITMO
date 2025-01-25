n, m, p = map(int, input().split())
data = sorted(list(map(int, input().split())))

cnt = 0
i = n - 1
while m > 0 and i >= 0:
    buckets = data[i] // p
    if m > buckets:
        med = buckets * p
        m -= buckets
    else:
        med = buckets * m
        m = 0
    cnt += med
    data[i] %= p
    i -= 1

if m > 0:
    data = sorted([x for x in data if x < p and x != 0])
    i = len(data) - 1
    while m > 0 and i >= 0:
        cnt += data[i]
        m -= 1
        i -= 1

print(cnt)
