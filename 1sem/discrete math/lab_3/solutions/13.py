n, k = map(int, input().split())
k -= 1
numbers = [x for x in range(1, n+ 1)]
answ = []
fact = [1]
for i in range(1, n):
    fact.append(fact[i - 1] * i)

for i in range(n - 1, -1, -1):
    index = k // fact[i]
    k %= fact[i]
    answ.append(numbers.pop(index))

print(*answ)
