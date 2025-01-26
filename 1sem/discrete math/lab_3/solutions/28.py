n = int(input())

a = list(map(int, input().split()))

def more(arr, target, i):
    left, right = i, len(arr) - 1
    result_index = -1

    while left <= right:
        mid = (left + right) // 2
        if arr[mid] > target:
            result_index = mid
            left = mid + 1
        else:
            right = mid - 1

    return result_index


for i in range(n - 1, 0, -1):
    if a[i] > a[i - 1]:
        answ1 = a[:]
        mx = more(answ1, a[i - 1], i)
        if mx != -1:
            answ1[i - 1], answ1[mx] = answ1[mx], answ1[i - 1]
        answ1 = answ1[:i] + answ1[i:][::-1]
        break
else:
    answ1 = [0] * n

print(*answ1)
