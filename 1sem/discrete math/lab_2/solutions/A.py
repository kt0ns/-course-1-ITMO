n = int(input())
data = sorted(map(int, input().split()))
ar = [10 ** 8] * n
i, j, l = 0, 0, 0
s = 0

while l < n:
    if i + 1 < n and (j >= l - 1 or (data[i] + data[i + 1] <= ar[j] + ar[j + 1] if j + 1 < l else 1)) and (data[i + 1] <= ar[j] if j < l else 1):
        ar[l] = data[i] + data[i + 1]
        s += ar[l]
        i += 2
    elif i < n and (j >= l - 1 or (data[i] <= ar[j + 1] if j < l - 1 else 1)):
        ar[l] = data[i] + ar[j]
        s += ar[l]
        i += 1
        j += 1
    elif j < l - 1:
        ar[l] = ar[j] + ar[j + 1]
        s += ar[l]
        j += 2
    l += 1

print(s)
