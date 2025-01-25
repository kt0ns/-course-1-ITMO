size = int(input())
data = list(map(int, input().split()))

cnt = 0

for i in range(1, size):
    for j in range(0, i):
        if data[i] < data[j]:
            cnt += 1

print(cnt)
