s = input()
l = len(s)
d = [x for x in s]
a = sorted([x for x in s])

while len(a[0]) < l:
    for i in range(l):
        a[i] = d[i] + a[i]
    a.sort()

print(min(a))