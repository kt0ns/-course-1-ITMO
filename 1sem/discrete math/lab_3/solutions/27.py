s = input()
def find_string(s):
    open = 0
    close = 0
    for char in reversed(s):
        if char == '(':
            open += 1
            if close > open:
                return open, close, s[:len(s) - close - open]
        elif char == ')':
            close += 1
    return open, close, ""


def next_psp(s):
    open, close, hvost = find_string(s)

    if open + close == len(s):
        return "-"

    return hvost + ")" + "(" * open + ")" * (close - 1)


print(next_psp(s))
