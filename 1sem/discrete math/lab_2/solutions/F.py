import string
int(input())
a = [x for x in string.ascii_lowercase]

l = list(map(int, input().split()))
answ = ['']

for i in l:
    if i < len(a):
        w = a[i]
        if answ[-1] + w[0] not in a:
            a.append(answ[-1] + w[0])
        answ.append(w)
    else:
        v = answ[-1] + answ[-1][0]
        if v not in a:
            a.append(v)
        answ.append(v)

print(''.join(answ))