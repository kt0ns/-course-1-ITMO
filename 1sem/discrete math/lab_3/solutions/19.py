# k = 2
# s = "([])()[]"
# n = len(s) // 2
# balance = [[0] * (n + 1) for x in range(2 * n + 1)]
# balance[0][0] = 1
#
# for i in range(1, 2 * n + 1):
#     balance[i][0] = balance[i - 1][1]
#     for j in range(1, n):
#         balance[i][j] = balance[i - 1][j - 1] + balance[i - 1][j + 1]
#     balance[i][n] = balance[i - 1][n - 1]
#
#
# def calculate_index(s, n, dp, k):
#     depth = 0
#     index = 0
#     length = len(s)
#
#     for i in range(length):
#         for c in range(ord('('), ord(s[i])):
#             ndepth = depth + 1 if chr(c) == '(' else depth - 1
#             if 0 <= ndepth <= n:
#                 remaining = 2 * n - i - 1
#                 index += dp[remaining][ndepth] * k ** ((remaining - ndepth) // 2)
#         if s[i] in '([':
#             depth += 1
#         else:
#             depth -= 1
#     return index
#
#
# index = calculate_index(s, n, balance, k)
#
# print(index)


def get_parenthesis(n, k):
    len_seq = 2 * n
    dp = [[0] * (n + 1) for _ in range(len_seq + 1)]
    dp[0][0] = 1

    # Fill the DP table
    for i in range(1, len_seq + 1):
        for j in range(n + 1):
            if j > 0:
                dp[i][j] += dp[i - 1][j - 1]
            if j + 1 <= n:
                dp[i][j] += dp[i - 1][j + 1]

    result = []
    dno = 0
    newlen = 0
    opend = []

    for i in range(len_seq - 1, -1, -1):
        # '(' case
        cur = 0
        if dno < n:
            if dno >= -1:
                cur = dp[i][dno + 1] << ((i - (dno + 1)) // 2)

        if cur >= k:
            result.append('(')
            opend.append('(')
            newlen += 1
            dno += 1
            continue
        k -= cur

        # ')' case
        cur = 0
        if newlen > 0 and opend[-1] == '(' and dno > 0:
            cur = dp[i][dno - 1] << ((i - (dno - 1)) // 2)

        if cur >= k:
            result.append(')')
            opend.pop()
            newlen -= 1
            dno -= 1
            continue
        k -= cur

        # '[' case
        cur = 0
        if dno < n:
            if dno >= -1:
                cur = dp[i][dno + 1] << ((i - (dno + 1)) // 2)

        if cur >= k:
            result.append('[')
            opend.append('[')
            newlen += 1
            dno += 1
            continue
        k -= cur

        # ']' case
        result.append(']')
        opend.pop()
        newlen -= 1
        dno -= 1

    return ''.join(result)


if __name__ == "__main__":
    n = 4
    k = 100
    print(get_parenthesis(n, k + 1))
