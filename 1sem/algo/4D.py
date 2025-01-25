import sys
from collections import deque

input = sys.stdin.read
data = input().splitlines()
q = int(data[0])
min_q = deque()
deq = deque()

for i in range(1, q + 1):
    s = data[i].split()
    if s[0] == "push":
        num = int(s[1])
        deq.append(num)
        while min_q and num < min_q[-1]:
            min_q.pop()
        min_q.append(num)
    elif s[0] == "pop":
        if deq:
            if deq[0] == min_q[0]:
                min_q.popleft()
            deq.popleft()
    else:
        print(min_q[0] if deq else -1)
