n = int(input())
arr = []

def f(n, arr):
    if n == 0:
        return [arr]

    resultat = []
    for i in range(min(arr[-1] if len(arr) != 0 else n, n), 0, -1):
        resultat.extend(f(n - i, arr + [i]))

    return resultat

answer = f(n, arr)
c = 0
answer.sort(key= lambda x: (-len(x), -x[0]))
print(len(answer))

print('---------------')
print('ANSWER')
