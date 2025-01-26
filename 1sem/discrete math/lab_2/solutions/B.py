s = input()
a = []

for i in range(len(s)):
    a.append(s[i:]+ s[:i])

print(''.join(x[-1] for x in sorted(a)))