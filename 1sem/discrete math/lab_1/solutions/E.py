n = 2**int(input())
func = [input().split() for _ in range(n)]
vect = "".join(func[i][1] for i in range(n))

def treug(lst):
    lst = [int(x) for x in lst]
    n = len(lst)
    if n == 1:
        return
    data = [[0 for _ in range(n)] for _ in range(n)]
    for i in range(n):
        data[0][i] = lst[i]
    for i in range(1, n):
        for j in range(n - i):
            data[i][j] = (data[i - 1][j] + data[i - 1][j + 1]) % 2
    return data


tr = treug(vect)

for i in range(n):
    print(func[i][0] + " " + str(tr[i][0]))