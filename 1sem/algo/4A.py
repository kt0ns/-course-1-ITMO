n = int(input())
a = list(map(int, input().split()))
q = int(input())
pref = [0]
for num in a:
    pref.append(pref[-1] + num)

while q > 0:
    l, r = map(int, input().split())
    print(pref[r] - pref[l - 1])
    q -= 1
