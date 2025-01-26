n = int(input())

answ = ['']

def f(answ):
    if len(answ[0]) == n:
        return answ

    new_answ = []
    for s in answ:
        for j in range(1, n+1):
            if str(j) not in s:
                new_answ.append(s + str(j))
    return f(new_answ)

a = f(answ)

for i in a:
    print(' '.join(i))
