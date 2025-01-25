n, m = map(int, input().split())
a = list(map(int, input().split()))
b = list(map(int, input().split()))


def left_search(num, n):
    l = -1
    r = n

    while l < r - 1:
        m = (l + r) // 2
        if a[m] < num:
            l = m
        else:
            r = m
    return r


def right_search(num, n):
    l = -1
    r = n

    while l < r - 1:
        m = (l + r) // 2
        if a[m] > num:
            r = m
        else:
            l = m
    return l


for num in b:
    ls = left_search(num, n)
    rs = right_search(num, n)
    if ls == n and rs == n - 1:
        print(0)
    else:
        print(ls + 1, rs + 1)
