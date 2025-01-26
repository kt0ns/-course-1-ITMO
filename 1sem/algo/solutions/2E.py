n = int(input()) - 1
data = set()
for i in range(1, n + 2):
    data.add(i ** 2)
    data.add(i ** 3)

print(sorted(data)[n])
