import string

a = [x for x in string.ascii_lowercase]
ans = []

s = input()
l = len(s)
for i in range(l):
    ind = a.index(s[i])
    a = [a.pop(ind)] + a
    ans.append(ind + 1)

print(*ans)