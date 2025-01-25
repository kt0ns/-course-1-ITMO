n = int(input())
a = list(map(int, input().split()))

pref = [0]
for i in range(n):
    pref.append(pref[-1] + a[i])

mx = -10 ** 9
l = r = 0
mn_pref = 0
ind = 0

for i in range(1, n + 1):
    pref_s = pref[i] - mn_pref
    if pref_s > mx or (pref_s == mx and i - ind > r - l):
        mx = pref_s
        l = ind + 1
        r = i
    if pref[i] < mn_pref:
        mn_pref = pref[i]
        ind = i

print(mx)
print(l, r)
