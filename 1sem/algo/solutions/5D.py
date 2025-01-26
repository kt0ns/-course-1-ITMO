n, k = map(int, input().split())

nA = sorted(map(int, input().split()))
nK = list(map(int, input().split()))


def binsearch(x):
    l, r = -1, n

    while r - l > 1:
        m = (l + r) // 2
        if nA[m] < x:
            l = m;
        else:
            r = m;

    return "YES" if r != n and nA[r] == x else "NO"


for i in range(k):
    print(binsearch(nK[i]))
