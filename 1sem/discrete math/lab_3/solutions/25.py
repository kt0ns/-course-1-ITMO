n, k = map(int, input().split())
a = list(map(int, input().split()))

a += [n + 1]
for i in range(k - 1, -1, -1):
    if a[i] < a[i+1] - 1:
        a[i] += 1
        print(*list(a[:i+1] + [x for x in range(a[i]+1, a[i]+k)])[:k])
        break
else:
    print(-1)

