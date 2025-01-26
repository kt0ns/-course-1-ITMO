l = int(input())


def f(i, n):
    string = ''
    while i > 0:
        string = str(i % 3) + string
        i //= 3
    return string.zfill(n)


answ = [f(i, l) for i in range(3 ** (l - 1))]


def n(answ, n):
    a = []
    for i in answ:
        a.append(i)
        s1 = ''
        for j in range(n):
            if i[j] == '0':
                s1 += '1'
            if i[j] == '1':
                s1 += '2'
            if i[j] == '2':
                s1 += '0'
        s2 = ''
        for j in range(n):
            if s1[j] == '0':
                s2 += '1'
            if s1[j] == '1':
                s2 += '2'
            if s1[j] == '2':
                s2 += '0'
        a.append(s1)
        a.append(s2)

    return a


answ = n(answ, l)

for i in answ:
    print(i)
