n, k = map(int, input().split())
a = list(map(int, input().split()))


pref = [0]
for i in range(n):
    pref.append(a[i] + pref[-1])

mx_len = 10**12
answ = [0, 0]
l = 0

for r in range(1, n + 1):
    while pref[r] - pref[l] >= k:
        l += 1
    if pref[r] - pref[l-1] == k and r - l < mx_len:
        answ = [l, r]
        mx_len = r - l


if mx_len != 10**10:
    print(*answ)
else:
    print(-1)
