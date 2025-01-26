n, k = map(int, input().split())


answ = [[]]


def f(answ):
    if len(answ[0]) == k:
        return answ

    new_answ = []
    for s in answ:
        if len(s) > 0:
            for j in range(s[-1] + 1, n + 1):
                new_answ.append(s + [j])
        else:
            for j in range(1, n + 1):
                new_answ.append(s + [j])

    return f(new_answ)


a = f(answ)

for i in a:
    print(*i)
