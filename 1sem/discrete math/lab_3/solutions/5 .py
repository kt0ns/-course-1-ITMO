n, k = map(int, input().split())


def f(i, answ):
    if i == n:
        return answ
    a = []
    for j in range(k):
        a += [x + str(j) for x in answ[::(-1) ** j]]
    return f(i + 1, a)


answ = f(0, [''])
print('\n'.join(answ))
    