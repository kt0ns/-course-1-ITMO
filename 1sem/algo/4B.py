n = int(input())
a = map(int, input().split())

pref = [0]
for num in a:
    pref.append(pref[-1] + num)

mx = -10 ** 9
min_pref = 0

for i in range(1, n + 1):
    cur = pref[i] - min_pref
    if cur > mx:
        mx = cur
    if min_pref > pref[i]:
        min_pref = pref[i]

print(mx)
