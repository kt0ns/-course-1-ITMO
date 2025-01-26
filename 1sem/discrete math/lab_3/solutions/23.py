n = input()

for i in range(len(n) - 1, -1, -1):
    if n[i] == '1':
        x1 = n[:i] + '0' + '1' * (len(n) - i - 1)
        break
else:
    x1 = '-'
for i in range(len(n) - 1, -1, -1):
    if n[i] == '0':
        x2 = n[:i] + '1' + '0' * (len(n) - i - 1)
        break
else:
    x2 = '-'

print(x1)
print(x2)
