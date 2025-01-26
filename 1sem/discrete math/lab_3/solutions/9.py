n = int(input())

def g(n, open, close, ans):
    if open + close == n * 2:
        print(ans)
        return
    if open < n:
        g(n, open + 1, close, ans + '(')
    if open > close:
        g(n, open, close + 1, ans + ')')

g(n, 0, 0, '')