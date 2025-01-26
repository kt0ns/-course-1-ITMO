import math
n = int(input())
func = [input().split()[1] for _ in range(n)]

classes = [[0]*n for _ in range(5)]

zero, one, monot, self, linal = 1, 1, 1, 1, 1

def ifOne(func):
    global one
    if one:
        one = (func[-1] == '1')
    return

def ifZero(func):
    global zero
    if zero:
        zero = (func[0] == '0')
    return

def ifSelf(func):
    global self
    if self:
        if len(func) == 1:
            self = 0
            return
        for i in range(len(func) // 2):
            if func[i] == func[- i - 1]:
                self = 0
                return
    return

# def isLine(func):
#     global linal
#     if linal:
#         if len(func) == 1:
#             return
#         while len(func) > 1:
#             curr = []
#             for j in range(len(func) - 1):
#                 curr.append(int(func[j]) ^ int(func[j + 1]))
#             if curr[0]:
#                 rang = len(func)
#                 if bin(rang).count('1') > 1:
#                     linal = 0
#                     break
#             func = ''.join(map(str, curr))
#     return
#
# def isLinear(func):
#     global linal
#     if linal:
#         if len(func) == 1:
#             return
#         if f.count('1') % 2 == 1:
#             linal = 0
#             return
#         while len(func) > 1:
#             currCoff = []
#             for i in range(len(func), 0, -1):
#                 for j in range(i-1):
#                     currCoff.append(int(func[j]) ^ int(func[j + 1]))
#                 if currCoff[0]:
#                     rangeVal = len(func)
#                     if bin(rangeVal - i + 1).count('1') > 1:
#                         linal = False
#                         break
#             func = ''.join(map(str, currCoff))
#     return

# def isLinear(func):
#     global linal
#     if linal:
#         if len(func) == 1:
#             return
#         rng = len(func)
#         i = rng
#         while i > 0:
#             currCoff = []
#             for j in range(0, i - 1):
#                 currCoff.append(int(func[j]) ^ int(func[j + 1]))
#             if len(currCoff) > 0:
#                 if bin(rng - i + 1).count('1') > 1:
#                     linal = False
#                     break
#             func = ''.join(map(str, currCoff))
#             i -= 1
#     return

# def isLinear(func):
#     global linal
#     if linal:
#         if len(func) == 1:
#             return
#         # if func.count('1') % 2 == 1:
#         #     linal = 0
#         #     return
#         for i in range(len(func), 0, -1):
#             currCoff = []
#             for j in range(i-1):
#                 currCoff.append(int(func[j]) ^ int(func[j + 1]))
#             if currCoff and currCoff[0]:
#                 rangeVal = len(func)
#                 if bin(rangeVal - i + 1).count('1') > 1:
#                     linal = False
#                     break
#             # print(currCoff)
#             func = ''.join(map(str, currCoff))
#     return

def is_linear(lst):
    lst = [int(x) for x in lst]
    n = len(lst)
    if n == 1:
        return True
    data = [[0 for _ in range(n)] for _ in range(n)]
    for i in range(n):
        data[0][i] = lst[i]
    for i in range(1, n):
        for j in range(n - i):
            data[i][j] = (data[i - 1][j] + data[i - 1][j + 1]) % 2
    for i in range(round(math.log2(n)) + 1, n):
        if data[i][0] == 1 and f'{i:b}'.count('1') > 1:
            return False
    return True



# def mono(func):
#     global monot
#     if monot:
#         if len(func) == 1:
#             monot = 1
#             return
#         size = len(func)
#         # for i in range(size, 1, -1):
#         #     for j in range(i // 2):
#         #         if func[j] > func[i // 2 + j]:
#         #             return 0
#         # return mono(func[size // 2:]) and mono(func[:size // 2])
#         for i in range(0, size - 1, 2):
#             if func[i] > func[i+1]:
#                 monot = 0
#                 return
#     return



def is_monotone(func):
    n = len(func)

    def check_monotone_step(step):
        for i in range(0, n, 2 * step):
            for j in range(step):
                if int(func[i + j]) > int(func[i + step + j]):
                    return False
        return True

    step = n // 2
    while step > 0:
        if not check_monotone_step(step):
            return False
        step //= 2

    return True



for n in range(len(func)):
    f = func[n]
    classes[0][n] = (f[0] == '0')
    classes[1][n] = (f[-1] == '1')
    classes[2][n] = all(f[i] != f[-i - 1] for i in range(len(f)//2)) if len(f) != 1 else False
    classes[3][n] = is_linear(f)
    classes[4][n] = is_monotone(f)
    # print(classes[n])

# for f in func:
#
#     ifOne(f)
#     ifZero(f)
#     ifSelf(f)
#     mono(f)
#     isLine(f)
#     print(one, zero, linal, monot, self)

# if one or zero or linal or monot or self:
#     print("NO")
# else:
#     print("YES")

for i in range(5):
    s = 0
    for j in range(len(func)):
        s += classes[i][j]
    if s == len(func):
        print("NO")
        break
else:
    print('YES')