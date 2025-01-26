n, r = map(int, input().split())
dp = [[0] * (n + 1) for _ in range(n + 1)]


def preCalc(n, dp):
    # dp[n][k] - количество способов разбить n на слагаемые >= k
    # dp[i][j] = dp[i - j][j] + dp[i][j - 1]
    # dp[k][k] = 1
    # dp[n][k] = 0 if k > n
    dp[0][0] = 1
    for i in range(n + 1):
        for j in range(n, -1, -1):
            if j == i:
                dp[i][j] = 1
            elif j > i:
                dp[i][j] = 0
            else:
                dp[i][j] = getDp(i - j, j, dp) + getDp(i, j + 1, dp)


def getDp(i, j, dp):
    if i < 0 or j < 0 or i > n or j > n:
        return 0
    return dp[i][j]


def gen(p, last, sum_, r, n, dp):
    if n == sum_:
        r -= 1
    for c in range(last, n + 1):
        if sum_ + c <= n:
            t = getDp(n - sum_ - c, c, dp)
            if r < t:
                p.append(c)
                r = gen(p, c, sum_ + c, r, n, dp)
                return r
            r -= t
    if r == 0:
        print(*p, n - sum_)
    return r



preCalc(n, dp)
gen([], 1, 0, r, n, dp)
