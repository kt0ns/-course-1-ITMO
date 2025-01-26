n = int(input())
answ = []
for i in range(2 ** n):
    s = bin(i)[2:].zfill(n)
    if '11' not in s:
        answ.append(s)

print(len(answ))
for i in answ:
    print(i)
