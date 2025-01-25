from collections import deque

n, k = map(int, input().split())

i = 0
q = deque()
a = []
for num in map(int, input().split()):
    a.append(num)
    if len(a) > k:
        a = a[1:]
        q.popleft()
        i -= 1

    while q and q[-1] > a[-1]:
        q.pop()

    q.append(a[-1])
    if i == k - 1:
        print(q[0])

    i += 1
