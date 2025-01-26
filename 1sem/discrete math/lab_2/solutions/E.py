import string

a = [x for x in string.ascii_lowercase]
t = ''
s = input()
ans = []
for i in s:
    if t + i in a:
        t += i
    else:
        j = a.index(t)
        ans.append(j)
        a.append(t + i)
        t = i

ans.append(a.index(t))
print(*ans)